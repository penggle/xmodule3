package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.*;
import com.penglecode.xmodule.common.model.BaseDTO;
import com.penglecode.xmodule.common.util.TemplateUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * API接口数据模型代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/20 21:25
 */
public class ApiModelCodegenParameterBuilder<D extends DomainObjectConfig> extends CodegenParameterBuilder<ApiCodegenConfigProperties, ApiModelConfig, D, ApiModelCodegenParameter> {

    public ApiModelCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, ApiModelConfig, D> codegenContext) {
        super(codegenContext);
    }

    public ApiModelCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, ApiModelConfig targetConfig, D domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected ApiModelCodegenParameter setCustomCodegenParameter(ApiModelCodegenParameter codegenParameter) {
        DomainObjectConfig domainObjectConfig = getDomainObjectConfig();
        ApiModelType apiModelType = getTargetConfig().getModelType();
        String targetComment = TemplateUtils.parseTemplate(apiModelType.getModelDescTemplate(), Collections.singletonMap("domainObjectTitle", domainObjectConfig.getDomainObjectTitle()));
        codegenParameter.setTargetComment(targetComment);
        codegenParameter.setTargetAnnotations(Collections.singletonList(String.format("@Schema(description=\"%s\")", targetComment)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Schema.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(BaseDTO.class.getName()));
        codegenParameter.setTargetImplements(Collections.singletonList("BaseDTO"));
        if(domainObjectConfig instanceof DomainEntityConfig) {
            setDomainEntityCodegenParameter(codegenParameter, (DomainEntityConfig) domainObjectConfig);
        } else if(domainObjectConfig instanceof DomainAggregateConfig) {
            setDomainAggregateCodegenParameter(codegenParameter, (DomainAggregateConfig) domainObjectConfig);
        }
        return codegenParameter;
    }

    /**
     * 处理DomainEntityConfig的情况
     * @param codegenParameter
     * @param domainEntityConfig
     */
    protected void setDomainEntityCodegenParameter(ApiModelCodegenParameter codegenParameter, DomainEntityConfig domainEntityConfig) {
        List<ObjectFieldParameter> inherentFields = new ArrayList<>();
        ApiModelType apiModelType = getTargetConfig().getModelType();
        for(Map.Entry<String,DomainEntityFieldConfig> entry : domainEntityConfig.getDomainEntityFields().entrySet()) {
            DomainEntityFieldConfig domainEntityFieldConfig = entry.getValue();
            if(domainEntityFieldConfig.getFieldClass().isSupportField()) { //领域实体辅助字段
                if(!ApiModelType.CREATE_REQUEST.equals(apiModelType) && !ApiModelType.MODIFY_REQUEST.equals(apiModelType)) { //新增/修改时不需要辅助字段
                    if(DomainObjectFieldClass.DOMAIN_ENTITY_SUPPORTS_QUERY_OUTBOUND_FIELD.equals(domainEntityFieldConfig.getFieldClass())) { //查询结果出站辅助字段?
                        if(ApiModelType.OUTBOUND_DTO.equals(apiModelType) || ApiModelType.QUERY_RESPONSE.equals(apiModelType)) {
                            inherentFields.add(buildDomainEntityField(domainEntityFieldConfig, codegenParameter));
                        }
                    } else if(DomainObjectFieldClass.DOMAIN_ENTITY_SUPPORTS_QUERY_INBOUND_FIELD.equals(domainEntityFieldConfig.getFieldClass())) { //查询条件入站辅助字段?
                        if(ApiModelType.QUERY_REQUEST.equals(apiModelType)) {
                            inherentFields.add(buildDomainEntityField(domainEntityFieldConfig, codegenParameter));
                        }
                    }
                }
            } else { //领域实体固有字段
                inherentFields.add(buildDomainEntityField(domainEntityFieldConfig, codegenParameter));
            }
        }
        codegenParameter.setInherentFields(inherentFields);
    }

    /**
     * 处理DomainAggregateConfig的情况
     * @param codegenParameter
     * @param domainAggregateConfig
     */
    protected void setDomainAggregateCodegenParameter(ApiModelCodegenParameter codegenParameter, DomainAggregateConfig domainAggregateConfig) {
        List<ObjectFieldParameter> inherentFields = new ArrayList<>();
        ApiModelType apiModelType = getTargetConfig().getModelType();
        DomainEntityConfig masterDomainEntityConfig = getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateConfig.getAggregateMasterEntity());
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(masterDomainEntityConfig.getGeneratedTargetName(masterDomainEntityConfig.getDomainEntityName(), true, false)));
        Map<String,DomainAggregateFieldConfig> domainAggregateFieldConfigs = domainAggregateConfig.getDomainAggregateFields();
        for(Map.Entry<String,DomainAggregateFieldConfig> entry : domainAggregateFieldConfigs.entrySet()) {
            DomainAggregateFieldConfig domainAggregateFieldConfig = entry.getValue();
            DomainEntityConfig slaveDomainEntityConfig = getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateFieldConfig.getDomainAggregateSlaveConfig().getAggregateSlaveEntity());
            inherentFields.add(buildDomainAggregateField(domainAggregateFieldConfig, slaveDomainEntityConfig, codegenParameter)); //添加聚合属性
        }
        codegenParameter.setInherentFields(inherentFields);
        codegenParameter.setTargetExtends(domainAggregateConfig.getAggregateMasterEntity());
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

    protected ObjectFieldParameter buildDomainAggregateField(DomainAggregateFieldConfig domainAggregateFieldConfig, DomainEntityConfig slaveDomainEntityConfig, CodegenParameter codegenParameter) {
        ObjectFieldParameter field = new ObjectFieldParameter();
        field.setFieldName(domainAggregateFieldConfig.getFieldName());
        field.setFieldType(domainAggregateFieldConfig.getFieldType().getShortName());
        field.setFieldComment(domainAggregateFieldConfig.getFieldComment());
        if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateFieldConfig.getDomainAggregateSlaveConfig().getMasterSlaveMapping().getMasterSlaveRelation())) {
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(List.class.getName()));
        }
        field.setFieldAnnotations(Collections.singletonList(String.format("@Schema(description=\"%s\")", domainAggregateFieldConfig.getFieldComment())));
        field.setFieldGetterName(domainAggregateFieldConfig.getFieldGetterName());
        field.setFieldSetterName(domainAggregateFieldConfig.getFieldSetterName());
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(slaveDomainEntityConfig.getGeneratedTargetName(slaveDomainEntityConfig.getDomainEntityName(), true, false)));
        return field;
    }

    @Override
    protected String getTargetTemplateName() {
        return "ApiModel.ftl";
    }

}
