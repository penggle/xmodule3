package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.config.ApiClientConfig;
import com.penglecode.xmodule.common.codegen.config.ApiCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.ApiRuntimeConfig;
import com.penglecode.xmodule.common.codegen.config.DomainObjectConfig;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * API接口的Client实现代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/26 22:55
 */
public class ApiClientRuntimeCodegenParameterBuilder<D extends DomainObjectConfig> extends ApiRuntimeCodegenParameterBuilder<D> {

    public ApiClientRuntimeCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, ApiRuntimeConfig, D> codegenContext) {
        super(codegenContext);
    }

    public ApiClientRuntimeCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, ApiRuntimeConfig targetConfig, D domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected ApiRuntimeCodegenParameter setCommonCodegenParameter(ApiRuntimeCodegenParameter codegenParameter) {
        codegenParameter = super.setCommonCodegenParameter(codegenParameter);
        codegenParameter.setTargetAnnotations(Collections.singletonList("@RestController"));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RestController.class.getName()));
        if(getTargetConfig().getApiExtendsClass() != null) {
            codegenParameter.setTargetExtends(getTargetConfig().getApiExtendsClass().getSimpleName());
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(getTargetConfig().getApiExtendsClass().getName()));
        }
        ApiClientConfig apiClientConfig = getCodegenConfig().getApi().getClientConfig();
        DomainObjectConfig domainObjectConfig = getDomainObjectConfig();
        codegenParameter.setTargetImplements(Collections.singletonList(apiClientConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectAlias(), false, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(apiClientConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectAlias(), true, false)));
        return codegenParameter;
    }

    @Override
    protected String getTargetTemplateName() {
        return "ApiClientRuntime.ftl";
    }

}
