package org.springframework.boot.autoconfigure.dal;

import com.penglecode.xmodule.common.util.DataSourceUtils;
import com.penglecode.xmodule.common.util.SpringUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;

/**
 * 基于@NamedDatabase("default")来区分数据的数据访问层(DAL)组件自动注册器
 * 涉及自动注册的组件有：
 *      1、DataSource                beanName=defaultDataSource
 *      2、JdbcTemplate              beanName=defaultJdbcTemplate
 *      3、TransactionManager        beanName=defaultTransactionManager
 *
 *      以上组件都会区分数据库，生成的beanName都是以database + 组件类.class.getSimpleName()这种命名方式
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/5/15 23:02
 */
public class DalJdbcComponentAutoConfigurer extends AbstractDalComponentAutoConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DalJdbcComponentAutoConfigurer.class);

    @Override
    protected void registerComponentBeans(String database, BeanDefinitionRegistry registry) {
        if(hasRequiredConfig(DalComponentConfigPrefix.DATASOURCE_CONFIG_PREFIX, database, true)) { //是否存在指定数据库的数据源必要配置?
            LOGGER.info("Prepare to dynamic register Jdbc DAL components for Database({}).", database);
            registerDataSource(database, registry); //1、注册DataSource
            registerJdbcTemplate(database, registry); //2、注册JdbcTemplate
            registerTransactionManager(database, registry); //3、注册PlatformTransactionManager
        }
    }

    /**
     * 注册DataSource
     * @param database
     * @param registry
     */
    protected void registerDataSource(String database, BeanDefinitionRegistry registry) {
        String dataSourceBeanName = DalComponentUtils.genBeanNameOfType(database, DataSource.class);
        if(!beanFactory.containsBean(dataSourceBeanName)) { //没有注册该bean?
            BeanDefinition dataSourcePropertiesDefinition = SpringUtils.createBindableBeanDefinition(DataSourceProperties.class, DalComponentConfigPrefix.DATASOURCE_CONFIG_PREFIX.getConfigPrefix() + "." + database);
            String dataSourcePropertiesBeanName = DalComponentUtils.genBeanNameOfType(database, DataSourceProperties.class);
            registry.registerBeanDefinition(dataSourcePropertiesBeanName, dataSourcePropertiesDefinition); //注册数据源配置bean
            DataSourceProperties dataSourceProperties = beanFactory.getBean(dataSourcePropertiesBeanName, DataSourceProperties.class);
            LOGGER.info("Dynamic register DataSourceProperties[database = {}] bean successfully: {}", database, dataSourceProperties);
            //创建DataSource数据源实例(此时未设置任何连接池配置)
            HikariDataSource dataSource = DataSourceUtils.createDataSource(dataSourceProperties, HikariDataSource.class);
            dataSource.setPoolName(StringUtils.defaultIfBlank(dataSourceProperties.getName(), database));
            //将当前数据源的公共连接池配置设置进去
            BeanDefinition dataSourceDefinition = SpringUtils.createBindableBeanDefinition(dataSource, DataSource.class, DalComponentConfigPrefix.DATASOURCE_CONFIG_PREFIX.getConfigPrefix() + ".hikari");
            registry.registerBeanDefinition(dataSourceBeanName, dataSourceDefinition); //注册数据源配bean
            LOGGER.info("Dynamic register DataSource[database = {}] bean successfully: {}", database, dataSource);
        }
    }

    /**
     * 注册JdbcTemplate
     * @param database
     * @param registry
     */
    protected void registerJdbcTemplate(String database, BeanDefinitionRegistry registry) {
        String jdbcTemplateBeanName = DalComponentUtils.genBeanNameOfType(database, JdbcTemplate.class);
        if(!beanFactory.containsBean(jdbcTemplateBeanName)) {
            String dataSourceBeanName = DalComponentUtils.genBeanNameOfType(database, DataSource.class);
            DataSource dataSource = beanFactory.getBean(dataSourceBeanName, DataSource.class);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            BeanDefinition jdbcTemplateDefinition = BeanDefinitionBuilder.genericBeanDefinition(JdbcTemplate.class, () -> jdbcTemplate).getBeanDefinition();
            registry.registerBeanDefinition(jdbcTemplateBeanName, jdbcTemplateDefinition); //注册JdbcTemplate
            LOGGER.info("Dynamic register JdbcTemplate[database = {}] bean successfully: {}", database, jdbcTemplate);
        }
    }

    /**
     * 注册PlatformTransactionManager
     * @param database
     * @param registry
     */
    protected void registerTransactionManager(String database, BeanDefinitionRegistry registry) {
        String transactionManagerBeanName = DalComponentUtils.genBeanNameOfType(database, TransactionManager.class);
        if(!beanFactory.containsBean(transactionManagerBeanName)) {
            String dataSourceBeanName = DalComponentUtils.genBeanNameOfType(database, DataSource.class);
            DataSource dataSource = beanFactory.getBean(dataSourceBeanName, DataSource.class);
            DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
            BeanDefinition transactionManagerDefinition = BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManager.class, () -> dataSourceTransactionManager).getBeanDefinition();
            registry.registerBeanDefinition(transactionManagerBeanName, transactionManagerDefinition); //注册JdbcTemplate
            LOGGER.info("Dynamic register DataSourceTransactionManager[database = {}] bean successfully: {}", database, dataSourceTransactionManager);
        }
    }

    @Override
    public final int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
