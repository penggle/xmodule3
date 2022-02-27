package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.support.CodegenParameter;
import com.penglecode.xmodule.common.codegen.support.DomainObjectParameter;

import java.util.Map;

/**
 * 领域实体对应的领域服务代码生成参数
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/6 21:48
 */
public abstract class AbstractDomainServiceCodegenParameter extends CodegenParameter {

    /** 对应的领域对象(领域实体)参数 */
    private DomainObjectParameter domainObjectParameter;

    /** 创建领域对象的方法参数 */
    private DomainServiceMethodParameter createDomainObject;

    /** 批量的创建领域对象的方法参数 */
    private DomainServiceMethodParameter batchCreateDomainObjects;

    /** 根据ID修改领域对象的方法参数 */
    private DomainServiceMethodParameter modifyDomainObjectById;

    /** 批量的根据ID修改领域对象的方法参数 */
    private DomainServiceMethodParameter batchModifyDomainObjectsById;

    /** 根据ID删除领域对象的方法参数 */
    private DomainServiceMethodParameter removeDomainObjectById;

    /** 根据多个ID删除领域对象的方法参数 */
    private DomainServiceMethodParameter removeDomainObjectsByIds;

    /** 根据某master领域对象ID删除当前领域对象(slave)的方法参数，其中Map.key为MasterId */
    private Map<String, ByMasterIdDomainServiceMethodParameter> removeDomainObjectsByXxxMasterId;

    /** 根据ID获取领域对象的方法参数 */
    private DomainServiceMethodParameter getDomainObjectById;

    /** 根据多个ID获取领域对象的方法参数 */
    private DomainServiceMethodParameter getDomainObjectsByIds;

    /** 根据某master领域对象ID获取当前领域对象(slave)的方法参数，其中Map.key为MasterId */
    private Map<String, ByMasterIdDomainServiceMethodParameter> getDomainObjectsByXxxMasterId;

    /** 根据多个master领域对象ID获取当前领域对象(slave)的方法参数，其中Map.key为MasterIds */
    private Map<String, ByMasterIdDomainServiceMethodParameter> getDomainObjectsByXxxMasterIds;

    /** 根据条件分页查询领域对象的方法参数 */
    private DomainServiceMethodParameter getDomainObjectsByPage;

    /** 获取领域对象总数的方法参数 */
    private DomainServiceMethodParameter getDomainObjectTotalCount;

    /** 基于游标遍历所有领域对象的方法参数 */
    private DomainServiceMethodParameter forEachDomainObject1;

    /** 基于游标遍历所有领域对象的方法参数 */
    private DomainServiceMethodParameter forEachDomainObject2;

    public AbstractDomainServiceCodegenParameter(String targetTemplateName) {
        super(targetTemplateName);
    }

    public DomainObjectParameter getDomainObjectParameter() {
        return domainObjectParameter;
    }

    public void setDomainObjectParameter(DomainObjectParameter domainObjectParameter) {
        this.domainObjectParameter = domainObjectParameter;
    }

    public DomainServiceMethodParameter getCreateDomainObject() {
        return createDomainObject;
    }

    public void setCreateDomainObject(DomainServiceMethodParameter createDomainObject) {
        this.createDomainObject = createDomainObject;
    }

    public DomainServiceMethodParameter getBatchCreateDomainObjects() {
        return batchCreateDomainObjects;
    }

    public void setBatchCreateDomainObjects(DomainServiceMethodParameter batchCreateDomainObjects) {
        this.batchCreateDomainObjects = batchCreateDomainObjects;
    }

    public DomainServiceMethodParameter getModifyDomainObjectById() {
        return modifyDomainObjectById;
    }

    public void setModifyDomainObjectById(DomainServiceMethodParameter modifyDomainObjectById) {
        this.modifyDomainObjectById = modifyDomainObjectById;
    }

    public DomainServiceMethodParameter getBatchModifyDomainObjectsById() {
        return batchModifyDomainObjectsById;
    }

    public void setBatchModifyDomainObjectsById(DomainServiceMethodParameter batchModifyDomainObjectsById) {
        this.batchModifyDomainObjectsById = batchModifyDomainObjectsById;
    }

    public DomainServiceMethodParameter getRemoveDomainObjectById() {
        return removeDomainObjectById;
    }

    public void setRemoveDomainObjectById(DomainServiceMethodParameter removeDomainObjectById) {
        this.removeDomainObjectById = removeDomainObjectById;
    }

    public DomainServiceMethodParameter getRemoveDomainObjectsByIds() {
        return removeDomainObjectsByIds;
    }

    public void setRemoveDomainObjectsByIds(DomainServiceMethodParameter removeDomainObjectsByIds) {
        this.removeDomainObjectsByIds = removeDomainObjectsByIds;
    }

    public Map<String, ByMasterIdDomainServiceMethodParameter> getRemoveDomainObjectsByXxxMasterId() {
        return removeDomainObjectsByXxxMasterId;
    }

    public void setRemoveDomainObjectsByXxxMasterId(Map<String, ByMasterIdDomainServiceMethodParameter> removeDomainObjectsByXxxMasterId) {
        this.removeDomainObjectsByXxxMasterId = removeDomainObjectsByXxxMasterId;
    }

    public DomainServiceMethodParameter getGetDomainObjectById() {
        return getDomainObjectById;
    }

    public void setGetDomainObjectById(DomainServiceMethodParameter getDomainObjectById) {
        this.getDomainObjectById = getDomainObjectById;
    }

    public DomainServiceMethodParameter getGetDomainObjectsByIds() {
        return getDomainObjectsByIds;
    }

    public void setGetDomainObjectsByIds(DomainServiceMethodParameter getDomainObjectsByIds) {
        this.getDomainObjectsByIds = getDomainObjectsByIds;
    }

    public Map<String, ByMasterIdDomainServiceMethodParameter> getGetDomainObjectsByXxxMasterId() {
        return getDomainObjectsByXxxMasterId;
    }

    public void setGetDomainObjectsByXxxMasterId(Map<String, ByMasterIdDomainServiceMethodParameter> getDomainObjectsByXxxMasterId) {
        this.getDomainObjectsByXxxMasterId = getDomainObjectsByXxxMasterId;
    }

    public Map<String, ByMasterIdDomainServiceMethodParameter> getGetDomainObjectsByXxxMasterIds() {
        return getDomainObjectsByXxxMasterIds;
    }

    public void setGetDomainObjectsByXxxMasterIds(Map<String, ByMasterIdDomainServiceMethodParameter> getDomainObjectsByXxxMasterIds) {
        this.getDomainObjectsByXxxMasterIds = getDomainObjectsByXxxMasterIds;
    }

    public DomainServiceMethodParameter getGetDomainObjectsByPage() {
        return getDomainObjectsByPage;
    }

    public void setGetDomainObjectsByPage(DomainServiceMethodParameter getDomainObjectsByPage) {
        this.getDomainObjectsByPage = getDomainObjectsByPage;
    }

    public DomainServiceMethodParameter getGetDomainObjectTotalCount() {
        return getDomainObjectTotalCount;
    }

    public void setGetDomainObjectTotalCount(DomainServiceMethodParameter getDomainObjectTotalCount) {
        this.getDomainObjectTotalCount = getDomainObjectTotalCount;
    }

    public DomainServiceMethodParameter getForEachDomainObject1() {
        return forEachDomainObject1;
    }

    public void setForEachDomainObject1(DomainServiceMethodParameter forEachDomainObject1) {
        this.forEachDomainObject1 = forEachDomainObject1;
    }

    public DomainServiceMethodParameter getForEachDomainObject2() {
        return forEachDomainObject2;
    }

    public void setForEachDomainObject2(DomainServiceMethodParameter forEachDomainObject2) {
        this.forEachDomainObject2 = forEachDomainObject2;
    }

}