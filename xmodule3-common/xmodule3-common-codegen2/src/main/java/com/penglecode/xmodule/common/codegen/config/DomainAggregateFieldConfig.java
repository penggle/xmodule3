package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.support.DomainObjectFieldType;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;

/**
 * 领域聚合对象字段配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/23 17:36
 */
public class DomainAggregateFieldConfig extends DomainObjectFieldConfig {

    /** 当前聚合对象属性对应的聚合对象配置 */
    private final DomainAggregateConfig domainAggregateConfig;

    /** 当前聚合对象属性对应的从属领域对象配置 */
    private final DomainAggregateSlaveConfig domainAggregateSlaveConfig;

    public DomainAggregateFieldConfig(String fieldName, FullyQualifiedJavaType fieldClass, String fieldTitle, String fieldComment, DomainObjectFieldType fieldType, DomainAggregateConfig domainAggregateConfig, DomainAggregateSlaveConfig domainAggregateSlaveConfig) {
        super(fieldName, fieldClass, fieldTitle, fieldComment, fieldType);
        this.domainAggregateConfig = domainAggregateConfig;
        this.domainAggregateSlaveConfig = domainAggregateSlaveConfig;
    }

    public DomainAggregateConfig getDomainAggregateConfig() {
        return domainAggregateConfig;
    }

    public DomainAggregateSlaveConfig getDomainAggregateSlaveConfig() {
        return domainAggregateSlaveConfig;
    }

}