package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.config.ApiCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.ApiRuntimeConfig;
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
 * API接口的Controller实现代码生成参数Builder
 * 注意此时生成的ApiController实现不会implements某个ApiClient接口，仅仅是一个SpringMVC的Controller提供HTTP接口服务
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/26 22:55
 */
public class ApiControllerRuntimeCodegenParameterBuilder<D extends DomainObjectConfig> extends ApiRuntimeCodegenParameterBuilder<D> {

    public ApiControllerRuntimeCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, ApiRuntimeConfig, D> codegenContext) {
        super(codegenContext);
    }

    public ApiControllerRuntimeCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, ApiRuntimeConfig targetConfig, D domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected ApiRuntimeCodegenParameter setCommonCodegenParameter(ApiRuntimeCodegenParameter codegenParameter) {
        codegenParameter = super.setCommonCodegenParameter(codegenParameter);
        List<String> targetAnnotations = new ArrayList<>();
        targetAnnotations.add("@RestController");
        targetAnnotations.add(String.format("@RequestMapping(\"/api/%s\")", codegenParameter.getDomainObjectParameter().getDomainObjectAlias().toLowerCase()));
        targetAnnotations.add(String.format("@Tag(name=\"%s\", description=\"%sAPI接口\")", codegenParameter.getTargetClass(), codegenParameter.getDomainObjectParameter().getDomainObjectTitle()));
        codegenParameter.setTargetAnnotations(targetAnnotations);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RestController.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Tag.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Operation.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(MediaType.class.getName()));
        if(getTargetConfig().getApiExtendsClass() != null) {
            codegenParameter.setTargetExtends(getTargetConfig().getApiExtendsClass().getSimpleName());
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(getTargetConfig().getApiExtendsClass().getName()));
        }
        return codegenParameter;
    }

    @Override
    protected ApiMethodParameter createDomainObject(ApiRuntimeCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PostMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestBody.class.getName()));
        return super.createDomainObject(codegenParameter);
    }

    @Override
    protected ApiMethodParameter modifyDomainObjectById(ApiRuntimeCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PostMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestBody.class.getName()));
        return super.modifyDomainObjectById(codegenParameter);
    }

    @Override
    protected ApiMethodParameter removeDomainObjectById(ApiRuntimeCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PostMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PathVariable.class.getName()));
        return super.removeDomainObjectById(codegenParameter);
    }

    @Override
    protected ApiMethodParameter removeDomainObjectsByIds(ApiRuntimeCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PostMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestBody.class.getName()));
        return super.removeDomainObjectsByIds(codegenParameter);
    }

    @Override
    protected ApiMethodParameter getDomainObjectById(ApiRuntimeCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(GetMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PathVariable.class.getName()));
        if(codegenParameter.getAggregateRoot()) {
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestParam.class.getName()));
        }
        return super.getDomainObjectById(codegenParameter);
    }

    @Override
    protected ApiMethodParameter getDomainObjectsByIds(ApiRuntimeCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PostMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestBody.class.getName()));
        if(codegenParameter.getAggregateRoot()) {
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestParam.class.getName()));
        }
        return super.getDomainObjectsByIds(codegenParameter);
    }

    @Override
    protected ApiMethodParameter getDomainObjectsByPage(ApiRuntimeCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PostMapping.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestBody.class.getName()));
        if(codegenParameter.getAggregateRoot()) {
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RequestParam.class.getName()));
        }
        return super.getDomainObjectsByPage(codegenParameter);
    }

    @Override
    protected String getTargetTemplateName() {
        return "ApiControllerRuntime.ftl";
    }

}
