package com.penglecode.xmodule.common.config;

import com.penglecode.xmodule.BasePackage;
import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import java.time.format.DateTimeFormatter;

/**
 * 默认的SpringBoot应用配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
@Configuration
@AutoConfigurationPackage(basePackageClasses=BasePackage.class)
public class DefaultSpringAppConfiguration extends AbstractSpringConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpringAppConfiguration.class);
	
	/**
	 * 全局默认的MessageSourceAccessor
	 */
	@Bean
	@ConditionalOnMissingBean(name="defaultMessageSourceAccessor")
	public static MessageSourceAccessor defaultMessageSourceAccessor(MessageSource messageSource) {
		MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource, GlobalConstants.DEFAULT_LOCALE);
		LOGGER.info(">>> 初始化Spring应用的全局国际化资源文件配置! messageSource = {}, messageSourceAccessor = {}", messageSource, messageSourceAccessor);
		return messageSourceAccessor;
	}
	
	/**
	 * 全局默认的ResourcePatternResolver
	 */
	@Bean
	@ConditionalOnMissingBean(name="defaultResourcePatternResolver")
	public static ResourcePatternResolver defaultResourcePatternResolver(AbstractApplicationContext applicationContext) {
		ResourcePatternResolver resourcePatternResolver = ReflectionUtils.getFieldValue(applicationContext, "resourcePatternResolver");
		LOGGER.info(">>> 初始化Spring应用的默认文件资源解析器配置! resourcePatternResolver = {}", resourcePatternResolver);
		return resourcePatternResolver;
	}
	
	/**
	 * 全局默认的ConversionService
	 */
	@Bean
	@ConditionalOnMissingBean(name="defaultConversionService")
	public static ConversionService defaultConversionService() {
		ConversionService conversionService = ApplicationConversionService.getSharedInstance();
		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        registrar.registerFormatters((FormatterRegistry) conversionService);
        LOGGER.info(">>> 初始化Spring应用的默认类型转换服务配置! conversionService = {}", conversionService.getClass());
        return conversionService;
	}

}
