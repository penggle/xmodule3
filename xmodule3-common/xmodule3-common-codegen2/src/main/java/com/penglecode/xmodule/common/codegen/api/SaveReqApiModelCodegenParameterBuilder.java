package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.*;
import com.penglecode.xmodule.common.model.BaseDTO;
import com.penglecode.xmodule.common.util.TemplateUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * (ApiModelType.SAVE_REQUEST)API接口数据模型代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/26 13:50
 */
public class SaveReqApiModelCodegenParameterBuilder<D extends DomainObjectConfig> extends AbstractApiModelCodegenParameterBuilder<D> {

    public SaveReqApiModelCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, ApiModelConfig, D> codegenContext) {
        super(codegenContext);
    }

    public SaveReqApiModelCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, ApiModelConfig targetConfig, D domainObjectConfig) {
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
        codegenParameter.setTargetImplements(Collections.singletonList(BaseDTO.class.getSimpleName()));
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
        for(Map.Entry<String,DomainEntityFieldConfig> entry : domainEntityConfig.getDomainEntityFields().entrySet()) {
            DomainEntityFieldConfig domainEntityFieldConfig = entry.getValue();
            if(isGenerableField(domainEntityFieldConfig)) { //是否是可生成的字段?
                inherentFields.add(buildDomainEntityField(domainEntityFieldConfig, codegenParameter));
            }
        }
        codegenParameter.setInherentFields(inherentFields);
    }

    protected boolean isGenerableField(DomainEntityFieldConfig domainEntityFieldConfig) {
        //不是辅助字段 && 不是create_time/update_time字段
        return !domainEntityFieldConfig.getFieldClass().isSupportField()
                && !domainEntityFieldConfig.getDomainEntityColumnConfig().getColumnName().equals(getCodegenConfig().getDomain().getDomainCommons().getDefaultCreateTimeColumn())
                && !domainEntityFieldConfig.getDomainEntityColumnConfig().getColumnName().equals(getCodegenConfig().getDomain().getDomainCommons().getDefaultUpdateTimeColumn());
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
        ApiModelConfig dtoApiModelConfig = getTargetConfig().withModelType(ApiModelType.SAVE_DTO);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(dtoApiModelConfig.getGeneratedTargetName(masterDomainEntityConfig.getDomainEntityAlias(), true, false)));
        Map<String,DomainAggregateFieldConfig> domainAggregateFieldConfigs = domainAggregateConfig.getDomainAggregateFields();
        for(Map.Entry<String,DomainAggregateFieldConfig> entry : domainAggregateFieldConfigs.entrySet()) {
            DomainAggregateFieldConfig domainAggregateFieldConfig = entry.getValue();
            DomainEntityConfig slaveDomainEntityConfig = getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateFieldConfig.getDomainAggregateSlaveConfig().getAggregateSlaveEntity());
            inherentFields.add(buildDomainAggregateField(domainAggregateFieldConfig, slaveDomainEntityConfig, dtoApiModelConfig, codegenParameter)); //添加聚合属性
        }
        codegenParameter.setInherentFields(inherentFields);
        codegenParameter.setTargetExtends(dtoApiModelConfig.getGeneratedTargetName(masterDomainEntityConfig.getDomainEntityAlias(), false, false));
    }

}
