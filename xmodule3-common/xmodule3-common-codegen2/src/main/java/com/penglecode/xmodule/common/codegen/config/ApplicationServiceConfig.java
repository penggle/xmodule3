package com.penglecode.xmodule.common.codegen.config;

/**
 * 应用服务代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 14:55
 */
public class ApplicationServiceConfig {

    /** 应用服务接口配置 */
    private ServiceInterfaceConfig interfaceConfig;

    /** 应用服务实现配置 */
    private ServiceImplementConfig implementConfig;

    public ServiceInterfaceConfig getInterfaceConfig() {
        return interfaceConfig;
    }

    public void setInterfaceConfig(ServiceInterfaceConfig interfaceConfig) {
        this.interfaceConfig = interfaceConfig;
    }

    public ServiceImplementConfig getImplementConfig() {
        return implementConfig;
    }

    public void setImplementConfig(ServiceImplementConfig implementConfig) {
        this.implementConfig = implementConfig;
    }

}