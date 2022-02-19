package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.CodegenParameterBuilder;
import com.penglecode.xmodule.common.codegen.support.DomainMasterSlaveRelation;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.model.Page;
import com.penglecode.xmodule.common.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

/**
 * 领域实体的领域服务代码生成参数Builder基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 22:03
 */
public abstract class AbstractDomainServiceCodegenParameterBuilder<T extends GenerableTargetConfig, P extends AbstractDomainServiceCodegenParameter> extends CodegenParameterBuilder<ServiceCodegenConfigProperties, T, DomainEntityConfig, P> {

    protected AbstractDomainServiceCodegenParameterBuilder(CodegenContext<ServiceCodegenConfigProperties, T, DomainEntityConfig> codegenContext) {
        super(codegenContext);
    }

    protected AbstractDomainServiceCodegenParameterBuilder(ServiceCodegenConfigProperties codegenConfig, T targetConfig, DomainEntityConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected P setCustomCodegenParameter(P codegenParameter) {
        attachServiceCommonImports(codegenParameter);
        codegenParameter.setCreateDomainObject(createDomainObject(codegenParameter));
        codegenParameter.setBatchCreateDomainObjects(batchCreateDomainObjects(codegenParameter));
        codegenParameter.setModifyDomainObjectById(modifyDomainObjectById(codegenParameter));
        codegenParameter.setBatchModifyDomainObjectsById(batchModifyDomainObjectsById(codegenParameter));
        codegenParameter.setRemoveDomainObjectById(removeDomainObjectById(codegenParameter));
        codegenParameter.setRemoveDomainObjectsByIds(removeDomainObjectsByIds(codegenParameter));
        codegenParameter.setRemoveDomainObjectsByXxxMasterId(removeDomainObjectsByXxxMasterId(codegenParameter));
        codegenParameter.setGetDomainObjectById(getDomainObjectById(codegenParameter));
        codegenParameter.setGetDomainObjectsByIds(getDomainObjectsByIds(codegenParameter));
        codegenParameter.setGetDomainObjectsByXxxMasterId(getDomainObjectsByXxxMasterId(codegenParameter));
        codegenParameter.setGetDomainObjectsByXxxMasterIds(getDomainObjectsByXxxMasterIds(codegenParameter));
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
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Page.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(List.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Consumer.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(ObjIntConsumer.class.getName()));
    }

    /**
     * 创建领域对象
     * @return
     */
    protected DomainServiceMethod createDomainObject(P codegenParameter) {
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        DomainServiceMethod serviceMethod = new DomainServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainEntityConfig));
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("create" + serviceMethod.getDomainObjectParameter().getDomainObjectName());
        return serviceMethod;
    }

    /**
     * 批量创建领域对象
     * @return
     */
    protected DomainServiceMethod batchCreateDomainObjects(P codegenParameter) {
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        DomainServiceMethod serviceMethod = new DomainServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainEntityConfig));
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("batchCreate" + serviceMethod.getDomainObjectParameter().getDomainObjectsAlias());
        return serviceMethod;
    }

    /**
     * 根据领域对象ID修改领域对象
     * @return
     */
    protected DomainServiceMethod modifyDomainObjectById(P codegenParameter) {
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        DomainServiceMethod serviceMethod = new DomainServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainEntityConfig));
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("modify" + serviceMethod.getDomainObjectParameter().getDomainObjectAlias() + "ById");
        return serviceMethod;
    }

    /**
     * 根据领域对象ID批量修改领域对象
     * @return
     */
    protected DomainServiceMethod batchModifyDomainObjectsById(P codegenParameter) {
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        DomainServiceMethod serviceMethod = new DomainServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainEntityConfig));
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("batchModify" + serviceMethod.getDomainObjectParameter().getDomainObjectsAlias() + "ById");
        return serviceMethod;
    }

    /**
     * 根据领域对象ID删除领域对象
     * @return
     */
    protected DomainServiceMethod removeDomainObjectById(P codegenParameter) {
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        DomainServiceMethod serviceMethod = new DomainServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainEntityConfig));
        serviceMethod.setMethodReturnType("int");
        serviceMethod.setMethodName("remove" + serviceMethod.getDomainObjectParameter().getDomainObjectAlias() + "ById");
        return serviceMethod;
    }

    /**
     * 根据多个领域对象ID删除领域对象
     * @return
     */
    protected DomainServiceMethod removeDomainObjectsByIds(P codegenParameter) {
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        DomainServiceMethod serviceMethod = new DomainServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainEntityConfig));
        serviceMethod.setMethodReturnType("int");
        serviceMethod.setMethodName("remove" + serviceMethod.getDomainObjectParameter().getDomainObjectsAlias() + "ByIds");
        return serviceMethod;
    }

    /**
     * 根据某master领域对象ID删除当前领域对象(slave)
     * @return
     */
    protected Map<String,ByMasterIdDomainServiceMethod> removeDomainObjectsByXxxMasterId(P codegenParameter) {
        DomainEntityConfig slaveDomainEntityConfig = getDomainObjectConfig();
        Map<String,ByMasterIdDomainServiceMethod> serviceMethodMap = new HashMap<>();
        //当前Slave实体可能属于多个Master
        processDomainObjectsByMasterId(slaveDomainEntityConfig, (domainAggregateSlaveConfig, masterDomainEntityConfig) -> {
            String masterIdNameOfSlave = domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfSlave(); //slave中与masterId关联的那个字段
            serviceMethodMap.put(masterIdNameOfSlave, removeDomainObjectsByMasterId(codegenParameter, slaveDomainEntityConfig, masterDomainEntityConfig, domainAggregateSlaveConfig, masterIdNameOfSlave));
        });
        return serviceMethodMap;
    }

    /**
     * 根据某master领域对象ID删除当前领域对象(slave)
     *
     * @param codegenParameter
     * @param slaveDomainEntityConfig
     * @param masterDomainEntityConfig
     * @param domainAggregateSlaveConfig
     * @param masterIdNameOfSlave
     * @return
     */
    protected ByMasterIdDomainServiceMethod removeDomainObjectsByMasterId(P codegenParameter, DomainEntityConfig slaveDomainEntityConfig, DomainEntityConfig masterDomainEntityConfig, DomainAggregateSlaveConfig domainAggregateSlaveConfig, String masterIdNameOfSlave) {
        ByMasterIdDomainServiceMethod serviceMethod = createByMasterIdDomainServiceMethod(codegenParameter, slaveDomainEntityConfig, masterDomainEntityConfig, masterIdNameOfSlave);
        serviceMethod.setMethodReturnType("int");
        serviceMethod.setMethodName("remove" + serviceMethod.getDomainObjectParameter().getDomainObjectsAlias() + "By" + serviceMethod.getUpperMasterIdNameOfSlave());
        return serviceMethod;
    }

    /**
     * 根据领域对象ID获取领域对象
     * @return
     */
    protected DomainServiceMethod getDomainObjectById(P codegenParameter) {
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        DomainServiceMethod serviceMethod = new DomainServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainEntityConfig));
        serviceMethod.setMethodReturnType(serviceMethod.getDomainObjectParameter().getDomainObjectName());
        serviceMethod.setMethodName("get" + serviceMethod.getDomainObjectParameter().getDomainObjectAlias() + "ById");
        return serviceMethod;
    }

    /**
     * 根据多个领域对象ID获取领域对象
     * @return
     */
    protected DomainServiceMethod getDomainObjectsByIds(P codegenParameter) {
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        DomainServiceMethod serviceMethod = new DomainServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainEntityConfig));
        serviceMethod.setMethodReturnType("List<" + serviceMethod.getDomainObjectParameter().getDomainObjectName() + ">");
        serviceMethod.setMethodName("get" + serviceMethod.getDomainObjectParameter().getDomainObjectsAlias() + "ByIds");
        return serviceMethod;
    }

    /**
     * 根据某个master领域对象ID获取当前领域对象(slave)
     * @return
     */
    protected Map<String,ByMasterIdDomainServiceMethod> getDomainObjectsByXxxMasterId(P codegenParameter) {
        DomainEntityConfig slaveDomainEntityConfig = getDomainObjectConfig();
        Map<String,ByMasterIdDomainServiceMethod> serviceMethodMap = new HashMap<>();
        //当前Slave实体可能属于多个Master
        processDomainObjectsByMasterId(slaveDomainEntityConfig, (domainAggregateSlaveConfig, masterDomainEntityConfig) -> {
            String masterIdNameOfSlave = domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfSlave(); //slave中与masterId关联的那个字段
            serviceMethodMap.put(masterIdNameOfSlave, getDomainObjectsByMasterId(codegenParameter, slaveDomainEntityConfig, masterDomainEntityConfig, domainAggregateSlaveConfig, masterIdNameOfSlave));
        });
        return serviceMethodMap;
    }

    /**
     * 根据某master领域对象ID获取当前领域对象(slave)
     *
     * @param codegenParameter
     * @param slaveDomainEntityConfig
     * @param masterDomainEntityConfig
     * @param domainAggregateSlaveConfig
     * @param masterIdNameOfSlave
     * @return
     */
    protected ByMasterIdDomainServiceMethod getDomainObjectsByMasterId(P codegenParameter, DomainEntityConfig slaveDomainEntityConfig, DomainEntityConfig masterDomainEntityConfig, DomainAggregateSlaveConfig domainAggregateSlaveConfig, String masterIdNameOfSlave) {
        ByMasterIdDomainServiceMethod serviceMethod = createByMasterIdDomainServiceMethod(codegenParameter, slaveDomainEntityConfig, masterDomainEntityConfig, masterIdNameOfSlave);
        String methodReturnType = serviceMethod.getDomainObjectParameter().getDomainObjectName();
        if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //如果是1:N关系?
            methodReturnType = "List<" + serviceMethod.getDomainObjectParameter().getDomainObjectName() + ">";
        }
        serviceMethod.setMethodReturnType(methodReturnType);
        serviceMethod.setMethodName("get" + serviceMethod.getDomainObjectParameter().getDomainObjectsAlias() + "By" + serviceMethod.getUpperMasterIdNameOfSlave());
        return serviceMethod;
    }

    /**
     * 根据多个master领域对象ID获取当前领域对象(slave)
     * @return
     */
    protected Map<String,ByMasterIdDomainServiceMethod> getDomainObjectsByXxxMasterIds(P codegenParameter) {
        DomainEntityConfig slaveDomainEntityConfig = getDomainObjectConfig();
        Map<String,ByMasterIdDomainServiceMethod> serviceMethodMap = new HashMap<>();
        //当前Slave实体可能属于多个Master
        processDomainObjectsByMasterId(slaveDomainEntityConfig, (domainAggregateSlaveConfig, masterDomainEntityConfig) -> {
            String masterIdNameOfSlave = domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfSlave(); //slave中与masterId关联的那个字段
            serviceMethodMap.put(masterIdNameOfSlave, getDomainObjectsByMasterIds(codegenParameter, slaveDomainEntityConfig, masterDomainEntityConfig, domainAggregateSlaveConfig, masterIdNameOfSlave));
        });
        return serviceMethodMap;
    }

    /**
     * 根据多个master领域对象ID获取当前领域对象(slave)
     *
     * @param codegenParameter
     * @param slaveDomainEntityConfig
     * @param masterDomainEntityConfig
     * @param domainAggregateSlaveConfig
     * @param masterIdNameOfSlave
     * @return
     */
    protected ByMasterIdDomainServiceMethod getDomainObjectsByMasterIds(P codegenParameter, DomainEntityConfig slaveDomainEntityConfig, DomainEntityConfig masterDomainEntityConfig, DomainAggregateSlaveConfig domainAggregateSlaveConfig, String masterIdNameOfSlave) {
        ByMasterIdDomainServiceMethod serviceMethod = createByMasterIdDomainServiceMethod(codegenParameter, slaveDomainEntityConfig, masterDomainEntityConfig, masterIdNameOfSlave);
        String methodReturnType = String.format("Map<%s,%s>", serviceMethod.getMasterDomainObjectParameter().getDomainObjectIdType(), serviceMethod.getDomainObjectParameter().getDomainObjectName());
        if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //如果是1:N关系?
            methodReturnType = String.format("Map<%s,%s>", serviceMethod.getMasterDomainObjectParameter().getDomainObjectIdType(), "List<" + serviceMethod.getDomainObjectParameter().getDomainObjectName() + ">");
        }
        serviceMethod.setMethodReturnType(methodReturnType);
        serviceMethod.setMethodName("get" + serviceMethod.getDomainObjectParameter().getDomainObjectsAlias() + "By" + serviceMethod.getUpperMasterIdsNameOfSlave());
        return serviceMethod;
    }

    protected ByMasterIdDomainServiceMethod createByMasterIdDomainServiceMethod(P codegenParameter, DomainEntityConfig slaveDomainEntityConfig, DomainEntityConfig masterDomainEntityConfig, String masterIdNameOfSlave) {
        ByMasterIdDomainServiceMethod serviceMethod = new ByMasterIdDomainServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(slaveDomainEntityConfig));
        serviceMethod.setMasterDomainObjectParameter(createDomainObjectParameter(masterDomainEntityConfig));
        serviceMethod.setMasterIdNameOfSlave(masterIdNameOfSlave);
        serviceMethod.setUpperMasterIdNameOfSlave(StringUtils.upperCaseFirstChar(serviceMethod.getMasterIdNameOfSlave()));
        serviceMethod.setMasterIdsNameOfSlave(CodegenUtils.getPluralNameOfDomainObject(masterIdNameOfSlave));
        serviceMethod.setUpperMasterIdsNameOfSlave(StringUtils.upperCaseFirstChar(serviceMethod.getMasterIdsNameOfSlave()));
        return serviceMethod;
    }

    /**
     * 根据条件查询领域对象(分页、排序)
     * @return
     */
    protected DomainServiceMethod getDomainObjectsByPage(P codegenParameter) {
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        DomainServiceMethod serviceMethod = new DomainServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainEntityConfig));
        serviceMethod.setMethodReturnType("List<" + serviceMethod.getDomainObjectParameter().getDomainObjectName() + ">");
        serviceMethod.setMethodName("get" + serviceMethod.getDomainObjectParameter().getDomainObjectsAlias() + "ByPage");
        return serviceMethod;
    }

    /**
     * 获取领域对象总数
     * @return
     */
    protected DomainServiceMethod getDomainObjectTotalCount(P codegenParameter) {
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        DomainServiceMethod serviceMethod = new DomainServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainEntityConfig));
        serviceMethod.setMethodReturnType("int");
        serviceMethod.setMethodName("get" + serviceMethod.getDomainObjectParameter().getDomainObjectAlias() + "TotalCount");
        return serviceMethod;
    }

    /**
     * 基于游标遍历所有领域对象
     * @return
     */
    protected DomainServiceMethod forEachDomainObject1(P codegenParameter) {
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        DomainServiceMethod serviceMethod = new DomainServiceMethod();
        serviceMethod.setActivated(true);
        serviceMethod.setDomainObjectParameter(createDomainObjectParameter(domainEntityConfig));
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("forEach" + serviceMethod.getDomainObjectParameter().getDomainObjectAlias());
        return serviceMethod;
    }

    /**
     * 基于游标遍历所有领域对象
     * @return
     */
    protected DomainServiceMethod forEachDomainObject2(P codegenParameter) {
        return forEachDomainObject1(codegenParameter);
    }

    protected void processDomainObjectsByMasterId(DomainEntityConfig slaveDomainEntityConfig, BiConsumer<DomainAggregateSlaveConfig,DomainEntityConfig> biConsumer) {
        Map<String, DomainAggregateConfig> domainAggregateConfigs = getCodegenConfig().getDomain().getDomainAggregates();
        for(Map.Entry<String,DomainAggregateConfig> entry : domainAggregateConfigs.entrySet()) {
            DomainAggregateConfig domainAggregateConfig = entry.getValue();
            for (DomainAggregateSlaveConfig domainAggregateSlaveConfig : domainAggregateConfig.getAggregateSlaveEntities()) {
                //找到当前Slave实体的对应Master
                if (domainAggregateSlaveConfig.getAggregateSlaveEntity().equals(slaveDomainEntityConfig.getDomainEntityName())) {
                    DomainEntityConfig masterDomainEntityConfig = getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateConfig.getAggregateMasterEntity());
                    biConsumer.accept(domainAggregateSlaveConfig, masterDomainEntityConfig);
                }
            }
        }
    }

    @Override
    protected DomainObjectParameter createDomainObjectParameter(DomainEntityConfig domainObjectConfig) {
        DomainObjectParameter parameter = super.createDomainObjectParameter(domainObjectConfig);
        parameter.setDomainObjectIdType(domainObjectConfig.getIdType().getShortName());
        return parameter;
    }

}
