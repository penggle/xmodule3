package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.ModuleCodeGenerator;
import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.CodegenParameter;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.model.Page;
import com.penglecode.xmodule.common.mybatis.MybatisHelper;
import com.penglecode.xmodule.common.mybatis.dsl.LambdaQueryCriteria;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import com.penglecode.xmodule.common.support.BeanValidator;
import com.penglecode.xmodule.common.support.MapLambdaBuilder;
import com.penglecode.xmodule.common.support.MessageSupplier;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    }

    protected CodegenParameter createDomainServiceInterfaceCodegenParameter(CodegenContext<ServiceCodegenConfigProperties, ServiceInterfaceConfig, DomainEntityConfig> codegenContext) {
        CodegenParameter codegenParameter = super.createCodegenParameter(codegenContext, "DomainServiceInterface.ftl");
        codegenParameter.setTargetComment(codegenContext.getDomainObjectConfig().getDomainEntityTitle() + "领域服务接口");
        attachDomainServiceCommonImports(codegenContext, codegenParameter);
        attachDomainServiceCodegenMethodParameters(codegenContext, codegenParameter, false);
        return codegenParameter;
    }

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

    protected void attachDomainServiceCommonImports(CodegenContext<ServiceCodegenConfigProperties, ?, DomainEntityConfig> codegenContext, CodegenParameter codegenParameter) {
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(codegenContext.getDomainObjectConfig().getGeneratedTargetName(codegenContext.getDomainObjectConfig().getDomainEntityName(), true, false)));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Page.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(List.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Consumer.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(ObjIntConsumer.class.getName()));
    }

    protected void attachDomainServiceImplementImports(CodegenContext<ServiceCodegenConfigProperties, ?, DomainEntityConfig> codegenContext, CodegenParameter codegenParameter) {
        attachDomainServiceCommonImports(codegenContext, codegenParameter);
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(MybatisHelper.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(LambdaQueryCriteria.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(MapLambdaBuilder.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(QueryCriteria.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(BeanValidator.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(MessageSupplier.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(ValidationAssert.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Cursor.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Consumer.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(ObjIntConsumer.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Service.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Resource.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Transactional.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(CollectionUtils.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(ObjectUtils.class.getName()));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Collections.class.getName()));
    }

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
        codegenParameter.put("getDomainObjectsByMasterIds", codegenMethodParameters.getDomainObjectsByMasterIds(implementation));
        codegenParameter.put("getDomainObjectsByPage", codegenMethodParameters.getDomainObjectsByPage(implementation));
        codegenParameter.put("getDomainObjectTotalCount", codegenMethodParameters.getDomainObjectTotalCount(implementation));
        codegenParameter.put("forEachDomainObject1", codegenMethodParameters.forEachDomainObject1(implementation));
        codegenParameter.put("forEachDomainObject2", codegenMethodParameters.forEachDomainObject2(implementation));
    }

}
