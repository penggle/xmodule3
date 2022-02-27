package com.penglecode.xmodule.common.util;


import com.penglecode.xmodule.common.validation.validator.CustomValidatorFactoryBean;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 基于Spring容器环境的Validator工具类
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/6/18 23:50
 */
public class ValidatorUtils {

    public static final String PROPERTY_NAME_HIBERNATE_VALIDATOR_FAIL_FAST = "hibernate.validator.fail_fast";

    private static final ConcurrentMap<Boolean,ValidatorFactory> failFastValidatorFactorys = new ConcurrentHashMap<>();

    private static volatile CustomValidatorFactoryBean defaultValidatorFactoryBean;

    private static final Object MUTEX = new Object();

    private ValidatorUtils() {}

    protected static CustomValidatorFactoryBean getDefaultValidatorFactoryBean() {
        if(defaultValidatorFactoryBean == null) {
            synchronized (MUTEX) {
                if(defaultValidatorFactoryBean == null) {
                    //bean的名字是约定好的，见springboot关于validation的自动配置
                    defaultValidatorFactoryBean = SpringUtils.getBean("defaultValidator");
                }
            }
        }
        return defaultValidatorFactoryBean;
    }

    /**
     * 获取默认的Validator
     * @return
     */
    public static Validator getDefaultValidator() {
        return getDefaultValidatorFactoryBean();
    }

    /**
     * 获取默认的ValidatorFactory
     * @return
     */
    public static ValidatorFactory getDefaultValidatorFactory() {
        return getDefaultValidatorFactoryBean();
    }

    public static ValidatorFactory getValidatorFactory(boolean failFast) {
        return failFastValidatorFactorys.computeIfAbsent(failFast, key -> createValidatorFactory(Collections.singletonMap(PROPERTY_NAME_HIBERNATE_VALIDATOR_FAIL_FAST, String.valueOf(failFast))));
    }

    public static ValidatorFactory createValidatorFactory(Map<String,String> validationProperties) {
        return getDefaultValidatorFactoryBean().createValidatorFactory(validationProperties);
    }

}
