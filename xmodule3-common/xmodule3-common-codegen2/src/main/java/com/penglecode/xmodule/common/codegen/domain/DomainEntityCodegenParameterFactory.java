package com.penglecode.xmodule.common.codegen.domain;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.domain.DomainEntityCodegenParameter.*;
import com.penglecode.xmodule.common.codegen.support.*;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.domain.DomainObject;
import com.penglecode.xmodule.common.domain.ID;

import java.util.*;

/**
 * 领域实体代码生成参数Factory
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 22:03
 */
public class DomainEntityCodegenParameterFactory extends CodegenParameterFactory<DomainObjectCodegenConfigProperties, DomainEntityConfig, DomainEntityConfig, DomainEntityCodegenParameter> {

    public DomainEntityCodegenParameterFactory(CodegenContext<DomainObjectCodegenConfigProperties, DomainEntityConfig, DomainEntityConfig> codegenContext) {
        super(codegenContext);
    }

    public DomainEntityCodegenParameterFactory(DomainObjectCodegenConfigProperties codegenConfig, DomainEntityConfig targetConfig, DomainEntityConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected DomainEntityCodegenParameter setCodegenParameterCustom(DomainEntityCodegenParameter codegenParameter) {
        codegenParameter.setTargetComment(getDomainObjectConfig().getDomainEntityTitle() + "实体");
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(DomainObject.class.getName()));
        List<ObjectField> inherentFields = new ArrayList<>(); //实体固有字段
        List<ObjectField> supportFields = new ArrayList<>(); //实体辅助字段
        List<ObjectField> allFields = new ArrayList<>(); //实体所有字段
        List<DomainEntityCodegenParameter.EnumFieldDecode> enumFieldDecodes = new ArrayList<>();
        for(Map.Entry<String,DomainEntityFieldConfig> entry : getTargetConfig().getDomainEntityFields().entrySet()) {
            DomainEntityFieldConfig domainEntityFieldConfig = entry.getValue();
            if(domainEntityFieldConfig.getFieldClass().isSupportField()) { //领域实体辅助字段
                supportFields.add(buildEntitySupportField(domainEntityFieldConfig, codegenParameter));
                //处理领域对象数据出站DomainObject#beforeOutbound()实现
                if(DomainObjectFieldClass.DOMAIN_ENTITY_SUPPORTS_QUERY_OUTPUT_FIELD.equals(domainEntityFieldConfig.getFieldClass())) {
                    DomainEntityColumnConfig domainEntityColumnConfig = domainEntityFieldConfig.getDomainEntityColumnConfig(); //当前辅助字段是辅助谁的?
                    String shortDomainEnumType = getShortDomainEnumType(domainEntityColumnConfig.getDecodeEnumType());
                    DomainEnumConfig refDomainEnumConfig = resolveDecodeEnumConfig(domainEntityColumnConfig.getDecodeEnumType());
                    if(refDomainEnumConfig != null) {
                        enumFieldDecodes.add(buildEnumFieldDecode(domainEntityFieldConfig, refDomainEnumConfig, codegenParameter));
                    }
                }
            } else { //领域实体固有字段
                inherentFields.add(buildEntityInherentField(domainEntityFieldConfig, codegenParameter));
            }
        }
        codegenParameter.setInherentFields(inherentFields);
        codegenParameter.setSupportFields(supportFields);
        codegenParameter.setEnumFieldDecodes(enumFieldDecodes);
        allFields.addAll(inherentFields);
        allFields.addAll(supportFields);
        codegenParameter.setAllFields(allFields);
        attachDomainEntityIdField(codegenParameter);
        return codegenParameter;
    }

    protected ObjectField buildEntitySupportField(DomainEntityFieldConfig domainEntityFieldConfig, CodegenParameter codegenParameter) {
        ObjectField field = createDomainObjectFields(domainEntityFieldConfig);
        field.setFieldAnnotations(Collections.emptyList());
        field.setFieldGetterName(CodegenUtils.getGetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        field.setFieldSetterName(CodegenUtils.getSetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.addTargetImportType(domainEntityFieldConfig.getFieldType());
        return field;
    }

    protected ObjectField buildEntityInherentField(DomainEntityFieldConfig domainEntityFieldConfig, CodegenParameter codegenParameter) {
        ObjectField field = createDomainObjectFields(domainEntityFieldConfig);
        List<String> fieldAnnotations = new ArrayList<>();
        for(String validateExpression : domainEntityFieldConfig.getDomainEntityColumnConfig().getValidateExpressions()) {
            String[] expressions = validateExpression.split(":");
            fieldAnnotations.add(expressions[1]);
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(expressions[0]));
        }
        field.setFieldAnnotations(fieldAnnotations);
        field.setFieldGetterName(CodegenUtils.getGetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        field.setFieldSetterName(CodegenUtils.getSetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.addTargetImportType(domainEntityFieldConfig.getFieldType());
        return field;
    }

    protected EnumFieldDecode buildEnumFieldDecode(DomainEntityFieldConfig domainEntityFieldConfig, DomainEnumConfig refDomainEnumConfig, CodegenParameter codegenParameter) {
        EnumFieldDecode field = new EnumFieldDecode();
        field.setRefEnumTypeName(refDomainEnumConfig.getDomainEnumName());
        field.setEntityFieldName(domainEntityFieldConfig.getDomainEntityColumnConfig().getIntrospectedColumn().getJavaFieldName()); //当前辅助字段关联的枚举值字段
        field.setEntityFieldSetterName(CodegenUtils.getSetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        field.setRefEnumNameFieldGetterName(CodegenUtils.getGetterMethodName(refDomainEnumConfig.getDomainEnumNameField().getFieldName(), refDomainEnumConfig.getDomainEnumNameField().getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(refDomainEnumConfig.getGeneratedTargetName(refDomainEnumConfig.getDomainEnumName(), true, false)));
        codegenParameter.addTargetImportType(refDomainEnumConfig.getDomainEnumNameField().getFieldType());
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Optional.class.getName()));
        return field;
    }

    /**
     * 附带上领域实体ID字段参数
     * @param codegenParameter
     */
    protected void attachDomainEntityIdField(DomainEntityCodegenParameter codegenParameter) {
        List<DomainEntityFieldConfig> idFields = getTargetConfig().getIdFields();
        if(idFields.size() == 1) { //单主键
            DomainEntityFieldConfig idField = idFields.get(0);
            codegenParameter.setIdFieldType(idField.getFieldType().getShortName());
            codegenParameter.setIdFieldName(idField.getFieldName());
        } else if(idFields.size() > 1) { //复合主键
            FullyQualifiedJavaType idFieldType = new FullyQualifiedJavaType(ID.class.getName());
            codegenParameter.addTargetImportType(idFieldType);
            StringBuilder sb = new StringBuilder("new ID()");
            for(DomainEntityFieldConfig idField : idFields) {
                sb.append(".addKey(\"").append(idField.getFieldName()).append("\", ").append(idField.getFieldName()).append(")");
            }
            codegenParameter.setIdFieldType(idFieldType.getShortName());
            codegenParameter.setIdFieldName(sb.toString());
        }
    }

    protected ObjectField createDomainObjectFields(DomainObjectFieldConfig domainObjectFieldConfig) {
        ObjectField field = new ObjectField();
        field.setFieldName(domainObjectFieldConfig.getFieldName());
        field.setFieldType(domainObjectFieldConfig.getFieldType().getShortName());
        field.setFieldComment(domainObjectFieldConfig.getFieldComment());
        return field;
    }

    protected String getShortDomainEnumType(String domainEnumType) {
        if(domainEnumType.contains(".")) {
            return domainEnumType.substring(domainEnumType.lastIndexOf('.') + 1);
        }
        return domainEnumType;
    }

    protected DomainEnumConfig resolveDecodeEnumConfig(String domainEnumName) {
        return getCodegenConfig().getDomain().getDomainCommons().getDomainEnums()
                .stream()
                .filter(config -> config.getDomainEnumName().equals(domainEnumName))
                .findFirst().orElse(null);
    }


    @Override
    protected String getTargetTemplateName() {
        return "DomainEntity.ftl";
    }

}
