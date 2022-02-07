package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.service.DomainServiceParameters.DomainServiceParameter;
import com.penglecode.xmodule.common.codegen.support.*;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.support.MapLambdaBuilder;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 应用服务代码生成方法参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 13:40
 */
public class ApplicationServiceCodegenMethodParameters {

    private final CodegenContext<ServiceCodegenConfigProperties, ?, DomainAggregateConfig> codegenContext;

    private final CodegenParameter codegenParameter;

    public ApplicationServiceCodegenMethodParameters(CodegenContext<ServiceCodegenConfigProperties, ?, DomainAggregateConfig> codegenContext, CodegenParameter codegenParameter) {
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
            DomainAggregateConfig domainAggregateConfig = codegenContext.getDomainObjectConfig();
            String domainObjectVariable = methodParameter.getDomainObjectVariable();
            methodCodeLines.add(String.format("ValidationAssert.notNull(%s, MessageSupplier.ofRequiredParameter(\"%s\"));", domainObjectVariable, domainObjectVariable));
            String validateFields = domainAggregateConfig.getValidateFields("create");
            if(StringUtils.isNotBlank(validateFields)) {
                methodCodeLines.add(String.format("BeanValidator.validateBean(%s);", domainObjectVariable + validateFields));
            }
            DomainServiceParameters domainServiceParameters = (DomainServiceParameters) codegenParameter.get("domainServiceParameters");
            DomainServiceParameter masterDomainServiceParameter = domainServiceParameters.getMasterDomainServiceParameter();
            methodCodeLines.add(String.format("%s.create%s(product);"));
            methodParameter.setMethodCodeLines(methodCodeLines);
        }
        return methodParameter;
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

    protected CodegenMethodParameter createCodegenMethodParameter() {
        DomainAggregateConfig domainAggregateConfig = codegenContext.getDomainObjectConfig();
        CodegenMethodParameter methodParameter = new CodegenMethodParameter();
        methodParameter.setDomainObjectName(domainAggregateConfig.getDomainObjectName());
        methodParameter.setDomainObjectTitle(domainAggregateConfig.getDomainObjectTitle());
        methodParameter.setDomainObjectAlias(domainAggregateConfig.getDomainObjectAlias());
        methodParameter.setDomainObjectAliases(CodegenUtils.getPluralNameOfDomainObject(methodParameter.getDomainObjectAlias()));
        methodParameter.setDomainObjectVariable(StringUtils.lowerCaseFirstChar(domainAggregateConfig.getDomainObjectAlias()));
        methodParameter.setDomainObjectVariables(CodegenUtils.getPluralNameOfDomainObject(methodParameter.getDomainObjectVariable()));
        DomainEntityConfig masterDomainEntityConfig = codegenContext.getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateConfig.getAggregateMasterEntity());
        methodParameter.setDomainObjectIdType(masterDomainEntityConfig.getIdType().getShortName());
        //考虑到联合主键，所以统一取做id/ids
        methodParameter.setDomainObjectIdName("id");
        methodParameter.setDomainObjectIdsName("ids");
        return methodParameter;
    }

    protected CodegenContext<ServiceCodegenConfigProperties, ?, DomainAggregateConfig> getCodegenContext() {
        return codegenContext;
    }

    protected CodegenParameter getCodegenParameter() {
        return codegenParameter;
    }

}
