package org.springframework.boot.autoconfigure.dal;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.util.StringUtils;


/**
 * 基于@NamedDatabase("db1")注解，区分数据库的Mapper beanName生成器实现
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 22:48
 */
public class DatabaseDifferentiatedBeanNameGenerator implements BeanNameGenerator {

    private final String targetDatabase;

    public DatabaseDifferentiatedBeanNameGenerator(String targetDatabase) {
        this.targetDatabase = targetDatabase;
    }

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        GenericBeanDefinition genericBeanDefinition = (GenericBeanDefinition) definition;
        String beanClassName = genericBeanDefinition.getBeanClassName();
        if(!StringUtils.hasText(beanClassName)) {
            beanClassName = genericBeanDefinition.getBeanClass().getName();
        }
        return DalComponentUtils.genBeanNameOfType(targetDatabase, beanClassName);
    }

}