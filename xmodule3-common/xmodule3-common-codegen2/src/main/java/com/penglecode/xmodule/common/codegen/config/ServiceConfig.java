package com.penglecode.xmodule.common.codegen.config;

/**
 * 领域服务/应用服务配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 14:52
 */
public class ServiceConfig {

    /** 领域服务配置 */
    private DomainServiceConfig domain;

    /** 应用服务配置 */
    private ApplicationServiceConfig application;

    public DomainServiceConfig getDomain() {
        return domain;
    }

    public void setDomain(DomainServiceConfig domain) {
        this.domain = domain;
    }

    public ApplicationServiceConfig getApplication() {
        return application;
    }

    public void setApplication(ApplicationServiceConfig application) {
        this.application = application;
    }

}
