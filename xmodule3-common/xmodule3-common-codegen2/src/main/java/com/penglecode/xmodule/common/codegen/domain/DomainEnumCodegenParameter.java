package com.penglecode.xmodule.common.codegen.domain;

import com.penglecode.xmodule.common.codegen.support.CodegenParameter;

import java.util.List;

/**
 * 领域枚举代码生成模板参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 21:48
 */
public class DomainEnumCodegenParameter extends CodegenParameter {

    /** 枚举值列表 */
    private List<EnumValue> enumValues;

    /** 枚举code字段的类型  */
    private String codeFieldType;

    /** 枚举code字段的名称  */
    private String codeFieldName;

    /** 枚举code字段的Getter方法名 */
    private String codeGetterName;

    /** 枚举name字段的类型  */
    private String nameFieldType;

    /** 枚举name字段的名称  */
    private String nameFieldName;

    /** 枚举name字段的Getter方法名 */
    private String nameGetterName;

    public DomainEnumCodegenParameter(String targetTemplateName) {
        super(targetTemplateName);
    }

    public List<EnumValue> getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(List<EnumValue> enumValues) {
        this.enumValues = enumValues;
    }

    public String getCodeFieldType() {
        return codeFieldType;
    }

    public void setCodeFieldType(String codeFieldType) {
        this.codeFieldType = codeFieldType;
    }

    public String getCodeFieldName() {
        return codeFieldName;
    }

    public void setCodeFieldName(String codeFieldName) {
        this.codeFieldName = codeFieldName;
    }

    public String getCodeGetterName() {
        return codeGetterName;
    }

    public void setCodeGetterName(String codeGetterName) {
        this.codeGetterName = codeGetterName;
    }

    public String getNameFieldType() {
        return nameFieldType;
    }

    public void setNameFieldType(String nameFieldType) {
        this.nameFieldType = nameFieldType;
    }

    public String getNameFieldName() {
        return nameFieldName;
    }

    public void setNameFieldName(String nameFieldName) {
        this.nameFieldName = nameFieldName;
    }

    public String getNameGetterName() {
        return nameGetterName;
    }

    public void setNameGetterName(String nameGetterName) {
        this.nameGetterName = nameGetterName;
    }

    public static class EnumValue {

        /** 枚举值 */
        private String enumValue;

        /** 枚举code字段的值 */
        private String codeValue;

        /** 枚举name字段的值 */
        private String nameValue;

        public String getEnumValue() {
            return enumValue;
        }

        public void setEnumValue(String enumValue) {
            this.enumValue = enumValue;
        }

        public String getCodeValue() {
            return codeValue;
        }

        public void setCodeValue(String codeValue) {
            this.codeValue = codeValue;
        }

        public String getNameValue() {
            return nameValue;
        }

        public void setNameValue(String nameValue) {
            this.nameValue = nameValue;
        }
    }

}
