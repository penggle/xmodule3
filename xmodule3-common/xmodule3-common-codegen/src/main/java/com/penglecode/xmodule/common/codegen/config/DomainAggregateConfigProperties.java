package com.penglecode.xmodule.common.codegen.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 领域对象聚合配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/10/17 17:24
 */
public class DomainAggregateConfigProperties extends GeneratedTargetConfigProperties {

    /**
     * 聚合对象类名(命名建议aggregateMasterName对应的那个领域对象以Info结尾，为aggregateObjectName让路)
     */
    private String aggregateObjectName;

    /**
     * 聚合对象中文名称
     */
    private String aggregateObjectTitle;

    /**
     * 聚合对象别名
     */
    private String aggregateObjectAlias;

    /**
     * 聚合对象的主要领域对象名称(指向下面domainObjects列表中的某一个)
     */
    private String aggregateMasterName;

    /**
     * 聚合对象的从属领域对象列表
     */
    private Set<DomainAggregateSlaveConfigProperties> aggregateObjectSlaves;

    /**
     * 聚合对象的字段列表
     */
    private final Map<String,DomainAggregateFieldConfigProperties> aggregateObjectFields = new LinkedHashMap<>();

    public String getAggregateObjectName() {
        return aggregateObjectName;
    }

    public void setAggregateObjectName(String aggregateObjectName) {
        this.aggregateObjectName = aggregateObjectName;
    }

    public String getAggregateObjectTitle() {
        return aggregateObjectTitle;
    }

    public void setAggregateObjectTitle(String aggregateObjectTitle) {
        this.aggregateObjectTitle = aggregateObjectTitle;
    }

    public String getAggregateObjectAlias() {
        return aggregateObjectAlias;
    }

    public void setAggregateObjectAlias(String aggregateObjectAlias) {
        this.aggregateObjectAlias = aggregateObjectAlias;
    }

    public String getAggregateMasterName() {
        return aggregateMasterName;
    }

    public void setAggregateMasterName(String aggregateMasterName) {
        this.aggregateMasterName = aggregateMasterName;
    }

    public Set<DomainAggregateSlaveConfigProperties> getAggregateObjectSlaves() {
        return aggregateObjectSlaves;
    }

    public void setAggregateObjectSlaves(Set<DomainAggregateSlaveConfigProperties> aggregateObjectSlaves) {
        this.aggregateObjectSlaves = aggregateObjectSlaves;
    }

    public Map<String, DomainAggregateFieldConfigProperties> getAggregateObjectFields() {
        return aggregateObjectFields;
    }

    @Override
    public <T extends GeneratedTargetConfigProperties> String getGeneratedTargetName(DomainBoundedTargetConfigProperties<T> targetConfig, boolean includePackage, boolean includeSuffix) {
        return (includePackage ? getTargetPackage() + "." : "") + aggregateObjectName + (includeSuffix ? ".java" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainAggregateConfigProperties)) return false;

        DomainAggregateConfigProperties that = (DomainAggregateConfigProperties) o;

        return aggregateObjectName.equals(that.aggregateObjectName);
    }

    @Override
    public int hashCode() {
        return aggregateObjectName.hashCode();
    }

}
