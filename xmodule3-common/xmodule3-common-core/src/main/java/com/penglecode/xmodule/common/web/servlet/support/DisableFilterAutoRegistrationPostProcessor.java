package com.penglecode.xmodule.common.web.servlet.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

import java.util.Arrays;

/**
 * 阻止SpringBoot自动注册Filter
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/14 10:49
 */
public class DisableFilterAutoRegistrationPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) throws BeansException {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) bf;
		Arrays.stream(beanFactory.getBeanNamesForType(javax.servlet.Filter.class)).forEach(name -> {
			BeanDefinition definition = BeanDefinitionBuilder.genericBeanDefinition(FilterRegistrationBean.class)
					.setScope(BeanDefinition.SCOPE_SINGLETON).addConstructorArgReference(name)
					.addConstructorArgValue(new ServletRegistrationBean[] {}).addPropertyValue("enabled", false)
					.getBeanDefinition();
			beanFactory.registerBeanDefinition(name + "FilterRegistrationBean", definition);
		});
	}

}
