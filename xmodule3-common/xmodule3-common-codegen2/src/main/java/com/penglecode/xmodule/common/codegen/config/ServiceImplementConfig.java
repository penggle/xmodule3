package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.support.ServiceType;

/**
 * Service实现配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 14:51
 */
public class ServiceImplementConfig extends GenerableTargetConfig {

    private ServiceType serviceType;

    protected void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix) {
        String endName = ServiceType.APPLICATION_SERVICE.equals(serviceType) ? "AppServiceImpl" : "ServiceImpl";
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + endName + (includeSuffix ? ".java" : "");
    }

}
