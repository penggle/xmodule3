package com.penglecode.xmodule.common.codegen.config;

import org.springframework.util.Assert;

/**
 * Service代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/24 16:53
 */
public class ServiceCodegenConfigProperties extends MybatisCodegenConfigProperties {

    private ServiceConfig service;

    public ServiceCodegenConfigProperties(String module) {
        super(module);
    }

    public ServiceConfig getService() {
        return service;
    }

    public void setService(ServiceConfig service) {
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
        String codegenConfigPrefix = getCodegenConfigPrefix(getModule());
        Assert.hasText(service.getDomain().getInterfaceConfig().getTargetProject(), String.format("Service代码生成配置(%s.service.domain.interfaceConfig.targetProject)必须指定!", codegenConfigPrefix));
        Assert.hasText(service.getDomain().getInterfaceConfig().getTargetPackage(), String.format("Service代码生成配置(%s.service.domain.interfaceConfig.targetPackage)必须指定!", codegenConfigPrefix));
        Assert.hasText(service.getDomain().getImplementConfig().getTargetProject(), String.format("Service代码生成配置(%s.service.domain.implementConfig.targetProject)必须指定!", codegenConfigPrefix));
        Assert.hasText(service.getDomain().getImplementConfig().getTargetPackage(), String.format("Service代码生成配置(%s.service.domain.implementConfig.targetPackage)必须指定!", codegenConfigPrefix));
        Assert.hasText(service.getApplication().getInterfaceConfig().getTargetProject(), String.format("Service代码生成配置(%s.service.application.interfaceConfig.targetProject)必须指定!", codegenConfigPrefix));
        Assert.hasText(service.getApplication().getInterfaceConfig().getTargetPackage(), String.format("Service代码生成配置(%s.service.application.interfaceConfig.targetPackage)必须指定!", codegenConfigPrefix));
        Assert.hasText(service.getApplication().getImplementConfig().getTargetProject(), String.format("Service代码生成配置(%s.service.application.implementConfig.targetProject)必须指定!", codegenConfigPrefix));
        Assert.hasText(service.getApplication().getImplementConfig().getTargetPackage(), String.format("Service代码生成配置(%s.service.application.implementConfig.targetPackage)必须指定!", codegenConfigPrefix));
    }

}
