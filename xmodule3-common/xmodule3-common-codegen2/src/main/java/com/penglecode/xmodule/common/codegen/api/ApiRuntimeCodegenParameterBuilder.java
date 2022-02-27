package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.model.Page;
import com.penglecode.xmodule.common.model.PageResult;
import com.penglecode.xmodule.common.model.Result;
import com.penglecode.xmodule.common.support.BeanCopier;
import com.penglecode.xmodule.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ApiRuntime代码生成参数Builder基类
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/26 21:37
 */
public abstract class ApiRuntimeCodegenParameterBuilder<D extends DomainObjectConfig> extends AbstractApiCodegenParameterBuilder<ApiRuntimeConfig, D, ApiRuntimeCodegenParameter> {

    public ApiRuntimeCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, ApiRuntimeConfig, D> codegenContext) {
        super(codegenContext);
    }

    public ApiRuntimeCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, ApiRuntimeConfig targetConfig, D domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected ApiRuntimeCodegenParameter setCommonCodegenParameter(ApiRuntimeCodegenParameter codegenParameter) {
        codegenParameter = super.setCommonCodegenParameter(codegenParameter);
        DomainObjectConfig domainObjectConfig = getDomainObjectConfig();
        ServiceInterfaceConfig serviceInterfaceConfig;
        if(domainObjectConfig instanceof DomainEntityConfig){
            serviceInterfaceConfig = getCodegenConfig().getService().getDomain().getInterfaceConfig();
        } else {
            serviceInterfaceConfig = getCodegenConfig().getService().getApplication().getInterfaceConfig();
        }
        String domainServiceName = serviceInterfaceConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectName(), false, false);
        codegenParameter.setDomainServiceName(domainServiceName);
        codegenParameter.setDomainServiceBeanName(StringUtils.lowerCaseFirstChar(domainServiceName));
        codegenParameter.setDomainServiceInstanceName(StringUtils.lowerCaseFirstChar(domainServiceName));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(serviceInterfaceConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectName(), true, false)));
        return codegenParameter;
    }

    @Override
    protected ApiMethodParameter createDomainObject(ApiRuntimeCodegenParameter codegenParameter) {
        ApiMethodParameter apiMethodParameter = super.createDomainObject(codegenParameter);
        String domainObjectName = codegenParameter.getDomainObjectParameter().getDomainObjectName();
        String domainObjectVariableName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectsAlias();
        List<String> methodBodyLines = new ArrayList<>();
        methodBodyLines.add(String.format("%s %s = %s.copy(createRequest, %s::new);", domainObjectName, domainObjectVariableName, BeanCopier.class.getSimpleName(), domainObjectName));
        methodBodyLines.add(String.format("%s.create%s(%s);", codegenParameter.getDomainServiceInstanceName(), codegenParameter.getDomainObjectParameter().getDomainObjectAlias(), domainObjectVariableName));
        methodBodyLines.add(String.format("return %s.success().data(%s.%s()).build();", Result.class.getSimpleName(), domainObjectVariableName, codegenParameter.getDomainObjectParameter().getDomainObjectIdGetterName()));
        apiMethodParameter.setMethodBodyLines(methodBodyLines);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(getDomainObjectConfig().getGeneratedTargetName(domainObjectName, true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(BeanCopier.class.getName()));
        return apiMethodParameter;
    }

    @Override
    protected ApiMethodParameter modifyDomainObjectById(ApiRuntimeCodegenParameter codegenParameter) {
        ApiMethodParameter apiMethodParameter = super.modifyDomainObjectById(codegenParameter);
        String domainObjectName = codegenParameter.getDomainObjectParameter().getDomainObjectName();
        String domainObjectVariableName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectsAlias();
        List<String> methodBodyLines = new ArrayList<>();
        methodBodyLines.add(String.format("%s %s = %s.copy(modifyRequest, %s::new);", domainObjectName, domainObjectVariableName, BeanCopier.class.getSimpleName(), domainObjectName));
        methodBodyLines.add(String.format("%s.modify%sById(%s);", codegenParameter.getDomainServiceInstanceName(), codegenParameter.getDomainObjectParameter().getDomainObjectAlias(), domainObjectVariableName));
        methodBodyLines.add(String.format("return %s.success().build();", Result.class.getSimpleName()));
        apiMethodParameter.setMethodBodyLines(methodBodyLines);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(getDomainObjectConfig().getGeneratedTargetName(domainObjectName, true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(BeanCopier.class.getName()));
        return apiMethodParameter;
    }

    @Override
    protected ApiMethodParameter removeDomainObjectById(ApiRuntimeCodegenParameter codegenParameter) {
        ApiMethodParameter apiMethodParameter = super.removeDomainObjectById(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        methodBodyLines.add(String.format("%s.remove%sById(id);", codegenParameter.getDomainServiceInstanceName(), codegenParameter.getDomainObjectParameter().getDomainObjectAlias()));
        methodBodyLines.add(String.format("return %s.success().build();", Result.class.getSimpleName()));
        apiMethodParameter.setMethodBodyLines(methodBodyLines);
        return apiMethodParameter;
    }

    @Override
    protected ApiMethodParameter removeDomainObjectsByIds(ApiRuntimeCodegenParameter codegenParameter) {
        ApiMethodParameter apiMethodParameter = super.removeDomainObjectsByIds(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        methodBodyLines.add(String.format("%s.remove%sByIds(id);", codegenParameter.getDomainServiceInstanceName(), codegenParameter.getDomainObjectParameter().getDomainObjectAlias()));
        methodBodyLines.add(String.format("return %s.success().build();", Result.class.getSimpleName()));
        apiMethodParameter.setMethodBodyLines(methodBodyLines);
        return apiMethodParameter;
    }

    @Override
    protected ApiMethodParameter getDomainObjectById(ApiRuntimeCodegenParameter codegenParameter) {
        ApiMethodParameter apiMethodParameter = super.getDomainObjectById(codegenParameter);
        String domainObjectName = codegenParameter.getDomainObjectParameter().getDomainObjectName();
        String domainObjectVariableName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectsAlias();
        List<String> methodBodyLines = new ArrayList<>();
        methodBodyLines.add(String.format("%s %s = %s.get%sById(id%s);", domainObjectName, domainObjectVariableName, codegenParameter.getDomainServiceInstanceName(), codegenParameter.getDomainObjectParameter().getDomainObjectAlias(), codegenParameter.getAggregateRoot() ? ", cascade" : ""));
        methodBodyLines.add(String.format("%s queryResponse = %s.copy(%s, %s::new);", apiMethodParameter.getOutputApiModelName(), BeanCopier.class.getSimpleName(), domainObjectVariableName, apiMethodParameter.getOutputApiModelName()));
        methodBodyLines.add(String.format("return %s.success().data(queryResponse).build();", Result.class.getSimpleName()));
        apiMethodParameter.setMethodBodyLines(methodBodyLines);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(getDomainObjectConfig().getGeneratedTargetName(domainObjectName, true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(BeanCopier.class.getName()));
        return apiMethodParameter;
    }

    @Override
    protected ApiMethodParameter getDomainObjectsByIds(ApiRuntimeCodegenParameter codegenParameter) {
        ApiMethodParameter apiMethodParameter = super.getDomainObjectsByIds(codegenParameter);
        String domainObjectName = codegenParameter.getDomainObjectParameter().getDomainObjectName();
        String domainObjectVariablesName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectsAlias();
        List<String> methodBodyLines = new ArrayList<>();
        methodBodyLines.add(String.format("List<%s> %s = %s.get%sByIds(ids%s);", domainObjectName, domainObjectVariablesName, codegenParameter.getDomainServiceInstanceName(), codegenParameter.getDomainObjectParameter().getDomainObjectsAlias(), codegenParameter.getAggregateRoot() ? ", cascade" : ""));
        methodBodyLines.add(String.format("List<%s> queryResponses = %s.copy(%s, %s::new);", apiMethodParameter.getOutputApiModelName(), BeanCopier.class.getSimpleName(), domainObjectVariablesName, apiMethodParameter.getOutputApiModelName()));
        methodBodyLines.add(String.format("return %s.success().data(queryResponses).build();", Result.class.getSimpleName()));
        apiMethodParameter.setMethodBodyLines(methodBodyLines);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(getDomainObjectConfig().getGeneratedTargetName(domainObjectName, true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(BeanCopier.class.getName()));
        return apiMethodParameter;
    }

    @Override
    protected ApiMethodParameter getDomainObjectsByPage(ApiRuntimeCodegenParameter codegenParameter) {
        ApiMethodParameter apiMethodParameter = super.getDomainObjectsByPage(codegenParameter);
        String domainObjectName = codegenParameter.getDomainObjectParameter().getDomainObjectName();
        String domainObjectVariablesName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectsAlias();
        List<String> methodBodyLines = new ArrayList<>();
        methodBodyLines.add(String.format("%s condition = %s.copy(queryRequest, %s::new);", domainObjectName, BeanCopier.class.getSimpleName(), domainObjectName));
        methodBodyLines.add(String.format("%s page = %s.copyOf(queryRequest);", Page.class.getSimpleName(), Page.class.getSimpleName()));
        methodBodyLines.add(String.format("List<%s> %s = %s.get%sByPage(condition, page%s);", domainObjectName, domainObjectVariablesName, codegenParameter.getDomainServiceInstanceName(), codegenParameter.getDomainObjectParameter().getDomainObjectsAlias(), codegenParameter.getAggregateRoot() ? ", cascade" : ""));
        methodBodyLines.add(String.format("List<%s> queryResponses = %s.copy(%s, %s::new);", apiMethodParameter.getOutputApiModelName(), BeanCopier.class.getSimpleName(), domainObjectVariablesName, apiMethodParameter.getOutputApiModelName()));
        methodBodyLines.add(String.format("return %s.success().data(queryResponses).totalRowCount(page.getTotalRowCount()).build();", PageResult.class.getSimpleName()));
        apiMethodParameter.setMethodBodyLines(methodBodyLines);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(getDomainObjectConfig().getGeneratedTargetName(domainObjectName, true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(BeanCopier.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Page.class.getName()));
        return apiMethodParameter;
    }

}
