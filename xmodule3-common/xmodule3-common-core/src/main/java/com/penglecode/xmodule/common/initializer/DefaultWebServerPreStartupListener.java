package com.penglecode.xmodule.common.initializer;

import com.penglecode.xmodule.common.exception.ApplicationInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 默认的Web应用启动时的初始化程序主类
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/5/15 14:02
 */
@Component
public class DefaultWebServerPreStartupListener implements BeanPostProcessor, ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebServerPreStartupListener.class);
	
	private ConfigurableApplicationContext applicationContext;
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof WebServerFactory) {
			preWebServerStartup((WebServerFactory) bean);
		}
		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

	protected void preWebServerStartup(WebServerFactory webServerFactory) {
		Map<String,SpringWebAppStartupInitializer> springWebAppInitializerBeans = applicationContext.getBeansOfType(SpringWebAppStartupInitializer.class);
		
		List<SpringWebAppStartupInitializer> initializers = new ArrayList<>(springWebAppInitializerBeans.values());
		if(!CollectionUtils.isEmpty(initializers)) {
			AnnotationAwareOrderComparator.sort(initializers);
			if(webServerFactory instanceof ServletWebServerFactory) {
				LOGGER.info(">>> 基于Servlet环境的Spring Web应用启动, 即将执行的初始化程序：{}", initializers);
			} else if (webServerFactory instanceof ReactiveWebServerFactory) {
				LOGGER.info(">>> 基于Reactive环境的Spring Web应用启动, 即将执行的初始化程序：{}", initializers);
			}
			for(SpringWebAppStartupInitializer initializer : initializers) {
				try {
					initializer.initialize(applicationContext);
				} catch (Exception e) {
					throw new ApplicationInitializationException(e);
				}
			}
		}
	}

	protected ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
