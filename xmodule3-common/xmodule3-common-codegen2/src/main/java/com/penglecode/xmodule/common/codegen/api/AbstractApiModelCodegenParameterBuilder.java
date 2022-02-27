package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.*;

import java.util.Collections;
import java.util.List;

/**
 * API接口数据模型代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/20 21:25
 */
public abstract class AbstractApiModelCodegenParameterBuilder<D extends DomainObjectConfig> extends CodegenParameterBuilder<ApiCodegenConfigProperties, ApiModelConfig, D, ApiModelCodegenParameter> {

    public AbstractApiModelCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, ApiModelConfig, D> codegenContext) {
        super(codegenContext);
    }

    public AbstractApiModelCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, ApiModelConfig targetConfig, D domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected ApiModelCodegenParameter setCommonCodegenParameter(ApiModelCodegenParameter codegenParameter) {
        codegenParameter = super.setCommonCodegenParameter(codegenParameter);
        String domainObjectName = getDomainObjectConfig().getDomainObjectAlias(); //使用别名来构造DTO名字
        codegenParameter.setTargetFileName(getTargetConfig().getGeneratedTargetName(domainObjectName, false, true));
        codegenParameter.setTargetClass(getTargetConfig().getGeneratedTargetName(domainObjectName, false, false));
        return codegenParameter;
    }

    protected ObjectFieldParameter buildDomainEntityField(DomainEntityFieldConfig domainEntityFieldConfig, CodegenParameter codegenParameter) {
        ObjectFieldParameter field = new ObjectFieldParameter();
        field.setFieldName(domainEntityFieldConfig.getFieldName());
        field.setFieldType(domainEntityFieldConfig.getFieldType().getShortName());
        field.setFieldComment(domainEntityFieldConfig.getFieldComment());
        field.setFieldAnnotations(Collections.singletonList(String.format("@Schema(description=\"%s\")", domainEntityFieldConfig.getFieldComment())));
        field.setFieldGetterName(domainEntityFieldConfig.getFieldGetterName());
        field.setFieldSetterName(domainEntityFieldConfig.getFieldSetterName());
        codegenParameter.addTargetImportType(domainEntityFieldConfig.getFieldType());
        return field;
    }

    protected ObjectFieldParameter buildDomainAggregateField(DomainAggregateFieldConfig domainAggregateFieldConfig, DomainEntityConfig slaveDomainEntityConfig, ApiModelConfig slaveApiModelConfig, CodegenParameter codegenParameter) {
        String domainObjectName = slaveDomainEntityConfig.getDomainEntityAlias();
        ObjectFieldParameter field = new ObjectFieldParameter();
        field.setFieldName(domainAggregateFieldConfig.getFieldName());
        field.setFieldType(domainAggregateFieldConfig.getFieldType().getShortName().replace(slaveDomainEntityConfig.getDomainEntityName(), slaveApiModelConfig.getGeneratedTargetName(domainObjectName, false, false)));
        field.setFieldComment(domainAggregateFieldConfig.getFieldComment());
        if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateFieldConfig.getDomainAggregateSlaveConfig().getMasterSlaveMapping().getMasterSlaveRelation())) {
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(List.class.getName()));
        }
        field.setFieldAnnotations(Collections.singletonList(String.format("@Schema(description=\"%s\")", domainAggregateFieldConfig.getFieldComment())));
        field.setFieldGetterName(domainAggregateFieldConfig.getFieldGetterName());
        field.setFieldSetterName(domainAggregateFieldConfig.getFieldSetterName());
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(slaveApiModelConfig.getGeneratedTargetName(domainObjectName, true, false)));
        return field;
    }

    @Override
    protected String getTargetTemplateName() {
        return "ApiModel.ftl";
    }

}
