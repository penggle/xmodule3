package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;
import java.util.Objects;

/**
 * 领域下面的枚举定义
 *
 * 领域下的枚举定义都是如下约定的格式：
 *
 * public enum ${enumName}Enum {
 *
 *     A(1, 'aaa'), B(2, 'bbb');
 *
 *     private final Integer xxxCode;
 *
 *     private final String xxxName;
 *
 *     private ${enumName}Enum(Integer xxxCode, String xxxName) {...}
 *
 *     public static ${enumName}Enum of(Integer xxxCode) {...}
 *
 *     //getter ...
 *
 * }
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/8/21 11:28
 */
public class DomainEnumConfigProperties extends GeneratedTargetConfigProperties {

    /** 枚举类名称 */
    private String enumName;

    /** 枚举类标题 */
    private String enumTitle;

    /** 枚举字段-xxxCode */
    private ImmutablePair<String,FullyQualifiedJavaType> codeField;

    /** 枚举字段-xxxName */
    private ImmutablePair<String,FullyQualifiedJavaType> nameField;

    /** 枚举值 */
    private Map<String,Object[]> enumValues;

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }

    public String getEnumTitle() {
        return enumTitle;
    }

    public void setEnumTitle(String enumTitle) {
        this.enumTitle = enumTitle;
    }

    /**
     * @param enumFields - 枚举字段定义,例如为'type:Integer',则按约定生成typeCode,typeName两个字段,且typeCode的类型为Integer
     */
    public void setEnumFields(String enumFields) {
        if(StringUtils.isNotBlank(enumFields)) {
            String[] fieldArray = enumFields.split(":");
            String codeFieldName = fieldArray[0] + "Code";
            String nameFieldName = fieldArray[0] + "Name";
            String codeFieldType = fieldArray.length == 2 ? StringUtils.defaultIfEmpty(fieldArray[1], "String") : "String";
            if(!codeFieldType.contains(".")) {
                codeFieldType = "java.lang." + codeFieldType;
            }
            this.codeField = new ImmutablePair<>(codeFieldName, new FullyQualifiedJavaType(codeFieldType));
            this.nameField = new ImmutablePair<>(nameFieldName, FullyQualifiedJavaType.getStringInstance());
        }
    }

    public ImmutablePair<String, FullyQualifiedJavaType> getCodeField() {
        return codeField;
    }

    public void setCodeField(ImmutablePair<String, FullyQualifiedJavaType> codeField) {
        this.codeField = codeField;
    }

    public ImmutablePair<String, FullyQualifiedJavaType> getNameField() {
        return nameField;
    }

    public void setNameField(ImmutablePair<String, FullyQualifiedJavaType> nameField) {
        this.nameField = nameField;
    }

    public Map<String, Object[]> getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(Map<String, Object[]> enumValues) {
        this.enumValues = enumValues;
    }

    @Override
    public <T extends GeneratedTargetConfigProperties> String getGeneratedTargetName(DomainBoundedTargetConfigProperties<T> targetConfig, boolean includePackage, boolean includeSuffix) {
        return (includePackage ? getTargetPackage() + "." : "") + enumName + (includeSuffix ? ".java" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainEnumConfigProperties)) return false;
        DomainEnumConfigProperties that = (DomainEnumConfigProperties) o;
        return enumName.equals(that.enumName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enumName);
    }

}
