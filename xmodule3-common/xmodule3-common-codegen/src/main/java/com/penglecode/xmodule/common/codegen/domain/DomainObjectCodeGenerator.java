package com.penglecode.xmodule.common.codegen.domain;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.ModuleCodeGenerator;
import com.penglecode.xmodule.common.codegen.support.*;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.domain.DomainObject;
import com.penglecode.xmodule.common.domain.ID;
import com.penglecode.xmodule.common.util.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 领域对象代码生成器
 * 专门用于生成指定bizModule模块下的领域对象代码(如DO、Enum等)
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/9/7 21:26
 */
public class DomainObjectCodeGenerator extends ModuleCodeGenerator<DomainObjectCodegenConfigProperties> {

    public DomainObjectCodeGenerator(String bizModule) {
        super(bizModule);
    }

    @Override
    protected void generateCodes() throws Exception {
        DomainObjectCodegenConfigProperties codegenConfig = getModuleCodegenConfig();
        Set<DomainEnumConfigProperties> domainEnumConfigs = codegenConfig.getDomain().getDomainCommons().getDomainEnums();
        if(!CollectionUtils.isEmpty(domainEnumConfigs)) { //1、生成领域下的枚举
            for(DomainEnumConfigProperties domainEnumConfig : domainEnumConfigs) {
                DomainBoundedTargetConfigProperties<DomainEnumConfigProperties> targetConfig = new DomainBoundedTargetConfigProperties<>(domainEnumConfig, null, null);
                generateCode(codegenConfig, targetConfig, createDomainEnumTemplateParameter(codegenConfig, targetConfig));
            }
        }
        Map<String,DomainObjectConfigProperties> domainObjectConfigs = codegenConfig.getDomain().getDomainObjects();
        if(!CollectionUtils.isEmpty(domainObjectConfigs)) { //2、生成领域对象
            for(Map.Entry<String,DomainObjectConfigProperties> entry : domainObjectConfigs.entrySet()) {
                DomainObjectConfigProperties domainObjectConfig = entry.getValue();
                DomainBoundedTargetConfigProperties<DomainObjectConfigProperties> targetConfig = new DomainBoundedTargetConfigProperties<>(domainObjectConfig, domainObjectConfig, null);
                generateCode(codegenConfig, targetConfig, createDomainObjectTemplateParameter(codegenConfig, targetConfig));
            }
        }
        Map<String,DomainAggregateConfigProperties> domainAggregateConfigs = codegenConfig.getDomain().getDomainAggregates();
        if(!CollectionUtils.isEmpty(domainAggregateConfigs)) { //3、生成聚合对象
            for(Map.Entry<String,DomainAggregateConfigProperties> entry : domainAggregateConfigs.entrySet()) {
                DomainAggregateConfigProperties domainAggregateConfig = entry.getValue();
                DomainObjectConfigProperties masterDomainObjectConfig = domainObjectConfigs.get(domainAggregateConfig.getAggregateMasterName());
                DomainBoundedTargetConfigProperties<DomainAggregateConfigProperties> targetConfig = new DomainBoundedTargetConfigProperties<>(domainAggregateConfig, masterDomainObjectConfig, domainAggregateConfig);
                generateCode(codegenConfig, targetConfig, createDomainAggregateTemplateParameter(codegenConfig, targetConfig));
            }
        }
    }

    protected TemplateParameter createDomainEnumTemplateParameter(ModuleCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<DomainEnumConfigProperties> targetConfig) {
        TemplateParameter templateParameter = new TemplateParameter();
        Set<FullyQualifiedJavaType> allImportedTypes = new HashSet<>();
        addCommonTemplateParameter(templateParameter, codegenConfig, targetConfig, "DomainEnum.ftl");
        templateParameter.put("enumTitle", targetConfig.getGeneratedTargetConfig().getEnumTitle());
        templateParameter.put("enumName", targetConfig.getGeneratedTargetConfig().getEnumName());
        List<Map<String,Object>> enumValues = targetConfig.getGeneratedTargetConfig().getEnumValues().entrySet().stream().map(entry -> {
            Map<String,Object> enumValue = new HashMap<>();
            enumValue.put("value", entry.getKey());
            enumValue.put("code", CodegenUtils.quotingValueIfNecessary(entry.getValue()[0]));
            enumValue.put("name", CodegenUtils.quotingValueIfNecessary(entry.getValue()[1]));
            return enumValue;
        }).collect(Collectors.toList());
        templateParameter.put("enumValues", enumValues);
        templateParameter.put("codeFieldType", targetConfig.getGeneratedTargetConfig().getCodeField().getRight().getShortName());
        templateParameter.put("codeFieldName", targetConfig.getGeneratedTargetConfig().getCodeField().getLeft());
        templateParameter.put("codeGetterName", CodegenUtils.getGetterMethodName(targetConfig.getGeneratedTargetConfig().getCodeField().getLeft(), targetConfig.getGeneratedTargetConfig().getCodeField().getRight().getFullyQualifiedNameWithoutTypeParameters()));
        allImportedTypes.add(targetConfig.getGeneratedTargetConfig().getCodeField().getRight());
        templateParameter.put("nameFieldType", targetConfig.getGeneratedTargetConfig().getNameField().getRight().getShortName());
        templateParameter.put("nameFieldName", targetConfig.getGeneratedTargetConfig().getNameField().getLeft());
        templateParameter.put("nameGetterName", CodegenUtils.getGetterMethodName(targetConfig.getGeneratedTargetConfig().getNameField().getLeft(), targetConfig.getGeneratedTargetConfig().getNameField().getRight().getFullyQualifiedNameWithoutTypeParameters()));
        allImportedTypes.add(targetConfig.getGeneratedTargetConfig().getNameField().getRight());
        calculateImportedTypes(allImportedTypes, templateParameter);
        return templateParameter;
    }

    protected TemplateParameter createDomainObjectTemplateParameter(ModuleCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<DomainObjectConfigProperties> targetConfig) {
        TemplateParameter templateParameter = new TemplateParameter();
        Set<FullyQualifiedJavaType> allImportedTypes = new HashSet<>();
        addCommonTemplateParameter(templateParameter, codegenConfig, targetConfig, "DomainObject.ftl");
        allImportedTypes.add(new FullyQualifiedJavaType(DomainObject.class.getName()));

        List<Map<String,Object>> domainFields = new ArrayList<>();
        List<Map<String,Object>> supportFields = new ArrayList<>();
        List<Map<String,Object>> allFields = new ArrayList<>();
        List<Map<String,Object>> decodeEnumFields = new ArrayList<>();
        for(Map.Entry<String,DomainObjectFieldConfigProperties> entry : targetConfig.getGeneratedTargetConfig().getDomainObjectFields().entrySet()) {
            DomainObjectFieldConfigProperties domainFieldConfig = entry.getValue();
            if(domainFieldConfig.isSupportField()) { //领域对象辅助字段
                supportFields.add(buildSupportField(domainFieldConfig, allImportedTypes));
                if(ObjectSupportFieldType.QUERY_OUTPUT.equals(domainFieldConfig.getSupportFieldType())) {
                    DomainObjectColumnConfigProperties refEnumFieldConfig = domainFieldConfig.getDomainColumnConfig();
                    String shortDomainEnumType = getShortDomainEnumType(refEnumFieldConfig.getDecodeEnumType());
                    DomainEnumTypeInfo refEnumTypeInfo = resolveDecodeEnumTypeInfo(refEnumFieldConfig.getDecodeEnumType());
                    if(refEnumTypeInfo != null) {
                        Map<String,Object> decodeEnumField = new HashMap<>();
                        decodeEnumField.put("refEnumTypeName", shortDomainEnumType);
                        decodeEnumField.put("refFieldName", refEnumFieldConfig.getIntrospectedColumn().getJavaFieldName()); //当前辅助字段关联的枚举值字段
                        decodeEnumField.put("fieldSetterName", CodegenUtils.getSetterMethodName(domainFieldConfig.getFieldName(), domainFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
                        decodeEnumField.put("refEnumGetterName", CodegenUtils.getGetterMethodName(refEnumTypeInfo.getNameFieldName(), refEnumTypeInfo.getNameFieldType().getFullyQualifiedNameWithoutTypeParameters()));
                        decodeEnumFields.add(decodeEnumField);
                        allImportedTypes.add(refEnumTypeInfo.getEnumType());
                        allImportedTypes.add(refEnumTypeInfo.getNameFieldType());
                        allImportedTypes.add(refEnumTypeInfo.getCodeFieldType());
                        allImportedTypes.add(new FullyQualifiedJavaType(Optional.class.getName()));
                    }
                }
            } else { //领域对象固有字段
                domainFields.add(buildDomainField(domainFieldConfig, allImportedTypes));
            }
        }
        templateParameter.put("domainFields", domainFields);
        templateParameter.put("supportFields", supportFields);
        allFields.addAll(domainFields);
        allFields.addAll(supportFields);
        templateParameter.put("allFields", allFields);
        List<DomainObjectFieldConfigProperties> pkFields = targetConfig.getGeneratedTargetConfig().getPkFields();
        if(pkFields.size() == 1) { //单主键
            DomainObjectFieldConfigProperties pkField = pkFields.get(0);
            templateParameter.put("pkFieldType", pkField.getFieldType().getShortName());
            templateParameter.put("pkFieldName", pkField.getFieldName());
        } else if(pkFields.size() > 1) { //复合主键
            FullyQualifiedJavaType pkFieldType = new FullyQualifiedJavaType(ID.class.getName());
            allImportedTypes.add(pkFieldType);
            StringBuilder sb = new StringBuilder("new ID()");
            for(DomainObjectFieldConfigProperties pkField : pkFields) {
                sb.append(".addKey(\"").append(pkField.getFieldName()).append("\", ").append(pkField.getFieldName()).append(")");
            }
            templateParameter.put("pkFieldType", pkFieldType.getShortName());
            templateParameter.put("pkFieldName", sb.toString());
        }
        templateParameter.put("overrideProcessMethod", !decodeEnumFields.isEmpty());
        templateParameter.put("decodeEnumFields", decodeEnumFields);
        calculateImportedTypes(allImportedTypes, templateParameter);
        return templateParameter;
    }

    protected TemplateParameter createDomainAggregateTemplateParameter(ModuleCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<DomainAggregateConfigProperties> targetConfig) {
        TemplateParameter templateParameter = new TemplateParameter();
        Set<FullyQualifiedJavaType> allImportedTypes = new HashSet<>();
        addCommonTemplateParameter(templateParameter, codegenConfig, targetConfig, "DomainAggregate.ftl");
        List<Map<String,Object>> domainFields = new ArrayList<>();
        DomainAggregateConfigProperties domainAggregateConfig = targetConfig.getGeneratedTargetConfig();
        DomainObjectConfigProperties masterDomainObjectConfig = targetConfig.getDomainObjectConfig();
        if(!domainAggregateConfig.getTargetPackage().equals(masterDomainObjectConfig.getTargetPackage())) { //不在同一个包中?
            allImportedTypes.add(new FullyQualifiedJavaType(masterDomainObjectConfig.getGeneratedTargetName(null, true, false)));
        }
        Map<String,DomainAggregateFieldConfigProperties> domainAggregateFieldConfigs = domainAggregateConfig.getAggregateObjectFields();
        for(Map.Entry<String,DomainAggregateFieldConfigProperties> entry : domainAggregateFieldConfigs.entrySet()) {
            DomainAggregateFieldConfigProperties domainAggregateFieldConfig = entry.getValue();
            DomainObjectConfigProperties slaveDomainObjectConfig = codegenConfig.getDomain().getDomainObjects().get(domainAggregateFieldConfig.getDomainAggregateSlaveConfig().getAggregateSlaveName());
            domainFields.add(buildAggregateField(domainAggregateFieldConfig, slaveDomainObjectConfig, allImportedTypes)); //添加聚合属性
        }
        calculateImportedTypes(allImportedTypes, templateParameter);
        templateParameter.put("domainFields", domainFields);
        templateParameter.put("allFields", domainFields);
        templateParameter.put("aggregateMasterName", domainAggregateConfig.getAggregateMasterName());
        templateParameter.put("aggregateObjectTitle", domainAggregateConfig.getAggregateObjectTitle());
        return templateParameter;
    }

    private Map<String,Object> buildSupportField(DomainObjectFieldConfigProperties domainFieldConfig, Set<FullyQualifiedJavaType> allImportedTypes) {
        Map<String,Object> field = new HashMap<>();
        field.put("fieldName", domainFieldConfig.getFieldName());
        field.put("fieldType", domainFieldConfig.getFieldType().getShortName());
        field.put("fieldComment", domainFieldConfig.getFieldComment());
        field.put("fieldAnnotations", Collections.emptyList());
        field.put("fieldGetterName", CodegenUtils.getGetterMethodName(domainFieldConfig.getFieldName(), domainFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        field.put("fieldSetterName", CodegenUtils.getSetterMethodName(domainFieldConfig.getFieldName(), domainFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        allImportedTypes.add(domainFieldConfig.getFieldType());
        return field;
    }

    private Map<String,Object> buildDomainField(DomainObjectFieldConfigProperties domainFieldConfig, Set<FullyQualifiedJavaType> allImportedTypes) {
        Map<String,Object> field = new HashMap<>();
        field.put("fieldName", domainFieldConfig.getFieldName());
        field.put("fieldType", domainFieldConfig.getFieldType().getShortName());
        field.put("fieldComment", domainFieldConfig.getFieldComment());
        List<String> fieldAnnotations = new ArrayList<>();
        for(String validateExpression : domainFieldConfig.getDomainColumnConfig().getValidateExpressions()) {
            String[] expressions = validateExpression.split(":");
            fieldAnnotations.add(expressions[1]);
            allImportedTypes.add(new FullyQualifiedJavaType(expressions[0]));
        }
        field.put("fieldAnnotations", fieldAnnotations);
        field.put("fieldGetterName", CodegenUtils.getGetterMethodName(domainFieldConfig.getFieldName(), domainFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        field.put("fieldSetterName", CodegenUtils.getSetterMethodName(domainFieldConfig.getFieldName(), domainFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        allImportedTypes.add(domainFieldConfig.getFieldType());
        return field;
    }

    private Map<String,Object> buildAggregateField(DomainAggregateFieldConfigProperties domainAggregateFieldConfig, DomainObjectConfigProperties slaveDomainObjectConfig, Set<FullyQualifiedJavaType> allImportedTypes) {
        Map<String,Object> field = new HashMap<>();
        field.put("fieldName", domainAggregateFieldConfig.getFieldName());
        field.put("fieldType", domainAggregateFieldConfig.getFieldType().getShortName());
        field.put("fieldComment", domainAggregateFieldConfig.getFieldComment());
        List<String> fieldAnnotations = new ArrayList<>();
        Class<? extends Annotation> notEmptyAnnotationClass = NotNull.class;
        if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateFieldConfig.getDomainAggregateSlaveConfig().getMasterSlaveMapping().getMasterSlaveRelation())) {
            notEmptyAnnotationClass = NotEmpty.class; //1:N关系使用集合
            allImportedTypes.add(new FullyQualifiedJavaType(List.class.getName()));
        }
        fieldAnnotations.add(String.format("@%s(message=\"%s\")", notEmptyAnnotationClass.getSimpleName(), domainAggregateFieldConfig.getFieldTitle() + "不能为空!"));
        field.put("fieldAnnotations", fieldAnnotations);
        field.put("fieldGetterName", CodegenUtils.getGetterMethodName(domainAggregateFieldConfig.getFieldName(), domainAggregateFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        field.put("fieldSetterName", CodegenUtils.getSetterMethodName(domainAggregateFieldConfig.getFieldName(), domainAggregateFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        allImportedTypes.add(new FullyQualifiedJavaType(notEmptyAnnotationClass.getName()));
        if(!domainAggregateFieldConfig.getDomainAggregateConfig().getTargetPackage().equals(slaveDomainObjectConfig.getTargetPackage())) { //不在同一个包中?
            allImportedTypes.add(new FullyQualifiedJavaType(slaveDomainObjectConfig.getGeneratedTargetName(null, true, false)));
        }
        return field;
    }

    @Override
    protected CodegenModule getCodeModule() {
        return CodegenModule.DOMAIN;
    }

}
