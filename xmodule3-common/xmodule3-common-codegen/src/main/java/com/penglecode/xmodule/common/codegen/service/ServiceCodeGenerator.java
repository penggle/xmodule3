package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.ModuleCodeGenerator;
import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.CodegenModule;
import com.penglecode.xmodule.common.codegen.support.DomainMasterSlaveRelation;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.codegen.support.TemplateParameter;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.domain.ID;
import com.penglecode.xmodule.common.domain.Page;
import com.penglecode.xmodule.common.mybatis.dsl.LambdaQueryCriteria;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import com.penglecode.xmodule.common.support.*;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.mybatis.MybatisHelper;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.boot.autoconfigure.dal.DalComponentUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;

/**
 * Service代码生成器
 * 专门用于生成Service接口及其实现类的代码生成器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/8/25 8:52
 */
public class ServiceCodeGenerator extends ModuleCodeGenerator<ServiceCodegenConfigProperties> {

    public ServiceCodeGenerator(String bizModule) {
        super(bizModule);
    }

    @Override
    protected void generateCodes() throws Exception {
        ServiceCodegenConfigProperties codegenConfig = getModuleCodegenConfig();
        Map<String, DomainObjectConfigProperties> domainObjectConfigs = codegenConfig.getDomain().getDomainObjects();
        if(!CollectionUtils.isEmpty(domainObjectConfigs)) {
            for(Map.Entry<String,DomainObjectConfigProperties> entry : domainObjectConfigs.entrySet()) {
                DomainObjectConfigProperties domainObjectConfig = entry.getValue();
                //1、生成领域服务XxxService.java接口
                DomainBoundedTargetConfigProperties<ServiceInterfaceConfigProperties> targetConfig1 = new DomainBoundedTargetConfigProperties<>(codegenConfig.getService().getDomain().getInterfaceConfig(), domainObjectConfig, null);
                generateCode(codegenConfig, targetConfig1, createTemplateParameter4DomainServiceInterface(codegenConfig, targetConfig1));
                //2、生成领域服务XxxServiceImpl.java实现
                DomainBoundedTargetConfigProperties<ServiceImplementConfigProperties> targetConfig2 = new DomainBoundedTargetConfigProperties<>(codegenConfig.getService().getDomain().getImplementConfig(), domainObjectConfig, null);
                generateCode(codegenConfig, targetConfig2, createTemplateParameter4DomainServiceImplement(codegenConfig, targetConfig2));
            }
        }
        Map<String,DomainAggregateConfigProperties> domainAggregateConfigs = codegenConfig.getDomain().getDomainAggregates();
        if(!CollectionUtils.isEmpty(domainAggregateConfigs)) {
            for(Map.Entry<String,DomainAggregateConfigProperties> entry : domainAggregateConfigs.entrySet()) {
                DomainAggregateConfigProperties domainAggregateConfig = entry.getValue();
                //3、生成应用服务XxxAppService.java接口(如果需要的话)
                DomainBoundedTargetConfigProperties<ServiceInterfaceConfigProperties> targetConfig3 = new DomainBoundedTargetConfigProperties<>(codegenConfig.getService().getApp().getInterfaceConfig(), codegenConfig.getDomain().getDomainObjects().get(domainAggregateConfig.getAggregateMasterName()), domainAggregateConfig);
                generateCode(codegenConfig, targetConfig3, createTemplateParameter4AppServiceInterface(codegenConfig, targetConfig3));
                //4、生成应用服务XxxAppServiceImpl.java实现(如果需要的话)
                DomainBoundedTargetConfigProperties<ServiceImplementConfigProperties> targetConfig4 = new DomainBoundedTargetConfigProperties<>(codegenConfig.getService().getApp().getImplementConfig(), codegenConfig.getDomain().getDomainObjects().get(domainAggregateConfig.getAggregateMasterName()), domainAggregateConfig);
                generateCode(codegenConfig, targetConfig4, createTemplateParameter4AppServiceImplement(codegenConfig, targetConfig4));
            }
        }
    }

    protected TemplateParameter createTemplateParameter4DomainServiceInterface(ServiceCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<ServiceInterfaceConfigProperties> targetConfig) {
        Set<FullyQualifiedJavaType> allImportedTypes = new HashSet<>();
        TemplateParameter templateParameter = createDomainServiceCommonTemplateParameter(codegenConfig, targetConfig, "DomainServiceInterface.ftl", allImportedTypes);
        if(targetConfig.getDomainObjectConfig().getDomainMasterSlaveConfig() != null) { //处理动态生成getSlaveByMasterId()|removeSlaveByMasterId()方法
            Map<String,Object> commonParameter = collectXxxSlaveByMasterIdOperationParameter(targetConfig.getDomainObjectConfig().getDomainMasterSlaveConfig());
            templateParameter.put("getSlaveByMasterIdParameter", commonParameter);
            templateParameter.put("removeSlaveByMasterIdParameter", commonParameter);
            templateParameter.put("getSlaveByMasterIdsParameter", collectGetSlaveByMasterIdsOperationParameter(targetConfig.getDomainObjectConfig().getDomainMasterSlaveConfig()));
            allImportedTypes.add(new FullyQualifiedJavaType(Map.class.getName()));
        }
        calculateImportedTypes(allImportedTypes, templateParameter);
        return templateParameter;
    }

    protected TemplateParameter createTemplateParameter4AppServiceInterface(ServiceCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<ServiceInterfaceConfigProperties> targetConfig) {
        Set<FullyQualifiedJavaType> allImportedTypes = new HashSet<>();
        TemplateParameter templateParameter = createAppServiceCommonTemplateParameter(codegenConfig, targetConfig, "AppServiceInterface.ftl", allImportedTypes);
        calculateImportedTypes(allImportedTypes, templateParameter);
        return templateParameter;
    }

    protected TemplateParameter createTemplateParameter4DomainServiceImplement(ServiceCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<ServiceImplementConfigProperties> targetConfig) {
        Set<FullyQualifiedJavaType> allImportedTypes = new HashSet<>();
        TemplateParameter templateParameter = createDomainServiceCommonTemplateParameter(codegenConfig, targetConfig, "DomainServiceImplement.ftl", allImportedTypes);
        String serviceInterfaceName = codegenConfig.getService().getDomain().getInterfaceConfig().getGeneratedTargetName(targetConfig, false, false);
        templateParameter.put("serviceInterfaceName", serviceInterfaceName);
        templateParameter.put("serviceBeanName", CodegenUtils.lowerCaseFirstChar(serviceInterfaceName));
        String mapperInterfaceName = codegenConfig.getMybatis().getJavaMapperConfig().getGeneratedTargetName(targetConfig, false, false);
        templateParameter.put("mapperInterfaceName", mapperInterfaceName);
        templateParameter.put("mapperBeanName", targetConfig.getDomainObjectConfig().getRuntimeDataSource() + mapperInterfaceName);
        templateParameter.put("mapperInstanceName", Character.toLowerCase(mapperInterfaceName.charAt(0)) + mapperInterfaceName.substring(1));
        templateParameter.put("transactionManagerName", targetConfig.getDomainObjectConfig().getRuntimeDataSource() + "DataSourceTransactionManager");
        addRequiredImportedTypes4DomainServiceImplement(codegenConfig, targetConfig, allImportedTypes);

        DomainServiceCodegenContext codegenContext = new DomainServiceCodegenContext(codegenConfig, targetConfig, templateParameter, allImportedTypes);
        processCreateOperationCode4DomainService(codegenContext);
        processBatchCreateOperationCode4DomainService(codegenContext);
        processModifyByIdOperationCode4DomainService(codegenContext);
        processBatchModifyOperationCode4DomainService(codegenContext);
        processRemoveByIdOperationCode4DomainService(codegenContext);
        processRemoveByIdsOperationCode4DomainService(codegenContext);
        processGetByMasterIdOperationCode4DomainService(codegenContext);
        processGetByMasterIdsOperationCode4DomainService(codegenContext);
        processRemoveByMasterIdOperationCode4DomainService(codegenContext);
        processPageListOperationCode4DomainService(codegenContext);

        calculateImportedTypes(allImportedTypes, templateParameter);
        return templateParameter;
    }

    protected void addRequiredImportedTypes4DomainServiceImplement(ServiceCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<ServiceImplementConfigProperties> targetConfig, Set<FullyQualifiedJavaType> allImportedTypes) {
        addRequiredImportedTypes4ServiceImplement(codegenConfig, targetConfig, allImportedTypes);
        allImportedTypes.add(new FullyQualifiedJavaType(codegenConfig.getService().getDomain().getInterfaceConfig().getGeneratedTargetName(targetConfig, true, false)));
        allImportedTypes.add(new FullyQualifiedJavaType(codegenConfig.getMybatis().getJavaMapperConfig().getGeneratedTargetName(targetConfig, true, false)));
        allImportedTypes.add(new FullyQualifiedJavaType(MybatisHelper.class.getName()));
        allImportedTypes.add(new FullyQualifiedJavaType(ObjectUtils.class.getName()));
    }

    protected void addRequiredImportedTypes4AppServiceImplement(AppServiceCodegenContext codegenContext) {
        addRequiredImportedTypes4ServiceImplement(codegenContext.getCodegenConfig(), codegenContext.getTargetConfig(), codegenContext.getAllImportedTypes());
        codegenContext.getAllImportedTypes().add(new FullyQualifiedJavaType(codegenContext.getCodegenConfig().getService().getApp().getInterfaceConfig().getGeneratedTargetName(codegenContext.getTargetConfig(), true, false)));
        codegenContext.getAllImportedTypes().add(new FullyQualifiedJavaType(AppServiceHelper.class.getName()));
    }

    protected void addRequiredImportedTypes4ServiceImplement(ServiceCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<ServiceImplementConfigProperties> targetConfig, Set<FullyQualifiedJavaType> allImportedTypes) {
        allImportedTypes.add(new FullyQualifiedJavaType(Service.class.getName()));
        allImportedTypes.add(new FullyQualifiedJavaType(Resource.class.getName()));
        allImportedTypes.add(new FullyQualifiedJavaType(Transactional.class.getName()));
        allImportedTypes.add(new FullyQualifiedJavaType(Exception.class.getName()));
        allImportedTypes.add(new FullyQualifiedJavaType(MessageSupplier.class.getName()));
        allImportedTypes.add(new FullyQualifiedJavaType(ValidationAssert.class.getName()));
        allImportedTypes.add(new FullyQualifiedJavaType(BeanValidator.class.getName()));
        allImportedTypes.add(new FullyQualifiedJavaType(CollectionUtils.class.getName()));
        allImportedTypes.add(new FullyQualifiedJavaType(Collections.class.getName()));
        allImportedTypes.add(new FullyQualifiedJavaType(Cursor.class.getName()));
    }

    /**
     * 1、处理createXxx()方法
     *
     * @param codegenContext
     */
    protected void processCreateOperationCode4DomainService(DomainServiceCodegenContext codegenContext) {
        List<String> createOperationCodes = new ArrayList<>();
        createOperationCodes.add(String.format("ValidationAssert.notNull(%s, MessageSupplier.of(\"message.parameter.required\", \"%s\"));", codegenContext.getTemplateParameter().getDomainObjectVarName(), codegenContext.getTemplateParameter().getDomainObjectVarName()));
        if(codegenContext.getDefaultCreateTimeColumn() != null && codegenContext.getDefaultCreateTimeColumn().isColumnOnInsert()) {
            codegenContext.setDefaultCreateTimeFieldName(CodegenUtils.upperCaseFirstChar(codegenContext.getDefaultCreateTimeColumn().getIntrospectedColumn().getJavaFieldName()));
            createOperationCodes.add(String.format("%s.set%s(StringUtils.defaultIfBlank(%s.get%s(), DateTimeUtils.formatNow()));", codegenContext.getTemplateParameter().getDomainObjectVarName(), codegenContext.getDefaultCreateTimeFieldName(), codegenContext.getTemplateParameter().getDomainObjectVarName(), codegenContext.getDefaultCreateTimeFieldName()));
            codegenContext.getAllImportedTypes().add(new FullyQualifiedJavaType(StringUtils.class.getName()));
            codegenContext.getAllImportedTypes().add(new FullyQualifiedJavaType(DateTimeUtils.class.getName()));
        }
        if(codegenContext.getDefaultUpdateTimeColumn() != null && codegenContext.getDefaultUpdateTimeColumn().isColumnOnInsert()) {
            codegenContext.setDefaultUpdateTimeFieldName(CodegenUtils.upperCaseFirstChar(codegenContext.getDefaultUpdateTimeColumn().getIntrospectedColumn().getJavaFieldName()));
            if(codegenContext.getDefaultCreateTimeFieldName() != null) {
                createOperationCodes.add(String.format("%s.set%s(%s.get%s());", codegenContext.getTemplateParameter().getDomainObjectVarName(), codegenContext.getDefaultUpdateTimeFieldName(), codegenContext.getTemplateParameter().getDomainObjectVarName(), codegenContext.getDefaultCreateTimeFieldName()));
            } else {
                createOperationCodes.add(String.format("%s.set%s(DateTimeUtils.formatNow());", codegenContext.getTemplateParameter().getDomainObjectVarName(), codegenContext.getDefaultUpdateTimeFieldName()));
            }
            codegenContext.getAllImportedTypes().add(new FullyQualifiedJavaType(DateTimeUtils.class.getName()));
        }

        String createValidateFields = collectBeanValidateFields(codegenContext.getTargetConfig().getDomainObjectConfig(), "create");
        if(StringUtils.isNotBlank(createValidateFields)) {
            createOperationCodes.add(String.format("BeanValidator.validateBean(%s);", codegenContext.getTemplateParameter().getDomainObjectVarName() + createValidateFields));
        }
        codegenContext.getTemplateParameter().put("createOperationCodes", createOperationCodes);
    }

    /**
     * 2、处理batchCreateXxx()方法
     *
     * @param codegenContext
     */
    protected void processBatchCreateOperationCode4DomainService(DomainServiceCodegenContext codegenContext) {
        String opParameterName = codegenContext.getTemplateParameter().getDomainObjectVarName() + "List";
        List<String> batchCreateOperationCodes = new ArrayList<>();
        batchCreateOperationCodes.add(String.format("ValidationAssert.notEmpty(%s, MessageSupplier.of(\"message.parameter.required\", \"%s\"));", opParameterName, opParameterName));
        codegenContext.getTemplateParameter().put("batchCreateOperationCodes", batchCreateOperationCodes);
    }

    /**
     * 3、处理modifyXxxById()方法
     *
     * @param codegenContext
     */
    protected void processModifyByIdOperationCode4DomainService(DomainServiceCodegenContext codegenContext) {
        List<String> modifyByIdOperationCodes = new ArrayList<>();
        modifyByIdOperationCodes.add(String.format("ValidationAssert.notNull(%s, MessageSupplier.of(\"message.parameter.required\", \"%s\"));", codegenContext.getTemplateParameter().getDomainObjectVarName(), codegenContext.getTemplateParameter().getDomainObjectVarName()));
        if(codegenContext.getDefaultUpdateTimeColumn() != null && codegenContext.getDefaultUpdateTimeColumn().isColumnOnUpdate()) {
            codegenContext.setDefaultUpdateTimeFieldName(CodegenUtils.upperCaseFirstChar(codegenContext.getDefaultUpdateTimeColumn().getIntrospectedColumn().getJavaFieldName()));
            modifyByIdOperationCodes.add(String.format("%s.set%s(StringUtils.defaultIfBlank(%s.get%s(), DateTimeUtils.formatNow()));", codegenContext.getTemplateParameter().getDomainObjectVarName(), codegenContext.getDefaultUpdateTimeFieldName(), codegenContext.getTemplateParameter().getDomainObjectVarName(), codegenContext.getDefaultUpdateTimeFieldName()));
        }
        String updateValidateFields = collectBeanValidateFields(codegenContext.getTargetConfig().getDomainObjectConfig(), "modify");
        if(StringUtils.isNotBlank(updateValidateFields)) {
            modifyByIdOperationCodes.add(String.format("BeanValidator.validateBean(%s);", codegenContext.getTemplateParameter().getDomainObjectVarName() + updateValidateFields));
        }
        StringBuilder updateFields = new StringBuilder(String.format("Map<String,Object> updateColumns = MapLambdaBuilder.of(%s)", codegenContext.getTemplateParameter().getDomainObjectVarName())).append("\n");
        Map<String,DomainObjectColumnConfigProperties> domainObjectColumns = codegenContext.getTargetConfig().getDomainObjectConfig().getDomainObjectColumns();
        for(Map.Entry<String,DomainObjectColumnConfigProperties> entry : domainObjectColumns.entrySet()) {
            DomainObjectColumnConfigProperties domainObjectColumn = entry.getValue();
            if(domainObjectColumn.isColumnOnUpdate()) {
                String fieldName = domainObjectColumn.getIntrospectedColumn().getJavaFieldName();
                String fieldType = domainObjectColumn.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters();
                updateFields.append("                .with(").append(codegenContext.getTargetConfig().getDomainObjectConfig().getDomainObjectName()).append("::").append(CodegenUtils.getGetterMethodName(fieldName, fieldType)).append(")").append("\n");
            }
        }
        if(updateFields.length() > 0) {
            updateFields.append("                .build();");
            modifyByIdOperationCodes.add(updateFields.toString());
            codegenContext.getAllImportedTypes().add(new FullyQualifiedJavaType(Map.class.getName()));
            codegenContext.getAllImportedTypes().add(new FullyQualifiedJavaType(MapLambdaBuilder.class.getName()));
        }
        codegenContext.getTemplateParameter().put("modifyByIdOperationCodes", modifyByIdOperationCodes);
    }

    /**
     * 4、处理batchModifyXxxById()方法
     *
     * @param codegenContext
     */
    protected void processBatchModifyOperationCode4DomainService(DomainServiceCodegenContext codegenContext) {
        String opParameterName = codegenContext.getTemplateParameter().getDomainObjectVarName() + "List";
        List<String> batchModifyOperationCodes = new ArrayList<>();
        batchModifyOperationCodes.add(String.format("ValidationAssert.notEmpty(%s, MessageSupplier.of(\"message.parameter.required\", \"%s\"));", opParameterName, opParameterName));
        codegenContext.getTemplateParameter().put("batchModifyOperationCodes", batchModifyOperationCodes);
    }

    /**
     * 5、处理removeXxxById()方法
     *
     * @param codegenContext
     */
    protected void processRemoveByIdOperationCode4DomainService(DomainServiceCodegenContext codegenContext) {
        List<String> removeByIdOperationCodes = new ArrayList<>();
        List<DomainObjectColumnConfigProperties> pkColumns = codegenContext.getTargetConfig().getDomainObjectConfig().getPkColumns();
        if(pkColumns.size() == 1) { //单一主键
            String pkFieldName = pkColumns.get(0).getIntrospectedColumn().getJavaFieldName();
            String pkFieldType = pkColumns.get(0).getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters();
            removeByIdOperationCodes.add(String.format("BeanValidator.validateProperty(id, %s);", codegenContext.getTargetConfig().getDomainObjectConfig().getDomainObjectName() + "::" + CodegenUtils.getGetterMethodName(pkFieldName, pkFieldType)));
        } else {
            removeByIdOperationCodes.add(String.format("BeanValidator.validateMap(id%s);", collectBeanValidateFields(codegenContext.getTargetConfig().getDomainObjectConfig(), "id")));
        }
        codegenContext.getTemplateParameter().put("removeByIdOperationCodes", removeByIdOperationCodes);
    }

    /**
     * 6、处理removeXxxByIds()方法
     *
     * @param codegenContext
     */
    protected void processRemoveByIdsOperationCode4DomainService(DomainServiceCodegenContext codegenContext) {
        String opParameterName = "ids";
        List<String> removeByIdsOperationCodes = new ArrayList<>();
        removeByIdsOperationCodes.add(String.format("ValidationAssert.notEmpty(%s, MessageSupplier.of(\"message.parameter.required\", \"%s\"));", opParameterName, opParameterName));
        codegenContext.getTemplateParameter().put("removeByIdsOperationCodes", removeByIdsOperationCodes);
    }

    /**
     * 收集生成getSlaveByMasterId()/removeSlaveByMasterId()方法的模板参数
     *
     * @param domainMasterSlaveConfig
     * @return
     */
    protected Map<String,Object> collectXxxSlaveByMasterIdOperationParameter(DomainMasterSlaveConfigProperties domainMasterSlaveConfig) {
        DomainAggregateSlaveConfigProperties aggregateObjectSlave = domainMasterSlaveConfig.getDomainAggregateSlaveConfig();
        DomainObjectConfigProperties masterDomainObjectConfig = domainMasterSlaveConfig.getMasterDomainObjectConfig();
        DomainObjectConfigProperties slaveDomainObjectConfig = domainMasterSlaveConfig.getSlaveDomainObjectConfig();
        DomainObjectColumnConfigProperties masterDomainObjectId = domainMasterSlaveConfig.getMasterDomainObjectId();
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("masterDomainObjectTitle", masterDomainObjectConfig.getDomainObjectTitle());
        parameter.put("masterDomainObjectId", aggregateObjectSlave.getMasterSlaveMapping().getRelateFieldNameOfMaster());
        parameter.put("upperMasterDomainObjectId", CodegenUtils.upperCaseFirstChar(aggregateObjectSlave.getMasterSlaveMapping().getRelateFieldNameOfMaster()));
        parameter.put("masterDomainObjectIdType", masterDomainObjectId.getIntrospectedColumn().getJavaFieldType().getShortName());
        parameter.put("slaveDomainObjectTitle", slaveDomainObjectConfig.getDomainObjectTitle() + (DomainMasterSlaveRelation.RELATION_1N.equals(aggregateObjectSlave.getMasterSlaveMapping().getMasterSlaveRelation()) ? "列表" : ""));
        parameter.put("serviceMethodReturnType", DomainMasterSlaveRelation.RELATION_1N.equals(aggregateObjectSlave.getMasterSlaveMapping().getMasterSlaveRelation()) ? "List<" + slaveDomainObjectConfig.getDomainObjectName() + ">" : slaveDomainObjectConfig.getDomainObjectName());
        parameter.put("slaveDomainObjectAlias", DomainMasterSlaveRelation.RELATION_1N.equals(aggregateObjectSlave.getMasterSlaveMapping().getMasterSlaveRelation()) ? slaveDomainObjectConfig.getDomainObjectAlias() + "List" : slaveDomainObjectConfig.getDomainObjectAlias());
        String getMasterDomainObjectIdRef = slaveDomainObjectConfig.getDomainObjectName() + "::" + CodegenUtils.getGetterMethodName(aggregateObjectSlave.getMasterSlaveMapping().getRelateFieldNameOfMaster(), Object.class.getName());
        parameter.put("getMasterDomainObjectIdRef", getMasterDomainObjectIdRef);
        parameter.put("selectSlaveMapperMethod", DomainMasterSlaveRelation.RELATION_1N.equals(aggregateObjectSlave.getMasterSlaveMapping().getMasterSlaveRelation()) ? "selectModelListByCriteria" : "selectModelByCriteria");
        parameter.put("deleteSlaveMapperMethod", "deleteModelByCriteria");
        parameter.put("serviceMethodReturnEmpty", DomainMasterSlaveRelation.RELATION_1N.equals(aggregateObjectSlave.getMasterSlaveMapping().getMasterSlaveRelation()) ? "Collections.emptyList()" : "null");
        return parameter;
    }

    /**
     * 收集生成getSlaveByMasterIds()方法的模板参数
     *
     * @param domainMasterSlaveConfig
     * @return
     */
    protected Map<String,Object> collectGetSlaveByMasterIdsOperationParameter(DomainMasterSlaveConfigProperties domainMasterSlaveConfig) {
        DomainAggregateSlaveConfigProperties aggregateObjectSlave = domainMasterSlaveConfig.getDomainAggregateSlaveConfig();
        DomainObjectConfigProperties masterDomainObjectConfig = domainMasterSlaveConfig.getMasterDomainObjectConfig();
        DomainObjectConfigProperties slaveDomainObjectConfig = domainMasterSlaveConfig.getSlaveDomainObjectConfig();
        DomainObjectColumnConfigProperties masterDomainObjectId = domainMasterSlaveConfig.getMasterDomainObjectId();
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("masterDomainObjectTitle", masterDomainObjectConfig.getDomainObjectTitle());
        parameter.put("masterDomainObjectIds", aggregateObjectSlave.getMasterSlaveMapping().getRelateFieldNameOfMaster() + "s");
        parameter.put("upperMasterDomainObjectIds", CodegenUtils.upperCaseFirstChar(aggregateObjectSlave.getMasterSlaveMapping().getRelateFieldNameOfMaster()) + "s");
        parameter.put("masterDomainObjectIdsType", "List<" + masterDomainObjectId.getIntrospectedColumn().getJavaFieldType().getShortName() + ">");
        parameter.put("slaveDomainObjectTitle", slaveDomainObjectConfig.getDomainObjectTitle());
        parameter.put("serviceMethodReturnType", DomainMasterSlaveRelation.RELATION_1N.equals(aggregateObjectSlave.getMasterSlaveMapping().getMasterSlaveRelation()) ? "Map<" + masterDomainObjectId.getIntrospectedColumn().getJavaFieldType().getShortName() + ",List<" + slaveDomainObjectConfig.getDomainObjectName() + ">>" : "Map<" + masterDomainObjectId.getIntrospectedColumn().getJavaFieldType().getShortName() + "," + slaveDomainObjectConfig.getDomainObjectName() + ">");
        parameter.put("slaveDomainObjectsAlias", slaveDomainObjectConfig.getDomainObjectAlias() + "List");
        String getMasterDomainObjectIdRef = slaveDomainObjectConfig.getDomainObjectName() + "::" + CodegenUtils.getGetterMethodName(aggregateObjectSlave.getMasterSlaveMapping().getRelateFieldNameOfMaster(), Object.class.getName());
        parameter.put("getMasterDomainObjectIdRef", getMasterDomainObjectIdRef);
        parameter.put("lowerSlaveDomainObjectAlias", CodegenUtils.lowerCaseFirstChar(slaveDomainObjectConfig.getDomainObjectAlias()));
        parameter.put("slaveDomainObjectCollector", DomainMasterSlaveRelation.RELATION_1N.equals(aggregateObjectSlave.getMasterSlaveMapping().getMasterSlaveRelation()) ? String.format("Collectors.groupingBy(%s, Collectors.toList())", getMasterDomainObjectIdRef) : String.format("Collectors.toMap(%s, Function.identity())", getMasterDomainObjectIdRef));
        return parameter;
    }

    /**
     * 7、处理动态生成getSlaveByMasterId()方法
     *
     * @param codegenContext
     */
    protected void processGetByMasterIdOperationCode4DomainService(DomainServiceCodegenContext codegenContext) {
        if(codegenContext.getTargetConfig().getDomainObjectConfig().getDomainMasterSlaveConfig() != null) { //处理动态生成getSlaveByMasterId()方法
            codegenContext.getTemplateParameter().put("getSlaveByMasterIdParameter", collectXxxSlaveByMasterIdOperationParameter(codegenContext.getTargetConfig().getDomainObjectConfig().getDomainMasterSlaveConfig()));
        }
    }

    /**
     * 8、处理动态生成getSlaveByMasterIds()方法
     *
     * @param codegenContext
     */
    protected void processGetByMasterIdsOperationCode4DomainService(DomainServiceCodegenContext codegenContext) {
        if(codegenContext.getTargetConfig().getDomainObjectConfig().getDomainMasterSlaveConfig() != null) { //处理动态生成getSlaveByMasterIds()方法
            codegenContext.getTemplateParameter().put("getSlaveByMasterIdsParameter", collectGetSlaveByMasterIdsOperationParameter(codegenContext.getTargetConfig().getDomainObjectConfig().getDomainMasterSlaveConfig()));
            codegenContext.getAllImportedTypes().add(new FullyQualifiedJavaType(Map.class.getName()));
            codegenContext.getAllImportedTypes().add(new FullyQualifiedJavaType(Collectors.class.getName()));
        }
    }

    /**
     * 9、处理动态生成removeSlaveByMasterId()方法
     *
     * @param codegenContext
     */
    protected void processRemoveByMasterIdOperationCode4DomainService(DomainServiceCodegenContext codegenContext) {
        if(codegenContext.getTargetConfig().getDomainObjectConfig().getDomainMasterSlaveConfig() != null) { //处理动态生成getSlaveByMasterId()方法
            codegenContext.getTemplateParameter().put("removeSlaveByMasterIdParameter", collectXxxSlaveByMasterIdOperationParameter(codegenContext.getTargetConfig().getDomainObjectConfig().getDomainMasterSlaveConfig()));
        }
    }

    /**
     * 10、处理getXxxListByPage()方法
     *
     * @param codegenContext
     */
    protected void processPageListOperationCode4DomainService(DomainServiceCodegenContext codegenContext) {
        List<String> pageListOperationCodes = new ArrayList<>();
        StringBuilder criteriaCodes = new StringBuilder(String.format("QueryCriteria<%s> criteria = LambdaQueryCriteria.of(condition)", codegenContext.getTargetConfig().getDomainObjectConfig().getDomainObjectName())).append("\n");
        Map<String,DomainObjectFieldConfigProperties> domainObjectFields = codegenContext.getTargetConfig().getDomainObjectConfig().getDomainObjectFields();
        for(Map.Entry<String,DomainObjectFieldConfigProperties> entry : domainObjectFields.entrySet()) {
            DomainObjectFieldConfigProperties domainObjectField = entry.getValue();
            if(domainObjectField.getQueryConditionOperator() != null) {
                String fieldGetterName = CodegenUtils.getGetterMethodName(domainObjectField.getFieldName(), domainObjectField.getFieldType().getFullyQualifiedNameWithoutTypeParameters());
                if(!domainObjectField.isSupportField()) { //持久化字段
                    criteriaCodes.append("                .").append(domainObjectField.getQueryConditionOperator().getOpName()).append("(").append(codegenContext.getTargetConfig().getDomainObjectConfig().getDomainObjectName()).append("::").append(fieldGetterName).append(")").append("\n");
                } else { //辅助字段
                    DomainObjectColumnConfigProperties refDomainColumn = domainObjectField.getDomainColumnConfig();
                    String refFieldGetterName = CodegenUtils.getGetterMethodName(refDomainColumn.getIntrospectedColumn().getJavaFieldName(), refDomainColumn.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters());
                    criteriaCodes.append("                .").append(domainObjectField.getQueryConditionOperator().getOpName()).append("(").append(codegenContext.getTargetConfig().getDomainObjectConfig().getDomainObjectName()).append("::").append(refFieldGetterName).append(", condition.").append(fieldGetterName).append("())").append("\n");
                }
            }
        }
        criteriaCodes.append("                .dynamic(true)").append("\n");
        criteriaCodes.append("                .orderBy(page.getOrders());");
        pageListOperationCodes.add(criteriaCodes.toString());
        codegenContext.getTemplateParameter().put("pageListOperationCodes", pageListOperationCodes);
        codegenContext.getAllImportedTypes().add(new FullyQualifiedJavaType(QueryCriteria.class.getName()));
        codegenContext.getAllImportedTypes().add(new FullyQualifiedJavaType(LambdaQueryCriteria.class.getName()));
        codegenContext.getAllImportedTypes().add(new FullyQualifiedJavaType(MybatisHelper.class.getName()));
    }

    protected TemplateParameter createTemplateParameter4AppServiceImplement(ServiceCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<ServiceImplementConfigProperties> targetConfig) {
        Set<FullyQualifiedJavaType> allImportedTypes = new HashSet<>();
        TemplateParameter templateParameter = createAppServiceCommonTemplateParameter(codegenConfig, targetConfig, "AppServiceImplement.ftl", allImportedTypes);

        AppServiceCodegenContext codegenContext = buildAppServiceCodegenContext(codegenConfig, targetConfig, templateParameter, allImportedTypes);
        String serviceInterfaceName = codegenConfig.getService().getApp().getInterfaceConfig().getGeneratedTargetName(targetConfig, false, false);
        templateParameter.put("serviceInterfaceName", serviceInterfaceName);
        templateParameter.put("serviceBeanName", CodegenUtils.lowerCaseFirstChar(serviceInterfaceName));
        templateParameter.put("domainServices", buildAppServiceDomainServicesParameter(codegenContext));
        templateParameter.put("transactionManagerName", DalComponentUtils.genBeanNameOfType(targetConfig.getDomainObjectConfig().getRuntimeDataSource(), TransactionManager.class));
        addRequiredImportedTypes4AppServiceImplement(codegenContext);

        processCreateOperationCode4AppService(codegenContext);
        processModifyByIdOperationCode4AppService(codegenContext);
        processRemoveByIdOperationCode4AppService(codegenContext);
        /*processRemoveByIdsOperationCode4DomainService(codegenContext);
        processGetByMasterIdOperationCode4DomainService(codegenContext);
        processGetByMasterIdsOperationCode4DomainService(codegenContext);
        processRemoveByMasterIdOperationCode4DomainService(codegenContext);
        processPageListOperationCode4DomainService(codegenContext);*/

        calculateImportedTypes(allImportedTypes, templateParameter);
        return templateParameter;
    }

    protected AppServiceCodegenContext buildAppServiceCodegenContext(ServiceCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<ServiceImplementConfigProperties> targetConfig, TemplateParameter templateParameter, Set<FullyQualifiedJavaType> allImportedTypes) {
        DomainAggregateConfigProperties domainAggregateConfig = targetConfig.getDomainAggregateConfig();
        DomainBoundedTargetConfigProperties<ServiceInterfaceConfigProperties> masterTargetConfig = new DomainBoundedTargetConfigProperties<>(codegenConfig.getService().getDomain().getInterfaceConfig(), targetConfig.getDomainObjectConfig(), null);
        MasterDomainServiceConfig masterDomainService = new MasterDomainServiceConfig();
        initDomainServiceConfig(masterDomainService, masterTargetConfig);

        List<SlaveDomainServiceConfig> slaveDomainServices = new ArrayList<>();
        for(DomainAggregateSlaveConfigProperties domainAggregateSlaveConfig : domainAggregateConfig.getAggregateObjectSlaves()) {
            DomainBoundedTargetConfigProperties<ServiceInterfaceConfigProperties> slaveTargetConfig = new DomainBoundedTargetConfigProperties<>(codegenConfig.getService().getDomain().getInterfaceConfig(), codegenConfig.getDomain().getDomainObjects().get(domainAggregateSlaveConfig.getAggregateSlaveName()), null);
            SlaveDomainServiceConfig slaveDomainService = new SlaveDomainServiceConfig();
            initDomainServiceConfig(slaveDomainService, slaveTargetConfig);
            slaveDomainService.setDomainAggregateSlaveConfig(domainAggregateSlaveConfig);
            slaveDomainService.setDomainAggregateFieldConfig(domainAggregateConfig.getAggregateObjectFields().get(slaveTargetConfig.getDomainObjectConfig().getDomainObjectName()));
            slaveDomainServices.add(slaveDomainService);
        }
        return new AppServiceCodegenContext(codegenConfig, targetConfig, masterDomainService, slaveDomainServices, templateParameter, allImportedTypes);
    }

    private void initDomainServiceConfig(DomainServiceConfig domainService, DomainBoundedTargetConfigProperties<ServiceInterfaceConfigProperties> targetConfig) {
        domainService.setDomainObjectName(targetConfig.getDomainObjectConfig().getDomainObjectName());
        domainService.setDomainObjectAlias(targetConfig.getDomainObjectConfig().getDomainObjectAlias());
        domainService.setDomainServiceName(targetConfig.getGeneratedTargetConfig().getGeneratedTargetName(targetConfig, false, false));
        domainService.setDomainServiceBeanName(CodegenUtils.lowerCaseFirstChar(domainService.getDomainServiceName()));
        domainService.setDomainServiceInstanceName(CodegenUtils.lowerCaseFirstChar(domainService.getDomainServiceName()));
        domainService.setDomainServiceType(new FullyQualifiedJavaType(targetConfig.getGeneratedTargetConfig().getGeneratedTargetName(targetConfig, true, false)));
        domainService.setDomainObjectIdType(resolveDomainObjectIdType(targetConfig.getDomainObjectConfig()));
    }

    protected void processCreateOperationCode4AppService(AppServiceCodegenContext codegenContext) {
        List<String> createOperationCodes = new ArrayList<>();
        processSaveOperationCommonCode4AppService(codegenContext, createOperationCodes, "create");
        String aggregateObjectVarName = CodegenUtils.lowerCaseFirstChar(codegenContext.getAggregateObjectAlias());
        MasterDomainServiceConfig masterDomainService = codegenContext.getMasterDomainService();
        createOperationCodes.add(String.format("%s.create%s(%s);", masterDomainService.getDomainServiceInstanceName(), masterDomainService.getDomainObjectAlias(), aggregateObjectVarName));
        List<SlaveDomainServiceConfig> slaveDomainServices = codegenContext.getSlaveDomainServices();
        for(SlaveDomainServiceConfig slaveDomainService : slaveDomainServices) {
            DomainMasterSlaveRelation masterSlaveRelation = slaveDomainService.getDomainAggregateSlaveConfig().getMasterSlaveMapping().getMasterSlaveRelation();
            if(DomainMasterSlaveRelation.RELATION_11.equals(masterSlaveRelation)) { //1:1关系
                createOperationCodes.add(String.format("%s.create%s(%s.%s());", slaveDomainService.getDomainServiceInstanceName(), slaveDomainService.getDomainObjectAlias(), aggregateObjectVarName, CodegenUtils.getGetterMethodName(slaveDomainService.getDomainAggregateFieldConfig().getFieldName(), Object.class.getName())));
            } else if(DomainMasterSlaveRelation.RELATION_1N.equals(masterSlaveRelation)) {
                createOperationCodes.add(String.format("%s.batchCreate%s(%s.%s());", slaveDomainService.getDomainServiceInstanceName(), slaveDomainService.getDomainObjectAlias(), aggregateObjectVarName, CodegenUtils.getGetterMethodName(slaveDomainService.getDomainAggregateFieldConfig().getFieldName(), Object.class.getName())));
            }
        }
        codegenContext.getTemplateParameter().put("createOperationCodes", createOperationCodes);
    }

    protected void processModifyByIdOperationCode4AppService(AppServiceCodegenContext codegenContext) {
        List<String> modifyByIdOperationCodes = new ArrayList<>();
        processSaveOperationCommonCode4AppService(codegenContext, modifyByIdOperationCodes, "modify");
        String aggregateObjectVarName = CodegenUtils.lowerCaseFirstChar(codegenContext.getAggregateObjectAlias());
        MasterDomainServiceConfig masterDomainService = codegenContext.getMasterDomainService();
        modifyByIdOperationCodes.add(String.format("%s.modify%sById(%s);", masterDomainService.getDomainServiceInstanceName(), masterDomainService.getDomainObjectAlias(), aggregateObjectVarName));
        List<SlaveDomainServiceConfig> slaveDomainServices = codegenContext.getSlaveDomainServices();
        for(SlaveDomainServiceConfig slaveDomainService : slaveDomainServices) {
            DomainMasterSlaveRelation masterSlaveRelation = slaveDomainService.getDomainAggregateSlaveConfig().getMasterSlaveMapping().getMasterSlaveRelation();
            if(DomainMasterSlaveRelation.RELATION_11.equals(masterSlaveRelation)) { //1:1关系
                modifyByIdOperationCodes.add(String.format("%s.modify%sById(%s.%s());", slaveDomainService.getDomainServiceInstanceName(), slaveDomainService.getDomainObjectAlias(), aggregateObjectVarName, CodegenUtils.getGetterMethodName(slaveDomainService.getDomainAggregateFieldConfig().getFieldName(), Object.class.getName())));
            } else if(DomainMasterSlaveRelation.RELATION_1N.equals(masterSlaveRelation)) {
                String relateFieldNameOfMaster = slaveDomainService.getDomainAggregateSlaveConfig().getMasterSlaveMapping().getRelateFieldNameOfMaster();
                modifyByIdOperationCodes.add(String.format("List<%s> persistent%s = %s.get%sListBy%s(%s.%s());", slaveDomainService.getDomainObjectName(), CodegenUtils.upperCaseFirstChar(slaveDomainService.getDomainAggregateFieldConfig().getFieldName()), slaveDomainService.getDomainServiceInstanceName(), slaveDomainService.getDomainObjectAlias(), CodegenUtils.upperCaseFirstChar(relateFieldNameOfMaster), aggregateObjectVarName, CodegenUtils.getGetterMethodName(relateFieldNameOfMaster, Object.class.getName())));
                modifyByIdOperationCodes.add(String.format("AppServiceHelper.batchSaveDomainObjects(%s.%s(), persistent%s, %s::identity, %s::batchCreate%s, %s::batchModify%sById, %s::remove%sByIds);", aggregateObjectVarName, CodegenUtils.getGetterMethodName(slaveDomainService.getDomainAggregateFieldConfig().getFieldName(), Object.class.getName()), CodegenUtils.upperCaseFirstChar(slaveDomainService.getDomainAggregateFieldConfig().getFieldName()), slaveDomainService.getDomainObjectName(), slaveDomainService.getDomainServiceInstanceName(), slaveDomainService.getDomainObjectAlias(), slaveDomainService.getDomainServiceInstanceName(), slaveDomainService.getDomainObjectAlias(), slaveDomainService.getDomainServiceInstanceName(), slaveDomainService.getDomainObjectAlias()));
            }
        }
        codegenContext.getTemplateParameter().put("modifyByIdOperationCodes", modifyByIdOperationCodes);
    }

    protected void processRemoveByIdOperationCode4AppService(AppServiceCodegenContext codegenContext) {
        List<String> removeByIdOperationCodes = new ArrayList<>();
        MasterDomainServiceConfig masterDomainService = codegenContext.getMasterDomainService();
        removeByIdOperationCodes.add(String.format("%s.remove%sById(id);", masterDomainService.getDomainServiceInstanceName(), masterDomainService.getDomainObjectAlias()));
        List<SlaveDomainServiceConfig> slaveDomainServices = codegenContext.getSlaveDomainServices();
        for(SlaveDomainServiceConfig slaveDomainService : slaveDomainServices) {
            DomainMasterSlaveRelation masterSlaveRelation = slaveDomainService.getDomainAggregateSlaveConfig().getMasterSlaveMapping().getMasterSlaveRelation();
            if(DomainMasterSlaveRelation.RELATION_11.equals(masterSlaveRelation)) { //1:1关系
                removeByIdOperationCodes.add(String.format("%s.remove%sById(id);", masterDomainService.getDomainServiceInstanceName(), masterDomainService.getDomainObjectAlias()));
            }
        }
    }

    private void processSaveOperationCommonCode4AppService(AppServiceCodegenContext codegenContext, List<String> operationCodes, String type) {
        operationCodes.add(String.format("ValidationAssert.notNull(%s, MessageSupplier.of(\"message.parameter.required\", \"%s\"));", codegenContext.getTemplateParameter().getAggregateObjectVarName(), codegenContext.getTemplateParameter().getAggregateObjectVarName()));
        String createValidateFields = collectBeanValidateFields(codegenContext.getTargetConfig().getDomainAggregateConfig(), type);
        if(StringUtils.isNotBlank(createValidateFields)) {
            operationCodes.add(String.format("BeanValidator.validateBean(%s);", codegenContext.getTemplateParameter().getAggregateObjectVarName() + createValidateFields));
        }
    }

    protected List<Map<String,Object>> buildAppServiceDomainServicesParameter(AppServiceCodegenContext codegenContext) {
        List<Map<String,Object>> domainServices = new ArrayList<>();
        domainServices.add(createDomainServiceParamter4AppService(codegenContext.getMasterDomainService(), codegenContext.getAllImportedTypes()));
        for(DomainServiceConfig slaveDomainService : codegenContext.getSlaveDomainServices()) {
            domainServices.add(createDomainServiceParamter4AppService(slaveDomainService, codegenContext.getAllImportedTypes()));
        }
        return domainServices;
    }

    private Map<String,Object> createDomainServiceParamter4AppService(DomainServiceConfig domainServiceConfig, Set<FullyQualifiedJavaType> allImportedTypes) {
        Map<String,Object> domainService = new HashMap<>();
        domainService.put("domainServiceName", domainServiceConfig.getDomainServiceName());
        domainService.put("domainServiceBeanName", domainServiceConfig.getDomainServiceBeanName());
        domainService.put("domainServiceInstanceName", domainServiceConfig.getDomainServiceInstanceName());
        allImportedTypes.add(domainServiceConfig.getDomainServiceType());
        return domainService;
    }

    protected <T extends GeneratedTargetConfigProperties> TemplateParameter createDomainServiceCommonTemplateParameter(ServiceCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<T> targetConfig, String templateFileName, Set<FullyQualifiedJavaType> allImportedTypes) {
        TemplateParameter templateParameter = new TemplateParameter();
        addCommonTemplateParameter(templateParameter, codegenConfig, targetConfig, templateFileName);
        FullyQualifiedJavaType domainObjectIdType = resolveDomainObjectIdType(targetConfig.getDomainObjectConfig());
        templateParameter.put("domainObjectIdType", domainObjectIdType.getShortName());
        allImportedTypes.add(domainObjectIdType);

        templateParameter.setDomainObjectVarName(CodegenUtils.lowerCaseFirstChar(targetConfig.getDomainObjectConfig().getDomainObjectAlias()));
        allImportedTypes.add(new FullyQualifiedJavaType(targetConfig.getDomainObjectConfig().getGeneratedTargetName(targetConfig, true, false)));
        addCommonImportedTypes(allImportedTypes);
        return templateParameter;
    }

    protected <T extends GeneratedTargetConfigProperties> TemplateParameter createAppServiceCommonTemplateParameter(ServiceCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<T> targetConfig, String templateFileName, Set<FullyQualifiedJavaType> allImportedTypes) {
        TemplateParameter templateParameter = new TemplateParameter();
        addCommonTemplateParameter(templateParameter, codegenConfig, targetConfig, templateFileName);
        FullyQualifiedJavaType aggregateObjectIdType = resolveDomainObjectIdType(targetConfig.getDomainObjectConfig());
        templateParameter.put("aggregateObjectIdType", aggregateObjectIdType.getShortName());
        allImportedTypes.add(aggregateObjectIdType);

        templateParameter.setAggregateObjectName(targetConfig.getDomainAggregateConfig().getAggregateObjectName());
        templateParameter.setAggregateObjectTitle(targetConfig.getDomainAggregateConfig().getAggregateObjectTitle());
        templateParameter.setAggregateObjectAlias(targetConfig.getDomainAggregateConfig().getAggregateObjectAlias());
        templateParameter.setAggregateObjectVarName(CodegenUtils.lowerCaseFirstChar(targetConfig.getDomainAggregateConfig().getAggregateObjectAlias()));
        allImportedTypes.add(new FullyQualifiedJavaType(targetConfig.getDomainAggregateConfig().getGeneratedTargetName(targetConfig, true, false)));
        addCommonImportedTypes(allImportedTypes);
        return templateParameter;
    }

    protected FullyQualifiedJavaType resolveDomainObjectIdType(DomainObjectConfigProperties domainObjectConfig) {
        List<DomainObjectColumnConfigProperties> pkColumns = domainObjectConfig.getPkColumns();
        if(pkColumns.size() == 1) { //单一主键
            return pkColumns.get(0).getIntrospectedColumn().getJavaFieldType();
        } else {
            return new FullyQualifiedJavaType(ID.class.getName());
        }
    }

    protected void addCommonImportedTypes(Set<FullyQualifiedJavaType> allImportedTypes) {
        allImportedTypes.add(new FullyQualifiedJavaType(Page.class.getName()));
        allImportedTypes.add(new FullyQualifiedJavaType(List.class.getName()));
        allImportedTypes.add(new FullyQualifiedJavaType(Consumer.class.getName()));
        allImportedTypes.add(new FullyQualifiedJavaType(ObjIntConsumer.class.getName()));
    }

    protected String collectBeanValidateFields(DomainObjectConfigProperties domainObjectConfig, String type) {
        StringBuilder beanValidateFields = new StringBuilder();
        Map<String,DomainObjectColumnConfigProperties> domainObjectColumns = domainObjectConfig.getDomainObjectColumns();
        for(Map.Entry<String,DomainObjectColumnConfigProperties> entry : domainObjectColumns.entrySet()) {
            DomainObjectColumnConfigProperties domainObjectColumn = entry.getValue();
            if(("create".equals(type) && domainObjectColumn.isValidateOnInsert())
            || ("modify".equals(type) && domainObjectColumn.isValidateOnUpdate())
            || ("id".equals(type) && domainObjectColumn.isPrimaryKey())) {
                String fieldName = domainObjectColumn.getIntrospectedColumn().getJavaFieldName();
                String fieldType = domainObjectColumn.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters();
                beanValidateFields.append(", ").append(domainObjectConfig.getGeneratedTargetName(null, false, false)).append("::").append(CodegenUtils.getGetterMethodName(fieldName, fieldType));
            }
        }
        return beanValidateFields.toString();
    }

    protected String collectBeanValidateFields(DomainAggregateConfigProperties domainAggregateConfig, String type) {
        StringBuilder beanValidateFields = new StringBuilder();
        Map<String,DomainAggregateFieldConfigProperties> aggregateObjectFields = domainAggregateConfig.getAggregateObjectFields();
        for(Map.Entry<String,DomainAggregateFieldConfigProperties> entry : aggregateObjectFields.entrySet()) {
            DomainAggregateFieldConfigProperties aggregateObjectField = entry.getValue();
            if(("create".equals(type) && aggregateObjectField.getDomainAggregateSlaveConfig().isValidateOnInsert())
                    || ("modify".equals(type) && aggregateObjectField.getDomainAggregateSlaveConfig().isValidateOnUpdate())) {
                String fieldName = aggregateObjectField.getFieldName();
                String fieldType = aggregateObjectField.getFieldType().getFullyQualifiedNameWithoutTypeParameters();
                beanValidateFields.append(", ").append(domainAggregateConfig.getGeneratedTargetName(null, false, false)).append("::").append(CodegenUtils.getGetterMethodName(fieldName, fieldType));
            }
        }
        return beanValidateFields.toString();
    }

    @Override
    protected CodegenModule getCodeModule() {
        return CodegenModule.SERVICE;
    }

    class DomainServiceCodegenContext {

        private final ServiceCodegenConfigProperties codegenConfig;

        private final DomainBoundedTargetConfigProperties<ServiceImplementConfigProperties> targetConfig;

        private final DomainObjectColumnConfigProperties defaultCreateTimeColumn;

        private final DomainObjectColumnConfigProperties defaultUpdateTimeColumn;

        private final TemplateParameter templateParameter;

        private final Set<FullyQualifiedJavaType> allImportedTypes;

        private String defaultCreateTimeFieldName;

        private String defaultUpdateTimeFieldName;

        public DomainServiceCodegenContext(ServiceCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<ServiceImplementConfigProperties> targetConfig, TemplateParameter templateParameter, Set<FullyQualifiedJavaType> allImportedTypes) {
            this.codegenConfig = codegenConfig;
            this.targetConfig = targetConfig;
            this.templateParameter = templateParameter;
            this.allImportedTypes = allImportedTypes;
            this.defaultCreateTimeColumn = ServiceCodeGenerator.this.getDefaultCreateTimeColumn(codegenConfig, targetConfig.getDomainObjectConfig());
            this.defaultUpdateTimeColumn = ServiceCodeGenerator.this.getDefaultUpdateTimeColumn(codegenConfig, targetConfig.getDomainObjectConfig());
        }

        public ServiceCodegenConfigProperties getCodegenConfig() {
            return codegenConfig;
        }

        public DomainBoundedTargetConfigProperties<ServiceImplementConfigProperties> getTargetConfig() {
            return targetConfig;
        }

        public DomainObjectColumnConfigProperties getDefaultCreateTimeColumn() {
            return defaultCreateTimeColumn;
        }

        public DomainObjectColumnConfigProperties getDefaultUpdateTimeColumn() {
            return defaultUpdateTimeColumn;
        }

        public TemplateParameter getTemplateParameter() {
            return templateParameter;
        }

        public Set<FullyQualifiedJavaType> getAllImportedTypes() {
            return allImportedTypes;
        }

        public String getDefaultCreateTimeFieldName() {
            return defaultCreateTimeFieldName;
        }

        public void setDefaultCreateTimeFieldName(String defaultCreateTimeFieldName) {
            this.defaultCreateTimeFieldName = defaultCreateTimeFieldName;
        }

        public String getDefaultUpdateTimeFieldName() {
            return defaultUpdateTimeFieldName;
        }

        public void setDefaultUpdateTimeFieldName(String defaultUpdateTimeFieldName) {
            this.defaultUpdateTimeFieldName = defaultUpdateTimeFieldName;
        }
    }

    class AppServiceCodegenContext {

        private final ServiceCodegenConfigProperties codegenConfig;

        private final DomainBoundedTargetConfigProperties<ServiceImplementConfigProperties> targetConfig;

        private final String aggregateObjectName;

        private final String aggregateObjectAlias;

        private final MasterDomainServiceConfig masterDomainService;

        private final List<SlaveDomainServiceConfig> slaveDomainServices;

        private final TemplateParameter templateParameter;

        private final Set<FullyQualifiedJavaType> allImportedTypes;

        public AppServiceCodegenContext(ServiceCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<ServiceImplementConfigProperties> targetConfig, MasterDomainServiceConfig masterDomainService, List<SlaveDomainServiceConfig> slaveDomainServices, TemplateParameter templateParameter, Set<FullyQualifiedJavaType> allImportedTypes) {
            this.codegenConfig = codegenConfig;
            this.targetConfig = targetConfig;
            this.aggregateObjectName = targetConfig.getDomainAggregateConfig().getAggregateObjectName();
            this.aggregateObjectAlias = targetConfig.getDomainAggregateConfig().getAggregateObjectAlias();
            this.masterDomainService = masterDomainService;
            this.slaveDomainServices = slaveDomainServices;
            this.templateParameter = templateParameter;
            this.allImportedTypes = allImportedTypes;
        }

        public ServiceCodegenConfigProperties getCodegenConfig() {
            return codegenConfig;
        }

        public DomainBoundedTargetConfigProperties<ServiceImplementConfigProperties> getTargetConfig() {
            return targetConfig;
        }

        public String getAggregateObjectName() {
            return aggregateObjectName;
        }

        public String getAggregateObjectAlias() {
            return aggregateObjectAlias;
        }

        public MasterDomainServiceConfig getMasterDomainService() {
            return masterDomainService;
        }

        public List<SlaveDomainServiceConfig> getSlaveDomainServices() {
            return slaveDomainServices;
        }

        public TemplateParameter getTemplateParameter() {
            return templateParameter;
        }

        public Set<FullyQualifiedJavaType> getAllImportedTypes() {
            return allImportedTypes;
        }
    }

    class DomainServiceConfig {

        private String domainObjectName;

        private String domainObjectAlias;

        private FullyQualifiedJavaType domainObjectIdType;

        private FullyQualifiedJavaType domainServiceType;

        private String domainServiceName;

        private String domainServiceBeanName;

        private String domainServiceInstanceName;

        public String getDomainObjectName() {
            return domainObjectName;
        }

        public void setDomainObjectName(String domainObjectName) {
            this.domainObjectName = domainObjectName;
        }

        public String getDomainObjectAlias() {
            return domainObjectAlias;
        }

        public void setDomainObjectAlias(String domainObjectAlias) {
            this.domainObjectAlias = domainObjectAlias;
        }

        public FullyQualifiedJavaType getDomainObjectIdType() {
            return domainObjectIdType;
        }

        public void setDomainObjectIdType(FullyQualifiedJavaType domainObjectIdType) {
            this.domainObjectIdType = domainObjectIdType;
        }

        public FullyQualifiedJavaType getDomainServiceType() {
            return domainServiceType;
        }

        public void setDomainServiceType(FullyQualifiedJavaType domainServiceType) {
            this.domainServiceType = domainServiceType;
        }

        public String getDomainServiceName() {
            return domainServiceName;
        }

        public void setDomainServiceName(String domainServiceName) {
            this.domainServiceName = domainServiceName;
        }

        public String getDomainServiceBeanName() {
            return domainServiceBeanName;
        }

        public void setDomainServiceBeanName(String domainServiceBeanName) {
            this.domainServiceBeanName = domainServiceBeanName;
        }

        public String getDomainServiceInstanceName() {
            return domainServiceInstanceName;
        }

        public void setDomainServiceInstanceName(String domainServiceInstanceName) {
            this.domainServiceInstanceName = domainServiceInstanceName;
        }
    }

    class MasterDomainServiceConfig extends DomainServiceConfig {

    }

    class SlaveDomainServiceConfig extends DomainServiceConfig {

        private DomainAggregateSlaveConfigProperties domainAggregateSlaveConfig;

        private DomainAggregateFieldConfigProperties domainAggregateFieldConfig;

        public DomainAggregateSlaveConfigProperties getDomainAggregateSlaveConfig() {
            return domainAggregateSlaveConfig;
        }

        public void setDomainAggregateSlaveConfig(DomainAggregateSlaveConfigProperties domainAggregateSlaveConfig) {
            this.domainAggregateSlaveConfig = domainAggregateSlaveConfig;
        }

        public DomainAggregateFieldConfigProperties getDomainAggregateFieldConfig() {
            return domainAggregateFieldConfig;
        }

        public void setDomainAggregateFieldConfig(DomainAggregateFieldConfigProperties domainAggregateFieldConfig) {
            this.domainAggregateFieldConfig = domainAggregateFieldConfig;
        }
    }

}
