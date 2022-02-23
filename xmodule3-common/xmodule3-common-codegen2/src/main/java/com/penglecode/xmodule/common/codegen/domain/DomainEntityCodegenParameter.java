package com.penglecode.xmodule.common.codegen.domain;

import com.penglecode.xmodule.common.codegen.support.CodegenParameter;
import com.penglecode.xmodule.common.codegen.support.ObjectFieldParameter;

import java.util.List;

/**
 * 领域实体代码生成模板参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 21:48
 */
public class DomainEntityCodegenParameter extends CodegenParameter {

    /** ID字段类型 */
    private String idFieldType;

    /** ID字段名称 */
    private String idFieldName;

    /** 实体自有字段 */
    private List<ObjectFieldParameter> inherentFields;

    /** 实体辅助字段 */
    private List<ObjectFieldParameter> supportFields;

    /** 实体所有字段(自有字段 + 辅助字段) */
    private List<ObjectFieldParameter> allFields;

    /** 实体枚举字段Decode */
    private List<EnumFieldDecode> enumFieldDecodes;

    public DomainEntityCodegenParameter(String targetTemplateName) {
        super(targetTemplateName);
    }

    public String getIdFieldType() {
        return idFieldType;
    }

    public void setIdFieldType(String idFieldType) {
        this.idFieldType = idFieldType;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    public List<ObjectFieldParameter> getInherentFields() {
        return inherentFields;
    }

    public void setInherentFields(List<ObjectFieldParameter> inherentFields) {
        this.inherentFields = inherentFields;
    }

    public List<ObjectFieldParameter> getSupportFields() {
        return supportFields;
    }

    public void setSupportFields(List<ObjectFieldParameter> supportFields) {
        this.supportFields = supportFields;
    }

    public List<ObjectFieldParameter> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<ObjectFieldParameter> allFields) {
        this.allFields = allFields;
    }

    public List<EnumFieldDecode> getEnumFieldDecodes() {
        return enumFieldDecodes;
    }

    public void setEnumFieldDecodes(List<EnumFieldDecode> enumFieldDecodes) {
        this.enumFieldDecodes = enumFieldDecodes;
    }

    public static class EnumFieldDecode {

        /** 关联的枚举类名 */
        private String refEnumTypeName;

        /** 解码实体字段名称 */
        private String entityFieldName;

        /** 解码实体字段Setter名称 */
        private String entityFieldSetterName;

        /** 关联的枚举Name字段Getter方法名 */
        private String refEnumNameFieldGetterName;

        public String getRefEnumTypeName() {
            return refEnumTypeName;
        }

        public void setRefEnumTypeName(String refEnumTypeName) {
            this.refEnumTypeName = refEnumTypeName;
        }

        public String getEntityFieldName() {
            return entityFieldName;
        }

        public void setEntityFieldName(String entityFieldName) {
            this.entityFieldName = entityFieldName;
        }

        public String getEntityFieldSetterName() {
            return entityFieldSetterName;
        }

        public void setEntityFieldSetterName(String entityFieldSetterName) {
            this.entityFieldSetterName = entityFieldSetterName;
        }

        public String getRefEnumNameFieldGetterName() {
            return refEnumNameFieldGetterName;
        }

        public void setRefEnumNameFieldGetterName(String refEnumNameFieldGetterName) {
            this.refEnumNameFieldGetterName = refEnumNameFieldGetterName;
        }

    }

}
