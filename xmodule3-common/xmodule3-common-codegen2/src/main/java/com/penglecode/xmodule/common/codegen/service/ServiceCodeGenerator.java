package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.ModuleCodeGenerator;
import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.service.DomainServiceParameters.DomainServiceParameter;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.CodegenParameter;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.model.Page;
import com.penglecode.xmodule.common.mybatis.MybatisHelper;
import com.penglecode.xmodule.common.mybatis.dsl.LambdaQueryCriteria;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import com.penglecode.xmodule.common.support.*;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

/**
 * Service代码生成器
 * 专门用于生成领域服务/应用服务接口及其实现类的代码生成器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/28 20:51
 */
public class ServiceCodeGenerator extends ModuleCodeGenerator<ServiceCodegenConfigProperties> {

    public ServiceCodeGenerator(String module) {
        super(module);
    }

    @Override
    protected String getCodeName() {
        return "Service代码";
    }

    @Override
    protected void executeGenerate() throws Exception {
        ServiceCodegenConfigProperties codegenConfig = getCodegenConfig();
        Map<String,DomainEntityConfig> domainEntityConfigs = codegenConfig.getDomain().getDomainEntities();
        if(!CollectionUtils.isEmpty(domainEntityConfigs)) {
            for(Map.Entry<String, DomainEntityConfig> entry : domainEntityConfigs.entrySet()) {
                DomainEntityConfig domainEntityConfig = entry.getValue();
                //1、生成领域服务XxxService.java接口
                CodegenContext<ServiceCodegenConfigProperties,ServiceInterfaceConfig,DomainEntityConfig> codegenContext1 = new CodegenContext<>(codegenConfig, codegenConfig.getService().getDomain().getInterfaceConfig(), domainEntityConfig);
                generateTarget(codegenContext1, createDomainServiceInterfaceCodegenParameter(codegenContext1));
                //2、生成领域服务XxxServiceImpl.java实现
                CodegenContext<ServiceCodegenConfigProperties,ServiceImplementConfig,DomainEntityConfig> codegenContext2 = new CodegenContext<>(codegenConfig, codegenConfig.getService().getDomain().getImplementConfig(), domainEntityConfig);
                generateTarget(codegenContext2, createDomainServiceImplementCodegenParameter(codegenContext2));
            }
        }
        Map<String,DomainAggregateConfig> domainAggregateConfigs = codegenConfig.getDomain().getDomainAggregates();
        if(!CollectionUtils.isEmpty(domainAggregateConfigs)) {
            for(Map.Entry<String,DomainAggregateConfig> entry : domainAggregateConfigs.entrySet()) {
                DomainAggregateConfig domainAggregateConfig = entry.getValue();
                //3、生成应用服务XxxAppService.java接口(如果需要的话)
                CodegenContext<ServiceCodegenConfigProperties,ServiceInterfaceConfig,DomainAggregateConfig> codegenContext1 = new CodegenContext<>(codegenConfig, codegenConfig.getService().getApplication().getInterfaceConfig(), domainAggregateConfig);
                generateTarget(codegenContext1, createApplicationServiceInterfaceCodegenParameter(codegenContext1));
                //4、生成应用服务XxxAppServiceImpl.java实现(如果需要的话)
                CodegenContext<ServiceCodegenConfigProperties,ServiceImplementConfig,DomainAggregateConfig> codegenContext2 = new CodegenContext<>(codegenConfig, codegenConfig.getService().getApplication().getImplementConfig(), domainAggregateConfig);
                generateTarget(codegenContext2, createApplicationServiceImplementCodegenParameter(codegenContext2));
            }
        }
    }

    /**
     * 创建领域服务接口代码生成参数
     * @param codegenContext
     * @return
     */
    protected CodegenParameter createDomainServiceInterfaceCodegenParameter(CodegenContext<ServiceCodegenConfigProperties, ServiceInterfaceConfig, DomainEntityConfig> codegenContext) {
        CodegenParameter codegenParameter = super.createCodegenParameter(codegenContext, "DomainServiceInterface.ftl");
        codegenParameter.setTargetComment(codegenContext.getDomainObjectConfig().getDomainEntityTitle() + "领域服务接口");
        attachServiceCommonImports(codegenContext, codegenParameter);
        attachDomainServiceCodegenMethodParameters(codegenContext, codegenParameter, false);
        return codegenParameter;
    }

    /**
     * 创建领域服务实现代码生成参数
     * @param codegenContext
     * @return
     */
    protected CodegenParameter createDomainServiceImplementCodegenParameter(CodegenContext<ServiceCodegenConfigProperties, ServiceImplementConfig, DomainEntityConfig> codegenContext) {
        CodegenParameter codegenParameter = super.createCodegenParameter(codegenContext, "DomainServiceImplement.ftl");
        DomainEntityConfig domainEntityConfig = codegenContext.getDomainObjectConfig();
        codegenParameter.setTargetComment(domainEntityConfig.getDomainEntityTitle() + "领域服务实现");
        ServiceInterfaceConfig serviceInterfaceConfig = codegenContext.getCodegenConfig().getService().getDomain().getInterfaceConfig();
        String serviceInterfaceName = serviceInterfaceConfig.getGeneratedTargetName(domainEntityConfig.getDomainEntityName(), false, false);
        codegenParameter.setTargetAnnotations(Collections.singletonList(String.format("@Service(\"%s\")", StringUtils.lowerCaseFirstChar(serviceInterfaceName))));
        codegenParameter.setTargetImplements(Collections.singletonList(serviceInterfaceName));
        MybatisJavaMapperConfig mybatisJavaMapperConfig = codegenContext.getCodegenConfig().getMybatis().getJavaMapperConfig();
        String mapperInterfaceName = mybatisJavaMapperConfig.getGeneratedTargetName(domainEntityConfig.getDomainEntityName(), false, false);
        codegenParameter.put("mapperInterfaceName", mapperInterfaceName);
        codegenParameter.put("mapperBeanName", domainEntityConfig.getRuntimeDataSource() + mapperInterfaceName);
        codegenParameter.put("mapperInstanceName", StringUtils.lowerCaseFirstChar(mapperInterfaceName));
        codegenParameter.put("transactionManagerName", domainEntityConfig.getRuntimeDataSource() + "TransactionManager");
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(mybatisJavaMapperConfig.getGeneratedTargetName(domainEntityConfig.getDomainEntityName(), true, false)));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(serviceInterfaceConfig.getGeneratedTargetName(domainEntityConfig.getDomainEntityName(), true, false)));
        attachDomainServiceImplementImports(codegenContext, codegenParameter);
        attachDomainServiceCodegenMethodParameters(codegenContext, codegenParameter, true);
        return codegenParameter;
    }

    /**
     * 附带上领域/应用服务代码公共import
     * @param codegenContext
     * @param codegenParameter
     */
    protected void attachServiceCommonImports(CodegenContext<ServiceCodegenConfigProperties, ?, ?> codegenContext, CodegenParameter codegenParameter) {
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(codegenContext.getDomainObjectConfig().getGeneratedTargetName(codegenContext.getDomainObjectConfig().getDomainObjectName(), true, false)));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Page.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(List.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Consumer.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(ObjIntConsumer.class.getName()));
    }

    /**
     * 附带上领域服务代码import
     * @param codegenContext
     * @param codegenParameter
     */
    protected void attachDomainServiceImplementImports(CodegenContext<ServiceCodegenConfigProperties, ?, DomainEntityConfig> codegenContext, CodegenParameter codegenParameter) {
        attachServiceCommonImports(codegenContext, codegenParameter);
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(MybatisHelper.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(LambdaQueryCriteria.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(MapLambdaBuilder.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(QueryCriteria.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(BeanValidator.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(MessageSupplier.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(ValidationAssert.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Cursor.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Service.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Resource.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Transactional.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(CollectionUtils.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(ObjectUtils.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Collections.class.getName()));
    }

    /**
     * 附带上领域服务方法生成参数
     * @param codegenContext
     * @param codegenParameter
     * @param implementation
     */
    protected void attachDomainServiceCodegenMethodParameters(CodegenContext<ServiceCodegenConfigProperties, ?, DomainEntityConfig> codegenContext, CodegenParameter codegenParameter, boolean implementation) {
        DomainServiceCodegenMethodParameters codegenMethodParameters = new DomainServiceCodegenMethodParameters(codegenContext, codegenParameter);
        codegenParameter.put("createDomainObject", codegenMethodParameters.createDomainObject(implementation));
        codegenParameter.put("batchCreateDomainObjects", codegenMethodParameters.batchCreateDomainObjects(implementation));
        codegenParameter.put("modifyDomainObjectById", codegenMethodParameters.modifyDomainObjectById(implementation));
        codegenParameter.put("batchModifyDomainObjectsById", codegenMethodParameters.batchModifyDomainObjectsById(implementation));
        codegenParameter.put("removeDomainObjectById", codegenMethodParameters.removeDomainObjectById(implementation));
        codegenParameter.put("removeDomainObjectsByIds", codegenMethodParameters.removeDomainObjectsByIds(implementation));
        codegenParameter.put("removeDomainObjectsByXxxMasterId", codegenMethodParameters.removeDomainObjectsByXxxMasterId(implementation));
        codegenParameter.put("getDomainObjectById", codegenMethodParameters.getDomainObjectById(implementation));
        codegenParameter.put("getDomainObjectsByIds", codegenMethodParameters.getDomainObjectsByIds(implementation));
        codegenParameter.put("getDomainObjectsByXxxMasterId", codegenMethodParameters.getDomainObjectsByXxxMasterId(implementation));
        codegenParameter.put("getDomainObjectsByXxxMasterIds", codegenMethodParameters.getDomainObjectsByXxxMasterIds(implementation));
        codegenParameter.put("getDomainObjectsByPage", codegenMethodParameters.getDomainObjectsByPage(implementation));
        codegenParameter.put("getDomainObjectTotalCount", codegenMethodParameters.getDomainObjectTotalCount(implementation));
        codegenParameter.put("forEachDomainObject1", codegenMethodParameters.forEachDomainObject1(implementation));
        codegenParameter.put("forEachDomainObject2", codegenMethodParameters.forEachDomainObject2(implementation));
    }

    /**
     * 创建应用服务接口代码生成参数
     * @param codegenContext
     * @return
     */
    protected CodegenParameter createApplicationServiceInterfaceCodegenParameter(CodegenContext<ServiceCodegenConfigProperties, ServiceInterfaceConfig, DomainAggregateConfig> codegenContext) {
        CodegenParameter codegenParameter = super.createCodegenParameter(codegenContext, "ApplicationServiceInterface.ftl");
        codegenParameter.setTargetComment(codegenContext.getDomainObjectConfig().getDomainAggregateTitle() + "应用服务接口");
        attachServiceCommonImports(codegenContext, codegenParameter);
        attachApplicationServiceCodegenMethodParameters(codegenContext, codegenParameter, false);
        return codegenParameter;
    }

    /**
     * 创建应用服务实现代码生成参数
     * @param codegenContext
     * @return
     */
    protected CodegenParameter createApplicationServiceImplementCodegenParameter(CodegenContext<ServiceCodegenConfigProperties, ServiceImplementConfig, DomainAggregateConfig> codegenContext) {
        CodegenParameter codegenParameter = super.createCodegenParameter(codegenContext, "ApplicationServiceImplement.ftl");
        DomainAggregateConfig domainAggregateConfig = codegenContext.getDomainObjectConfig();
        codegenParameter.setTargetComment(domainAggregateConfig.getDomainAggregateTitle() + "应用服务实现");
        ServiceInterfaceConfig serviceInterfaceConfig = codegenContext.getCodegenConfig().getService().getApplication().getInterfaceConfig();
        String serviceInterfaceName = serviceInterfaceConfig.getGeneratedTargetName(domainAggregateConfig.getDomainAggregateName(), false, false);
        codegenParameter.setTargetAnnotations(Collections.singletonList(String.format("@Service(\"%s\")", StringUtils.lowerCaseFirstChar(serviceInterfaceName))));
        codegenParameter.setTargetImplements(Collections.singletonList(serviceInterfaceName));
        DomainEntityConfig masterDomainEntityConfig = codegenContext.getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateConfig.getAggregateMasterEntity());
        codegenParameter.put("transactionManagerName", masterDomainEntityConfig.getRuntimeDataSource() + "TransactionManager");
        List<Map<String,Object>> domainServices = new ArrayList<>();
        DomainServiceParameter masterDomainServiceParameter = buildAppServiceDomainServiceParameter(masterDomainEntityConfig, codegenContext, codegenParameter);
        domainServices.add(masterDomainServiceParameter.asMap());
        List<DomainServiceParameter> slaveDomainServiceParameters = new ArrayList<>();
        for(DomainAggregateSlaveConfig domainAggregateSlaveConfig : domainAggregateConfig.getAggregateSlaveEntities()) {
            DomainEntityConfig slaveDomainEntityConfig = codegenContext.getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateSlaveConfig.getAggregateSlaveEntity());
            DomainServiceParameter slaveDomainServiceParameter = buildAppServiceDomainServiceParameter(slaveDomainEntityConfig, codegenContext, codegenParameter);
            slaveDomainServiceParameters.add(slaveDomainServiceParameter);
            domainServices.add(slaveDomainServiceParameter.asMap());
        }
        codegenParameter.put("domainServices", domainServices);
        codegenParameter.put("domainServiceParameters", new DomainServiceParameters(masterDomainServiceParameter, slaveDomainServiceParameters));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(serviceInterfaceConfig.getGeneratedTargetName(domainAggregateConfig.getDomainAggregateName(), true, false)));
        attachApplicationServiceImplementImports(codegenContext, codegenParameter);
        attachApplicationServiceCodegenMethodParameters(codegenContext, codegenParameter, true);
        return codegenParameter;
    }

    /**
     * 附带上应用服务代码import
     * @param codegenContext
     * @param codegenParameter
     */
    protected void attachApplicationServiceImplementImports(CodegenContext<ServiceCodegenConfigProperties, ?, DomainAggregateConfig> codegenContext, CodegenParameter codegenParameter) {
        attachServiceCommonImports(codegenContext, codegenParameter);
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(BeanValidator.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(MessageSupplier.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(ValidationAssert.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(DomainServiceHelper.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Service.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Resource.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Transactional.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(CollectionUtils.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Collections.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Map.class.getName()));
    }

    /**
     * 附带上应用服务方法生成参数
     * @param codegenContext
     * @param codegenParameter
     * @param implementation
     */
    protected void attachApplicationServiceCodegenMethodParameters(CodegenContext<ServiceCodegenConfigProperties, ?, DomainAggregateConfig> codegenContext, CodegenParameter codegenParameter, boolean implementation) {
        ApplicationServiceCodegenMethodParameters codegenMethodParameters = new ApplicationServiceCodegenMethodParameters(codegenContext, codegenParameter);
        codegenParameter.put("createDomainObject", codegenMethodParameters.createDomainObject(implementation));
        codegenParameter.put("modifyDomainObjectById", codegenMethodParameters.modifyDomainObjectById(implementation));
        codegenParameter.put("removeDomainObjectById", codegenMethodParameters.removeDomainObjectById(implementation));
        codegenParameter.put("removeDomainObjectsByIds", codegenMethodParameters.removeDomainObjectsByIds(implementation));
        codegenParameter.put("getDomainObjectById", codegenMethodParameters.getDomainObjectById(implementation));
        codegenParameter.put("getDomainObjectsByIds", codegenMethodParameters.getDomainObjectsByIds(implementation));
        codegenParameter.put("getDomainObjectsByPage", codegenMethodParameters.getDomainObjectsByPage(implementation));
        codegenParameter.put("getDomainObjectTotalCount", codegenMethodParameters.getDomainObjectTotalCount(implementation));
        codegenParameter.put("forEachDomainObject1", codegenMethodParameters.forEachDomainObject1(implementation));
        codegenParameter.put("forEachDomainObject2", codegenMethodParameters.forEachDomainObject2(implementation));
    }

    /**
     * 创建指定领域对象的领域服务代码生成参数
     * @param domainEntityConfig
     * @param codegenContext
     * @param codegenParameter
     * @return
     */
    private DomainServiceParameter buildAppServiceDomainServiceParameter(DomainEntityConfig domainEntityConfig, CodegenContext<ServiceCodegenConfigProperties, ?, DomainAggregateConfig> codegenContext, CodegenParameter codegenParameter) {
        ServiceInterfaceConfig domainServiceInterfaceConfig = codegenContext.getCodegenConfig().getService().getDomain().getInterfaceConfig();
        String domainServiceName = domainServiceInterfaceConfig.getGeneratedTargetName(domainEntityConfig.getDomainEntityName(), false, false);
        FullyQualifiedJavaType domainServiceType = new FullyQualifiedJavaType(domainServiceInterfaceConfig.getGeneratedTargetName(domainEntityConfig.getDomainEntityName(), true, false));
        codegenParameter.getTargetAllImportTypes().add(domainServiceType); //领域服务类型
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(domainEntityConfig.getGeneratedTargetName(domainEntityConfig.getDomainEntityName(), true, false))); //领域对象类型
        return new DomainServiceParameter(domainEntityConfig, domainServiceName, StringUtils.lowerCaseFirstChar(domainServiceName), StringUtils.lowerCaseFirstChar(domainServiceName));
    }

}
