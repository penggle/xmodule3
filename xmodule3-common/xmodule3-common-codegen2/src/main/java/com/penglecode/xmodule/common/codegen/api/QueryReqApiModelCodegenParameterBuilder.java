package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.*;
import com.penglecode.xmodule.common.model.Page;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.util.TemplateUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * (ApiModelType.QUERY_REQUEST)API接口数据模型代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/26 13:50
 */
public class QueryReqApiModelCodegenParameterBuilder<D extends DomainObjectConfig> extends AbstractApiModelCodegenParameterBuilder<D> {

    public QueryReqApiModelCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, ApiModelConfig, D> codegenContext) {
        super(codegenContext);
    }

    public QueryReqApiModelCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, ApiModelConfig targetConfig, D domainObjectConfig) {
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
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Page.class.getName()));
        codegenParameter.setTargetExtends(Page.class.getSimpleName());
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
            if(isGenerableField4Query(domainEntityFieldConfig)) { //是否是可生成的字段?
                inherentFields.add(buildDomainEntityField(domainEntityFieldConfig, codegenParameter));
            }
        }
        codegenParameter.setInherentFields(inherentFields);
    }

    protected boolean isGenerableField4Query(DomainEntityFieldConfig domainEntityFieldConfig) {
        //查询辅助字段?
        if(DomainObjectFieldClass.DOMAIN_ENTITY_SUPPORTS_QUERY_INBOUND_FIELD.equals(domainEntityFieldConfig.getFieldClass())) {
            return true;
        }
        //领域实体字段且是查询条件列
        return !domainEntityFieldConfig.getFieldClass().isSupportField() && StringUtils.isNotBlank(domainEntityFieldConfig.getDomainEntityColumnConfig().getOperatorOnQuery());
    }

    /**
     * 处理DomainAggregateConfig的情况
     * @param codegenParameter
     * @param domainAggregateConfig
     */
    protected void setDomainAggregateCodegenParameter(ApiModelCodegenParameter codegenParameter, DomainAggregateConfig domainAggregateConfig) {
        DomainEntityConfig masterDomainEntityConfig = getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateConfig.getAggregateMasterEntity());
        //聚合对象的查询条件来自于Master
        setDomainEntityCodegenParameter(codegenParameter, masterDomainEntityConfig);
    }

}
