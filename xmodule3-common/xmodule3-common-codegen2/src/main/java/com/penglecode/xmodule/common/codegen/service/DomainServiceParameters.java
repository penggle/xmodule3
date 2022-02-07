package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.config.DomainEntityConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 领域服务参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 21:23
 */
public class DomainServiceParameters {

    private final DomainServiceParameter masterDomainServiceParameter;

    private final List<DomainServiceParameter> slaveDomainServiceParameters;

    public DomainServiceParameters(DomainServiceParameter masterDomainServiceParameter, List<DomainServiceParameter> slaveDomainServiceParameters) {
        this.masterDomainServiceParameter = masterDomainServiceParameter;
        this.slaveDomainServiceParameters = slaveDomainServiceParameters;
    }

    public DomainServiceParameter getMasterDomainServiceParameter() {
        return masterDomainServiceParameter;
    }

    public List<DomainServiceParameter> getSlaveDomainServiceParameters() {
        return slaveDomainServiceParameters;
    }

    public static class DomainServiceParameter {

        private final DomainEntityConfig domainEntityConfig;

        private final String domainServiceName;

        private final String domainServiceBeanName;

        private final String domainServiceInstanceName;

        public DomainServiceParameter(DomainEntityConfig domainEntityConfig, String domainServiceName, String domainServiceBeanName, String domainServiceInstanceName) {
            this.domainEntityConfig = domainEntityConfig;
            this.domainServiceName = domainServiceName;
            this.domainServiceBeanName = domainServiceBeanName;
            this.domainServiceInstanceName = domainServiceInstanceName;
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

        public Map<String,Object> asMap() {
            Map<String,Object> map = new HashMap<>();
            map.put("domainServiceName", domainServiceName);
            map.put("domainServiceBeanName", domainServiceBeanName);
            map.put("domainServiceInstanceName", domainServiceInstanceName);
            return map;
        }

    }

}
