package com.penglecode.xmodule.common.codegen.domain;

import com.penglecode.xmodule.common.codegen.ModuleCodeGenerator;
import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.CodegenParameter;
import com.penglecode.xmodule.common.codegen.support.DomainObjectFieldType;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.domain.DomainObject;
import com.penglecode.xmodule.common.domain.ID;
import com.penglecode.xmodule.common.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 领域对象代码生成器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/28 21:01
 */
public class DomainObjectCodeGenerator extends ModuleCodeGenerator<DomainObjectCodegenConfigProperties> {

    public DomainObjectCodeGenerator(String module) {
        super(module);
    }

    @Override
    protected void executeGenerate() throws Exception {
        DomainObjectCodegenConfigProperties codegenConfig = getCodegenConfig();
        Set<DomainEnumConfig> domainEnumConfigs = codegenConfig.getDomain().getDomainCommons().getDomainEnums();
        if(!CollectionUtils.isEmpty(domainEnumConfigs)) { //1、生成领域枚举对象
            for(DomainEnumConfig domainEnumConfig : domainEnumConfigs) {
                if(domainEnumConfig.getDomainEnumClass() == null) { //需要生成领域枚举对象?
                    CodegenContext<DomainObjectCodegenConfigProperties,DomainEnumConfig,DomainEnumConfig> codegenContext = new CodegenContext<>(codegenConfig, domainEnumConfig, domainEnumConfig);
                    generateTarget(codegenContext, createDomainEnumCodegenParameter(codegenContext));
                }
            }
        }
        Map<String,DomainEntityConfig> domainEntityConfigs = codegenConfig.getDomain().getDomainEntities();
        if(!CollectionUtils.isEmpty(domainEntityConfigs)) { //2、生成领域实体对象
            for(Map.Entry<String,DomainEntityConfig> entry : domainEntityConfigs.entrySet()) {
                DomainEntityConfig domainEntityConfig = entry.getValue();
                CodegenContext<DomainObjectCodegenConfigProperties,DomainEntityConfig,DomainEntityConfig> codegenContext = new CodegenContext<>(codegenConfig, domainEntityConfig, domainEntityConfig);
                generateTarget(codegenContext, createDomainEntityCodegenParameter(codegenContext));
            }
        }

    }

    protected CodegenParameter createDomainEnumCodegenParameter(CodegenContext<DomainObjectCodegenConfigProperties,DomainEnumConfig,DomainEnumConfig> codegenContext) {
        CodegenParameter codegenParameter = super.createCodegenParameter(codegenContext, "DomainEnum.ftl");
        List<Map<String,Object>> enumValues = codegenContext.getTargetConfig().getDomainEnumValues().entrySet().stream().map(entry -> {
            Map<String,Object> enumValue = new HashMap<>();
            enumValue.put("enumValue", entry.getKey());
            enumValue.put("enumCode", CodegenUtils.quotingValueIfNecessary(entry.getValue()[0]));
            enumValue.put("enumName", CodegenUtils.quotingValueIfNecessary(entry.getValue()[1]));
            return enumValue;
        }).collect(Collectors.toList());
        codegenParameter.put("enumValues", enumValues);
        codegenParameter.put("enumCodeFieldType", codegenContext.getTargetConfig().getDomainEnumCodeField().getFieldType().getShortName());
        codegenParameter.put("enumCodeFieldName", codegenContext.getTargetConfig().getDomainEnumCodeField().getFieldName());
        codegenParameter.put("enumCodeGetterName", CodegenUtils.getGetterMethodName(codegenContext.getTargetConfig().getDomainEnumCodeField().getFieldName(), codegenContext.getTargetConfig().getDomainEnumCodeField().getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.getTargetAllImportTypes().add(codegenContext.getTargetConfig().getDomainEnumCodeField().getFieldType());
        codegenParameter.put("enumNameFieldType", codegenContext.getTargetConfig().getDomainEnumNameField().getFieldType().getShortName());
        codegenParameter.put("enumNameFieldName", codegenContext.getTargetConfig().getDomainEnumNameField().getFieldName());
        codegenParameter.put("enumNameGetterName", CodegenUtils.getGetterMethodName(codegenContext.getTargetConfig().getDomainEnumNameField().getFieldName(), codegenContext.getTargetConfig().getDomainEnumNameField().getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.getTargetAllImportTypes().add(codegenContext.getTargetConfig().getDomainEnumNameField().getFieldType());
        return codegenParameter;
    }

    protected CodegenParameter createDomainEntityCodegenParameter(CodegenContext<DomainObjectCodegenConfigProperties,DomainEntityConfig,DomainEntityConfig> codegenContext) {
        CodegenParameter codegenParameter = super.createCodegenParameter(codegenContext, "DomainEntity.ftl");
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(DomainObject.class.getName()));
        List<Map<String,Object>> entityInherentFields = new ArrayList<>(); //实体固有字段
        List<Map<String,Object>> entitySupportFields = new ArrayList<>(); //实体辅助字段
        List<Map<String,Object>> allEntityFields = new ArrayList<>(); //实体所有字段
        List<Map<String,Object>> entityDecodeEnumFields = new ArrayList<>();
        for(Map.Entry<String,DomainEntityFieldConfig> entry : codegenContext.getTargetConfig().getDomainEntityFields().entrySet()) {
            DomainEntityFieldConfig domainEntityFieldConfig = entry.getValue();
            if(domainEntityFieldConfig.getFieldType().isSupportField()) { //领域实体辅助字段
                entitySupportFields.add(buildEntitySupportField(domainEntityFieldConfig, codegenParameter));
                if(DomainObjectFieldType.DOMAIN_ENTITY_SUPPORTS_QUERY_OUTPUT_FIELD.equals(domainEntityFieldConfig.getFieldType())) {
                    DomainEntityColumnConfig domainEntityColumnConfig = domainEntityFieldConfig.getDomainEntityColumnConfig();
                    String shortDomainEnumType = getShortDomainEnumType(domainEntityColumnConfig.getDecodeEnumType());
                    DomainEnumConfig refDomainEnumConfig = resolveDecodeEnumConfig(domainEntityColumnConfig.getDecodeEnumType());
                    if(refDomainEnumConfig != null) {
                        entityDecodeEnumFields.add(buildEntityDecodeEnumField(domainEntityFieldConfig, refDomainEnumConfig, codegenParameter));
                    }
                }
            } else { //领域实体固有字段
                entityInherentFields.add(buildEntityInherentField(domainEntityFieldConfig, codegenParameter));
            }
        }
        codegenParameter.put("entityInherentFields", entityInherentFields);
        codegenParameter.put("entitySupportFields", entitySupportFields);
        allEntityFields.addAll(entityInherentFields);
        allEntityFields.addAll(entitySupportFields);
        codegenParameter.put("allEntityFields", allEntityFields);
        List<DomainEntityFieldConfig> idFields = codegenContext.getTargetConfig().getIdFields();
        if(idFields.size() == 1) { //单主键
            DomainEntityFieldConfig idField = idFields.get(0);
            codegenParameter.put("entityIdFieldType", idField.getFieldClass().getShortName());
            codegenParameter.put("entityIdFieldName", idField.getFieldName());
        } else if(idFields.size() > 1) { //复合主键
            FullyQualifiedJavaType idFieldType = new FullyQualifiedJavaType(ID.class.getName());
            codegenParameter.getTargetAllImportTypes().add(idFieldType);
            StringBuilder sb = new StringBuilder("new ID()");
            for(DomainEntityFieldConfig idField : idFields) {
                sb.append(".addKey(\"").append(idField.getFieldName()).append("\", ").append(idField.getFieldName()).append(")");
            }
            codegenParameter.put("entityIdFieldType", idFieldType.getShortName());
            codegenParameter.put("entityIdFieldName", sb.toString());
        }
        codegenParameter.put("entityDecodeEnumFields", entityDecodeEnumFields);
        return codegenParameter;
    }

    private Map<String,Object> buildEntitySupportField(DomainEntityFieldConfig domainEntityFieldConfig, CodegenParameter codegenParameter) {
        Map<String,Object> field = new HashMap<>();
        field.put("entityFieldName", domainEntityFieldConfig.getFieldName());
        field.put("entityFieldType", domainEntityFieldConfig.getFieldClass().getShortName());
        field.put("entityFieldComment", domainEntityFieldConfig.getFieldComment());
        field.put("entityFieldAnnotations", Collections.emptyList());
        field.put("entityFieldGetterName", CodegenUtils.getGetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldClass().getFullyQualifiedNameWithoutTypeParameters()));
        field.put("entityFieldSetterName", CodegenUtils.getSetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldClass().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.getTargetAllImportTypes().add(domainEntityFieldConfig.getFieldClass());
        return field;
    }

    private Map<String,Object> buildEntityInherentField(DomainEntityFieldConfig domainEntityFieldConfig, CodegenParameter codegenParameter) {
        Map<String,Object> field = new HashMap<>();
        field.put("entityFieldName", domainEntityFieldConfig.getFieldName());
        field.put("entityFieldType", domainEntityFieldConfig.getFieldClass().getShortName());
        field.put("entityFieldComment", domainEntityFieldConfig.getFieldComment());
        List<String> fieldAnnotations = new ArrayList<>();
        for(String validateExpression : domainEntityFieldConfig.getDomainEntityColumnConfig().getValidateExpressions()) {
            String[] expressions = validateExpression.split(":");
            fieldAnnotations.add(expressions[1]);
            codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(expressions[0]));
        }
        field.put("entityFieldAnnotations", fieldAnnotations);
        field.put("entityFieldGetterName", CodegenUtils.getGetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldClass().getFullyQualifiedNameWithoutTypeParameters()));
        field.put("entityFieldSetterName", CodegenUtils.getSetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldClass().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.getTargetAllImportTypes().add(domainEntityFieldConfig.getFieldClass());
        return field;
    }

    private Map<String,Object> buildEntityDecodeEnumField(DomainEntityFieldConfig domainEntityFieldConfig, DomainEnumConfig refDomainEnumConfig, CodegenParameter codegenParameter) {
        Map<String,Object> decodeEnumField = new HashMap<>();
        decodeEnumField.put("refEnumTypeName", refDomainEnumConfig.getDomainEnumName());
        decodeEnumField.put("entityFieldName", domainEntityFieldConfig.getDomainEntityColumnConfig().getIntrospectedColumn().getJavaFieldName()); //当前辅助字段关联的枚举值字段
        decodeEnumField.put("entityFieldSetterName", CodegenUtils.getSetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldClass().getFullyQualifiedNameWithoutTypeParameters()));
        decodeEnumField.put("refEnumNameFieldGetterName", CodegenUtils.getGetterMethodName(refDomainEnumConfig.getDomainEnumNameField().getFieldName(), refDomainEnumConfig.getDomainEnumNameField().getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(refDomainEnumConfig.getGeneratedTargetName(refDomainEnumConfig.getDomainEnumName(), true, false)));
        codegenParameter.getTargetAllImportTypes().add(refDomainEnumConfig.getDomainEnumNameField().getFieldType());
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Optional.class.getName()));
        return decodeEnumField;
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
    protected String getCodeName() {
        return "Domain领域对象代码";
    }

}
