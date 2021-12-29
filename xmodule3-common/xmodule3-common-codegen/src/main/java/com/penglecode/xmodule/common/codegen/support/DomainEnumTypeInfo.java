package com.penglecode.xmodule.common.codegen.support;

/**
 * 与枚举字段相关的枚举类信息
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/9/16 10:46
 */
public class DomainEnumTypeInfo {

    private final FullyQualifiedJavaType enumType;

    private final String codeFieldName;

    private final FullyQualifiedJavaType codeFieldType;

    private final String nameFieldName;

    private final FullyQualifiedJavaType nameFieldType;

    public DomainEnumTypeInfo(FullyQualifiedJavaType enumType, String codeFieldName, FullyQualifiedJavaType codeFieldType, String nameFieldName, FullyQualifiedJavaType nameFieldType) {
        this.enumType = enumType;
        this.codeFieldName = codeFieldName;
        this.codeFieldType = codeFieldType;
        this.nameFieldName = nameFieldName;
        this.nameFieldType = nameFieldType;
    }

    public FullyQualifiedJavaType getEnumType() {
        return enumType;
    }

    public String getCodeFieldName() {
        return codeFieldName;
    }

    public FullyQualifiedJavaType getCodeFieldType() {
        return codeFieldType;
    }

    public String getNameFieldName() {
        return nameFieldName;
    }

    public FullyQualifiedJavaType getNameFieldType() {
        return nameFieldType;
    }

}
