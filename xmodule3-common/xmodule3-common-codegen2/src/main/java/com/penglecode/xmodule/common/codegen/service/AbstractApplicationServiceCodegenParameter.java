package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.support.CodegenParameter;

/**
 * 聚合根对应的应用服务代码生成参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 21:48
 */
public abstract class AbstractApplicationServiceCodegenParameter extends CodegenParameter {

    /** 创建聚合根的方法参数 */
    private ApplicationServiceMethod createDomainObject;

    /** 根据ID修改聚合根的方法参数 */
    private ApplicationServiceMethod modifyDomainObjectById;

    /** 根据ID删除聚合根的方法参数 */
    private ApplicationServiceMethod removeDomainObjectById;

    /** 根据多个ID删除聚合根的方法参数 */
    private ApplicationServiceMethod removeDomainObjectsByIds;

    /** 根据ID获取聚合根的方法参数 */
    private ApplicationServiceMethod getDomainObjectById;

    /** 根据多个ID获取聚合根的方法参数 */
    private ApplicationServiceMethod getDomainObjectsByIds;

    /** 根据条件分页查询聚合根的方法参数 */
    private ApplicationServiceMethod getDomainObjectsByPage;

    /** 获取聚合根总数的方法参数 */
    private ApplicationServiceMethod getDomainObjectTotalCount;

    /** 基于游标遍历所有聚合根的方法参数 */
    private ApplicationServiceMethod forEachDomainObject1;

    /** 基于游标遍历所有聚合根的方法参数 */
    private ApplicationServiceMethod forEachDomainObject2;

    public AbstractApplicationServiceCodegenParameter(String targetTemplateName) {
        super(targetTemplateName);
    }

    public ApplicationServiceMethod getCreateDomainObject() {
        return createDomainObject;
    }

    public void setCreateDomainObject(ApplicationServiceMethod createDomainObject) {
        this.createDomainObject = createDomainObject;
    }

    public ApplicationServiceMethod getModifyDomainObjectById() {
        return modifyDomainObjectById;
    }

    public void setModifyDomainObjectById(ApplicationServiceMethod modifyDomainObjectById) {
        this.modifyDomainObjectById = modifyDomainObjectById;
    }

    public ApplicationServiceMethod getRemoveDomainObjectById() {
        return removeDomainObjectById;
    }

    public void setRemoveDomainObjectById(ApplicationServiceMethod removeDomainObjectById) {
        this.removeDomainObjectById = removeDomainObjectById;
    }

    public ApplicationServiceMethod getRemoveDomainObjectsByIds() {
        return removeDomainObjectsByIds;
    }

    public void setRemoveDomainObjectsByIds(ApplicationServiceMethod removeDomainObjectsByIds) {
        this.removeDomainObjectsByIds = removeDomainObjectsByIds;
    }

    public ApplicationServiceMethod getGetDomainObjectById() {
        return getDomainObjectById;
    }

    public void setGetDomainObjectById(ApplicationServiceMethod getDomainObjectById) {
        this.getDomainObjectById = getDomainObjectById;
    }

    public ApplicationServiceMethod getGetDomainObjectsByIds() {
        return getDomainObjectsByIds;
    }

    public void setGetDomainObjectsByIds(ApplicationServiceMethod getDomainObjectsByIds) {
        this.getDomainObjectsByIds = getDomainObjectsByIds;
    }

    public ApplicationServiceMethod getGetDomainObjectsByPage() {
        return getDomainObjectsByPage;
    }

    public void setGetDomainObjectsByPage(ApplicationServiceMethod getDomainObjectsByPage) {
        this.getDomainObjectsByPage = getDomainObjectsByPage;
    }

    public ApplicationServiceMethod getGetDomainObjectTotalCount() {
        return getDomainObjectTotalCount;
    }

    public void setGetDomainObjectTotalCount(ApplicationServiceMethod getDomainObjectTotalCount) {
        this.getDomainObjectTotalCount = getDomainObjectTotalCount;
    }

    public ApplicationServiceMethod getForEachDomainObject1() {
        return forEachDomainObject1;
    }

    public void setForEachDomainObject1(ApplicationServiceMethod forEachDomainObject1) {
        this.forEachDomainObject1 = forEachDomainObject1;
    }

    public ApplicationServiceMethod getForEachDomainObject2() {
        return forEachDomainObject2;
    }

    public void setForEachDomainObject2(ApplicationServiceMethod forEachDomainObject2) {
        this.forEachDomainObject2 = forEachDomainObject2;
    }

}