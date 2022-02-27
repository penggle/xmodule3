package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;

/**
 * 领域聚合对象字段配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/8/20 22:15
 */
public class DomainAggregateFieldConfigProperties {

    /** 字段名称 */
    private final String fieldName;

    /** 字段类型 */
    private final FullyQualifiedJavaType fieldType;

    /** 字段标题 */
    private final String fieldTitle;

    /** 字段注释 */
    private final String fieldComment;

    /** 当前聚合对象属性对应的聚合对象配置 */
    private final DomainAggregateConfigProperties domainAggregateConfig;

    /** 当前聚合对象属性对应的从属领域对象配置 */
    private final DomainAggregateSlaveConfigProperties domainAggregateSlaveConfig;

    public DomainAggregateFieldConfigProperties(String fieldName, FullyQualifiedJavaType fieldType, String fieldTitle, String fieldComment, DomainAggregateConfigProperties domainAggregateConfig, DomainAggregateSlaveConfigProperties domainAggregateSlaveConfig) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldTitle = fieldTitle;
        this.fieldComment = fieldComment;
        this.domainAggregateConfig = domainAggregateConfig;
        this.domainAggregateSlaveConfig = domainAggregateSlaveConfig;
    }

    public String getFieldName() {
        return fieldName;
    }

    public FullyQualifiedJavaType getFieldType() {
        return fieldType;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public String getFieldComment() {
        return fieldComment;
    }

    public DomainAggregateConfigProperties getDomainAggregateConfig() {
        return domainAggregateConfig;
    }

    public DomainAggregateSlaveConfigProperties getDomainAggregateSlaveConfig() {
        return domainAggregateSlaveConfig;
    }

}