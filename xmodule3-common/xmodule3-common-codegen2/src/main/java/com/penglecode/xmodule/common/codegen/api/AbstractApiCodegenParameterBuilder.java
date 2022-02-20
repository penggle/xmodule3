package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.config.ApiCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.DomainObjectConfig;
import com.penglecode.xmodule.common.codegen.config.GenerableTargetConfig;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.CodegenParameterBuilder;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.model.Page;
import com.penglecode.xmodule.common.model.PageResult;

import java.util.List;

/**
 * API接口代码生成参数Builder基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/20 14:10
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
        codegenParameter.setDomainObjectParameter(createDomainObjectParameter(getDomainObjectConfig()));
        return codegenParameter;
    }

    @Override
    protected P setCustomCodegenParameter(P codegenParameter) {
        attachApiCommonImports(codegenParameter);
        codegenParameter.setCreateDomainObject(createDomainObject(codegenParameter));
        codegenParameter.setModifyDomainObjectById(modifyDomainObjectById(codegenParameter));
        codegenParameter.setRemoveDomainObjectById(removeDomainObjectById(codegenParameter));
        codegenParameter.setRemoveDomainObjectsByIds(removeDomainObjectsByIds(codegenParameter));
        codegenParameter.setGetDomainObjectById(getDomainObjectById(codegenParameter));
        codegenParameter.setGetDomainObjectsByIds(getDomainObjectsByIds(codegenParameter));
        codegenParameter.setGetDomainObjectsByPage(getDomainObjectsByPage(codegenParameter));
        return codegenParameter;
    }

    /**
     * 附带上API代码公共import
     * @param codegenParameter
     */
    protected void attachApiCommonImports(P codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Page.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(PageResult.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(List.class.getName()));
    }

    /**
     * 创建领域对象
     * @return
     */
    protected ApiMethodParameter createDomainObject(P codegenParameter) {
        ApiMethodParameter apiMethod = new ApiMethodParameter();
        apiMethod.setActivated(true);
        apiMethod.setMethodReturnType("Result<" + codegenParameter.getDomainObjectParameter().getDomainObjectIdType() + ">");
        apiMethod.setMethodName("create" + codegenParameter.getDomainObjectParameter().getDomainObjectName());
        return apiMethod;
    }

    /**
     * 根据领域对象ID修改领域对象
     * @return
     */
    protected ApiMethodParameter modifyDomainObjectById(P codegenParameter) {
        ApiMethodParameter apiMethod = new ApiMethodParameter();
        apiMethod.setActivated(true);
        apiMethod.setMethodReturnType("Result<Void>");
        apiMethod.setMethodName("modify" + codegenParameter.getDomainObjectParameter().getDomainObjectAlias() + "ById");
        return apiMethod;
    }

    /**
     * 根据领域对象ID删除领域对象
     * @return
     */
    protected ApiMethodParameter removeDomainObjectById(P codegenParameter) {
        ApiMethodParameter apiMethod = new ApiMethodParameter();
        apiMethod.setActivated(true);
        apiMethod.setMethodReturnType("Result<Void>");
        apiMethod.setMethodName("remove" + codegenParameter.getDomainObjectParameter().getDomainObjectAlias() + "ById");
        return apiMethod;
    }

    /**
     * 根据多个领域对象ID删除领域对象
     * @return
     */
    protected ApiMethodParameter removeDomainObjectsByIds(P codegenParameter) {
        ApiMethodParameter apiMethod = new ApiMethodParameter();
        apiMethod.setActivated(true);
        apiMethod.setMethodReturnType("Result<Void>");
        apiMethod.setMethodName("remove" + codegenParameter.getDomainObjectParameter().getDomainObjectsAlias() + "ByIds");
        return apiMethod;
    }


    /**
     * 根据领域对象ID获取领域对象
     * @return
     */
    protected ApiMethodParameter getDomainObjectById(P codegenParameter) {
        ApiMethodParameter apiMethod = new ApiMethodParameter();
        apiMethod.setActivated(true);
        apiMethod.setMethodReturnType(codegenParameter.getDomainObjectParameter().getDomainObjectName());
        apiMethod.setMethodName("get" + codegenParameter.getDomainObjectParameter().getDomainObjectAlias() + "ById");
        return apiMethod;
    }

    /**
     * 根据多个领域对象ID获取领域对象
     * @return
     */
    protected ApiMethodParameter getDomainObjectsByIds(P codegenParameter) {
        ApiMethodParameter apiMethod = new ApiMethodParameter();
        apiMethod.setActivated(true);
        apiMethod.setMethodReturnType("List<" + codegenParameter.getDomainObjectParameter().getDomainObjectName() + ">"); //TODO
        apiMethod.setMethodName("get" + codegenParameter.getDomainObjectParameter().getDomainObjectsAlias() + "ByIds");
        return apiMethod;
    }

    /**
     * 根据条件查询领域对象(分页、排序)
     * @return
     */
    protected ApiMethodParameter getDomainObjectsByPage(P codegenParameter) {
        ApiMethodParameter apiMethod = new ApiMethodParameter();
        apiMethod.setActivated(true);
        apiMethod.setMethodReturnType("List<" + codegenParameter.getDomainObjectParameter().getDomainObjectName() + ">"); //TODO
        apiMethod.setMethodName("get" + codegenParameter.getDomainObjectParameter().getDomainObjectsAlias() + "ByPage");
        return apiMethod;
    }

}
