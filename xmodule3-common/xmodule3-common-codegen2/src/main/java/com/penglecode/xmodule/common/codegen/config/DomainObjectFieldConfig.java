package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.support.DomainObjectFieldType;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;

/**
 * 领域对象字段配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/23 16:18
 */
public abstract class DomainObjectFieldConfig {

    /** 字段名称 */
    private final String fieldName;

    /** 字段类型 */
    private final FullyQualifiedJavaType fieldClass;

    /** 字段标题 */
    private final String fieldTitle;

    /** 字段注释 */
    private final String fieldComment;

    /** 领域对象字段分类 */
    private final DomainObjectFieldType fieldType;

    protected DomainObjectFieldConfig(String fieldName, FullyQualifiedJavaType fieldClass, String fieldTitle, String fieldComment, DomainObjectFieldType fieldType) {
        this.fieldName = fieldName;
        this.fieldClass = fieldClass;
        this.fieldTitle = fieldTitle;
        this.fieldComment = fieldComment;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public FullyQualifiedJavaType getFieldClass() {
        return fieldClass;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public String getFieldComment() {
        return fieldComment;
    }

    public DomainObjectFieldType getFieldType() {
        return fieldType;
    }

}
