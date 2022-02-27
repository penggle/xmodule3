package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.support.CodegenParameter;
import com.penglecode.xmodule.common.codegen.support.DomainObjectParameter;

/**
 * API接口代码生成参数基类
 *
 * @author  pengpeng
 * @version 1.0
 * @created 2021/2/20 13:58
 */
public abstract class AbstractApiCodegenParameter extends CodegenParameter {

    /** 是否是聚合根 */
    private Boolean aggregateRoot;

    /** 对应的领域对象参数 */
    private DomainObjectParameter domainObjectParameter;

    /** 创建领域对象的API方法参数 */
    private ApiMethodParameter createDomainObject;

    /** 根据ID修改领域对象的API方法参数 */
    private ApiMethodParameter modifyDomainObjectById;

    /** 根据ID删除领域对象的API方法参数 */
    private ApiMethodParameter removeDomainObjectById;

    /** 根据多个ID删除领域对象的API方法参数 */
    private ApiMethodParameter removeDomainObjectsByIds;

    /** 根据ID获取领域对象的API方法参数 */
    private ApiMethodParameter getDomainObjectById;

    /** 根据多个ID获取领域对象的API方法参数 */
    private ApiMethodParameter getDomainObjectsByIds;

    /** 根据条件分页查询领域对象的API方法参数 */
    private ApiMethodParameter getDomainObjectsByPage;

    public AbstractApiCodegenParameter(String targetTemplateName) {
        super(targetTemplateName);
    }

    public Boolean getAggregateRoot() {
        return aggregateRoot;
    }

    public void setAggregateRoot(Boolean aggregateRoot) {
        this.aggregateRoot = aggregateRoot;
    }

    public DomainObjectParameter getDomainObjectParameter() {
        return domainObjectParameter;
    }

    public void setDomainObjectParameter(DomainObjectParameter domainObjectParameter) {
        this.domainObjectParameter = domainObjectParameter;
    }

    public ApiMethodParameter getCreateDomainObject() {
        return createDomainObject;
    }

    public void setCreateDomainObject(ApiMethodParameter createDomainObject) {
        this.createDomainObject = createDomainObject;
    }

    public ApiMethodParameter getModifyDomainObjectById() {
        return modifyDomainObjectById;
    }

    public void setModifyDomainObjectById(ApiMethodParameter modifyDomainObjectById) {
        this.modifyDomainObjectById = modifyDomainObjectById;
    }

    public ApiMethodParameter getRemoveDomainObjectById() {
        return removeDomainObjectById;
    }

    public void setRemoveDomainObjectById(ApiMethodParameter removeDomainObjectById) {
        this.removeDomainObjectById = removeDomainObjectById;
    }

    public ApiMethodParameter getRemoveDomainObjectsByIds() {
        return removeDomainObjectsByIds;
    }

    public void setRemoveDomainObjectsByIds(ApiMethodParameter removeDomainObjectsByIds) {
        this.removeDomainObjectsByIds = removeDomainObjectsByIds;
    }

    public ApiMethodParameter getGetDomainObjectById() {
        return getDomainObjectById;
    }

    public void setGetDomainObjectById(ApiMethodParameter getDomainObjectById) {
        this.getDomainObjectById = getDomainObjectById;
    }

    public ApiMethodParameter getGetDomainObjectsByIds() {
        return getDomainObjectsByIds;
    }

    public void setGetDomainObjectsByIds(ApiMethodParameter getDomainObjectsByIds) {
        this.getDomainObjectsByIds = getDomainObjectsByIds;
    }

    public ApiMethodParameter getGetDomainObjectsByPage() {
        return getDomainObjectsByPage;
    }

    public void setGetDomainObjectsByPage(ApiMethodParameter getDomainObjectsByPage) {
        this.getDomainObjectsByPage = getDomainObjectsByPage;
    }
    
}
