package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.config.DomainAggregateConfig;
import com.penglecode.xmodule.common.codegen.config.DomainEntityConfig;
import com.penglecode.xmodule.common.codegen.config.GenerableTargetConfig;
import com.penglecode.xmodule.common.codegen.config.ServiceCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.CodegenParameterBuilder;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.model.Page;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

/**
 * 聚合根的应用服务代码生成参数Builder基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 22:03
 */
public abstract class AbstractApplicationServiceCodegenParameterBuilder<T extends GenerableTargetConfig, P extends AbstractApplicationServiceCodegenParameter> extends CodegenParameterBuilder<ServiceCodegenConfigProperties, T, DomainAggregateConfig, P> {

    protected AbstractApplicationServiceCodegenParameterBuilder(CodegenContext<ServiceCodegenConfigProperties, T, DomainAggregateConfig> codegenContext) {
        super(codegenContext);
    }

    protected AbstractApplicationServiceCodegenParameterBuilder(ServiceCodegenConfigProperties codegenConfig, T targetConfig, DomainAggregateConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected P setCustomCodegenParameter(P codegenParameter) {
        attachServiceCommonImports(codegenParameter);
        codegenParameter.setCreateDomainObject(createDomainObject(codegenParameter));
        codegenParameter.setModifyDomainObjectById(modifyDomainObjectById(codegenParameter));
        codegenParameter.setRemoveDomainObjectById(removeDomainObjectById(codegenParameter));
        codegenParameter.setRemoveDomainObjectsByIds(removeDomainObjectsByIds(codegenParameter));
        codegenParameter.setGetDomainObjectById(getDomainObjectById(codegenParameter));
        codegenParameter.setGetDomainObjectsByIds(getDomainObjectsByIds(codegenParameter));
        codegenParameter.setGetDomainObjectsByPage(getDomainObjectsByPage(codegenParameter));
        codegenParameter.setGetDomainObjectTotalCount(getDomainObjectTotalCount(codegenParameter));
        codegenParameter.setForEachDomainObject1(forEachDomainObject1(codegenParameter));
        codegenParameter.setForEachDomainObject2(forEachDomainObject2(codegenParameter));
        return codegenParameter;
    }

    /**
     * 附带上领域/应用服务代码公共import
     * @param codegenParameter
     */
    protected void attachServiceCommonImports(P codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(getDomainObjectConfig().getGeneratedTargetName(getDomainObjectConfig().getDomainObjectName(), true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(ObjIntConsumer.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Consumer.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(List.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Page.class.getName()));
    }

    /**
     * 创建领域对象
     * @return
     */
    protected ApplicationServiceMethod createDomainObject(P codegenParameter) {
        DomainAggregateConfig domainAggregateConfig = getDomainObjectConfig();
        ApplicationServiceMethod serviceMethod = new ApplicationServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainAggregateConfig));
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("create" + serviceMethod.getDomainObjectParameter().getDomainObjectName());
        return serviceMethod;
    }

    /**
     * 根据领域对象ID修改领域对象
     * @return
     */
    protected ApplicationServiceMethod modifyDomainObjectById(P codegenParameter) {
        DomainAggregateConfig domainAggregateConfig = getDomainObjectConfig();
        ApplicationServiceMethod serviceMethod = new ApplicationServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainAggregateConfig));
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("modify" + serviceMethod.getDomainObjectParameter().getDomainObjectAlias() + "ById");
        return serviceMethod;
    }

    /**
     * 根据领域对象ID删除领域对象
     * @return
     */
    protected ApplicationServiceMethod removeDomainObjectById(P codegenParameter) {
        DomainAggregateConfig domainAggregateConfig = getDomainObjectConfig();
        ApplicationServiceMethod serviceMethod = new ApplicationServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainAggregateConfig));
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("remove" + serviceMethod.getDomainObjectParameter().getDomainObjectAlias() + "ById");
        return serviceMethod;
    }

    /**
     * 根据多个领域对象ID删除领域对象
     * @return
     */
    protected ApplicationServiceMethod removeDomainObjectsByIds(P codegenParameter) {
        DomainAggregateConfig domainAggregateConfig = getDomainObjectConfig();
        ApplicationServiceMethod serviceMethod = new ApplicationServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainAggregateConfig));
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("remove" + serviceMethod.getDomainObjectParameter().getDomainObjectsAlias() + "ByIds");
        return serviceMethod;
    }

    /**
     * 根据领域对象ID获取领域对象
     * @return
     */
    protected ApplicationServiceMethod getDomainObjectById(P codegenParameter) {
        DomainAggregateConfig domainAggregateConfig = getDomainObjectConfig();
        ApplicationServiceMethod serviceMethod = new ApplicationServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainAggregateConfig));
        serviceMethod.setMethodReturnType(serviceMethod.getDomainObjectParameter().getDomainObjectName());
        serviceMethod.setMethodName("get" + serviceMethod.getDomainObjectParameter().getDomainObjectAlias() + "ById");
        return serviceMethod;
    }

    /**
     * 根据多个领域对象ID获取领域对象
     * @return
     */
    protected ApplicationServiceMethod getDomainObjectsByIds(P codegenParameter) {
        DomainAggregateConfig domainAggregateConfig = getDomainObjectConfig();
        ApplicationServiceMethod serviceMethod = new ApplicationServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainAggregateConfig));
        serviceMethod.setMethodReturnType("List<" + serviceMethod.getDomainObjectParameter().getDomainObjectName() + ">");
        serviceMethod.setMethodName("get" + serviceMethod.getDomainObjectParameter().getDomainObjectsAlias() + "ByIds");
        return serviceMethod;
    }

    /**
     * 根据条件查询领域对象(分页、排序)
     * @return
     */
    protected ApplicationServiceMethod getDomainObjectsByPage(P codegenParameter) {
        DomainAggregateConfig domainAggregateConfig = getDomainObjectConfig();
        ApplicationServiceMethod serviceMethod = new ApplicationServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainAggregateConfig));
        serviceMethod.setMethodReturnType("List<" + serviceMethod.getDomainObjectParameter().getDomainObjectName() + ">");
        serviceMethod.setMethodName("get" + serviceMethod.getDomainObjectParameter().getDomainObjectsAlias() + "ByPage");
        return serviceMethod;
    }

    /**
     * 获取领域对象总数
     * @return
     */
    protected ApplicationServiceMethod getDomainObjectTotalCount(P codegenParameter) {
        DomainAggregateConfig domainAggregateConfig = getDomainObjectConfig();
        ApplicationServiceMethod serviceMethod = new ApplicationServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainAggregateConfig));
        serviceMethod.setMethodReturnType("int");
        serviceMethod.setMethodName("get" + serviceMethod.getDomainObjectParameter().getDomainObjectAlias() + "TotalCount");
        return serviceMethod;
    }

    /**
     * 基于游标遍历所有领域对象
     * @return
     */
    protected ApplicationServiceMethod forEachDomainObject1(P codegenParameter) {
        DomainAggregateConfig domainAggregateConfig = getDomainObjectConfig();
        ApplicationServiceMethod serviceMethod = new ApplicationServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainAggregateConfig));
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("forEach" + serviceMethod.getDomainObjectParameter().getDomainObjectAlias());
        return serviceMethod;
    }

    /**
     * 基于游标遍历所有领域对象
     * @return
     */
    protected ApplicationServiceMethod forEachDomainObject2(P codegenParameter) {
        return forEachDomainObject1(codegenParameter);
    }

    @Override
    protected DomainObjectParameter createDomainObjectParameter(DomainAggregateConfig domainObjectConfig) {
        DomainObjectParameter parameter = super.createDomainObjectParameter(domainObjectConfig);
        //聚合根必定是继承Master领域对象的，所以Master领域对象的ID代表了聚合根的ID
        DomainEntityConfig masterDomainEntityConfig = getCodegenConfig().getDomain().getDomainEntities().get(domainObjectConfig.getAggregateMasterEntity());
        parameter.setDomainObjectIdType(masterDomainEntityConfig.getIdType().getShortName());
        return parameter;
    }
}
