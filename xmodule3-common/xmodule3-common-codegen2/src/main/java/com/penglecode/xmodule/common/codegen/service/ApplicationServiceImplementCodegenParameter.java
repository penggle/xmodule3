package com.penglecode.xmodule.common.codegen.service;

/**
 * 聚合根对应的应用服务实现代码生成参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 21:48
 */
public class ApplicationServiceImplementCodegenParameter extends AbstractApplicationServiceCodegenParameter {

    /** 应用服务所依赖的领域服务集合 */
    private DomainServiceParameters domainServices;

    /** 事务管理器的bean名称 */
    private String transactionManagerName;

    /** 创建聚合根的方法参数 */
    private ApplicationServiceMethodParameter prepareAggregateObjects;

    public ApplicationServiceImplementCodegenParameter(String targetTemplateName) {
        super(targetTemplateName);
    }

    public DomainServiceParameters getDomainServices() {
        return domainServices;
    }

    public void setDomainServices(DomainServiceParameters domainServices) {
        this.domainServices = domainServices;
    }

    public String getTransactionManagerName() {
        return transactionManagerName;
    }

    public void setTransactionManagerName(String transactionManagerName) {
        this.transactionManagerName = transactionManagerName;
    }

    public ApplicationServiceMethodParameter getPrepareAggregateObjects() {
        return prepareAggregateObjects;
    }

    public void setPrepareAggregateObjects(ApplicationServiceMethodParameter prepareAggregateObjects) {
        this.prepareAggregateObjects = prepareAggregateObjects;
    }

}