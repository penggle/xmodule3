package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.*;
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
 * @created 2021/2/6 22:03
 */
public abstract class AbstractDomainServiceCodegenParameterBuilder<T extends GenerableTargetConfig, P extends AbstractDomainServiceCodegenParameter> extends CodegenParameterBuilder<ServiceCodegenConfigProperties, T, DomainEntityConfig, P> {

    protected AbstractDomainServiceCodegenParameterBuilder(CodegenContext<ServiceCodegenConfigProperties, T, DomainEntityConfig> codegenContext) {
        super(codegenContext);
    }

    protected AbstractDomainServiceCodegenParameterBuilder(ServiceCodegenConfigProperties codegenConfig, T targetConfig, DomainEntityConfig domainObjectConfig) {
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
    protected DomainServiceMethodParameter createDomainObject(P codegenParameter) {
        DomainEntityConfig domainEntityConfig = getDomainObjectConfig();
        DomainServiceMethodParameter serviceMethod = new DomainServiceMethodParameter();
        serviceMethod.setActivated(true);
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("create" + codegenParameter.getDomainObjectParameter().getDomainObjectName());
        return serviceMethod;
    }

    /**
     * 批量创建领域对象
     * @return
     */
    protected DomainServiceMethodParameter batchCreateDomainObjects(P codegenParameter) {
        DomainServiceMethodParameter serviceMethod = new DomainServiceMethodParameter();
        serviceMethod.setActivated(true);
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("batchCreate" + codegenParameter.getDomainObjectParameter().getDomainObjectsAlias());
        return serviceMethod;
    }

    /**
     * 根据领域对象ID修改领域对象
     * @return
     */
    protected DomainServiceMethodParameter modifyDomainObjectById(P codegenParameter) {
        DomainServiceMethodParameter serviceMethod = new DomainServiceMethodParameter();
        serviceMethod.setActivated(true);
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("modify" + codegenParameter.getDomainObjectParameter().getDomainObjectAlias() + "ById");
        return serviceMethod;
    }

    /**
     * 根据领域对象ID批量修改领域对象
     * @return
     */
    protected DomainServiceMethodParameter batchModifyDomainObjectsById(P codegenParameter) {
        DomainServiceMethodParameter serviceMethod = new DomainServiceMethodParameter();
        serviceMethod.setActivated(true);
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("batchModify" + codegenParameter.getDomainObjectParameter().getDomainObjectsAlias() + "ById");
        return serviceMethod;
    }

    /**
     * 根据领域对象ID删除领域对象
     * @return
     */
    protected DomainServiceMethodParameter removeDomainObjectById(P codegenParameter) {
        DomainServiceMethodParameter serviceMethod = new DomainServiceMethodParameter();
        serviceMethod.setActivated(true);
        serviceMethod.setMethodReturnType("int");
        serviceMethod.setMethodName("remove" + codegenParameter.getDomainObjectParameter().getDomainObjectAlias() + "ById");
        return serviceMethod;
    }

    /**
     * 根据多个领域对象ID删除领域对象
     * @return
     */
    protected DomainServiceMethodParameter removeDomainObjectsByIds(P codegenParameter) {
        DomainServiceMethodParameter serviceMethod = new DomainServiceMethodParameter();
        serviceMethod.setActivated(true);
        serviceMethod.setMethodReturnType("int");
        serviceMethod.setMethodName("remove" + codegenParameter.getDomainObjectParameter().getDomainObjectsAlias() + "ByIds");
        return serviceMethod;
    }

    /**
     * 根据某master领域对象ID删除当前领域对象(slave)
     * @return
     */
    protected Map<String, ByMasterIdDomainServiceMethodParameter> removeDomainObjectsByXxxMasterId(P codegenParameter) {
        DomainEntityConfig slaveDomainEntityConfig = getDomainObjectConfig();
        Map<String, ByMasterIdDomainServiceMethodParameter> serviceMethodMap = new HashMap<>();
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
    protected ByMasterIdDomainServiceMethodParameter removeDomainObjectsByMasterId(P codegenParameter, DomainEntityConfig slaveDomainEntityConfig, DomainEntityConfig masterDomainEntityConfig, DomainAggregateSlaveConfig domainAggregateSlaveConfig, String masterIdNameOfSlave) {
        ByMasterIdDomainServiceMethodParameter serviceMethod = createByMasterIdDomainServiceMethod(codegenParameter, slaveDomainEntityConfig, masterDomainEntityConfig, masterIdNameOfSlave);
        serviceMethod.setMethodReturnType("int");
        serviceMethod.setMethodName("remove" + codegenParameter.getDomainObjectParameter().getDomainObjectsAlias() + "By" + serviceMethod.getUpperMasterIdNameOfSlave());
        return serviceMethod;
    }

    /**
     * 根据领域对象ID获取领域对象
     * @return
     */
    protected DomainServiceMethodParameter getDomainObjectById(P codegenParameter) {
        DomainServiceMethodParameter serviceMethod = new DomainServiceMethodParameter();
        serviceMethod.setActivated(true);
        serviceMethod.setMethodReturnType(codegenParameter.getDomainObjectParameter().getDomainObjectName());
        serviceMethod.setMethodName("get" + codegenParameter.getDomainObjectParameter().getDomainObjectAlias() + "ById");
        return serviceMethod;
    }

    /**
     * 根据多个领域对象ID获取领域对象
     * @return
     */
    protected DomainServiceMethodParameter getDomainObjectsByIds(P codegenParameter) {
        DomainServiceMethodParameter serviceMethod = new DomainServiceMethodParameter();
        serviceMethod.setActivated(true);
        serviceMethod.setMethodReturnType("List<" + codegenParameter.getDomainObjectParameter().getDomainObjectName() + ">");
        serviceMethod.setMethodName("get" + codegenParameter.getDomainObjectParameter().getDomainObjectsAlias() + "ByIds");
        return serviceMethod;
    }

    /**
     * 根据某个master领域对象ID获取当前领域对象(slave)
     * @return
     */
    protected Map<String, ByMasterIdDomainServiceMethodParameter> getDomainObjectsByXxxMasterId(P codegenParameter) {
        DomainEntityConfig slaveDomainEntityConfig = getDomainObjectConfig();
        Map<String, ByMasterIdDomainServiceMethodParameter> serviceMethodMap = new HashMap<>();
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
    protected ByMasterIdDomainServiceMethodParameter getDomainObjectsByMasterId(P codegenParameter, DomainEntityConfig slaveDomainEntityConfig, DomainEntityConfig masterDomainEntityConfig, DomainAggregateSlaveConfig domainAggregateSlaveConfig, String masterIdNameOfSlave) {
        ByMasterIdDomainServiceMethodParameter serviceMethod = createByMasterIdDomainServiceMethod(codegenParameter, slaveDomainEntityConfig, masterDomainEntityConfig, masterIdNameOfSlave);
        String methodReturnType = codegenParameter.getDomainObjectParameter().getDomainObjectName();
        if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //如果是1:N关系?
            methodReturnType = "List<" + codegenParameter.getDomainObjectParameter().getDomainObjectName() + ">";
        }
        serviceMethod.setMethodReturnType(methodReturnType);
        serviceMethod.setMethodName("get" + codegenParameter.getDomainObjectParameter().getDomainObjectsAlias() + "By" + serviceMethod.getUpperMasterIdNameOfSlave());
        return serviceMethod;
    }

    /**
     * 根据多个master领域对象ID获取当前领域对象(slave)
     * @return
     */
    protected Map<String, ByMasterIdDomainServiceMethodParameter> getDomainObjectsByXxxMasterIds(P codegenParameter) {
        DomainEntityConfig slaveDomainEntityConfig = getDomainObjectConfig();
        Map<String, ByMasterIdDomainServiceMethodParameter> serviceMethodMap = new HashMap<>();
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
    protected ByMasterIdDomainServiceMethodParameter getDomainObjectsByMasterIds(P codegenParameter, DomainEntityConfig slaveDomainEntityConfig, DomainEntityConfig masterDomainEntityConfig, DomainAggregateSlaveConfig domainAggregateSlaveConfig, String masterIdNameOfSlave) {
        ByMasterIdDomainServiceMethodParameter serviceMethod = createByMasterIdDomainServiceMethod(codegenParameter, slaveDomainEntityConfig, masterDomainEntityConfig, masterIdNameOfSlave);
        String methodReturnType = String.format("Map<%s,%s>", serviceMethod.getMasterDomainObjectParameter().getDomainObjectIdType(), codegenParameter.getDomainObjectParameter().getDomainObjectName());
        if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //如果是1:N关系?
            methodReturnType = String.format("Map<%s,%s>", serviceMethod.getMasterDomainObjectParameter().getDomainObjectIdType(), "List<" + codegenParameter.getDomainObjectParameter().getDomainObjectName() + ">");
        }
        serviceMethod.setMethodReturnType(methodReturnType);
        serviceMethod.setMethodName("get" + codegenParameter.getDomainObjectParameter().getDomainObjectsAlias() + "By" + serviceMethod.getUpperMasterIdsNameOfSlave());
        return serviceMethod;
    }

    protected ByMasterIdDomainServiceMethodParameter createByMasterIdDomainServiceMethod(P codegenParameter, DomainEntityConfig slaveDomainEntityConfig, DomainEntityConfig masterDomainEntityConfig, String masterIdNameOfSlave) {
        ByMasterIdDomainServiceMethodParameter serviceMethod = new ByMasterIdDomainServiceMethodParameter();
        serviceMethod.setActivated(true);
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
    protected DomainServiceMethodParameter getDomainObjectsByPage(P codegenParameter) {
        DomainServiceMethodParameter serviceMethod = new DomainServiceMethodParameter();
        serviceMethod.setActivated(true);
        serviceMethod.setMethodReturnType("List<" + codegenParameter.getDomainObjectParameter().getDomainObjectName() + ">");
        serviceMethod.setMethodName("get" + codegenParameter.getDomainObjectParameter().getDomainObjectsAlias() + "ByPage");
        return serviceMethod;
    }

    /**
     * 获取领域对象总数
     * @return
     */
    protected DomainServiceMethodParameter getDomainObjectTotalCount(P codegenParameter) {
        DomainServiceMethodParameter serviceMethod = new DomainServiceMethodParameter();
        serviceMethod.setActivated(true);
        serviceMethod.setMethodReturnType("int");
        serviceMethod.setMethodName("get" + codegenParameter.getDomainObjectParameter().getDomainObjectAlias() + "TotalCount");
        return serviceMethod;
    }

    /**
     * 基于游标遍历所有领域对象
     * @return
     */
    protected DomainServiceMethodParameter forEachDomainObject1(P codegenParameter) {
        DomainServiceMethodParameter serviceMethod = new DomainServiceMethodParameter();
        serviceMethod.setActivated(true);
        serviceMethod.setMethodReturnType("void");
        serviceMethod.setMethodName("forEach" + codegenParameter.getDomainObjectParameter().getDomainObjectAlias());
        return serviceMethod;
    }

    /**
     * 基于游标遍历所有领域对象
     * @return
     */
    protected DomainServiceMethodParameter forEachDomainObject2(P codegenParameter) {
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
