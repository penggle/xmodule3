package com.penglecode.xmodule.common.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import javax.annotation.Nonnull;

/**
 * springboot配置基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
public abstract class AbstractSpringConfiguration implements EnvironmentAware, ApplicationContextAware {

    private ConfigurableEnvironment environment;
    
    private ApplicationContext applicationContext;

	@Override
	public void setEnvironment(@Nonnull Environment environment) {
		this.environment = (ConfigurableEnvironment) environment;
	}

	public ConfigurableEnvironment getEnvironment() {
		return environment;
	}

	@Override
	public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
