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
 * @since 2021/1/22 14:22
 */
public class DomainConfig {

    /** 领域对象公共配置 */
    private DomainCommonsConfig domainCommons;

    /** 领域实体对象列表 */
    private Map<String,DomainEntityConfig> domainEntities;

    /** 领域聚合对象(如果定义了聚合对象，则后端会生成对应的{domainAggregateName}AppService) */
    private Map<String,DomainAggregateConfig> domainAggregates;

    public DomainCommonsConfig getDomainCommons() {
        return domainCommons;
    }

    public void setDomainCommons(DomainCommonsConfig domainCommons) {
        this.domainCommons = domainCommons;
    }

    public Map<String,DomainEntityConfig> getDomainEntities() {
        return domainEntities;
    }

    public void setDomainEntities(Set<DomainEntityConfig> domainEntities) {
        if(!CollectionUtils.isEmpty(domainEntities)) {
            this.domainEntities = domainEntities.stream().collect(Collectors.toMap(DomainEntityConfig::getDomainEntityName, Function.identity()));
        } else {
            this.domainEntities = Collections.emptyMap();
        }
    }

    public Map<String,DomainAggregateConfig> getDomainAggregates() {
        return domainAggregates;
    }

    public void setDomainAggregates(Set<DomainAggregateConfig> domainAggregates) {
        if(!CollectionUtils.isEmpty(domainAggregates)) {
            this.domainAggregates = domainAggregates.stream().collect(Collectors.toMap(DomainAggregateConfig::getDomainAggregateName, Function.identity()));
        } else {
            this.domainAggregates = Collections.emptyMap();
        }
    }

}
