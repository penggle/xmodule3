package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.support.CodegenParameter;
import com.penglecode.xmodule.common.codegen.support.DomainObjectParameter;

/**
 * 聚合根对应的应用服务代码生成参数
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/6 21:48
 */
public abstract class AbstractApplicationServiceCodegenParameter extends CodegenParameter {

    /** 对应的领域对象(聚合根)参数 */
    private DomainObjectParameter domainObjectParameter;

    /** 创建聚合根的方法参数 */
    private ApplicationServiceMethodParameter createDomainObject;

    /** 根据ID修改聚合根的方法参数 */
    private ApplicationServiceMethodParameter modifyDomainObjectById;

    /** 根据ID删除聚合根的方法参数 */
    private ApplicationServiceMethodParameter removeDomainObjectById;

    /** 根据多个ID删除聚合根的方法参数 */
    private ApplicationServiceMethodParameter removeDomainObjectsByIds;

    /** 根据ID获取聚合根的方法参数 */
    private ApplicationServiceMethodParameter getDomainObjectById;

    /** 根据多个ID获取聚合根的方法参数 */
    private ApplicationServiceMethodParameter getDomainObjectsByIds;

    /** 根据条件分页查询聚合根的方法参数 */
    private ApplicationServiceMethodParameter getDomainObjectsByPage;

    /** 获取聚合根总数的方法参数 */
    private ApplicationServiceMethodParameter getDomainObjectTotalCount;

    /** 基于游标遍历所有聚合根的方法参数 */
    private ApplicationServiceMethodParameter forEachDomainObject1;

    /** 基于游标遍历所有聚合根的方法参数 */
    private ApplicationServiceMethodParameter forEachDomainObject2;

    public AbstractApplicationServiceCodegenParameter(String targetTemplateName) {
        super(targetTemplateName);
    }

    public DomainObjectParameter getDomainObjectParameter() {
        return domainObjectParameter;
    }

    public void setDomainObjectParameter(DomainObjectParameter domainObjectParameter) {
        this.domainObjectParameter = domainObjectParameter;
    }

    public ApplicationServiceMethodParameter getCreateDomainObject() {
        return createDomainObject;
    }

    public void setCreateDomainObject(ApplicationServiceMethodParameter createDomainObject) {
        this.createDomainObject = createDomainObject;
    }

    public ApplicationServiceMethodParameter getModifyDomainObjectById() {
        return modifyDomainObjectById;
    }

    public void setModifyDomainObjectById(ApplicationServiceMethodParameter modifyDomainObjectById) {
        this.modifyDomainObjectById = modifyDomainObjectById;
    }

    public ApplicationServiceMethodParameter getRemoveDomainObjectById() {
        return removeDomainObjectById;
    }

    public void setRemoveDomainObjectById(ApplicationServiceMethodParameter removeDomainObjectById) {
        this.removeDomainObjectById = removeDomainObjectById;
    }

    public ApplicationServiceMethodParameter getRemoveDomainObjectsByIds() {
        return removeDomainObjectsByIds;
    }

    public void setRemoveDomainObjectsByIds(ApplicationServiceMethodParameter removeDomainObjectsByIds) {
        this.removeDomainObjectsByIds = removeDomainObjectsByIds;
    }

    public ApplicationServiceMethodParameter getGetDomainObjectById() {
        return getDomainObjectById;
    }

    public void setGetDomainObjectById(ApplicationServiceMethodParameter getDomainObjectById) {
        this.getDomainObjectById = getDomainObjectById;
    }

    public ApplicationServiceMethodParameter getGetDomainObjectsByIds() {
        return getDomainObjectsByIds;
    }

    public void setGetDomainObjectsByIds(ApplicationServiceMethodParameter getDomainObjectsByIds) {
        this.getDomainObjectsByIds = getDomainObjectsByIds;
    }

    public ApplicationServiceMethodParameter getGetDomainObjectsByPage() {
        return getDomainObjectsByPage;
    }

    public void setGetDomainObjectsByPage(ApplicationServiceMethodParameter getDomainObjectsByPage) {
        this.getDomainObjectsByPage = getDomainObjectsByPage;
    }

    public ApplicationServiceMethodParameter getGetDomainObjectTotalCount() {
        return getDomainObjectTotalCount;
    }

    public void setGetDomainObjectTotalCount(ApplicationServiceMethodParameter getDomainObjectTotalCount) {
        this.getDomainObjectTotalCount = getDomainObjectTotalCount;
    }

    public ApplicationServiceMethodParameter getForEachDomainObject1() {
        return forEachDomainObject1;
    }

    public void setForEachDomainObject1(ApplicationServiceMethodParameter forEachDomainObject1) {
        this.forEachDomainObject1 = forEachDomainObject1;
    }

    public ApplicationServiceMethodParameter getForEachDomainObject2() {
        return forEachDomainObject2;
    }

    public void setForEachDomainObject2(ApplicationServiceMethodParameter forEachDomainObject2) {
        this.forEachDomainObject2 = forEachDomainObject2;
    }

}