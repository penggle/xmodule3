package com.penglecode.xmodule.common.codegen.domain;

import com.penglecode.xmodule.common.codegen.ModuleCodeGenerator;
import com.penglecode.xmodule.common.codegen.config.*;
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
 * 领域对象(枚举、实体、聚合根)代码生成器
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
    protected String getCodeName() {
        return "Domain领域对象代码";
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
        Map<String,DomainAggregateConfig> domainAggregateConfigs = codegenConfig.getDomain().getDomainAggregates();
        if(!CollectionUtils.isEmpty(domainAggregateConfigs)) { //3、生成聚合对象
            for(Map.Entry<String,DomainAggregateConfig> entry : domainAggregateConfigs.entrySet()) {
                DomainAggregateConfig domainAggregateConfig = entry.getValue();
                CodegenContext<DomainObjectCodegenConfigProperties,DomainAggregateConfig,DomainAggregateConfig> codegenContext = new CodegenContext<>(codegenConfig, domainAggregateConfig, domainAggregateConfig);
                generateTarget(codegenContext, createDomainAggregateCodegenParameter(codegenContext));
            }
        }
    }

    /**
     * 创建领域枚举代码生成模板参数
     * @param codegenContext
     * @return
     */
    protected CodegenParameter createDomainEnumCodegenParameter(CodegenContext<DomainObjectCodegenConfigProperties,DomainEnumConfig,DomainEnumConfig> codegenContext) {
        CodegenParameter codegenParameter = super.createCodegenParameter(codegenContext, "DomainEnum.ftl");
        codegenParameter.put("targetComment", codegenContext.getDomainObjectConfig().getDomainEnumTitle());
        List<Map<String,Object>> enumValues = codegenContext.getTargetConfig().getDomainEnumValues().entrySet().stream().map(entry -> {
            Map<String,Object> enumValue = new HashMap<>();
            enumValue.put("enumValue", entry.getKey());
            enumValue.put("codeValue", CodegenUtils.quotingValueIfNecessary(entry.getValue()[0]));
            enumValue.put("nameValue", CodegenUtils.quotingValueIfNecessary(entry.getValue()[1]));
            return enumValue;
        }).collect(Collectors.toList());
        codegenParameter.put("enumValues", enumValues);
        codegenParameter.put("codeFieldType", codegenContext.getTargetConfig().getDomainEnumCodeField().getFieldType().getShortName());
        codegenParameter.put("codeFieldName", codegenContext.getTargetConfig().getDomainEnumCodeField().getFieldName());
        codegenParameter.put("codeGetterName", CodegenUtils.getGetterMethodName(codegenContext.getTargetConfig().getDomainEnumCodeField().getFieldName(), codegenContext.getTargetConfig().getDomainEnumCodeField().getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.getTargetAllImportTypes().add(codegenContext.getTargetConfig().getDomainEnumCodeField().getFieldType());
        codegenParameter.put("nameFieldType", codegenContext.getTargetConfig().getDomainEnumNameField().getFieldType().getShortName());
        codegenParameter.put("nameFieldName", codegenContext.getTargetConfig().getDomainEnumNameField().getFieldName());
        codegenParameter.put("nameGetterName", CodegenUtils.getGetterMethodName(codegenContext.getTargetConfig().getDomainEnumNameField().getFieldName(), codegenContext.getTargetConfig().getDomainEnumNameField().getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.getTargetAllImportTypes().add(codegenContext.getTargetConfig().getDomainEnumNameField().getFieldType());
        return codegenParameter;
    }

    /**
     * 创建领域实体代码生成模板参数
     * @param codegenContext
     * @return
     */
    protected CodegenParameter createDomainEntityCodegenParameter(CodegenContext<DomainObjectCodegenConfigProperties,DomainEntityConfig,DomainEntityConfig> codegenContext) {
        CodegenParameter codegenParameter = super.createCodegenParameter(codegenContext, "DomainEntity.ftl");
        codegenParameter.put("targetComment", codegenContext.getDomainObjectConfig().getDomainEntityTitle() + "实体");
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(DomainObject.class.getName()));
        List<Map<String,Object>> inherentFields = new ArrayList<>(); //实体固有字段
        List<Map<String,Object>> supportFields = new ArrayList<>(); //实体辅助字段
        List<Map<String,Object>> allFields = new ArrayList<>(); //实体所有字段
        List<Map<String,Object>> decodeEnumFields = new ArrayList<>();
        for(Map.Entry<String,DomainEntityFieldConfig> entry : codegenContext.getTargetConfig().getDomainEntityFields().entrySet()) {
            DomainEntityFieldConfig domainEntityFieldConfig = entry.getValue();
            if(domainEntityFieldConfig.getFieldClass().isSupportField()) { //领域实体辅助字段
                supportFields.add(buildEntitySupportField(domainEntityFieldConfig, codegenParameter));
                //处理领域对象数据出站DomainObject#beforeOutbound()实现
                if(DomainObjectFieldClass.DOMAIN_ENTITY_SUPPORTS_QUERY_OUTPUT_FIELD.equals(domainEntityFieldConfig.getFieldType())) {
                    DomainEntityColumnConfig domainEntityColumnConfig = domainEntityFieldConfig.getDomainEntityColumnConfig(); //当前辅助字段是辅助谁的?
                    String shortDomainEnumType = getShortDomainEnumType(domainEntityColumnConfig.getDecodeEnumType());
                    DomainEnumConfig refDomainEnumConfig = resolveDecodeEnumConfig(domainEntityColumnConfig.getDecodeEnumType());
                    if(refDomainEnumConfig != null) {
                        decodeEnumFields.add(buildEntityDecodeEnumField(domainEntityFieldConfig, refDomainEnumConfig, codegenParameter));
                    }
                }
            } else { //领域实体固有字段
                inherentFields.add(buildEntityInherentField(domainEntityFieldConfig, codegenParameter));
            }
        }
        codegenParameter.put("inherentFields", inherentFields);
        codegenParameter.put("supportFields", supportFields);
        codegenParameter.put("decodeEnumFields", decodeEnumFields);
        allFields.addAll(inherentFields);
        allFields.addAll(supportFields);
        codegenParameter.put("allFields", allFields);
        attachDomainEntityIdField(codegenContext, codegenParameter);
        return codegenParameter;
    }

    /**
     * 附带上领域实体ID字段参数
     * @param codegenContext
     * @param codegenParameter
     */
    protected void attachDomainEntityIdField(CodegenContext<DomainObjectCodegenConfigProperties,DomainEntityConfig,DomainEntityConfig> codegenContext, CodegenParameter codegenParameter) {
        List<DomainEntityFieldConfig> idFields = codegenContext.getTargetConfig().getIdFields();
        if(idFields.size() == 1) { //单主键
            DomainEntityFieldConfig idField = idFields.get(0);
            codegenParameter.put("idFieldType", idField.getFieldType().getShortName());
            codegenParameter.put("idFieldName", idField.getFieldName());
        } else if(idFields.size() > 1) { //复合主键
            FullyQualifiedJavaType idFieldType = new FullyQualifiedJavaType(ID.class.getName());
            codegenParameter.getTargetAllImportTypes().add(idFieldType);
            StringBuilder sb = new StringBuilder("new ID()");
            for(DomainEntityFieldConfig idField : idFields) {
                sb.append(".addKey(\"").append(idField.getFieldName()).append("\", ").append(idField.getFieldName()).append(")");
            }
            codegenParameter.put("idFieldType", idFieldType.getShortName());
            codegenParameter.put("idFieldName", sb.toString());
        }
    }

    /**
     * 创建领域聚合根代码生成模板参数
     * @param codegenContext
     * @return
     */
    protected CodegenParameter createDomainAggregateCodegenParameter(CodegenContext<DomainObjectCodegenConfigProperties,DomainAggregateConfig,DomainAggregateConfig> codegenContext) {
        CodegenParameter codegenParameter = super.createCodegenParameter(codegenContext, "DomainAggregate.ftl");
        codegenParameter.put("targetComment", codegenContext.getDomainObjectConfig().getDomainAggregateTitle() + "聚合根");
        List<Map<String,Object>> inherentFields = new ArrayList<>();
        DomainAggregateConfig domainAggregateConfig = codegenContext.getTargetConfig();
        DomainEntityConfig masterDomainEntityConfig = codegenContext.getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateConfig.getAggregateMasterEntity());
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(masterDomainEntityConfig.getGeneratedTargetName(masterDomainEntityConfig.getDomainEntityName(), true, false)));
        Map<String,DomainAggregateFieldConfig> domainAggregateFieldConfigs = domainAggregateConfig.getDomainAggregateFields();
        for(Map.Entry<String,DomainAggregateFieldConfig> entry : domainAggregateFieldConfigs.entrySet()) {
            DomainAggregateFieldConfig domainAggregateFieldConfig = entry.getValue();
            DomainEntityConfig slaveDomainEntityConfig = codegenContext.getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateFieldConfig.getDomainAggregateSlaveConfig().getAggregateSlaveEntity());
            inherentFields.add(buildAggregateInherentField(domainAggregateFieldConfig, slaveDomainEntityConfig, codegenParameter)); //添加聚合属性
        }
        codegenParameter.put("inherentFields", inherentFields);
        codegenParameter.put("allFields", inherentFields);
        codegenParameter.setTargetExtends(domainAggregateConfig.getAggregateMasterEntity());
        return codegenParameter;
    }

    private Map<String,Object> buildEntitySupportField(DomainEntityFieldConfig domainEntityFieldConfig, CodegenParameter codegenParameter) {
        Map<String,Object> field = createDomainObjectFields(domainEntityFieldConfig);
        field.put("fieldAnnotations", Collections.emptyList());
        field.put("fieldGetterName", CodegenUtils.getGetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        field.put("fieldSetterName", CodegenUtils.getSetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.getTargetAllImportTypes().add(domainEntityFieldConfig.getFieldType());
        return field;
    }

    private Map<String,Object> buildEntityInherentField(DomainEntityFieldConfig domainEntityFieldConfig, CodegenParameter codegenParameter) {
        Map<String,Object> field = createDomainObjectFields(domainEntityFieldConfig);
        List<String> fieldAnnotations = new ArrayList<>();
        for(String validateExpression : domainEntityFieldConfig.getDomainEntityColumnConfig().getValidateExpressions()) {
            String[] expressions = validateExpression.split(":");
            fieldAnnotations.add(expressions[1]);
            codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(expressions[0]));
        }
        field.put("fieldAnnotations", fieldAnnotations);
        field.put("fieldGetterName", CodegenUtils.getGetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        field.put("fieldSetterName", CodegenUtils.getSetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.getTargetAllImportTypes().add(domainEntityFieldConfig.getFieldType());
        return field;
    }

    private Map<String,Object> buildEntityDecodeEnumField(DomainEntityFieldConfig domainEntityFieldConfig, DomainEnumConfig refDomainEnumConfig, CodegenParameter codegenParameter) {
        Map<String,Object> field = new HashMap<>();
        field.put("refEnumTypeName", refDomainEnumConfig.getDomainEnumName());
        field.put("entityFieldName", domainEntityFieldConfig.getDomainEntityColumnConfig().getIntrospectedColumn().getJavaFieldName()); //当前辅助字段关联的枚举值字段
        field.put("entityFieldSetterName", CodegenUtils.getSetterMethodName(domainEntityFieldConfig.getFieldName(), domainEntityFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        field.put("refEnumNameFieldGetterName", CodegenUtils.getGetterMethodName(refDomainEnumConfig.getDomainEnumNameField().getFieldName(), refDomainEnumConfig.getDomainEnumNameField().getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(refDomainEnumConfig.getGeneratedTargetName(refDomainEnumConfig.getDomainEnumName(), true, false)));
        codegenParameter.getTargetAllImportTypes().add(refDomainEnumConfig.getDomainEnumNameField().getFieldType());
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Optional.class.getName()));
        return field;
    }

    private Map<String,Object> buildAggregateInherentField(DomainAggregateFieldConfig domainAggregateFieldConfig, DomainEntityConfig slaveDomainEntityConfig, CodegenParameter codegenParameter) {
        Map<String,Object> field = createDomainObjectFields(domainAggregateFieldConfig);
        List<String> fieldAnnotations = new ArrayList<>();
        Class<? extends Annotation> notEmptyAnnotationClass = NotNull.class;
        if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateFieldConfig.getDomainAggregateSlaveConfig().getMasterSlaveMapping().getMasterSlaveRelation())) {
            notEmptyAnnotationClass = NotEmpty.class; //1:N关系使用集合
            codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(List.class.getName()));
        }
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(notEmptyAnnotationClass.getName()));
        fieldAnnotations.add(String.format("@%s(message=\"%s\")", notEmptyAnnotationClass.getSimpleName(), domainAggregateFieldConfig.getFieldTitle() + "不能为空!"));
        field.put("fieldAnnotations", fieldAnnotations);
        field.put("fieldGetterName", CodegenUtils.getGetterMethodName(domainAggregateFieldConfig.getFieldName(), domainAggregateFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        field.put("fieldSetterName", CodegenUtils.getSetterMethodName(domainAggregateFieldConfig.getFieldName(), domainAggregateFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(slaveDomainEntityConfig.getGeneratedTargetName(slaveDomainEntityConfig.getDomainEntityName(), true, false)));
        return field;
    }

    private Map<String,Object> createDomainObjectFields(DomainObjectFieldConfig domainObjectFieldConfig) {
        Map<String,Object> field = new HashMap<>();
        field.put("fieldName", domainObjectFieldConfig.getFieldName());
        field.put("fieldType", domainObjectFieldConfig.getFieldType().getShortName());
        field.put("fieldComment", domainObjectFieldConfig.getFieldComment());
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

}