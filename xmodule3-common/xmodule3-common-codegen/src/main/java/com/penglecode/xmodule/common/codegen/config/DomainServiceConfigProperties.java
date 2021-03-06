package com.penglecode.xmodule.common.codegen.config;

/**
 * 领域服务代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/8/25 10:40
 */
public class DomainServiceConfigProperties {

    /**
     * Service接口配置
     */
    private ServiceInterfaceConfigProperties interfaceConfig;

    /**
     * Service实现配置
     */
    private ServiceImplementConfigProperties implementConfig;

    public ServiceInterfaceConfigProperties getInterfaceConfig() {
        return interfaceConfig;
    }

    public void setInterfaceConfig(ServiceInterfaceConfigProperties interfaceConfig) {
        this.interfaceConfig = interfaceConfig;
    }

    public ServiceImplementConfigProperties getImplementConfig() {
        return implementConfig;
    }

    public void setImplementConfig(ServiceImplementConfigProperties implementConfig) {
        this.implementConfig = implementConfig;
    }

}
