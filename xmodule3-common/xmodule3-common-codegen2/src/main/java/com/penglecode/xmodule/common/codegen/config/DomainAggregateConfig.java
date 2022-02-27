package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.util.CodegenUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 领域聚合对象配置
 * (如果定义了聚合对象，则后端会生成对应的{domainAggregateName}AppService)
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/22 13:59
 */
public class DomainAggregateConfig extends DomainObjectConfig {

    /** 领域聚合对象类名(为避免与实体对象命名冲突,聚合根对象统一以Aggregate结尾) */
    private String domainAggregateName;

    /** 领域聚合对象中文名称 */
    private String domainAggregateTitle;

    /** 领域聚合对象别名(在方法命名、参数/变量命名、URL命名时使用) */
    private String domainAggregateAlias;

    /** 领域聚合对象的主要实体对象名称(指向domainEntities列表中的某一个) */
    private String aggregateMasterEntity;

    /** 领域聚合对象的从属实体对象列表 */
    private Set<DomainAggregateSlaveConfig> aggregateSlaveEntities;

    /** 领域聚合对象的字段列表 */
    private final Map<String,DomainAggregateFieldConfig> domainAggregateFields = new LinkedHashMap<>();

    public String getDomainAggregateName() {
        return domainAggregateName;
    }

    public void setDomainAggregateName(String domainAggregateName) {
        this.domainAggregateName = domainAggregateName;
    }

    public String getDomainAggregateTitle() {
        return domainAggregateTitle;
    }

    public void setDomainAggregateTitle(String domainAggregateTitle) {
        this.domainAggregateTitle = domainAggregateTitle;
    }

    public String getDomainAggregateAlias() {
        return domainAggregateAlias;
    }

    public void setDomainAggregateAlias(String domainAggregateAlias) {
        this.domainAggregateAlias = domainAggregateAlias;
    }

    public String getAggregateMasterEntity() {
        return aggregateMasterEntity;
    }

    public void setAggregateMasterEntity(String aggregateMasterEntity) {
        this.aggregateMasterEntity = aggregateMasterEntity;
    }

    public Set<DomainAggregateSlaveConfig> getAggregateSlaveEntities() {
        return aggregateSlaveEntities;
    }

    public void setAggregateSlaveEntities(Set<DomainAggregateSlaveConfig> aggregateSlaveEntities) {
        this.aggregateSlaveEntities = aggregateSlaveEntities;
    }

    public Map<String, DomainAggregateFieldConfig> getDomainAggregateFields() {
        return domainAggregateFields;
    }

    /**
     * 获取领域对象的Bean-Validation校验字段
     * 其返回值诸如：, Product::getProductName, Product::getUnitPrice, Product::getProductType
     * @param operationType     - 操作类型：create|modify
     * @return
     */
    public String getValidateFields(String operationType) {
        StringBuilder validateFields = new StringBuilder();
        Map<String,DomainAggregateFieldConfig> domainAggregateFields = getDomainAggregateFields();
        for(Map.Entry<String,DomainAggregateFieldConfig> entry : domainAggregateFields.entrySet()) {
            DomainAggregateFieldConfig domainAggregateFieldConfig = entry.getValue();
            if(("create".equals(operationType) && domainAggregateFieldConfig.getDomainAggregateSlaveConfig().isCascadingOnInsert() && domainAggregateFieldConfig.getDomainAggregateSlaveConfig().isValidateOnInsert())
            || ("modify".equals(operationType) && domainAggregateFieldConfig.getDomainAggregateSlaveConfig().isCascadingOnUpdate() && domainAggregateFieldConfig.getDomainAggregateSlaveConfig().isValidateOnUpdate())) {
                String fieldName = domainAggregateFieldConfig.getFieldName();
                String fieldType = domainAggregateFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters();
                validateFields.append(", ").append(getGeneratedTargetName(domainAggregateName, false, false)).append("::").append(CodegenUtils.getGetterMethodName(fieldName, fieldType));
            }
        }
        return validateFields.toString();
    }

    @Override
    public String getDomainObjectName() {
        return getDomainAggregateName();
    }

    @Override
    public String getDomainObjectTitle() {
        return getDomainAggregateTitle();
    }

    @Override
    public String getDomainObjectAlias() {
        return getDomainAggregateAlias();
    }

    @Override
    public String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix) {
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + (includeSuffix ? ".java" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainAggregateConfig)) return false;
        DomainAggregateConfig that = (DomainAggregateConfig) o;
        return domainAggregateName.equals(that.domainAggregateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domainAggregateName);
    }

}