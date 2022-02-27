package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.config.DomainEntityConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 领域服务参数
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/6 21:23
 */
public class DomainServiceParameters extends ArrayList<DomainServiceParameters.DomainServiceParameter> {

    private static final long serialVersionUID = 1L;

    private final DomainServiceParameter masterDomainServiceParameter;

    private final List<DomainServiceParameter> slaveDomainServiceParameters;

    public DomainServiceParameters(DomainServiceParameter masterDomainServiceParameter, List<DomainServiceParameter> slaveDomainServiceParameters) {
        this.masterDomainServiceParameter = masterDomainServiceParameter;
        this.slaveDomainServiceParameters = slaveDomainServiceParameters;
        List<DomainServiceParameters.DomainServiceParameter> allDomainServiceParameters = new ArrayList<>();
        allDomainServiceParameters.add(masterDomainServiceParameter);
        allDomainServiceParameters.addAll(slaveDomainServiceParameters);
        addAll(allDomainServiceParameters);
    }

    public DomainServiceParameter getMasterDomainServiceParameter() {
        return masterDomainServiceParameter;
    }

    public List<DomainServiceParameter> getSlaveDomainServiceParameters() {
        return slaveDomainServiceParameters;
    }

    public static class DomainServiceParameter {

        private final DomainServiceInterfaceCodegenParameter domainServiceCodegenParameter;

        private final DomainEntityConfig domainEntityConfig;

        private final String domainServiceName;

        private final String domainServiceBeanName;

        private final String domainServiceInstanceName;

        public DomainServiceParameter(DomainServiceInterfaceCodegenParameter domainServiceCodegenParameter, DomainEntityConfig domainEntityConfig, String domainServiceName, String domainServiceBeanName, String domainServiceInstanceName) {
            this.domainServiceCodegenParameter = domainServiceCodegenParameter;
            this.domainEntityConfig = domainEntityConfig;
            this.domainServiceName = domainServiceName;
            this.domainServiceBeanName = domainServiceBeanName;
            this.domainServiceInstanceName = domainServiceInstanceName;
        }

        public DomainServiceInterfaceCodegenParameter getDomainServiceCodegenParameter() {
            return domainServiceCodegenParameter;
        }

        public DomainEntityConfig getDomainEntityConfig() {
            return domainEntityConfig;
        }

        public String getDomainServiceName() {
            return domainServiceName;
        }

        public String getDomainServiceBeanName() {
            return domainServiceBeanName;
        }

        public String getDomainServiceInstanceName() {
            return domainServiceInstanceName;
        }

    }

}
