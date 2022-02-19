package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.support.CodegenParameter;

import java.util.Map;

/**
 * 领域实体对应的领域服务代码生成参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 21:48
 */
public abstract class AbstractDomainServiceCodegenParameter extends CodegenParameter {

    /** 创建领域对象的方法参数 */
    private DomainServiceMethod createDomainObject;

    /** 批量的创建领域对象的方法参数 */
    private DomainServiceMethod batchCreateDomainObjects;

    /** 根据ID修改领域对象的方法参数 */
    private DomainServiceMethod modifyDomainObjectById;

    /** 批量的根据ID修改领域对象的方法参数 */
    private DomainServiceMethod batchModifyDomainObjectsById;

    /** 根据ID删除领域对象的方法参数 */
    private DomainServiceMethod removeDomainObjectById;

    /** 根据多个ID删除领域对象的方法参数 */
    private DomainServiceMethod removeDomainObjectsByIds;

    /** 根据某master领域对象ID删除当前领域对象(slave)的方法参数，其中Map.key为MasterId */
    private Map<String,ByMasterIdDomainServiceMethod> removeDomainObjectsByXxxMasterId;

    /** 根据ID获取领域对象的方法参数 */
    private DomainServiceMethod getDomainObjectById;

    /** 根据多个ID获取领域对象的方法参数 */
    private DomainServiceMethod getDomainObjectsByIds;

    /** 根据某master领域对象ID获取当前领域对象(slave)的方法参数，其中Map.key为MasterId */
    private Map<String,ByMasterIdDomainServiceMethod> getDomainObjectsByXxxMasterId;

    /** 根据多个master领域对象ID获取当前领域对象(slave)的方法参数，其中Map.key为MasterIds */
    private Map<String,ByMasterIdDomainServiceMethod> getDomainObjectsByXxxMasterIds;

    /** 根据条件分页查询领域对象的方法参数 */
    private DomainServiceMethod getDomainObjectsByPage;

    /** 获取领域对象总数的方法参数 */
    private DomainServiceMethod getDomainObjectTotalCount;

    /** 基于游标遍历所有领域对象的方法参数 */
    private DomainServiceMethod forEachDomainObject1;

    /** 基于游标遍历所有领域对象的方法参数 */
    private DomainServiceMethod forEachDomainObject2;

    public AbstractDomainServiceCodegenParameter(String targetTemplateName) {
        super(targetTemplateName);
    }

    public DomainServiceMethod getCreateDomainObject() {
        return createDomainObject;
    }

    public void setCreateDomainObject(DomainServiceMethod createDomainObject) {
        this.createDomainObject = createDomainObject;
    }

    public DomainServiceMethod getBatchCreateDomainObjects() {
        return batchCreateDomainObjects;
    }

    public void setBatchCreateDomainObjects(DomainServiceMethod batchCreateDomainObjects) {
        this.batchCreateDomainObjects = batchCreateDomainObjects;
    }

    public DomainServiceMethod getModifyDomainObjectById() {
        return modifyDomainObjectById;
    }

    public void setModifyDomainObjectById(DomainServiceMethod modifyDomainObjectById) {
        this.modifyDomainObjectById = modifyDomainObjectById;
    }

    public DomainServiceMethod getBatchModifyDomainObjectsById() {
        return batchModifyDomainObjectsById;
    }

    public void setBatchModifyDomainObjectsById(DomainServiceMethod batchModifyDomainObjectsById) {
        this.batchModifyDomainObjectsById = batchModifyDomainObjectsById;
    }

    public DomainServiceMethod getRemoveDomainObjectById() {
        return removeDomainObjectById;
    }

    public void setRemoveDomainObjectById(DomainServiceMethod removeDomainObjectById) {
        this.removeDomainObjectById = removeDomainObjectById;
    }

    public DomainServiceMethod getRemoveDomainObjectsByIds() {
        return removeDomainObjectsByIds;
    }

    public void setRemoveDomainObjectsByIds(DomainServiceMethod removeDomainObjectsByIds) {
        this.removeDomainObjectsByIds = removeDomainObjectsByIds;
    }

    public Map<String, ByMasterIdDomainServiceMethod> getRemoveDomainObjectsByXxxMasterId() {
        return removeDomainObjectsByXxxMasterId;
    }

    public void setRemoveDomainObjectsByXxxMasterId(Map<String, ByMasterIdDomainServiceMethod> removeDomainObjectsByXxxMasterId) {
        this.removeDomainObjectsByXxxMasterId = removeDomainObjectsByXxxMasterId;
    }

    public DomainServiceMethod getGetDomainObjectById() {
        return getDomainObjectById;
    }

    public void setGetDomainObjectById(DomainServiceMethod getDomainObjectById) {
        this.getDomainObjectById = getDomainObjectById;
    }

    public DomainServiceMethod getGetDomainObjectsByIds() {
        return getDomainObjectsByIds;
    }

    public void setGetDomainObjectsByIds(DomainServiceMethod getDomainObjectsByIds) {
        this.getDomainObjectsByIds = getDomainObjectsByIds;
    }

    public Map<String, ByMasterIdDomainServiceMethod> getGetDomainObjectsByXxxMasterId() {
        return getDomainObjectsByXxxMasterId;
    }

    public void setGetDomainObjectsByXxxMasterId(Map<String, ByMasterIdDomainServiceMethod> getDomainObjectsByXxxMasterId) {
        this.getDomainObjectsByXxxMasterId = getDomainObjectsByXxxMasterId;
    }

    public Map<String, ByMasterIdDomainServiceMethod> getGetDomainObjectsByXxxMasterIds() {
        return getDomainObjectsByXxxMasterIds;
    }

    public void setGetDomainObjectsByXxxMasterIds(Map<String, ByMasterIdDomainServiceMethod> getDomainObjectsByXxxMasterIds) {
        this.getDomainObjectsByXxxMasterIds = getDomainObjectsByXxxMasterIds;
    }

    public DomainServiceMethod getGetDomainObjectsByPage() {
        return getDomainObjectsByPage;
    }

    public void setGetDomainObjectsByPage(DomainServiceMethod getDomainObjectsByPage) {
        this.getDomainObjectsByPage = getDomainObjectsByPage;
    }

    public DomainServiceMethod getGetDomainObjectTotalCount() {
        return getDomainObjectTotalCount;
    }

    public void setGetDomainObjectTotalCount(DomainServiceMethod getDomainObjectTotalCount) {
        this.getDomainObjectTotalCount = getDomainObjectTotalCount;
    }

    public DomainServiceMethod getForEachDomainObject1() {
        return forEachDomainObject1;
    }

    public void setForEachDomainObject1(DomainServiceMethod forEachDomainObject1) {
        this.forEachDomainObject1 = forEachDomainObject1;
    }

    public DomainServiceMethod getForEachDomainObject2() {
        return forEachDomainObject2;
    }

    public void setForEachDomainObject2(DomainServiceMethod forEachDomainObject2) {
        this.forEachDomainObject2 = forEachDomainObject2;
    }

}