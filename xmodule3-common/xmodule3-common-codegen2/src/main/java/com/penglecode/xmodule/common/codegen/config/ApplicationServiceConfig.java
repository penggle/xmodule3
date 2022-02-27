package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.support.ServiceType;

/**
 * 应用服务代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/22 14:55
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
        if(this.interfaceConfig != null) {
            this.interfaceConfig.setServiceType(ServiceType.APPLICATION_SERVICE);
        }
    }

    public ServiceImplementConfig getImplementConfig() {
        return implementConfig;
    }

    public void setImplementConfig(ServiceImplementConfig implementConfig) {
        this.implementConfig = implementConfig;
        if(this.interfaceConfig != null) {
            this.interfaceConfig.setServiceType(ServiceType.APPLICATION_SERVICE);
        }
    }

}