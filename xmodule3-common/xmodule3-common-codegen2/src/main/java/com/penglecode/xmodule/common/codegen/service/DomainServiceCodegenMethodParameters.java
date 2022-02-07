package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.*;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.support.MapLambdaBuilder;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.StringUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 领域服务代码生成方法参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/5 22:00
 */
public class DomainServiceCodegenMethodParameters {

    private final CodegenContext<ServiceCodegenConfigProperties, ?, DomainEntityConfig> codegenContext;

    private final CodegenParameter codegenParameter;

    public DomainServiceCodegenMethodParameters(CodegenContext<ServiceCodegenConfigProperties, ?, DomainEntityConfig> codegenContext, CodegenParameter codegenParameter) {
        this.codegenContext = codegenContext;
        this.codegenParameter = codegenParameter;
    }

    /**
     * 创建领域对象
     * @return
     */
    public CodegenMethodParameter createDomainObject(boolean implementation) {
        CodegenMethodParameter methodParameter = createCodegenMethodParameter();
        methodParameter.setMethodActivated(true);
        if(implementation) {
            List<String> methodCodeLines = new ArrayList<>();
            DomainEntityConfig domainEntityConfig = codegenContext.getDomainObjectConfig();
            String domainObjectVariable = methodParameter.getDomainObjectVariable();
            methodCodeLines.add(String.format("ValidationAssert.notNull(%s, MessageSupplier.ofRequiredParameter(\"%s\"));", domainObjectVariable, domainObjectVariable));
            DomainEntityColumnConfig defaultCreateTimeColumn = codegenContext.getCodegenConfig().getDefaultCreateTimeColumn(domainEntityConfig);
            String upperDefaultCreateTimeFieldName = null;
            if(defaultCreateTimeColumn != null && defaultCreateTimeColumn.isColumnOnInsert()) {
                upperDefaultCreateTimeFieldName = StringUtils.upperCaseFirstChar(defaultCreateTimeColumn.getIntrospectedColumn().getJavaFieldName());
                methodCodeLines.add(String.format("%s.set%s(StringUtils.defaultIfBlank(%s.get%s(), DateTimeUtils.formatNow()));", domainObjectVariable, upperDefaultCreateTimeFieldName, domainObjectVariable, upperDefaultCreateTimeFieldName));
                codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(StringUtils.class.getName()));
                codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(DateTimeUtils.class.getName()));
            }
            DomainEntityColumnConfig defaultUpdateTimeColumn = codegenContext.getCodegenConfig().getDefaultUpdateTimeColumn(domainEntityConfig);
            if(defaultUpdateTimeColumn != null && defaultUpdateTimeColumn.isColumnOnInsert()) {
                String upperDefaultUpdateTimeFieldName = StringUtils.upperCaseFirstChar(defaultUpdateTimeColumn.getIntrospectedColumn().getJavaFieldName());
                if(upperDefaultCreateTimeFieldName != null) {
                    methodCodeLines.add(String.format("%s.set%s(%s.get%s());", domainObjectVariable, upperDefaultUpdateTimeFieldName, domainObjectVariable, upperDefaultCreateTimeFieldName));
                } else {
                    methodCodeLines.add(String.format("%s.set%s(DateTimeUtils.formatNow());", domainObjectVariable, upperDefaultUpdateTimeFieldName));
                }
                codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(DateTimeUtils.class.getName()));
            }
            String validateFields = domainEntityConfig.getValidateFields("create");
            if(StringUtils.isNotBlank(validateFields)) {
                methodCodeLines.add(String.format("BeanValidator.validateBean(%s);", domainObjectVariable + validateFields));
            }
            methodParameter.setMethodCodeLines(methodCodeLines);
        }
        return methodParameter;
    }

    /**
     * 批量创建领域对象
     * @return
     */
    public CodegenMethodParameter batchCreateDomainObjects(boolean implementation) {
        return batchSaveDomainObjects(implementation);
    }

    /**
     * 根据领域对象ID修改领域对象
     * @return
     */
    public CodegenMethodParameter modifyDomainObjectById(boolean implementation) {
        CodegenMethodParameter methodParameter = createCodegenMethodParameter();
        methodParameter.setMethodActivated(true);
        if(implementation) {
            List<String> methodCodeLines = new ArrayList<>();
            DomainEntityConfig domainEntityConfig = codegenContext.getDomainObjectConfig();
            String domainObjectVariable = methodParameter.getDomainObjectVariable();
            methodCodeLines.add(String.format("ValidationAssert.notNull(%s, MessageSupplier.ofRequiredParameter(\"%s\"));", domainObjectVariable, domainObjectVariable));
            DomainEntityColumnConfig defaultUpdateTimeColumn = codegenContext.getCodegenConfig().getDefaultUpdateTimeColumn(domainEntityConfig);
            if(defaultUpdateTimeColumn != null && defaultUpdateTimeColumn.isColumnOnUpdate()) {
                String upperDefaultUpdateTimeFieldName = StringUtils.upperCaseFirstChar(defaultUpdateTimeColumn.getIntrospectedColumn().getJavaFieldName());
                methodCodeLines.add(String.format("%s.set%s(StringUtils.defaultIfBlank(%s.get%s(), DateTimeUtils.formatNow()));", domainObjectVariable, upperDefaultUpdateTimeFieldName, domainObjectVariable, upperDefaultUpdateTimeFieldName));
                codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(StringUtils.class.getName()));
                codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(DateTimeUtils.class.getName()));
            }
            String validateFields = domainEntityConfig.getValidateFields("modify");
            if(StringUtils.isNotBlank(validateFields)) {
                methodCodeLines.add(String.format("BeanValidator.validateBean(%s);", domainObjectVariable + validateFields));
            }
            StringBuilder updateFields = new StringBuilder(String.format("Map<String,Object> updateColumns = MapLambdaBuilder.of(%s)", domainObjectVariable)).append("\n");
            Map<String,DomainEntityColumnConfig> domainEntityColumns = domainEntityConfig.getDomainEntityColumns();
            for(Map.Entry<String,DomainEntityColumnConfig> entry : domainEntityColumns.entrySet()) {
                DomainEntityColumnConfig domainEntityColumn = entry.getValue();
                if(domainEntityColumn.isColumnOnUpdate()) {
                    String fieldName = domainEntityColumn.getIntrospectedColumn().getJavaFieldName();
                    String fieldType = domainEntityColumn.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters();
                    updateFields.append("                .with(").append(domainEntityConfig.getDomainEntityName()).append("::").append(CodegenUtils.getGetterMethodName(fieldName, fieldType)).append(")").append("\n");
                }
            }
            if(updateFields.length() > 0) {
                updateFields.append("                .build();");
                methodCodeLines.add(updateFields.toString());
                codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Map.class.getName()));
                codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(MapLambdaBuilder.class.getName()));
            }
            methodParameter.setMethodCodeLines(methodCodeLines);
        }
        return methodParameter;
    }

    /**
     * 根据领域对象ID批量修改领域对象
     * @return
     */
    public CodegenMethodParameter batchModifyDomainObjectsById(boolean implementation) {
        return batchSaveDomainObjects(implementation);
    }

    /**
     * 根据领域对象ID删除领域对象
     * @return
     */
    public CodegenMethodParameter removeDomainObjectById(boolean implementation) {
        CodegenMethodParameter methodParameter = createCodegenMethodParameter();
        methodParameter.setMethodActivated(true);
        if(implementation) {
            List<String> methodCodeLines = new ArrayList<>();
            DomainEntityConfig domainEntityConfig = codegenContext.getDomainObjectConfig();
            List<DomainEntityColumnConfig> idColumns = domainEntityConfig.getIdColumns();
            if(idColumns.size() == 1) { //单一主键
                String idFieldName = idColumns.get(0).getIntrospectedColumn().getJavaFieldName();
                String idFieldType = idColumns.get(0).getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters();
                methodCodeLines.add(String.format("BeanValidator.validateProperty(id, %s);", domainEntityConfig.getDomainEntityName() + "::" + CodegenUtils.getGetterMethodName(idFieldName, idFieldType)));
            } else {
                methodCodeLines.add(String.format("BeanValidator.validateMap(id%s);", domainEntityConfig.getValidateFields("byId")));
            }
            methodParameter.setMethodCodeLines(methodCodeLines);
        }
        return methodParameter;
    }

    /**
     * 根据多个领域对象ID删除领域对象
     * @return
     */
    public CodegenMethodParameter removeDomainObjectsByIds(boolean implementation) {
        CodegenMethodParameter methodParameter = createCodegenMethodParameter();
        methodParameter.setMethodActivated(true);
        if(implementation) {
            List<String> methodCodeLines = new ArrayList<>();
            methodCodeLines.add(String.format("ValidationAssert.notEmpty(%s, MessageSupplier.ofRequiredParameter(\"%s\"));", methodParameter.getDomainObjectIdsName(), methodParameter.getDomainObjectIdsName()));
            methodParameter.setMethodCodeLines(methodCodeLines);
        }
        return methodParameter;
    }

    /**
     * 根据某master领域对象ID删除当前领域对象(slave)
     * @return
     */
    public CodegenMethodParameter removeDomainObjectsByXxxMasterId(boolean implementation) {
        DomainEntityConfig slaveDomainEntityConfig = codegenContext.getDomainObjectConfig();
        CodegenMethodParameter methodParameters = new CodegenMethodParameter();
        List<CodegenMethodParameter> removeByMasterIdMethods = new ArrayList<>();
        //当前Slave实体可能属于多个Master
        processDomainObjectsByMasterId(slaveDomainEntityConfig, (domainAggregateSlaveConfig, masterDomainEntityConfig) -> {
            CodegenMethodParameter methodParameter = createCodegenMethodParameter();
            methodParameter.setMethodActivated(true);
            String masterDomainObjectIdName = domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfSlave(); //slave中与masterId关联的那个字段
            FullyQualifiedJavaType masterDomainObjectIdType = slaveDomainEntityConfig.getDomainEntityColumns().get(masterDomainObjectIdName).getIntrospectedColumn().getJavaFieldType();
            methodParameter.put("masterDomainObjectTitle", masterDomainEntityConfig.getDomainEntityTitle());
            methodParameter.put("masterDomainObjectIdName", masterDomainObjectIdName);
            methodParameter.put("masterDomainObjectIdType", masterDomainObjectIdType.getShortName());
            methodParameter.put("upperMasterDomainObjectIdName", StringUtils.upperCaseFirstChar(masterDomainObjectIdName));
            if(implementation) {
                List<String> methodCodeLines = new ArrayList<>();
                String getMasterDomainObjectIdRef = methodParameter.getDomainObjectName() + "::" + CodegenUtils.getGetterMethodName(masterDomainObjectIdName, masterDomainObjectIdType.getFullyQualifiedNameWithoutTypeParameters());
                methodCodeLines.add(String.format("BeanValidator.validateProperty(%s, %s);", masterDomainObjectIdName, getMasterDomainObjectIdRef));
                methodCodeLines.add(String.format("QueryCriteria<%s> criteria = LambdaQueryCriteria.ofEmpty(%s::new)", methodParameter.getDomainObjectName(), methodParameter.getDomainObjectName()));
                methodCodeLines.add(String.format("    .eq(%s, %s);", getMasterDomainObjectIdRef, masterDomainObjectIdName));
                methodParameter.setMethodCodeLines(methodCodeLines);
            }
            removeByMasterIdMethods.add(methodParameter);
        });
        methodParameters.setMethodActivated(true);
        methodParameters.put("removeByMasterIdMethods", removeByMasterIdMethods);
        return methodParameters;
    }

    /**
     * 根据领域对象ID获取领域对象
     * @return
     */
    public CodegenMethodParameter getDomainObjectById(boolean implementation) {
        return noopMethodParameter(implementation);
    }

    /**
     * 根据多个领域对象ID获取领域对象
     * @return
     */
    public CodegenMethodParameter getDomainObjectsByIds(boolean implementation) {
        return noopMethodParameter(implementation);
    }

    /**
     * 根据某个master领域对象ID获取当前领域对象(slave)
     * @return
     */
    public CodegenMethodParameter getDomainObjectsByXxxMasterId(boolean implementation) {
        DomainEntityConfig slaveDomainEntityConfig = codegenContext.getDomainObjectConfig();
        CodegenMethodParameter methodParameters = new CodegenMethodParameter();
        List<CodegenMethodParameter> getByMasterIdMethods = new ArrayList<>();
        //当前Slave实体可能属于多个Master
        processDomainObjectsByMasterId(slaveDomainEntityConfig, (domainAggregateSlaveConfig, masterDomainEntityConfig) -> {
            CodegenMethodParameter methodParameter = createCodegenMethodParameter();
            methodParameter.setMethodActivated(true);
            String masterDomainObjectIdName = domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfSlave(); //slave中与masterId关联的那个字段
            FullyQualifiedJavaType masterDomainObjectIdType = slaveDomainEntityConfig.getDomainEntityColumns().get(masterDomainObjectIdName).getIntrospectedColumn().getJavaFieldType();
            methodParameter.put("masterDomainObjectTitle", masterDomainEntityConfig.getDomainEntityTitle());
            methodParameter.put("masterDomainObjectIdType", masterDomainObjectIdType.getShortName());
            methodParameter.put("masterDomainObjectIdName", masterDomainObjectIdName);
            methodParameter.put("upperMasterDomainObjectIdName", StringUtils.upperCaseFirstChar(masterDomainObjectIdName));
            String methodDynamicReturnType = slaveDomainEntityConfig.getDomainEntityName();
            String methodDynamicReturnQuery = "selectModelByCriteria";
            if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //如果是1:N关系?
                methodDynamicReturnType = "List<" + slaveDomainEntityConfig.getDomainEntityName() + ">";
                methodDynamicReturnQuery = "selectModelListByCriteria";
            }
            methodParameter.put("methodDynamicReturnType", methodDynamicReturnType);
            if(implementation) {
                List<String> methodCodeLines = new ArrayList<>();
                methodCodeLines.add(String.format("if(!ObjectUtils.isEmpty(%s)) {", masterDomainObjectIdName));
                methodCodeLines.add(String.format("    QueryCriteria<%s> criteria = LambdaQueryCriteria.ofEmpty(%s::new)", slaveDomainEntityConfig.getDomainEntityName(), slaveDomainEntityConfig.getDomainEntityName()));
                methodCodeLines.add(String.format("        .eq(%s::%s, %s);", slaveDomainEntityConfig.getDomainEntityName(), CodegenUtils.getGetterMethodName(masterDomainObjectIdName, masterDomainObjectIdType.getFullyQualifiedNameWithoutTypeParameters()), masterDomainObjectIdName));
                methodCodeLines.add(String.format("    return %s.%s(criteria);", codegenParameter.get("mapperInstanceName"), methodDynamicReturnQuery));
                methodCodeLines.add("}");
                methodCodeLines.add("return Collections.emptyList();");
                methodParameter.setMethodCodeLines(methodCodeLines);
            }
            getByMasterIdMethods.add(methodParameter);
        });
        methodParameters.setMethodActivated(true);
        methodParameters.put("getByMasterIdMethods", getByMasterIdMethods);
        return methodParameters;
    }

    /**
     * 根据多个master领域对象ID获取当前领域对象(slave)
     * @return
     */
    public CodegenMethodParameter getDomainObjectsByXxxMasterIds(boolean implementation) {
        DomainEntityConfig slaveDomainEntityConfig = codegenContext.getDomainObjectConfig();
        CodegenMethodParameter methodParameters = new CodegenMethodParameter();
        List<CodegenMethodParameter> getByMasterIdsMethods = new ArrayList<>();
        //当前Slave实体可能属于多个Master
        processDomainObjectsByMasterId(slaveDomainEntityConfig, (domainAggregateSlaveConfig, masterDomainEntityConfig) -> {
            CodegenMethodParameter methodParameter = createCodegenMethodParameter();
            methodParameter.setMethodActivated(true);
            String masterDomainObjectIdName = domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfSlave(); //slave中与masterId关联的那个字段
            String masterDomainObjectIdsName = CodegenUtils.getPluralNameOfDomainObject(masterDomainObjectIdName);
            FullyQualifiedJavaType masterDomainObjectIdType = slaveDomainEntityConfig.getDomainEntityColumns().get(masterDomainObjectIdName).getIntrospectedColumn().getJavaFieldType();
            methodParameter.put("masterDomainObjectTitle", masterDomainEntityConfig.getDomainEntityTitle());
            methodParameter.put("masterDomainObjectIdName", masterDomainObjectIdName);
            methodParameter.put("masterDomainObjectIdType", masterDomainObjectIdType.getShortName());
            methodParameter.put("masterDomainObjectIdsName", masterDomainObjectIdsName);
            methodParameter.put("upperMasterDomainObjectIdsName", StringUtils.upperCaseFirstChar(masterDomainObjectIdsName));
            String methodDynamicReturnType = String.format("Map<%s,%s>", masterDomainObjectIdType.getShortName(), slaveDomainEntityConfig.getDomainEntityName());
            if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //如果是1:N关系?
                methodDynamicReturnType = String.format("Map<%s,%s>", masterDomainObjectIdType.getShortName(), "List<" + slaveDomainEntityConfig.getDomainEntityName() + ">");
            }
            if(implementation) {
                String getMasterIdOfSlaveRef = slaveDomainEntityConfig.getDomainEntityName() + "::" + CodegenUtils.getGetterMethodName(masterDomainObjectIdName, masterDomainObjectIdType.getFullyQualifiedNameWithoutTypeParameters());
                List<String> methodCodeLines = new ArrayList<>();
                methodCodeLines.add(String.format("if(!CollectionUtils.isEmpty(%s)) {", masterDomainObjectIdsName));
                methodCodeLines.add(String.format("    QueryCriteria<%s> criteria = LambdaQueryCriteria.ofEmpty(%s::new)", slaveDomainEntityConfig.getDomainEntityName(), slaveDomainEntityConfig.getDomainEntityName()));
                methodCodeLines.add(String.format("        .in(%s, %s.toArray());", getMasterIdOfSlaveRef, masterDomainObjectIdsName));
                methodCodeLines.add(String.format("    List<%s> %s = %s.selectModelListByCriteria(criteria);", slaveDomainEntityConfig.getDomainEntityName(), methodParameter.getDomainObjectVariables(), codegenParameter.get("mapperInstanceName")));
                methodCodeLines.add(String.format("    if(!CollectionUtils.isEmpty(%s)) {", methodParameter.getDomainObjectVariables()));
                if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //如果是1:N关系?
                    methodCodeLines.add(String.format("        return %s.stream().collect(Collectors.groupingBy(%s, Collectors.toList()));", methodParameter.getDomainObjectVariables(), getMasterIdOfSlaveRef));
                } else {
                    methodCodeLines.add(String.format("        return %s.stream().collect(Collectors.toMap(%s, Function.identity()));", methodParameter.getDomainObjectVariables(), getMasterIdOfSlaveRef));
                    codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Function.class.getName()));
                }
                methodCodeLines.add("    }");
                methodCodeLines.add("}");
                methodCodeLines.add("return Collections.emptyMap();");
                methodParameter.setMethodCodeLines(methodCodeLines);
                codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Map.class.getName()));
                codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(Collectors.class.getName()));
            }
            methodParameter.put("methodDynamicReturnType", methodDynamicReturnType);
            getByMasterIdsMethods.add(methodParameter);
        });
        methodParameters.setMethodActivated(true);
        methodParameters.put("getByMasterIdsMethods", getByMasterIdsMethods);
        return methodParameters;
    }

    /**
     * 根据条件查询领域对象(分页、排序)
     * @return
     */
    public CodegenMethodParameter getDomainObjectsByPage(boolean implementation) {
        CodegenMethodParameter methodParameter = createCodegenMethodParameter();
        methodParameter.setMethodActivated(true);
        if(implementation) {
            DomainEntityConfig domainEntityConfig = codegenContext.getDomainObjectConfig();
            StringBuilder criteriaCodes = new StringBuilder(String.format("QueryCriteria<%s> criteria = LambdaQueryCriteria.of(condition)", domainEntityConfig.getDomainEntityName())).append("\n");
            Map<String,DomainEntityFieldConfig> domainEntityFields = domainEntityConfig.getDomainEntityFields();
            for(Map.Entry<String,DomainEntityFieldConfig> entry : domainEntityFields.entrySet()) {
                DomainEntityFieldConfig domainEntityField = entry.getValue();
                if(domainEntityField.getQueryConditionOperator() != null) {
                    String fieldGetterName = CodegenUtils.getGetterMethodName(domainEntityField.getFieldName(), domainEntityField.getFieldType().getFullyQualifiedNameWithoutTypeParameters());
                    if(!domainEntityField.getFieldClass().isSupportField()) { //持久化字段
                        criteriaCodes.append("                .").append(domainEntityField.getQueryConditionOperator().getOpName()).append("(").append(domainEntityConfig.getDomainEntityName()).append("::").append(fieldGetterName).append(")").append("\n");
                    } else { //辅助字段
                        DomainEntityColumnConfig refDomainEntityColumn = domainEntityField.getDomainEntityColumnConfig();
                        String refFieldGetterName = CodegenUtils.getGetterMethodName(refDomainEntityColumn.getIntrospectedColumn().getJavaFieldName(), refDomainEntityColumn.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters());
                        criteriaCodes.append("                .").append(domainEntityField.getQueryConditionOperator().getOpName()).append("(").append(domainEntityConfig.getDomainEntityName()).append("::").append(refFieldGetterName).append(", condition.").append(fieldGetterName).append("())").append("\n");
                    }
                }
            }
            criteriaCodes.append("                .dynamic(true)").append("\n");
            criteriaCodes.append("                .orderBy(page.getOrderBys());");
            methodParameter.setMethodCodeLines(Collections.singletonList(criteriaCodes.toString()));
        }
        return methodParameter;
    }

    /**
     * 获取领域对象总数
     * @return
     */
    public CodegenMethodParameter getDomainObjectTotalCount(boolean implementation) {
        return noopMethodParameter(implementation);
    }

    /**
     * 基于游标遍历所有领域对象
     * @return
     */
    public CodegenMethodParameter forEachDomainObject1(boolean implementation) {
        return noopMethodParameter(implementation);
    }

    /**
     * 基于游标遍历所有领域对象
     * @return
     */
    public CodegenMethodParameter forEachDomainObject2(boolean implementation) {
        return noopMethodParameter(implementation);
    }

    protected CodegenMethodParameter noopMethodParameter(boolean implementation) {
        CodegenMethodParameter methodParameter = createCodegenMethodParameter();
        methodParameter.setMethodActivated(true);
        return methodParameter;
    }

    protected CodegenMethodParameter batchSaveDomainObjects(boolean implementation) {
        CodegenMethodParameter methodParameter = createCodegenMethodParameter();
        methodParameter.setMethodActivated(true);
        if(implementation) {
            List<String> methodCodeLines = new ArrayList<>();
            methodCodeLines.add(String.format("ValidationAssert.notEmpty(%s, MessageSupplier.ofRequiredParameter(\"%s\"));", methodParameter.getDomainObjectVariables(), methodParameter.getDomainObjectVariables()));
            methodParameter.setMethodCodeLines(methodCodeLines);
        }
        return methodParameter;
    }

    protected void processDomainObjectsByMasterId(DomainEntityConfig slaveDomainEntityConfig, BiConsumer<DomainAggregateSlaveConfig,DomainEntityConfig> biConsumer) {
        Map<String,DomainAggregateConfig> domainAggregateConfigs = codegenContext.getCodegenConfig().getDomain().getDomainAggregates();
        for(Map.Entry<String,DomainAggregateConfig> entry : domainAggregateConfigs.entrySet()) {
            DomainAggregateConfig domainAggregateConfig = entry.getValue();
            for (DomainAggregateSlaveConfig domainAggregateSlaveConfig : domainAggregateConfig.getAggregateSlaveEntities()) {
                //找到当前Slave实体的对应Master
                if (domainAggregateSlaveConfig.getAggregateSlaveEntity().equals(slaveDomainEntityConfig.getDomainEntityName())) {
                    DomainEntityConfig masterDomainEntityConfig = codegenContext.getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateConfig.getAggregateMasterEntity());
                    biConsumer.accept(domainAggregateSlaveConfig, masterDomainEntityConfig);
                }
            }
        }
    }

    protected CodegenMethodParameter createCodegenMethodParameter() {
        DomainEntityConfig domainEntityConfig = codegenContext.getDomainObjectConfig();
        CodegenMethodParameter methodParameter = new CodegenMethodParameter();
        methodParameter.setDomainObjectName(domainEntityConfig.getDomainObjectName());
        methodParameter.setDomainObjectTitle(domainEntityConfig.getDomainObjectTitle());
        methodParameter.setDomainObjectAlias(domainEntityConfig.getDomainObjectAlias());
        methodParameter.setDomainObjectAliases(CodegenUtils.getPluralNameOfDomainObject(methodParameter.getDomainObjectAlias()));
        methodParameter.setDomainObjectVariable(StringUtils.lowerCaseFirstChar(domainEntityConfig.getDomainObjectAlias()));
        methodParameter.setDomainObjectVariables(CodegenUtils.getPluralNameOfDomainObject(methodParameter.getDomainObjectVariable()));
        methodParameter.setDomainObjectIdType(domainEntityConfig.getIdType().getShortName());
        //考虑到联合主键，所以统一取做id/ids
        methodParameter.setDomainObjectIdName("id");
        methodParameter.setDomainObjectIdsName("ids");
        return methodParameter;
    }

    protected CodegenContext<ServiceCodegenConfigProperties, ?, DomainEntityConfig> getCodegenContext() {
        return codegenContext;
    }

    protected CodegenParameter getCodegenParameter() {
        return codegenParameter;
    }

}
