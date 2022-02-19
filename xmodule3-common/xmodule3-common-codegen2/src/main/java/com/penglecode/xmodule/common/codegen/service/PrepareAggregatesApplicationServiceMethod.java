package com.penglecode.xmodule.common.codegen.service;

/**
 * 应用服务的一个内部服务：
 * protected List<> prepareDomainObjectList()
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 22:31
 */
public class PrepareAggregatesApplicationServiceMethod extends ApplicationServiceMethod {

    /** Master方的领域对象参数 */
    private DomainObjectParameter masterDomainObjectParameter;

    public DomainObjectParameter getMasterDomainObjectParameter() {
        return masterDomainObjectParameter;
    }

    public void setMasterDomainObjectParameter(DomainObjectParameter masterDomainObjectParameter) {
        this.masterDomainObjectParameter = masterDomainObjectParameter;
    }

}
