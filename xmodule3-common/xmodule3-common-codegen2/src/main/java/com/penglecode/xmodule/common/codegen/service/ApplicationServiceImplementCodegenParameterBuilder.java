package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.service.DomainServiceParameters.DomainServiceParameter;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.DomainMasterSlaveRelation;
import com.penglecode.xmodule.common.codegen.support.DomainObjectParameter;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.support.*;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 聚合根对应的应用服务实现代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 22:03
 */
public class ApplicationServiceImplementCodegenParameterBuilder extends AbstractApplicationServiceCodegenParameterBuilder<ServiceImplementConfig, ApplicationServiceImplementCodegenParameter> {

    public ApplicationServiceImplementCodegenParameterBuilder(CodegenContext<ServiceCodegenConfigProperties, ServiceImplementConfig, DomainAggregateConfig> codegenContext) {
        super(codegenContext);
    }

    public ApplicationServiceImplementCodegenParameterBuilder(ServiceCodegenConfigProperties codegenConfig, ServiceImplementConfig targetConfig, DomainAggregateConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected ApplicationServiceImplementCodegenParameter setCustomCodegenParameter(ApplicationServiceImplementCodegenParameter codegenParameter) {
        DomainAggregateConfig domainAggregateConfig = getDomainObjectConfig();
        codegenParameter.setTargetComment(domainAggregateConfig.getDomainObjectTitle() + "应用服务实现");
        ServiceInterfaceConfig serviceInterfaceConfig = getCodegenConfig().getService().getApplication().getInterfaceConfig();
        String serviceInterfaceName = serviceInterfaceConfig.getGeneratedTargetName(domainAggregateConfig.getDomainAggregateName(), false, false);
        codegenParameter.setTargetAnnotations(Collections.singletonList(String.format("@Service(\"%s\")", StringUtils.lowerCaseFirstChar(serviceInterfaceName))));
        codegenParameter.setTargetImplements(Collections.singletonList(serviceInterfaceName));
        DomainEntityConfig masterDomainEntityConfig = getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateConfig.getAggregateMasterEntity());
        codegenParameter.setTransactionManagerName(masterDomainEntityConfig.getRuntimeDataSource() + "TransactionManager");
        DomainServiceParameter masterDomainServiceParameter = buildDomainServiceParameter(masterDomainEntityConfig, codegenParameter);
        List<DomainServiceParameter> slaveDomainServiceParameters = new ArrayList<>();
        for(DomainAggregateSlaveConfig domainAggregateSlaveConfig : domainAggregateConfig.getAggregateSlaveEntities()) {
            DomainEntityConfig slaveDomainEntityConfig = getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateSlaveConfig.getAggregateSlaveEntity());
            DomainServiceParameter slaveDomainServiceParameter = buildDomainServiceParameter(slaveDomainEntityConfig, codegenParameter);
            slaveDomainServiceParameters.add(slaveDomainServiceParameter);
        }
        codegenParameter.setDomainServices(new DomainServiceParameters(masterDomainServiceParameter, slaveDomainServiceParameters));
        attachApplicationServiceImplementImports(codegenParameter);
        codegenParameter.setPrepareAggregateObjects(prepareAggregateObjects(codegenParameter));
        codegenParameter = super.setCustomCodegenParameter(codegenParameter);
        return codegenParameter;
    }

    /**
     * 创建指定领域对象的领域服务代码生成参数
     * @param domainEntityConfig
     * @param codegenParameter
     * @return
     */
    protected DomainServiceParameter buildDomainServiceParameter(DomainEntityConfig domainEntityConfig, ApplicationServiceImplementCodegenParameter codegenParameter) {
        ServiceInterfaceConfig domainServiceInterfaceConfig = getCodegenConfig().getService().getDomain().getInterfaceConfig();
        String domainServiceName = domainServiceInterfaceConfig.getGeneratedTargetName(domainEntityConfig.getDomainEntityName(), false, false);
        FullyQualifiedJavaType domainServiceType = new FullyQualifiedJavaType(domainServiceInterfaceConfig.getGeneratedTargetName(domainEntityConfig.getDomainEntityName(), true, false));
        codegenParameter.addTargetImportType(domainServiceType); //领域服务类型
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(domainEntityConfig.getGeneratedTargetName(domainEntityConfig.getDomainEntityName(), true, false))); //领域对象类型
        DomainServiceInterfaceCodegenParameter domainServiceCodegenParameter = new DomainServiceInterfaceCodegenParameterBuilder(getCodegenConfig(), domainServiceInterfaceConfig, domainEntityConfig).buildCodegenParameter();
        return new DomainServiceParameter(domainServiceCodegenParameter, domainEntityConfig, domainServiceName, StringUtils.lowerCaseFirstChar(domainServiceName), StringUtils.lowerCaseFirstChar(domainServiceName));
    }

    /**
     * 附带上应用服务代码import
     * @param codegenParameter
     */
    protected void attachApplicationServiceImplementImports(ApplicationServiceImplementCodegenParameter codegenParameter) {
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(BeanValidator.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(MessageSupplier.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(ValidationAssert.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(DomainServiceHelper.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(BeanCopier.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Service.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Resource.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Transactional.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(CollectionUtils.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Collections.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Map.class.getName()));
    }
    
    @Override
    protected ApplicationServiceMethodParameter createDomainObject(ApplicationServiceImplementCodegenParameter codegenParameter) {
        ApplicationServiceMethodParameter serviceMethod = super.createDomainObject(codegenParameter);
        //开始生成方法实现(方法体代码)
        List<String> methodBodyLines = new ArrayList<>();
        DomainAggregateConfig domainAggregateConfig = getDomainObjectConfig();
        String domainObjectVariableName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectName(); //聚合对象变量名
        methodBodyLines.add(String.format("ValidationAssert.notNull(%s, MessageSupplier.ofRequiredParameter(\"%s\"));", domainObjectVariableName, domainObjectVariableName));
        String validateFields = domainAggregateConfig.getValidateFields("create");
        if(StringUtils.isNotBlank(validateFields)) {
            methodBodyLines.add(String.format("BeanValidator.validateBean(%s);", domainObjectVariableName + validateFields));
        }
        DomainServiceParameters domainServices = codegenParameter.getDomainServices();
        DomainServiceParameter masterDomainServiceParameter = domainServices.getMasterDomainServiceParameter();
        //保存Master
        methodBodyLines.add(String.format("%s.%s(%s);", masterDomainServiceParameter.getDomainServiceInstanceName(), masterDomainServiceParameter.getDomainServiceCodegenParameter().getCreateDomainObject().getMethodName(), domainObjectVariableName));
        //保存Slaves
        Map<String,DomainAggregateFieldConfig> domainAggregateFieldConfigs = getDomainObjectConfig().getDomainAggregateFields();
        List<DomainServiceParameter> slaveDomainServiceParameters = domainServices.getSlaveDomainServiceParameters();
        for(DomainServiceParameter slaveDomainServiceParameter : slaveDomainServiceParameters) {
            DomainAggregateFieldConfig domainAggregateSlaveFieldConfig = domainAggregateFieldConfigs.get(slaveDomainServiceParameter.getDomainEntityConfig().getDomainEntityName());
            DomainAggregateSlaveConfig domainAggregateSlaveConfig = domainAggregateSlaveFieldConfig.getDomainAggregateSlaveConfig();
            String relateFieldSetterOfSlave = CodegenUtils.getSetterMethodName(domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfSlave(), null);
            String relateFieldGetterOfMaster = CodegenUtils.getSetterMethodName(domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfMaster(), null);
            if(DomainMasterSlaveRelation.RELATION_11.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { // 1:1关系?
                //ProductExtraInfo productExtra = product.getProductExtra();
                methodBodyLines.add(String.format("%s %s = %s.%s();", domainAggregateSlaveFieldConfig.getFieldType().getShortName(), domainAggregateSlaveFieldConfig.getFieldName(), domainObjectVariableName, domainAggregateSlaveFieldConfig.getFieldGetterName()));
                //if(productExtra != null) {
                methodBodyLines.add(String.format("if(%s != null) {", domainAggregateSlaveFieldConfig.getFieldName()));
                //productExtra.setProductId(product.getProductId());
                methodBodyLines.add(String.format("    %s.%s(%s.%s());", domainAggregateSlaveFieldConfig.getFieldName(), relateFieldSetterOfSlave, domainObjectVariableName, relateFieldGetterOfMaster));
                //productExtraInfoService.createProductExtra(productExtra);
                methodBodyLines.add(String.format("    %s.%s(%s);", slaveDomainServiceParameter.getDomainServiceInstanceName(), slaveDomainServiceParameter.getDomainServiceCodegenParameter().getCreateDomainObject().getMethodName(), domainAggregateSlaveFieldConfig.getFieldName()));
                methodBodyLines.add("}");
            } else if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //1:N关系?
                //List<ProductSaleSpec> productSaleSpecs = product.getProductSaleSpecs();
                methodBodyLines.add(String.format("%s %s = %s.%s();", domainAggregateSlaveFieldConfig.getFieldType().getShortName(), domainAggregateSlaveFieldConfig.getFieldName(), domainObjectVariableName, domainAggregateSlaveFieldConfig.getFieldGetterName()));
                //if(!CollectionUtils.isEmpty(productSaleSpecs)) {
                methodBodyLines.add(String.format("if(!CollectionUtils.isEmpty(%s)) {", domainAggregateSlaveFieldConfig.getFieldName()));
                //productSaleSpecs.forEach(item -> item.setProductId(product.getProductId()));
                methodBodyLines.add(String.format("    %s.forEach(item -> item.%s(%s.%s()));", domainAggregateSlaveFieldConfig.getFieldName(), relateFieldSetterOfSlave, domainObjectVariableName, relateFieldGetterOfMaster));
                //productSaleSpecService.batchCreateProductSaleSpec(productSaleSpecs);
                methodBodyLines.add(String.format("    %s.%s(%s);", slaveDomainServiceParameter.getDomainServiceInstanceName(), slaveDomainServiceParameter.getDomainServiceCodegenParameter().getBatchCreateDomainObjects().getMethodName(), domainAggregateSlaveFieldConfig.getFieldName()));
                methodBodyLines.add("}");
            }
        }
        serviceMethod.setMethodBodyLines(methodBodyLines);
        return serviceMethod;
    }

    @Override
    protected ApplicationServiceMethodParameter modifyDomainObjectById(ApplicationServiceImplementCodegenParameter codegenParameter) {
        ApplicationServiceMethodParameter serviceMethod = super.modifyDomainObjectById(codegenParameter);
        //开始生成方法实现(方法体代码)
        List<String> methodBodyLines = new ArrayList<>();
        DomainAggregateConfig domainAggregateConfig = getDomainObjectConfig();
        String domainObjectVariableName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectName(); //聚合对象变量名
        methodBodyLines.add(String.format("ValidationAssert.notNull(%s, MessageSupplier.ofRequiredParameter(\"%s\"));", domainObjectVariableName, domainObjectVariableName));
        String validateFields = domainAggregateConfig.getValidateFields("create");
        if(StringUtils.isNotBlank(validateFields)) {
            methodBodyLines.add(String.format("BeanValidator.validateBean(%s);", domainObjectVariableName + validateFields));
        }
        DomainServiceParameters domainServices = codegenParameter.getDomainServices();
        DomainServiceParameter masterDomainServiceParameter = domainServices.getMasterDomainServiceParameter();
        //保存Master
        methodBodyLines.add(String.format("%s.%s(%s);", masterDomainServiceParameter.getDomainServiceInstanceName(), masterDomainServiceParameter.getDomainServiceCodegenParameter().getModifyDomainObjectById().getMethodName(), domainObjectVariableName));
        //保存Slaves
        Map<String,DomainAggregateFieldConfig> domainAggregateFieldConfigs = getDomainObjectConfig().getDomainAggregateFields();
        List<DomainServiceParameter> slaveDomainServiceParameters = domainServices.getSlaveDomainServiceParameters();
        for(DomainServiceParameter slaveDomainServiceParameter : slaveDomainServiceParameters) {
            DomainEntityConfig slaveDomainEntityConfig = slaveDomainServiceParameter.getDomainEntityConfig();
            DomainAggregateFieldConfig domainAggregateSlaveFieldConfig = domainAggregateFieldConfigs.get(slaveDomainEntityConfig.getDomainEntityName());
            DomainAggregateSlaveConfig domainAggregateSlaveConfig = domainAggregateSlaveFieldConfig.getDomainAggregateSlaveConfig();
            String relateFieldSetterOfSlave = CodegenUtils.getSetterMethodName(domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfSlave(), null);
            String relateFieldGetterOfMaster = CodegenUtils.getSetterMethodName(domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfMaster(), null);
            if(DomainMasterSlaveRelation.RELATION_11.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { // 1:1关系?
                //ProductExtraInfo productExtra = product.getProductExtra();
                methodBodyLines.add(String.format("%s %s = %s.%s();", domainAggregateSlaveFieldConfig.getFieldType().getShortName(), domainAggregateSlaveFieldConfig.getFieldName(), domainObjectVariableName, domainAggregateSlaveFieldConfig.getFieldGetterName()));
                //if(productExtra != null) {
                methodBodyLines.add(String.format("if(%s != null) {", domainAggregateSlaveFieldConfig.getFieldName()));
                //productExtra.setProductId(product.getProductId());
                methodBodyLines.add(String.format("    %s.%s(%s.%s());", domainAggregateSlaveFieldConfig.getFieldName(), relateFieldSetterOfSlave, domainObjectVariableName, relateFieldGetterOfMaster));
                //productExtraInfoService.modifyProductExtraById(productExtra);
                methodBodyLines.add(String.format("    %s.%s(%s);", slaveDomainServiceParameter.getDomainServiceInstanceName(), slaveDomainServiceParameter.getDomainServiceCodegenParameter().getModifyDomainObjectById().getMethodName(), domainAggregateSlaveFieldConfig.getFieldName()));
                methodBodyLines.add("}");
            } else if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //1:N关系?
                String transientSlavesVariable = "transient" + StringUtils.upperCaseFirstChar(domainAggregateSlaveFieldConfig.getFieldName());
                String persistedSlavesVariable = "persisted" + StringUtils.upperCaseFirstChar(domainAggregateSlaveFieldConfig.getFieldName());
                //List<ProductSaleSpec> transientProductSaleSpecs = product.getProductSaleSpecs();
                methodBodyLines.add(String.format("%s %s = %s.%s();", domainAggregateSlaveFieldConfig.getFieldType().getShortName(), transientSlavesVariable, domainObjectVariableName, domainAggregateSlaveFieldConfig.getFieldGetterName()));
                //if(!CollectionUtils.isEmpty(transientProductSaleSpecs)) {
                methodBodyLines.add(String.format("if(!CollectionUtils.isEmpty(%s)) {", transientSlavesVariable));
                ByMasterIdDomainServiceMethodParameter getByMasterIdDomainServiceMethod = slaveDomainServiceParameter.getDomainServiceCodegenParameter().getGetDomainObjectsByXxxMasterId().get(domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfSlave());
                //List<ProductSaleSpec> persistedProductSaleSpecs = productSaleSpecService.getProductSaleSpecsByProductId(product.getProductId());
                methodBodyLines.add(String.format("    %s %s = %s.%s(%s.%s());", getByMasterIdDomainServiceMethod.getMethodReturnType(), persistedSlavesVariable, slaveDomainServiceParameter.getDomainServiceInstanceName(), getByMasterIdDomainServiceMethod.getMethodName(), domainObjectVariableName, relateFieldGetterOfMaster));
                //DomainServiceHelper.batchMergeEntityObjects(transientProductSaleSpecs, persistedProductSaleSpecs, ProductSaleSpec::identity, productSaleSpecService::batchCreateProductSaleSpec, productSaleSpecService::batchModifyProductSaleSpecById, productSaleSpecService::removeProductSaleSpecByIds);
                methodBodyLines.add(String.format("    DomainServiceHelper.batchMergeEntityObjects(%s, %s, %s::identity, %s::%s, %s::%s, %s::%s);", transientSlavesVariable, persistedSlavesVariable, slaveDomainEntityConfig.getDomainEntityName(), slaveDomainServiceParameter.getDomainServiceInstanceName(), slaveDomainServiceParameter.getDomainServiceCodegenParameter().getBatchCreateDomainObjects().getMethodName(), slaveDomainServiceParameter.getDomainServiceInstanceName(), slaveDomainServiceParameter.getDomainServiceCodegenParameter().getBatchModifyDomainObjectsById().getMethodName(), slaveDomainServiceParameter.getDomainServiceInstanceName(), slaveDomainServiceParameter.getDomainServiceCodegenParameter().getRemoveDomainObjectsByIds().getMethodName()));
                methodBodyLines.add("}");
            }
        }
        serviceMethod.setMethodBodyLines(methodBodyLines);
        return serviceMethod;
    }

    @Override
    protected ApplicationServiceMethodParameter removeDomainObjectById(ApplicationServiceImplementCodegenParameter codegenParameter) {
        ApplicationServiceMethodParameter serviceMethod = super.removeDomainObjectById(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        DomainServiceParameters domainServices = codegenParameter.getDomainServices();
        DomainServiceParameter masterDomainServiceParameter = domainServices.getMasterDomainServiceParameter();
        String domainObjectIdName = codegenParameter.getDomainObjectParameter().getDomainObjectIdName();
        //删除Master
        methodBodyLines.add(String.format("%s.%s(%s);", masterDomainServiceParameter.getDomainServiceInstanceName(), masterDomainServiceParameter.getDomainServiceCodegenParameter().getRemoveDomainObjectById().getMethodName(), domainObjectIdName));
        //删除Slaves
        Map<String,DomainAggregateFieldConfig> domainAggregateFieldConfigs = getDomainObjectConfig().getDomainAggregateFields();
        List<DomainServiceParameter> slaveDomainServiceParameters = domainServices.getSlaveDomainServiceParameters();
        for(DomainServiceParameter slaveDomainServiceParameter : slaveDomainServiceParameters) {
            DomainAggregateSlaveConfig domainAggregateSlaveConfig = domainAggregateFieldConfigs.get(slaveDomainServiceParameter.getDomainEntityConfig().getDomainEntityName()).getDomainAggregateSlaveConfig();
            ByMasterIdDomainServiceMethodParameter removeByMasterIdDomainServiceMethod = slaveDomainServiceParameter.getDomainServiceCodegenParameter().getRemoveDomainObjectsByXxxMasterId().get(domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfSlave());
            methodBodyLines.add(String.format("%s.%s(%s);", slaveDomainServiceParameter.getDomainServiceInstanceName(), removeByMasterIdDomainServiceMethod.getMethodName(), domainObjectIdName));
        }
        serviceMethod.setMethodBodyLines(methodBodyLines);
        return serviceMethod;
    }

    @Override
    protected ApplicationServiceMethodParameter removeDomainObjectsByIds(ApplicationServiceImplementCodegenParameter codegenParameter) {
        ApplicationServiceMethodParameter serviceMethod = super.removeDomainObjectsByIds(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        DomainServiceParameters domainServices = codegenParameter.getDomainServices();
        DomainServiceParameter masterDomainServiceParameter = domainServices.getMasterDomainServiceParameter();
        methodBodyLines.add(String.format("for(%s id : %s) {", codegenParameter.getDomainObjectParameter().getDomainObjectIdType(), codegenParameter.getDomainObjectParameter().getDomainObjectIdsName()));
        methodBodyLines.add(String.format("    %s(id);", codegenParameter.getRemoveDomainObjectById().getMethodName()));
        methodBodyLines.add("}");
        serviceMethod.setMethodBodyLines(methodBodyLines);
        return serviceMethod;
    }

    @Override
    protected ApplicationServiceMethodParameter getDomainObjectById(ApplicationServiceImplementCodegenParameter codegenParameter) {
        ApplicationServiceMethodParameter serviceMethod = super.getDomainObjectById(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        DomainServiceParameters domainServices = codegenParameter.getDomainServices();
        DomainServiceParameter masterDomainServiceParameter = domainServices.getMasterDomainServiceParameter();
        String domainObjectIdName = codegenParameter.getDomainObjectParameter().getDomainObjectIdName();
        //获取Master
        DomainServiceMethodParameter getMasterByIdDomainServiceMethod = masterDomainServiceParameter.getDomainServiceCodegenParameter().getGetDomainObjectById();
        String masterDomainObjectVariableName = masterDomainServiceParameter.getDomainServiceCodegenParameter().getDomainObjectParameter().getLowerDomainObjectName();
        //ProductBaseInfo productInfo = productBaseInfoService.getProductBaseById(id);
        methodBodyLines.add(String.format("%s %s = %s.%s(%s);", getMasterByIdDomainServiceMethod.getMethodReturnType(), masterDomainObjectVariableName, masterDomainServiceParameter.getDomainServiceInstanceName(), getMasterByIdDomainServiceMethod.getMethodName(), codegenParameter.getDomainObjectParameter().getDomainObjectIdName()));
        //级联获取Slaves
        //if(productInfo != null) {
        methodBodyLines.add(String.format("if(%s != null) {", masterDomainObjectVariableName));
        String domainObjectVariableName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectName();
        //ProductAggregate product = BeanCopier.copy(productInfo, ProductAggregate::new);
        methodBodyLines.add(String.format("    %s %s = BeanCopier.copy(%s, %s::new);", codegenParameter.getDomainObjectParameter().getDomainObjectName(), domainObjectVariableName, masterDomainObjectVariableName, codegenParameter.getDomainObjectParameter().getDomainObjectName()));
        methodBodyLines.add("    if(cascade) {");
        Map<String,DomainAggregateFieldConfig> domainAggregateFieldConfigs = getDomainObjectConfig().getDomainAggregateFields();
        List<DomainServiceParameter> slaveDomainServiceParameters = domainServices.getSlaveDomainServiceParameters();
        for(DomainServiceParameter slaveDomainServiceParameter : slaveDomainServiceParameters) {
            DomainAggregateFieldConfig domainAggregateSlaveFieldConfig = domainAggregateFieldConfigs.get(slaveDomainServiceParameter.getDomainEntityConfig().getDomainEntityName());
            DomainAggregateSlaveConfig domainAggregateSlaveConfig = domainAggregateSlaveFieldConfig.getDomainAggregateSlaveConfig();
            ByMasterIdDomainServiceMethodParameter getByMasterIdDomainServiceMethod = slaveDomainServiceParameter.getDomainServiceCodegenParameter().getGetDomainObjectsByXxxMasterId().get(domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfSlave());
            //product.setProductExtra(productExtraInfoService.getProductExtraById(id));
            methodBodyLines.add(String.format("        %s.%s(%s.%s(%s));", domainObjectVariableName, domainAggregateSlaveFieldConfig.getFieldSetterName(), slaveDomainServiceParameter.getDomainServiceInstanceName(), getByMasterIdDomainServiceMethod.getMethodName(), domainObjectIdName));
        }
        methodBodyLines.add("    }");
        methodBodyLines.add("}");
        //return null;
        methodBodyLines.add("return null;");
        serviceMethod.setMethodBodyLines(methodBodyLines);
        return serviceMethod;
    }

    @Override
    protected ApplicationServiceMethodParameter getDomainObjectsByIds(ApplicationServiceImplementCodegenParameter codegenParameter) {
        ApplicationServiceMethodParameter serviceMethod = super.getDomainObjectsByIds(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        DomainServiceParameters domainServices = codegenParameter.getDomainServices();
        DomainServiceParameter masterDomainServiceParameter = domainServices.getMasterDomainServiceParameter();
        DomainServiceMethodParameter getByIdsDomainServiceMethod = masterDomainServiceParameter.getDomainServiceCodegenParameter().getGetDomainObjectsByIds();
        String masterDomainObjectVariableName = masterDomainServiceParameter.getDomainServiceCodegenParameter().getDomainObjectParameter().getLowerDomainObjectsName();
        //List<ProductBaseInfo> productBases = productBaseInfoService.getProductBasesByIds(ids);
        methodBodyLines.add(String.format("%s %s = %s.%s(%s);", getByIdsDomainServiceMethod.getMethodReturnType(), masterDomainObjectVariableName, masterDomainServiceParameter.getDomainServiceInstanceName(), getByIdsDomainServiceMethod.getMethodName(), masterDomainServiceParameter.getDomainServiceCodegenParameter().getDomainObjectParameter().getDomainObjectIdsName()));
        //return prepareProducts(productBases, cascade);
        methodBodyLines.add(String.format("return %s(%s, cascade);", codegenParameter.getPrepareAggregateObjects().getMethodName(), masterDomainObjectVariableName));
        serviceMethod.setMethodBodyLines(methodBodyLines);
        return serviceMethod;
    }

    @Override
    protected ApplicationServiceMethodParameter getDomainObjectsByPage(ApplicationServiceImplementCodegenParameter codegenParameter) {
        ApplicationServiceMethodParameter serviceMethod = super.getDomainObjectsByPage(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        DomainServiceParameters domainServices = codegenParameter.getDomainServices();
        DomainServiceParameter masterDomainServiceParameter = domainServices.getMasterDomainServiceParameter();
        DomainServiceMethodParameter getByPageDomainServiceMethod = masterDomainServiceParameter.getDomainServiceCodegenParameter().getGetDomainObjectsByIds();
        String masterDomainObjectVariableName = masterDomainServiceParameter.getDomainServiceCodegenParameter().getDomainObjectParameter().getLowerDomainObjectsName();
        //List<ProductBaseInfo> productBases = productBaseInfoService.getProductBasesByPage(condition, page);
        methodBodyLines.add(String.format("%s %s = %s.%s(condition, page);", getByPageDomainServiceMethod.getMethodReturnType(), masterDomainObjectVariableName, masterDomainServiceParameter.getDomainServiceInstanceName(), getByPageDomainServiceMethod.getMethodName()));
        //return prepareProducts(productBases, cascade);
        methodBodyLines.add(String.format("return %s(%s, cascade);", codegenParameter.getPrepareAggregateObjects().getMethodName(), masterDomainObjectVariableName));
        serviceMethod.setMethodBodyLines(methodBodyLines);
        return serviceMethod;
    }

    protected ApplicationServiceMethodParameter prepareAggregateObjects(ApplicationServiceImplementCodegenParameter codegenParameter) {
        DomainAggregateConfig domainAggregateConfig = getDomainObjectConfig();
        ApplicationServiceMethodParameter serviceMethod = new ApplicationServiceMethodParameter();
        serviceMethod.setActivated(true);
        serviceMethod.setMethodReturnType("List<" + codegenParameter.getDomainObjectParameter().getDomainObjectName() + ">");
        serviceMethod.setMethodName("prepare" + codegenParameter.getDomainObjectParameter().getDomainObjectsAlias());

        List<String> methodBodyLines = new ArrayList<>();
        DomainServiceParameters domainServices = codegenParameter.getDomainServices();
        DomainServiceParameter masterDomainServiceParameter = domainServices.getMasterDomainServiceParameter();
        String masterDomainObjectVariableName = masterDomainServiceParameter.getDomainServiceCodegenParameter().getDomainObjectParameter().getLowerDomainObjectsName();
        //if(!CollectionUtils.isEmpty(productBases)) {
        methodBodyLines.add(String.format("if(!CollectionUtils.isEmpty(%s)) {", masterDomainObjectVariableName));
        String domainObjectsVariableName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectsName();
        //List<ProductAggregate> products = BeanCopier.copy(productBases, ProductAggregate::new);
        methodBodyLines.add(String.format("    %s %s = BeanCopier.copy(%s, %s::new);", serviceMethod.getMethodReturnType(), domainObjectsVariableName, masterDomainObjectVariableName, codegenParameter.getDomainObjectParameter().getDomainObjectName()));
        methodBodyLines.add("    if(cascade) {");
        DomainEntityFieldConfig masterEntityIdFieldConfig = masterDomainServiceParameter.getDomainEntityConfig().getSingleIdField();
        String masterDomainObjectIdsName = CodegenUtils.getPluralNameOfDomainObject(masterEntityIdFieldConfig.getFieldName());
        String masterDomainObjectIdGetterName = masterEntityIdFieldConfig.getFieldGetterName();
        //List<Long> productIds = productBases.stream().map(ProductBaseInfo::getProductId).collect(Collectors.toList());
        methodBodyLines.add(String.format("        List<%s> %s = %s.stream().map(%s::%s).collect(Collectors.toList());", masterEntityIdFieldConfig.getFieldType().getShortName(), masterDomainObjectIdsName, masterDomainObjectVariableName, masterDomainServiceParameter.getDomainEntityConfig().getDomainEntityName(), masterDomainObjectIdGetterName));
        Map<String,DomainAggregateFieldConfig> domainAggregateFieldConfigs = getDomainObjectConfig().getDomainAggregateFields();
        List<DomainServiceParameter> slaveDomainServiceParameters = domainServices.getSlaveDomainServiceParameters();
        List<String> setSlaveDomainObjects = new ArrayList<>();
        String aggregateVariableName = codegenParameter.getDomainObjectParameter().getLowerDomainObjectName();
        for(DomainServiceParameter slaveDomainServiceParameter : slaveDomainServiceParameters) {
            DomainObjectParameter slaveDomainObjectParameter = slaveDomainServiceParameter.getDomainServiceCodegenParameter().getDomainObjectParameter();
            DomainAggregateFieldConfig domainAggregateSlaveFieldConfig = domainAggregateFieldConfigs.get(slaveDomainServiceParameter.getDomainEntityConfig().getDomainEntityName());
            DomainAggregateSlaveConfig domainAggregateSlaveConfig = domainAggregateSlaveFieldConfig.getDomainAggregateSlaveConfig();
            ByMasterIdDomainServiceMethodParameter getByMasterIdsDomainServiceMethod = slaveDomainServiceParameter.getDomainServiceCodegenParameter().getGetDomainObjectsByXxxMasterIds().get(domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfSlave());
            //Map<Long,ProductExtraInfo> productExtras = productExtraInfoService.getProductExtrasByIds(productIds);
            methodBodyLines.add(String.format("        %s %s = %s.%s(%s);", getByMasterIdsDomainServiceMethod.getMethodReturnType(), slaveDomainObjectParameter.getLowerDomainObjectsName(), slaveDomainServiceParameter.getDomainServiceInstanceName(), getByMasterIdsDomainServiceMethod.getMethodName(), masterDomainObjectIdsName));

            //product.setProductExtra(productExtras.get(product.getProductId()));
            setSlaveDomainObjects.add(String.format("            %s.%s(%s.get(%s.%s()));", aggregateVariableName, domainAggregateSlaveFieldConfig.getFieldSetterName(), slaveDomainObjectParameter.getLowerDomainObjectsName(), aggregateVariableName, masterDomainObjectIdGetterName));
        }
        //for(ProductAggregate product : products) {
        methodBodyLines.add(String.format("        for(%s %s : %s) {", codegenParameter.getDomainObjectParameter().getDomainObjectName(), aggregateVariableName, domainObjectsVariableName));
        methodBodyLines.addAll(setSlaveDomainObjects);
        methodBodyLines.add("        }");
        methodBodyLines.add("    }");
        methodBodyLines.add(String.format("    return %s;", domainObjectsVariableName));
        methodBodyLines.add("}");
        methodBodyLines.add("return Collections.emptyList();");
        serviceMethod.setMethodBodyLines(methodBodyLines);
        return serviceMethod;
    }

    @Override
    protected ApplicationServiceMethodParameter getDomainObjectTotalCount(ApplicationServiceImplementCodegenParameter codegenParameter) {
        ApplicationServiceMethodParameter serviceMethod = super.getDomainObjectTotalCount(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        DomainServiceParameters domainServices = codegenParameter.getDomainServices();
        DomainServiceParameter masterDomainServiceParameter = domainServices.getMasterDomainServiceParameter();
        methodBodyLines.add(String.format("return %s.%s();", masterDomainServiceParameter.getDomainServiceInstanceName(), masterDomainServiceParameter.getDomainServiceCodegenParameter().getGetDomainObjectTotalCount().getMethodName()));
        serviceMethod.setMethodBodyLines(methodBodyLines);
        return serviceMethod;
    }

    @Override
    protected ApplicationServiceMethodParameter forEachDomainObject1(ApplicationServiceImplementCodegenParameter codegenParameter) {
        ApplicationServiceMethodParameter serviceMethod = super.forEachDomainObject1(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        DomainServiceParameters domainServices = codegenParameter.getDomainServices();
        DomainServiceParameter masterDomainServiceParameter = domainServices.getMasterDomainServiceParameter();
        DomainServiceMethodParameter forEach1DomainServiceMethod = masterDomainServiceParameter.getDomainServiceCodegenParameter().getForEachDomainObject1();
        String masterDomainObjectVariableName = masterDomainServiceParameter.getDomainServiceCodegenParameter().getDomainObjectParameter().getLowerDomainObjectName();
        methodBodyLines.add(String.format("%s.%s(%s -> consumer.accept(BeanCopier.copy(%s, %s::new)));", masterDomainServiceParameter.getDomainServiceInstanceName(), forEach1DomainServiceMethod.getMethodName(), masterDomainObjectVariableName, masterDomainObjectVariableName, codegenParameter.getDomainObjectParameter().getDomainObjectName()));
        serviceMethod.setMethodBodyLines(methodBodyLines);
        return serviceMethod;
    }

    @Override
    protected ApplicationServiceMethodParameter forEachDomainObject2(ApplicationServiceImplementCodegenParameter codegenParameter) {
        ApplicationServiceMethodParameter serviceMethod = super.forEachDomainObject2(codegenParameter);
        List<String> methodBodyLines = new ArrayList<>();
        DomainServiceParameters domainServices = codegenParameter.getDomainServices();
        DomainServiceParameter masterDomainServiceParameter = domainServices.getMasterDomainServiceParameter();
        DomainServiceMethodParameter forEach2DomainServiceMethod = masterDomainServiceParameter.getDomainServiceCodegenParameter().getForEachDomainObject2();
        String masterDomainObjectVariableName = masterDomainServiceParameter.getDomainServiceCodegenParameter().getDomainObjectParameter().getLowerDomainObjectName();
        methodBodyLines.add(String.format("%s.%s((%s, index) -> consumer.accept(BeanCopier.copy(%s, %s::new), index));", masterDomainServiceParameter.getDomainServiceInstanceName(), forEach2DomainServiceMethod.getMethodName(), masterDomainObjectVariableName, masterDomainObjectVariableName, codegenParameter.getDomainObjectParameter().getDomainObjectName()));
        serviceMethod.setMethodBodyLines(methodBodyLines);
        return serviceMethod;
    }

    @Override
    protected String getTargetTemplateName() {
        return "ApplicationServiceImplement.ftl";
    }

}
