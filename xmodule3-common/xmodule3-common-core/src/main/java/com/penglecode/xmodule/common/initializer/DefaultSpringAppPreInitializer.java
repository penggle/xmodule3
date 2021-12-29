package com.penglecode.xmodule.common.initializer;

import com.penglecode.xmodule.common.consts.Constant;
import com.penglecode.xmodule.common.consts.SpringConstantPool;
import com.penglecode.xmodule.common.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Spring应用启动之处初始化程序
 * 该类配置方式如下：
 * 1、 在web.xml中以contextInitializerClasses上下文参数配置
 * 		<context-param>
 * 			<param-name>contextInitializerClasses</param-name>
 * 			<param-value>xyz.SpringAppPreBootingInitializer</param-value>
 * 		</context-param>
 * 
 * 2、 在Springboot的application.properties中配置
 * 		context.initializer.classes=xyz.SpringAppPreBootingInitializer
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
public class DefaultSpringAppPreInitializer extends AbstractSpringAppContextInitializer {

	private static final String NACOS_LOGGING_DEFAULT_CONFIG_ENABLED_PROPERTY = "nacos.logging.default.config.enabled";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpringAppPreInitializer.class);
	
	static {
		System.setProperty(NACOS_LOGGING_DEFAULT_CONFIG_ENABLED_PROPERTY, Boolean.FALSE.toString());
	}

	@Override
	public void doInitialize(ConfigurableApplicationContext applicationContext) {
		LOGGER.info(">>> Spring 应用启动前置初始化程序! applicationContext = {}", applicationContext);
		SpringUtils.setApplicationContext(applicationContext);
		SpringUtils.setEnvironment(applicationContext.getEnvironment());
		Constant.setConstantPool(new SpringConstantPool<>());
	}

}
