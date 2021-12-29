package com.penglecode.xmodule.common.codegen.config;

/**
 * Service代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/8/25 10:40
 */
public class ServiceConfigProperties {

    /**
     * 领域服务代码生成配置
     */
    private DomainServiceConfigProperties domain;

    /**
     * 应用服务代码生成配置
     */
    private AppServiceConfigProperties app;

    public DomainServiceConfigProperties getDomain() {
        return domain;
    }

    public void setDomain(DomainServiceConfigProperties domain) {
        this.domain = domain;
    }

    public AppServiceConfigProperties getApp() {
        return app;
    }

    public void setApp(AppServiceConfigProperties app) {
        this.app = app;
    }

}