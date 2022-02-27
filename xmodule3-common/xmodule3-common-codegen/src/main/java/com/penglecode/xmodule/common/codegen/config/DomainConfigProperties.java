package com.penglecode.xmodule.common.codegen.config;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 领域对象代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/7/25 15:18
 */
public class DomainConfigProperties {

    /**
     * 领域对象公共配置
     */
    private DomainCommonsConfigProperties domainCommons;

    /**
     * 领域对象列表
     */
    private Map<String,DomainObjectConfigProperties> domainObjects;

    /**
     * 聚合对象(如果定义了聚合对象，则后端会生成对应的{aggregateObjectName}AppService)
     */
    private Map<String,DomainAggregateConfigProperties> domainAggregates;

    public DomainCommonsConfigProperties getDomainCommons() {
        return domainCommons;
    }

    public void setDomainCommons(DomainCommonsConfigProperties domainCommons) {
        this.domainCommons = domainCommons;
    }

    public Map<String,DomainObjectConfigProperties> getDomainObjects() {
        return domainObjects;
    }

    public void setDomainObjects(Set<DomainObjectConfigProperties> domainObjects) {
        if(!CollectionUtils.isEmpty(domainObjects)) {
            this.domainObjects = domainObjects.stream().collect(Collectors.toMap(DomainObjectConfigProperties::getDomainObjectName, Function.identity()));
        } else {
            this.domainObjects = Collections.emptyMap();
        }
    }

    public Map<String,DomainAggregateConfigProperties> getDomainAggregates() {
        return domainAggregates;
    }

    public void setDomainAggregates(Set<DomainAggregateConfigProperties> domainAggregates) {
        if(!CollectionUtils.isEmpty(domainAggregates)) {
            this.domainAggregates = domainAggregates.stream().collect(Collectors.toMap(DomainAggregateConfigProperties::getAggregateObjectName, Function.identity()));
        } else {
            this.domainAggregates = Collections.emptyMap();
        }
    }

}
