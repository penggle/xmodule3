package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.support.DomainObjectParameter;

/**
 * 根据MasterId的一些领域方法参数
 * 例如：removeDomainObjectsByXxxMasterId,getDomainObjectsByXxxMasterId,getDomainObjectsByXxxMasterIds
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 22:31
 */
public class ByMasterIdDomainServiceMethodParameter extends DomainServiceMethodParameter {

    /** Master方的领域对象参数 */
    private DomainObjectParameter masterDomainObjectParameter;

    /** Master领域对象在当前领域对象(slave)中的关联字段名 */
    private String masterIdNameOfSlave;

    /** Master领域对象在当前领域对象(slave)中的关联字段名(首字母大写) */
    private String upperMasterIdNameOfSlave;

    /** Master领域对象在当前领域对象(slave)中的关联字段名(复数形式) */
    private String masterIdsNameOfSlave;

    /** Master领域对象在当前领域对象(slave)中的关联字段名(首字母大写)(复数形式) */
    private String upperMasterIdsNameOfSlave;

    public DomainObjectParameter getMasterDomainObjectParameter() {
        return masterDomainObjectParameter;
    }

    public void setMasterDomainObjectParameter(DomainObjectParameter masterDomainObjectParameter) {
        this.masterDomainObjectParameter = masterDomainObjectParameter;
    }

    public String getMasterIdNameOfSlave() {
        return masterIdNameOfSlave;
    }

    public void setMasterIdNameOfSlave(String masterIdNameOfSlave) {
        this.masterIdNameOfSlave = masterIdNameOfSlave;
    }

    public String getUpperMasterIdNameOfSlave() {
        return upperMasterIdNameOfSlave;
    }

    public void setUpperMasterIdNameOfSlave(String upperMasterIdNameOfSlave) {
        this.upperMasterIdNameOfSlave = upperMasterIdNameOfSlave;
    }

    public String getMasterIdsNameOfSlave() {
        return masterIdsNameOfSlave;
    }

    public void setMasterIdsNameOfSlave(String masterIdsNameOfSlave) {
        this.masterIdsNameOfSlave = masterIdsNameOfSlave;
    }

    public String getUpperMasterIdsNameOfSlave() {
        return upperMasterIdsNameOfSlave;
    }

    public void setUpperMasterIdsNameOfSlave(String upperMasterIdsNameOfSlave) {
        this.upperMasterIdsNameOfSlave = upperMasterIdsNameOfSlave;
    }

}
