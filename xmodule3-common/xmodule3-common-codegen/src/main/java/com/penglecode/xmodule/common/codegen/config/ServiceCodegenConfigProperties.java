package com.penglecode.xmodule.common.codegen.config;

import org.springframework.util.Assert;

/**
 * Service代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/8/25 8:53
 */
public class ServiceCodegenConfigProperties extends MybatisCodegenConfigProperties {

    private ServiceConfigProperties service;

    public ServiceCodegenConfigProperties(String bizModule) {
        super(bizModule);
    }

    public ServiceConfigProperties getService() {
        return service;
    }

    public void setService(ServiceConfigProperties service) {
        this.service = service;
    }

    @Override
    protected void validateCodegenConfig() throws Exception {
        super.validateCodegenConfig();
        validateServiceCodegenConfig();
    }

    /**
     * 校验Service代码生成配置
     */
    protected void validateServiceCodegenConfig() {
        Assert.hasText(service.getDomain().getInterfaceConfig().getTargetProject(), String.format("Service代码生成配置(%s.service.domain.interfaceConfig.targetProject')必须指定!", getModuleCodegenConfigPrefix(getBizModule())));
        Assert.hasText(service.getDomain().getInterfaceConfig().getTargetPackage(), String.format("Service代码生成配置(%s.service.domain.interfaceConfig.targetPackage')必须指定!", getModuleCodegenConfigPrefix(getBizModule())));
        Assert.hasText(service.getDomain().getImplementConfig().getTargetProject(), String.format("Service代码生成配置(%s.service.domain.implementConfig.targetProject')必须指定!", getModuleCodegenConfigPrefix(getBizModule())));
        Assert.hasText(service.getDomain().getImplementConfig().getTargetPackage(), String.format("Service代码生成配置(%s.service.domain.implementConfig.targetPackage')必须指定!", getModuleCodegenConfigPrefix(getBizModule())));
        Assert.hasText(service.getApp().getInterfaceConfig().getTargetProject(), String.format("Service代码生成配置(%s.service.app.interfaceConfig.targetProject')必须指定!", getModuleCodegenConfigPrefix(getBizModule())));
        Assert.hasText(service.getApp().getInterfaceConfig().getTargetPackage(), String.format("Service代码生成配置(%s.service.app.interfaceConfig.targetPackage')必须指定!", getModuleCodegenConfigPrefix(getBizModule())));
        Assert.hasText(service.getApp().getImplementConfig().getTargetProject(), String.format("Service代码生成配置(%s.service.app.implementConfig.targetProject')必须指定!", getModuleCodegenConfigPrefix(getBizModule())));
        Assert.hasText(service.getApp().getImplementConfig().getTargetPackage(), String.format("Service代码生成配置(%s.service.app.implementConfig.targetPackage')必须指定!", getModuleCodegenConfigPrefix(getBizModule())));
    }

}
