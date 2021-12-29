package com.penglecode.xmodule.common.validation.validator;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Configuration;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 自定义的LocalValidatorFactoryBean
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/17 23:10
 */
public class CustomValidatorFactoryBean extends LocalValidatorFactoryBean {

    private Configuration<?> configuration;

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Validator targetValidator = getValidator();
        Method setTargetValidator = ReflectionUtils.findMethod(getClass(), "setTargetValidator", Validator.class);
        Assert.notNull(setTargetValidator, "No method(setTargetValidator) found in " + getClass());
        setTargetValidator.setAccessible(true);
        ReflectionUtils.invokeMethod(setTargetValidator, this, targetValidator);
    }

    @Override
    protected void postProcessConfiguration(Configuration<?> configuration) {
        this.configuration = configuration;
    }

    /**
     * 创建新的ValidatorFactory
     * @param validationProperties
     * @return
     */
    public ValidatorFactory createValidatorFactory(Map<String,String> validationProperties) {
        Assert.state(configuration != null, "LocalValidatorFactoryBean not initialize yet");
        validationProperties.forEach(configuration::addProperty);
        return configuration.buildValidatorFactory();
    }

}
