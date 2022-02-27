package com.penglecode.xmodule.common.initializer;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认的SpringBoot应用上下文环境初始化
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/27 16:28
 */
@Component
@Priority(0)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultSpringAppEnvInitializer implements EnvironmentPostProcessor {

    private static final String CONTEXT_INITIALIZER_CLASSES = "context.initializer.classes";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        initDefaultAppProperties(environment); //初始化应用默认的配置
    }

    protected void initDefaultAppProperties(ConfigurableEnvironment environment) {
        Map<String,Object> appDefaultProperties = new HashMap<>();
        environment.getPropertySources().addLast(new MapPropertySource(ApplicationConstants.APP_DEFAULT_PROPERTY_SOURCE_NAME, appDefaultProperties));
        if(!environment.containsProperty(CONTEXT_INITIALIZER_CLASSES)) {
            appDefaultProperties.put(CONTEXT_INITIALIZER_CLASSES, DefaultSpringAppPreInitializer.class.getName());
        }
    }

}
