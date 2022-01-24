package com.penglecode.xmodule.common.codegen.config;

import org.springframework.util.Assert;

/**
 * Api代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/24 16:46
 */
public class ApiCodegenConfigProperties extends ServiceCodegenConfigProperties {

    private ApiConfig api;

    public ApiCodegenConfigProperties(String module) {
        super(module);
    }

    public ApiConfig getApi() {
        return api;
    }

    public void setApi(ApiConfig api) {
        this.api = api;
    }

    @Override
    protected void validateCodegenConfig() throws Exception {
        super.validateCodegenConfig();
        validateApiCodegenConfig();
    }

    @Override
    protected void initCodegenConfig() throws Exception {
        super.initCodegenConfig();
        initApiCodegenConfig();
    }

    /**
     * 校验Api代码生成配置
     */
    protected void validateApiCodegenConfig() {
        String codegenConfigPrefix = getCodegenConfigPrefix(getModule());
        //忽略xxx.api.targetProject的配置
        Assert.hasText(api.getClientConfig().getTargetProject(), String.format("Api代码生成配置(%s.api.clientConfig.targetProject')必须指定!", codegenConfigPrefix));
        Assert.hasText(api.getRuntimeConfig().getTargetProject(), String.format("Api代码生成配置(%s.api.runtimeConfig.targetProject')必须指定!", codegenConfigPrefix));
        Assert.hasText(api.getTargetPackage(), String.format("Api代码生成配置(%s.api.targetPackage')必须指定!", codegenConfigPrefix));
        Assert.hasText(api.getTargetProject(), String.format("Api代码生成配置(%s.api.clientConfig.targetProject')必须指定!", codegenConfigPrefix));
    }

    /**
     * 初始化Api代码生成配置
     */
    protected void initApiCodegenConfig() {

    }

}
