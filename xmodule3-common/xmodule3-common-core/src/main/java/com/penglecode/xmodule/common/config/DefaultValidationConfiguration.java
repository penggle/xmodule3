package com.penglecode.xmodule.common.config;

import com.penglecode.xmodule.common.validation.validator.CustomValidatorFactoryBean;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * 默认的javax.validation配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/17 22:47
 */
@Configuration
@ConditionalOnClass(HibernateValidator.class)
public class DefaultValidationConfiguration extends AbstractSpringConfiguration {

    private static final String CONFIG_KEY_HIBERNATE_VALIDATOR_FAIL_FAST = "hibernate.validator.fail_fast";

    @Primary
    @Bean(name="defaultValidator")
    public LocalValidatorFactoryBean defaultValidator() {
        String failFast = getEnvironment().getProperty(CONFIG_KEY_HIBERNATE_VALIDATOR_FAIL_FAST, "true");
        LocalValidatorFactoryBean defaultValidator = new CustomValidatorFactoryBean();
        defaultValidator.setProviderClass(HibernateValidator.class);
        defaultValidator.getValidationPropertyMap().put(CONFIG_KEY_HIBERNATE_VALIDATOR_FAIL_FAST, failFast);
        MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
        defaultValidator.setMessageInterpolator(interpolatorFactory.getObject());
        return defaultValidator;
    }

}
