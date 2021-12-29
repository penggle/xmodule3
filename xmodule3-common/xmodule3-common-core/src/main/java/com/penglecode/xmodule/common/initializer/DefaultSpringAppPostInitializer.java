package com.penglecode.xmodule.common.initializer;

import com.penglecode.xmodule.common.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Spring应用启动完成时的初始化程序
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
@Component
public class DefaultSpringAppPostInitializer implements ApplicationContextAware, EnvironmentAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpringAppPostInitializer.class);

	/**
	 * Spring应用的上下文
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		LOGGER.info(">>> 初始化Spring应用的应用上下文! applicationContext = {}", applicationContext);
		SpringUtils.setApplicationContext(applicationContext);
	}

	/**
	 * Spring应用的环境变量初始化
	 */
	@Override
	public void setEnvironment(Environment environment) {
		LOGGER.info(">>> 初始化Spring应用的环境变量! environment = {}", environment);
		SpringUtils.setEnvironment(environment);
	}

}
