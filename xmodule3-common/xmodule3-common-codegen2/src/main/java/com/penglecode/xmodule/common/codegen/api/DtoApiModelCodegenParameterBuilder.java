package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.config.ApiCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.ApiModelConfig;
import com.penglecode.xmodule.common.codegen.config.DomainEntityConfig;
import com.penglecode.xmodule.common.codegen.config.DomainEntityFieldConfig;
import com.penglecode.xmodule.common.codegen.support.ApiModelType;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.codegen.support.ObjectFieldParameter;
import com.penglecode.xmodule.common.model.BaseDTO;
import com.penglecode.xmodule.common.util.TemplateUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * (ApiModelType.SAVE_DTO|QUERY_DTO)API接口数据模型代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/20 21:25
 */
public abstract class DtoApiModelCodegenParameterBuilder extends AbstractApiModelCodegenParameterBuilder<DomainEntityConfig> {

    public DtoApiModelCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, ApiModelConfig, DomainEntityConfig> codegenContext) {
        super(codegenContext);
    }

    public DtoApiModelCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, ApiModelConfig targetConfig, DomainEntityConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected ApiModelCodegenParameter setCustomCodegenParameter(ApiModelCodegenParameter codegenParameter) {
        DomainEntityConfig domainObjectConfig = getDomainObjectConfig();
        ApiModelType apiModelType = getTargetConfig().getModelType();
        String targetComment = TemplateUtils.parseTemplate(apiModelType.getModelDescTemplate(), Collections.singletonMap("domainObjectTitle", domainObjectConfig.getDomainObjectTitle()));
        codegenParameter.setTargetComment(targetComment);
        codegenParameter.setTargetAnnotations(Collections.singletonList(String.format("@Schema(description=\"%s\")", targetComment)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Schema.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(BaseDTO.class.getName()));
        codegenParameter.setTargetImplements(Collections.singletonList(BaseDTO.class.getSimpleName()));

        List<ObjectFieldParameter> inherentFields = new ArrayList<>();
        for(Map.Entry<String,DomainEntityFieldConfig> entry : domainObjectConfig.getDomainEntityFields().entrySet()) {
            DomainEntityFieldConfig domainEntityFieldConfig = entry.getValue();
            if(isGenerableField(domainEntityFieldConfig)) { //是否是可生成的字段?
                inherentFields.add(buildDomainEntityField(domainEntityFieldConfig, codegenParameter));
            }
        }
        codegenParameter.setInherentFields(inherentFields);
        return codegenParameter;
    }

    /**
     * 是否是可生成的字段
     * @param domainEntityFieldConfig
     * @return
     */
    protected abstract boolean isGenerableField(DomainEntityFieldConfig domainEntityFieldConfig);

}
