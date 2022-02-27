package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.config.ApiClientConfig;
import com.penglecode.xmodule.common.codegen.config.ApiCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.DomainObjectConfig;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * API接口Client代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/26 21:37
 */
public class ApiClientCodegenParameterBuilder<D extends DomainObjectConfig> extends AbstractApiCodegenParameterBuilder<ApiClientConfig, D, ApiClientCodegenParameter> {

    public ApiClientCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, ApiClientConfig, D> codegenContext) {
        super(codegenContext);
    }

    public ApiClientCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, ApiClientConfig targetConfig, D domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected ApiClientCodegenParameter setCommonCodegenParameter(ApiClientCodegenParameter codegenParameter) {
        codegenParameter = super.setCommonCodegenParameter(codegenParameter);
        List<String> targetAnnotations = new ArrayList<>();
        targetAnnotations.add(String.format("@RequestMapping(\"/api/%s\")", codegenParameter.getDomainObjectParameter().getDomainObjectAlias().toLowerCase()));
        targetAnnotations.add(String.format("@Tag(name=\"%s\", description=\"%sAPI接口\")", codegenParameter.getTargetClass(), codegenParameter.getDomainObjectParameter().getDomainObjectTitle()));
        codegenParameter.setTargetAnnotations(targetAnnotations);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Tag.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Operation.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(MediaType.class.getName()));
        return codegenParameter;
    }

    @Override
    protected ApiMethodParameter createDomainObject(ApiClientCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PostMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestBody.class.getName()));
        return super.createDomainObject(codegenParameter);
    }

    @Override
    protected ApiMethodParameter modifyDomainObjectById(ApiClientCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PostMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestBody.class.getName()));
        return super.modifyDomainObjectById(codegenParameter);
    }

    @Override
    protected ApiMethodParameter removeDomainObjectById(ApiClientCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PostMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PathVariable.class.getName()));
        return super.removeDomainObjectById(codegenParameter);
    }

    @Override
    protected ApiMethodParameter removeDomainObjectsByIds(ApiClientCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PostMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestBody.class.getName()));
        return super.removeDomainObjectsByIds(codegenParameter);
    }

    @Override
    protected ApiMethodParameter getDomainObjectById(ApiClientCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(GetMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PathVariable.class.getName()));
        if(codegenParameter.getAggregateRoot()) {
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestParam.class.getName()));
        }
        return super.getDomainObjectById(codegenParameter);
    }

    @Override
    protected ApiMethodParameter getDomainObjectsByIds(ApiClientCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PostMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestBody.class.getName()));
        if(codegenParameter.getAggregateRoot()) {
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestParam.class.getName()));
        }
        return super.getDomainObjectsByIds(codegenParameter);
    }

    @Override
    protected ApiMethodParameter getDomainObjectsByPage(ApiClientCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PostMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestBody.class.getName()));
        if(codegenParameter.getAggregateRoot()) {
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestParam.class.getName()));
        }
        return super.getDomainObjectsByPage(codegenParameter);
    }

    @Override
    protected String getTargetTemplateName() {
        return "ApiClient.ftl";
    }
}
