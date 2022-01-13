package com.penglecode.xmodule.common.config;

import com.penglecode.xmodule.BasePackage;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * swagger配置项
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/4/13 23:01
 */
@Component
@ConfigurationProperties(prefix="swagger")
public class SwaggerConfigProperties {

    /**
     * 是否启用Swagger
     */
    private boolean enabled;

    /**
     * 扫描Swagger注解的基础包名
     */
    private String basePackage = BasePackage.class.getPackage().getName();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

}