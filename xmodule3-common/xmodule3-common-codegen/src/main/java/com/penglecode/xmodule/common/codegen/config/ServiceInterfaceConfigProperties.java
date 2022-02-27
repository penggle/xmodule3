package com.penglecode.xmodule.common.codegen.config;

/**
 * Service接口配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/8/21 13:39
 */
public class ServiceInterfaceConfigProperties extends GeneratedTargetConfigProperties {

    @Override
    public <T extends GeneratedTargetConfigProperties> String getGeneratedTargetName(DomainBoundedTargetConfigProperties<T> targetConfig, boolean includePackage, boolean includeSuffix) {
        if(targetConfig.getDomainAggregateConfig() == null) { //生成领域服务
            return (includePackage ? getTargetPackage() + "." : "") + targetConfig.getDomainObjectConfig().getDomainObjectName() + "Service" + (includeSuffix ? ".java" : "");
        } else { //生成应用服务
            return (includePackage ? getTargetPackage() + "." : "") + targetConfig.getDomainAggregateConfig().getAggregateObjectName() + "AppService" + (includeSuffix ? ".java" : "");
        }
    }

}
