package com.penglecode.xmodule.common.codegen.support;

import java.util.List;

/**
 * 对象字段
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/6 23:37
 */
public class ObjectFieldParameter {

    /** 字段名称 */
    private String fieldName;

    /** 字段类型 */
    private String fieldType;

    /** 字段注释 */
    private String fieldComment;

    /** 字段注解 */
    private List<String> fieldAnnotations;

    /** 字段Getter方法名称 */
    private String fieldGetterName;

    /** 字段Setter方法名称 */
    private String fieldSetterName;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldComment() {
        return fieldComment;
    }

    public void setFieldComment(String fieldComment) {
        this.fieldComment = fieldComment;
    }

    public List<String> getFieldAnnotations() {
        return fieldAnnotations;
    }

    public void setFieldAnnotations(List<String> fieldAnnotations) {
        this.fieldAnnotations = fieldAnnotations;
    }

    public String getFieldGetterName() {
        return fieldGetterName;
    }

    public void setFieldGetterName(String fieldGetterName) {
        this.fieldGetterName = fieldGetterName;
    }

    public String getFieldSetterName() {
        return fieldSetterName;
    }

    public void setFieldSetterName(String fieldSetterName) {
        this.fieldSetterName = fieldSetterName;
    }

}
