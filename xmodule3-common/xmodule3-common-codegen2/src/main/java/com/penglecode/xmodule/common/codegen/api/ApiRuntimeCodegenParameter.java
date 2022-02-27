package com.penglecode.xmodule.common.codegen.api;

/**
 * API接口实现的代码生成参数
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/26 21:35
 */
public class ApiRuntimeCodegenParameter extends AbstractApiCodegenParameter {

    private String domainServiceName;

    private String domainServiceBeanName;

    private String domainServiceInstanceName;

    public ApiRuntimeCodegenParameter(String targetTemplateName) {
        super(targetTemplateName);
    }

    public String getDomainServiceName() {
        return domainServiceName;
    }

    public void setDomainServiceName(String domainServiceName) {
        this.domainServiceName = domainServiceName;
    }

    public String getDomainServiceBeanName() {
        return domainServiceBeanName;
    }

    public void setDomainServiceBeanName(String domainServiceBeanName) {
        this.domainServiceBeanName = domainServiceBeanName;
    }

    public String getDomainServiceInstanceName() {
        return domainServiceInstanceName;
    }

    public void setDomainServiceInstanceName(String domainServiceInstanceName) {
        this.domainServiceInstanceName = domainServiceInstanceName;
    }
}
