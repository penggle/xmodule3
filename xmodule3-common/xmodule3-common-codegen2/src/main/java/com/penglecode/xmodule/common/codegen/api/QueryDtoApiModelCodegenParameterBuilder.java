package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.config.ApiCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.ApiModelConfig;
import com.penglecode.xmodule.common.codegen.config.DomainEntityConfig;
import com.penglecode.xmodule.common.codegen.config.DomainEntityFieldConfig;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.DomainObjectFieldClass;

/**
 * (ApiModelType.QUERY_DTO)API接口数据模型代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/20 21:25
 */
public class QueryDtoApiModelCodegenParameterBuilder extends DtoApiModelCodegenParameterBuilder {

    public QueryDtoApiModelCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, ApiModelConfig, DomainEntityConfig> codegenContext) {
        super(codegenContext);
    }

    public QueryDtoApiModelCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, ApiModelConfig targetConfig, DomainEntityConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected boolean isGenerableField(DomainEntityFieldConfig domainEntityFieldConfig) {
        //不是查询场景入站辅助字段
        return !DomainObjectFieldClass.DOMAIN_ENTITY_SUPPORTS_QUERY_INBOUND_FIELD.equals(domainEntityFieldConfig.getFieldClass());
    }

}
