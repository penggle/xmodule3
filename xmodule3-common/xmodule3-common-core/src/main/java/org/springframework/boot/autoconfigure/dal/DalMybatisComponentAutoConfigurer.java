package org.springframework.boot.autoconfigure.dal;

import com.penglecode.xmodule.common.util.SpringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * 基于@NamedDatabase("default")来区分数据的数据访问层(DAL)组件自动注册器
 * 涉及自动注册的组件有：
 *      4、SqlSessionFactory             beanName=defaultSqlSessionFactory
 *      5、SqlSessionTemplate            beanName=defaultSqlSessionTemplate
 *      6、XxxMapper                     beanName=defaultXxxMapper
 *
 *      以上组件都会区分数据库，生成的beanName都是以database + 组件类.class.getSimpleName()这种命名方式
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 23:02
 */
public class DalMybatisComponentAutoConfigurer extends AbstractDalComponentAutoConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DalMybatisComponentAutoConfigurer.class);

    private final DalMybatisComponentsBuilder dalMybatisComponentsBuilder;

    public DalMybatisComponentAutoConfigurer(DalMybatisComponentsBuilder dalMybatisComponentsBuilder) {
        this.dalMybatisComponentsBuilder = dalMybatisComponentsBuilder;
    }

    @Override
    protected void registerComponentBeans(String database, BeanDefinitionRegistry registry) {
        //是否存在Mybatis必要配置?(区分数据库)
        if(hasRequiredConfig(DalComponentConfigPrefix.MYBATIS_CONFIG_PREFIX, database, true)) {
            LOGGER.info("Prepare to dynamic register Mybatis DAL components for Database({}).", database);
            registerSqlSessionFactoryBean(database, registry, true); //4、注册SqlSessionFactoryBean
            registerSqlSessionTemplateBean(database, registry, true); //5、注册SqlSessionTemplate
            registerMapperByMapperScanner(database, registry, true); //6、注册Mapper实现bean
        }
        //是否存在Mybatis必要配置?(不区分数据库) || 未配置Mybatis相关配置(使用默认配置)
        else if(hasRequiredConfig(DalComponentConfigPrefix.MYBATIS_CONFIG_PREFIX, database, false) || isDataSourceExists(database)) {
            LOGGER.info("Prepare to dynamic register Mybatis DAL components for Database({}).", database);
            registerSqlSessionFactoryBean(database, registry, false); //4、注册SqlSessionFactoryBean
            registerSqlSessionTemplateBean(database, registry, false); //5、注册SqlSessionTemplate
            registerMapperByMapperScanner(database, registry, false); //6、注册Mapper实现bean
        }
    }

    /**
     * 注册SqlSessionFactoryBean
     * @param database
     * @param registry
     * @param databaseDifferentiated
     */
    protected void registerSqlSessionFactoryBean(String database, BeanDefinitionRegistry registry, boolean databaseDifferentiated) {
        String sqlSessionFactoryBeanName = DalComponentUtils.genBeanNameOfType(database, SqlSessionFactory.class);
        if(!beanFactory.containsBean(sqlSessionFactoryBeanName)) {
            BeanDefinition mybatisPropertiesDefinition = SpringUtils.createBindableBeanDefinition(new MybatisProperties(), MybatisProperties.class, DalComponentConfigPrefix.MYBATIS_CONFIG_PREFIX.getConfigPrefix() + (databaseDifferentiated ? "." + database : ""));
            String mybatisPropertiesBeanName = DalComponentUtils.genBeanNameOfType(database, MybatisProperties.class);
            registry.registerBeanDefinition(mybatisPropertiesBeanName, mybatisPropertiesDefinition); //注册Mybatis配置bean
            MybatisProperties mybatisProperties = beanFactory.getBean(mybatisPropertiesBeanName, MybatisProperties.class);
            LOGGER.info("Dynamic register MybatisProperties[database = {}] bean successfully: {}", database, mybatisProperties);

            DataSource dataSource = beanFactory.getBean(DalComponentUtils.genBeanNameOfType(database, DataSource.class), DataSource.class);
            SqlSessionFactoryBean sqlSessionFactoryBean = dalMybatisComponentsBuilder.buildSqlSessionFactoryBean(dataSource, mybatisProperties);
            BeanDefinition sqlSessionFactoryBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class, () -> sqlSessionFactoryBean).getBeanDefinition();
            registry.registerBeanDefinition(sqlSessionFactoryBeanName, sqlSessionFactoryBeanDefinition); //注册SqlSessionFactory bean
            SqlSessionFactory sqlSessionFactory = beanFactory.getBean(sqlSessionFactoryBeanName, SqlSessionFactory.class);
            LOGGER.info("Dynamic register SqlSessionFactory[database = {}] bean successfully: {}", database, sqlSessionFactory);
        }
    }

    /**
     * 注册SqlSessionTemplate
     * @param database
     * @param registry
     * @param databaseDifferentiated
     */
    protected void registerSqlSessionTemplateBean(String database, BeanDefinitionRegistry registry, boolean databaseDifferentiated) {
        String sqlSessionTemplateBeanName = DalComponentUtils.genBeanNameOfType(database, SqlSessionTemplate.class);
        if(!beanFactory.containsBean(sqlSessionTemplateBeanName)) {
            String sqlSessionFactoryBeanName = DalComponentUtils.genBeanNameOfType(database, SqlSessionFactory.class);
            SqlSessionFactory sqlSessionFactory = beanFactory.getBean(sqlSessionFactoryBeanName, SqlSessionFactory.class);
            String mybatisPropertiesBeanName = DalComponentUtils.genBeanNameOfType(database, MybatisProperties.class);
            MybatisProperties mybatisProperties = beanFactory.getBean(mybatisPropertiesBeanName, MybatisProperties.class);
            ExecutorType executorType = mybatisProperties.getExecutorType();
            SqlSessionTemplate sqlSessionTemplate = executorType != null ? new SqlSessionTemplate(sqlSessionFactory, executorType) : new SqlSessionTemplate(sqlSessionFactory);
            BeanDefinition sqlSessionTemplateBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionTemplate.class, () -> sqlSessionTemplate).getBeanDefinition();
            registry.registerBeanDefinition(sqlSessionTemplateBeanName, sqlSessionTemplateBeanDefinition); //注册SqlSessionTemplate bean
            LOGGER.info("Dynamic register SqlSessionTemplate[database = {}] bean successfully: {}", database, sqlSessionTemplate);
        }
    }

    /**
     * 注册ClassPathMapperScanner
     * @param database
     * @param registry
     * @param databaseDifferentiated
     */
    protected void registerMapperByMapperScanner(String database, BeanDefinitionRegistry registry, boolean databaseDifferentiated) {
        String mapperScannerBeanName = DalComponentUtils.genBeanNameOfType(database, ClassPathMapperScanner.class);
        if(!beanFactory.containsBean(mapperScannerBeanName)) {
            ClassPathMapperScanner classPathMapperScanner = dalMybatisComponentsBuilder.buildClassPathMapperScanner(database, registry);
            classPathMapperScanner.registerFilters(); //注册Bean注册过滤器
            String basePackage = StringUtils.collectionToCommaDelimitedString(AutoConfigurationPackages.get(beanFactory));
            int totalMappers = classPathMapperScanner.scan(StringUtils.tokenizeToStringArray(basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
            LOGGER.info("Dynamic register XxxMapper[database = {}] beans(Total {}) successfully", database, totalMappers);
        }
    }

    @Override
    protected boolean hasRequiredConfig(DalComponentConfigPrefix configPrefix, String database, boolean databaseDifferentiated) {
        boolean hasRequiredConfig = super.hasRequiredConfig(configPrefix, database, databaseDifferentiated);
        if(hasRequiredConfig) { //如果存在配置，则还需进一步检测一下对应database的DataSource是否存在
            return isDataSourceExists(database);
        }
        return false;
    }

    @Override
    public final int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }

}
