package org.springframework.boot.autoconfigure.dal;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Stream;

/**
 * 基于@NamedDatabase("dbName")来区分数据的数据访问层(DAL)组件自动注册器
 * 涉及自动注册的组件有：
 *      1、DataSource
 *      2、JdbcTemplate
 *      3、DataSourceTransactionManager
 *      4、SqlSessionFactory
 *      5、SqlSessionTemplate
 *      6、XxxMapper
 *
 *      以上组件都会区分数据库，生成的beanName都是以database + 组件类.getClass().getSimpleName()这种命名方式
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 23:02
 */
public abstract class AbstractDalComponentAutoConfigurer implements BeanFactoryAware, EnvironmentAware, ResourceLoaderAware, Ordered {

    protected Environment environment;

    protected BeanFactory beanFactory;

    protected ResourceLoader resourceLoader;

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes componentAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableDalAutoConfigure.class.getName()));
        if(componentAttributes != null) {
            AnnotationAttributes[] databaseAttributesArray = componentAttributes.getAnnotationArray("value");
            if(databaseAttributesArray.length > 0) {
                for(AnnotationAttributes databaseAttributes : databaseAttributesArray) {
                    String database = databaseAttributes.getString("value");
                    registerComponentBeans(database, registry);
                }
            }
        }
    }

    protected abstract void registerComponentBeans(String database, BeanDefinitionRegistry registry);

    /**
     * 是否存在指定数据库的数据源必要配置?
     * @param configPrefix              - 配置前缀,例如：spring
     * @param database
     * @param databaseDifferentiated    - 区分数据库?
     * @return
     */
    protected boolean hasRequiredConfig(DalComponentConfigPrefix configPrefix, String database, boolean databaseDifferentiated) {
        Set<String> configProperties = configPrefix.getRequiredProperties();
        if(!CollectionUtils.isEmpty(configProperties)) {
            String propertyPrefix =  configPrefix.getConfigPrefix() + "." + (databaseDifferentiated ? database + "." : "");
            return configProperties.stream().allMatch(name -> {
                String[] propertyNames = name.split("\\|");
                return Stream.of(propertyNames)
                        .map(propertyName -> propertyPrefix + propertyName)
                        .anyMatch(propertyName -> StringUtils.hasText(environment.getProperty(propertyName)));
            });
        }
        return false;
    }

    /**
     * Spring上下文容器中是否存在对应database的DataSource?
     * @param database
     * @return
     */
    protected boolean isDataSourceExists(String database) {
        return beanFactory.containsBean(DalComponentUtils.genBeanNameOfType(database, DataSource.class));
    }

    @Override
    public final void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public final void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public final void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
