package org.springframework.boot.autoconfigure.dal;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据访问层(DAL)组件自动配置类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/16 11:25
 */
@Configuration
@ComponentScan
@EnableTransactionManagement(proxyTargetClass=true)
public class DalComponentAutoConfiguration implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        List<AbstractDalComponentAutoConfigurer> dalComponentAutoConfigurers = beanFactory.getBeanProvider(AbstractDalComponentAutoConfigurer.class).orderedStream().collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(dalComponentAutoConfigurers)) {
            for(AbstractDalComponentAutoConfigurer dalComponentAutoConfigurer : dalComponentAutoConfigurers) {
                dalComponentAutoConfigurer.registerBeanDefinitions(importingClassMetadata, registry);
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
