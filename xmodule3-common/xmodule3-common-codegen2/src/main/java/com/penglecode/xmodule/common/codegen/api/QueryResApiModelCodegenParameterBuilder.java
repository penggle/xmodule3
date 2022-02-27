package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.config.ApiCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.ApiModelConfig;
import com.penglecode.xmodule.common.codegen.config.DomainEntityFieldConfig;
import com.penglecode.xmodule.common.codegen.config.DomainObjectConfig;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.DomainObjectFieldClass;

/**
 * (ApiModelType.QUERY_RESPONSE)API接口数据模型代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/26 13:50
 */
public class QueryResApiModelCodegenParameterBuilder<D extends DomainObjectConfig> extends SaveReqApiModelCodegenParameterBuilder<D> {

    public QueryResApiModelCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, ApiModelConfig, D> codegenContext) {
        super(codegenContext);
    }

    public QueryResApiModelCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, ApiModelConfig targetConfig, D domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    protected boolean isGenerableField(DomainEntityFieldConfig domainEntityFieldConfig) {
        //不是查询辅助字段
        return !DomainObjectFieldClass.DOMAIN_ENTITY_SUPPORTS_QUERY_INBOUND_FIELD.equals(domainEntityFieldConfig.getFieldClass());
    }

}
