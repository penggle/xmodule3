package com.penglecode.xmodule.common.codegen.domain;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.*;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 领域聚合根代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @since 202q/2/6 22:03
 */
public class DomainAggregateCodegenParameterBuilder extends CodegenParameterBuilder<DomainObjectCodegenConfigProperties, DomainAggregateConfig, DomainAggregateConfig, DomainAggregateCodegenParameter> {

    public DomainAggregateCodegenParameterBuilder(CodegenContext<DomainObjectCodegenConfigProperties, DomainAggregateConfig, DomainAggregateConfig> codegenContext) {
        super(codegenContext);
    }

    public DomainAggregateCodegenParameterBuilder(DomainObjectCodegenConfigProperties codegenConfig, DomainAggregateConfig targetConfig, DomainAggregateConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected DomainAggregateCodegenParameter setCustomCodegenParameter(DomainAggregateCodegenParameter codegenParameter) {
        codegenParameter.setTargetComment(getDomainObjectConfig().getDomainAggregateTitle() + "聚合根");
        List<ObjectField> inherentFields = new ArrayList<>();
        DomainAggregateConfig domainAggregateConfig = getTargetConfig();
        DomainEntityConfig masterDomainEntityConfig = getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateConfig.getAggregateMasterEntity());
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(masterDomainEntityConfig.getGeneratedTargetName(masterDomainEntityConfig.getDomainEntityName(), true, false)));
        Map<String,DomainAggregateFieldConfig> domainAggregateFieldConfigs = domainAggregateConfig.getDomainAggregateFields();
        for(Map.Entry<String,DomainAggregateFieldConfig> entry : domainAggregateFieldConfigs.entrySet()) {
            DomainAggregateFieldConfig domainAggregateFieldConfig = entry.getValue();
            DomainEntityConfig slaveDomainEntityConfig = getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateFieldConfig.getDomainAggregateSlaveConfig().getAggregateSlaveEntity());
            inherentFields.add(buildAggregateInherentField(domainAggregateFieldConfig, slaveDomainEntityConfig, codegenParameter)); //添加聚合属性
        }
        codegenParameter.setInherentFields(inherentFields);
        codegenParameter.setAllFields(inherentFields);
        codegenParameter.setTargetExtends(domainAggregateConfig.getAggregateMasterEntity());
        return codegenParameter;
    }

    private ObjectField buildAggregateInherentField(DomainAggregateFieldConfig domainAggregateFieldConfig, DomainEntityConfig slaveDomainEntityConfig, CodegenParameter codegenParameter) {
        ObjectField field = createDomainObjectFields(domainAggregateFieldConfig);
        List<String> fieldAnnotations = new ArrayList<>();
        Class<? extends Annotation> notEmptyAnnotationClass = NotNull.class;
        if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateFieldConfig.getDomainAggregateSlaveConfig().getMasterSlaveMapping().getMasterSlaveRelation())) {
            notEmptyAnnotationClass = NotEmpty.class; //1:N关系使用集合
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(List.class.getName()));
        }
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(notEmptyAnnotationClass.getName()));
        fieldAnnotations.add(String.format("@%s(message=\"%s\")", notEmptyAnnotationClass.getSimpleName(), domainAggregateFieldConfig.getFieldTitle() + "不能为空!"));
        field.setFieldAnnotations(fieldAnnotations);
        field.setFieldGetterName(domainAggregateFieldConfig.getFieldGetterName());
        field.setFieldSetterName(domainAggregateFieldConfig.getFieldSetterName());
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(slaveDomainEntityConfig.getGeneratedTargetName(slaveDomainEntityConfig.getDomainEntityName(), true, false)));
        return field;
    }

    protected ObjectField createDomainObjectFields(DomainObjectFieldConfig domainObjectFieldConfig) {
        ObjectField field = new ObjectField();
        field.setFieldName(domainObjectFieldConfig.getFieldName());
        field.setFieldType(domainObjectFieldConfig.getFieldType().getShortName());
        field.setFieldComment(domainObjectFieldConfig.getFieldComment());
        return field;
    }

    @Override
    protected String getTargetTemplateName() {
        return "DomainAggregate.ftl";
    }

}
