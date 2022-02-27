package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.ApiModelType;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.CodegenParameterBuilder;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.model.PageResult;
import com.penglecode.xmodule.common.model.Result;

import java.util.List;

/**
 * API接口代码生成参数Builder基类
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/20 14:10
 */
public abstract class AbstractApiCodegenParameterBuilder<T extends GenerableTargetConfig, D extends DomainObjectConfig, P extends AbstractApiCodegenParameter> extends CodegenParameterBuilder<ApiCodegenConfigProperties, T, D, P> {

    protected AbstractApiCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, T, D> codegenContext) {
        super(codegenContext);
    }

    protected AbstractApiCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, T targetConfig, D domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected P setCommonCodegenParameter(P codegenParameter) {
        codegenParameter = super.setCommonCodegenParameter(codegenParameter);
        D domainObjectConfig = getDomainObjectConfig();
        String domainObjectName = domainObjectConfig.getDomainObjectAlias(); //使用别名来构造API接口的名字
        codegenParameter.setTargetFileName(getTargetConfig().getGeneratedTargetName(domainObjectName, false, true));
        codegenParameter.setTargetClass(getTargetConfig().getGeneratedTargetName(domainObjectName, false, false));
        codegenParameter.setDomainObjectParameter(createDomainObjectParameter(domainObjectConfig));
        codegenParameter.setAggregateRoot(domainObjectConfig instanceof DomainAggregateConfig);
        return codegenParameter;
    }

    @Override
    protected P setCustomCodegenParameter(P codegenParameter) {
        codegenParameter.setCreateDomainObject(createDomainObject(codegenParameter));
        codegenParameter.setModifyDomainObjectById(modifyDomainObjectById(codegenParameter));
        codegenParameter.setRemoveDomainObjectsByIds(removeDomainObjectsByIds(codegenParameter));
        codegenParameter.setRemoveDomainObjectById(removeDomainObjectById(codegenParameter));
        codegenParameter.setGetDomainObjectById(getDomainObjectById(codegenParameter));
        codegenParameter.setGetDomainObjectsByIds(getDomainObjectsByIds(codegenParameter));
        codegenParameter.setGetDomainObjectsByPage(getDomainObjectsByPage(codegenParameter));
        return codegenParameter;
    }

    /**
     * 创建领域对象
     * @return
     */
    protected ApiMethodParameter createDomainObject(P codegenParameter) {
        String methodName = "create" + codegenParameter.getDomainObjectParameter().getDomainObjectAlias();
        String methodReturnType = String.format("%s<%s>", Result.class.getSimpleName(), codegenParameter.getDomainObjectParameter().getDomainObjectIdType());
        return saveDomainObject(codegenParameter, methodName, methodReturnType);
    }

    /**
     * 根据领域对象ID修改领域对象
     * @return
     */
    protected ApiMethodParameter modifyDomainObjectById(P codegenParameter) {
        String methodName = "modify" + codegenParameter.getDomainObjectParameter().getDomainObjectAlias() + "ById";
        String methodReturnType = String.format("%s<%s>", Result.class.getSimpleName(), "Void");
        return saveDomainObject(codegenParameter, methodName, methodReturnType);
    }

    protected ApiMethodParameter saveDomainObject(P codegenParameter, String methodName, String methodReturnType) {
        DomainObjectConfig domainObjectConfig = getDomainObjectConfig();
        ApiModelConfig apiModelConfig = getCodegenConfig().getApi().getModelConfig().withModelType(ApiModelType.SAVE_REQUEST);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(apiModelConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectAlias(), true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Result.class.getName()));
        ApiMethodParameter apiMethod = new ApiMethodParameter();
        apiMethod.setActivated(true);
        apiMethod.setMethodReturnType(methodReturnType);
        apiMethod.setMethodName(methodName);
        apiMethod.setInputApiModelName(apiModelConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectAlias(), false, false));
        return apiMethod;
    }

    /**
     * 根据领域对象ID删除领域对象
     * @return
     */
    protected ApiMethodParameter removeDomainObjectById(P codegenParameter) {
        ApiMethodParameter apiMethod = new ApiMethodParameter();
        apiMethod.setActivated(true);
        apiMethod.setMethodReturnType(String.format("%s<%s>", Result.class.getSimpleName(), "Void"));
        apiMethod.setMethodName("remove" + codegenParameter.getDomainObjectParameter().getDomainObjectAlias() + "ById");
        return apiMethod;
    }

    /**
     * 根据多个领域对象ID删除领域对象
     * @return
     */
    protected ApiMethodParameter removeDomainObjectsByIds(P codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(List.class.getName()));
        ApiMethodParameter apiMethod = new ApiMethodParameter();
        apiMethod.setActivated(true);
        apiMethod.setMethodReturnType(String.format("%s<%s>", Result.class.getSimpleName(), "Void"));
        apiMethod.setMethodName("remove" + codegenParameter.getDomainObjectParameter().getDomainObjectAlias() + "ByIds");
        return apiMethod;
    }


    /**
     * 根据领域对象ID获取领域对象
     * @return
     */
    protected ApiMethodParameter getDomainObjectById(P codegenParameter) {
        DomainObjectConfig domainObjectConfig = getDomainObjectConfig();
        ApiModelConfig apiModelConfig = getCodegenConfig().getApi().getModelConfig().withModelType(ApiModelType.QUERY_RESPONSE);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(apiModelConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectAlias(), true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Result.class.getName()));
        ApiMethodParameter apiMethod = new ApiMethodParameter();
        apiMethod.setOutputApiModelName(apiModelConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectAlias(), false, false));
        apiMethod.setActivated(true);
        apiMethod.setMethodReturnType(String.format("%s<%s>", Result.class.getSimpleName(), apiMethod.getOutputApiModelName()));
        apiMethod.setMethodName("get" + codegenParameter.getDomainObjectParameter().getDomainObjectAlias() + "ById");
        return apiMethod;
    }

    /**
     * 根据多个领域对象ID获取领域对象
     * @return
     */
    protected ApiMethodParameter getDomainObjectsByIds(P codegenParameter) {
        DomainObjectConfig domainObjectConfig = getDomainObjectConfig();
        ApiModelConfig apiModelConfig = getCodegenConfig().getApi().getModelConfig().withModelType(ApiModelType.QUERY_RESPONSE);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(apiModelConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectAlias(), true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Result.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(List.class.getName()));
        ApiMethodParameter apiMethod = new ApiMethodParameter();
        apiMethod.setOutputApiModelName(apiModelConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectAlias(), false, false));
        apiMethod.setActivated(true);
        apiMethod.setMethodReturnType(String.format("%s<List<%S>>", Result.class.getSimpleName(), apiMethod.getOutputApiModelName()));
        apiMethod.setMethodName("get" + codegenParameter.getDomainObjectParameter().getDomainObjectsAlias() + "ByIds");
        return apiMethod;
    }

    /**
     * 根据条件查询领域对象(分页、排序)
     * @return
     */
    protected ApiMethodParameter getDomainObjectsByPage(P codegenParameter) {
        DomainObjectConfig domainObjectConfig = getDomainObjectConfig();
        ApiModelConfig reqApiModelConfig = getCodegenConfig().getApi().getModelConfig().withModelType(ApiModelType.QUERY_REQUEST);
        ApiModelConfig resApiModelConfig = getCodegenConfig().getApi().getModelConfig().withModelType(ApiModelType.QUERY_RESPONSE);
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(reqApiModelConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectAlias(), true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(resApiModelConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectAlias(), true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PageResult.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(List.class.getName()));
        ApiMethodParameter apiMethod = new ApiMethodParameter();
        apiMethod.setInputApiModelName(reqApiModelConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectAlias(), false, false));
        apiMethod.setOutputApiModelName(resApiModelConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectAlias(), false, false));
        apiMethod.setActivated(true);
        apiMethod.setMethodReturnType(String.format("%s<List<%s>>", PageResult.class.getSimpleName(), apiMethod.getOutputApiModelName()));
        apiMethod.setMethodName("get" + codegenParameter.getDomainObjectParameter().getDomainObjectsAlias() + "ByPage");
        return apiMethod;
    }

}
