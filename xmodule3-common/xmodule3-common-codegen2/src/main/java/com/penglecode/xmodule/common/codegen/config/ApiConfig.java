package com.penglecode.xmodule.common.codegen.config;

/**
 * API接口代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 15:21
 */
public class ApiConfig extends GenerableTargetLocation {

    /** API接口客户端配置 */
    private ApiClientConfig clientConfig;

    /** API接口实现配置 */
    private ApiRuntimeConfig runtimeConfig;

    public ApiClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ApiClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public ApiRuntimeConfig getRuntimeConfig() {
        return runtimeConfig;
    }

    public void setRuntimeConfig(ApiRuntimeConfig runtimeConfig) {
        this.runtimeConfig = runtimeConfig;
    }

}