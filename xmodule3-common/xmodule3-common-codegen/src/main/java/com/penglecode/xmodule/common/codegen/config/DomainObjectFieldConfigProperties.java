package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.codegen.support.ObjectSupportFieldType;
import com.penglecode.xmodule.common.codegen.support.QueryConditionOperator;

/**
 * 领域对象字段配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/8/20 22:15
 */
public class DomainObjectFieldConfigProperties {

    /** 字段名称 */
    private final String fieldName;

    /** 字段类型 */
    private final FullyQualifiedJavaType fieldType;

    /** 字段标题 */
    private final String fieldTitle;

    /** 字段注释 */
    private final String fieldComment;

    /** 是否是主键字段 */
    private final boolean pkField;

    /** 是否是辅助字段 */
    private final boolean supportField;

    /** 辅助字段类型 */
    private final ObjectSupportFieldType supportFieldType;

    /** 如果是ObjectSupportFieldType.QUERY_INPUT则有值 */
    private QueryConditionOperator queryConditionOperator;

    /**
     * 如果supportField=true，则domainColumnConfig为当前字段的对应数据库列配置，否则为当前字段的关联数据库列配置
     */
    private final DomainObjectColumnConfigProperties domainColumnConfig;

    public DomainObjectFieldConfigProperties(String fieldName, FullyQualifiedJavaType fieldType, String fieldTitle, String fieldComment, boolean pkField, ObjectSupportFieldType supportFieldType, QueryConditionOperator queryConditionOperator, DomainObjectColumnConfigProperties domainColumnConfig) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldTitle = fieldTitle;
        this.fieldComment = fieldComment;
        this.pkField = pkField;
        this.supportField = supportFieldType != null;
        this.supportFieldType = supportFieldType;
        this.queryConditionOperator = queryConditionOperator;
        this.domainColumnConfig = domainColumnConfig;
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

    public boolean isPkField() {
        return pkField;
    }

    public boolean isSupportField() {
        return supportField;
    }

    public ObjectSupportFieldType getSupportFieldType() {
        return supportFieldType;
    }

    public QueryConditionOperator getQueryConditionOperator() {
        return queryConditionOperator;
    }

    protected void setQueryConditionOperator(QueryConditionOperator queryConditionOperator) {
        this.queryConditionOperator = queryConditionOperator;
    }

    public DomainObjectColumnConfigProperties getDomainColumnConfig() {
        return domainColumnConfig;
    }

}