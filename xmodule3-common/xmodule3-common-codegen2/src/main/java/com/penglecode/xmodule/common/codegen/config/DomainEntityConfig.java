package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.database.IntrospectedTable;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.domain.ID;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 领域实体配置，领域实体直接对应着数据库中的表
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/22 13:30
 */
public class DomainEntityConfig extends DomainObjectConfig {

    /** 领域实体类名 */
    private String domainEntityName;

    /** 领域实体对应的数据库表名 */
    private String domainEntityTable;

    /** 领域实体中文名称 */
    private String domainEntityTitle;

    /** 领域实体对象别名(在方法命名、参数/变量命名、URL命名时使用) */
    private String domainEntityAlias;

    /** 领域实体对象运行时数据源 */
    private String runtimeDataSource;

    /** 领域实体数据库自省配置 */
    private DomainIntrospectConfig introspectConfig = new DomainIntrospectConfig();

    /** 领域实体对应的数据库表的所有列 */
    private Map<String,DomainEntityColumnConfig> domainEntityColumns;

    /** 领域实体对应的所有字段 */
    private Map<String,DomainEntityFieldConfig> domainEntityFields;

    /** 对应的自省表 */
    private IntrospectedTable introspectedTable;

    public String getDomainEntityName() {
        return domainEntityName;
    }

    public void setDomainEntityName(String domainEntityName) {
        this.domainEntityName = domainEntityName;
    }

    public String getDomainEntityTable() {
        return domainEntityTable;
    }

    public void setDomainEntityTable(String domainEntityTable) {
        this.domainEntityTable = domainEntityTable;
    }

    public String getDomainEntityTitle() {
        return domainEntityTitle;
    }

    public void setDomainEntityTitle(String domainEntityTitle) {
        this.domainEntityTitle = domainEntityTitle;
    }

    public String getDomainEntityAlias() {
        return domainEntityAlias;
    }

    public void setDomainEntityAlias(String domainEntityAlias) {
        this.domainEntityAlias = domainEntityAlias;
    }

    public String getRuntimeDataSource() {
        return runtimeDataSource;
    }

    public void setRuntimeDataSource(String runtimeDataSource) {
        this.runtimeDataSource = runtimeDataSource;
    }

    public DomainIntrospectConfig getIntrospectConfig() {
        return introspectConfig;
    }

    public void setIntrospectConfig(DomainIntrospectConfig introspectConfig) {
        this.introspectConfig = introspectConfig;
    }

    public Map<String, DomainEntityColumnConfig> getDomainEntityColumns() {
        return domainEntityColumns;
    }

    public void setDomainEntityColumns(Set<DomainEntityColumnConfig> domainEntityColumns) {
        this.domainEntityColumns = new LinkedHashMap<>();
        if(!CollectionUtils.isEmpty(domainEntityColumns)) {
            for(DomainEntityColumnConfig domainEntityColumn : domainEntityColumns) {
                this.domainEntityColumns.put(domainEntityColumn.getColumnName().toLowerCase(), domainEntityColumn);
            }
        }
    }

    public Map<String, DomainEntityFieldConfig> getDomainEntityFields() {
        return domainEntityFields;
    }

    public void setDomainEntityFields(Map<String, DomainEntityFieldConfig> domainEntityFields) {
        this.domainEntityFields = domainEntityFields;
    }

    public IntrospectedTable getIntrospectedTable() {
        return introspectedTable;
    }

    public void setIntrospectedTable(IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }

    /**
     * 返回领域对象的ID列
     * @return
     */
    public List<DomainEntityColumnConfig> getIdColumns() {
        return domainEntityColumns.values().stream().filter(DomainEntityColumnConfig::isIdColumn).collect(Collectors.toList());
    }

    /**
     * 以单一主键的假设来获取主键列，如果不是单一主键则会报错
     * @return
     */
    public DomainEntityColumnConfig getSingleIdColumn() {
        List<DomainEntityColumnConfig> idColumns = getIdColumns();
        Assert.state(idColumns.size() == 1, String.format("领域实体(%s)必须是单一主键!", getDomainEntityName()));
        return idColumns.get(0);
    }

    /**
     * 返回领域对象的ID字段
     * @return
     */
    public List<DomainEntityFieldConfig> getIdFields() {
        return domainEntityFields.values().stream().filter(DomainEntityFieldConfig::isIdField).collect(Collectors.toList());
    }

    /**
     * 以单一主键的假设来获取主键列，如果不是单一主键则会报错
     * @return
     */
    public DomainEntityFieldConfig getSingleIdField() {
        List<DomainEntityFieldConfig> idFields = getIdFields();
        Assert.state(idFields.size() == 1, String.format("领域实体(%s)必须是单一主键!", getDomainEntityName()));
        return idFields.get(0);
    }

    /**
     * 返回领域实体的ID类型
     * @return
     */
    public FullyQualifiedJavaType getIdType() {
        List<DomainEntityColumnConfig> pkColumns = getIdColumns();
        if(pkColumns.size() == 1) { //单一主键
            return pkColumns.get(0).getIntrospectedColumn().getJavaFieldType();
        } else {
            return new FullyQualifiedJavaType(ID.class.getName());
        }
    }

    /**
     * 获取领域对象的Bean-Validation校验字段
     * 其返回值诸如：, Product::getProductName, Product::getUnitPrice, Product::getProductType
     * @param operationType     - 操作类型：create|modify|byId
     * @return
     */
    public String getValidateFields(String operationType) {
        StringBuilder validateFields = new StringBuilder();
        Map<String,DomainEntityFieldConfig> domainEntityFieldConfigs = getDomainEntityFields();
        for(Map.Entry<String,DomainEntityFieldConfig> entry : domainEntityFieldConfigs.entrySet()) {
            DomainEntityFieldConfig domainEntityFieldConfig = entry.getValue();
            if(("create".equals(operationType) && domainEntityFieldConfig.getDomainEntityColumnConfig().isValidateOnInsert())
                    || ("modify".equals(operationType) && domainEntityFieldConfig.getDomainEntityColumnConfig().isValidateOnUpdate())
                    || ("byId".equals(operationType) && domainEntityFieldConfig.getDomainEntityColumnConfig().isIdColumn())) {
                String fieldName = domainEntityFieldConfig.getFieldName();
                String fieldType = domainEntityFieldConfig.getFieldType().getFullyQualifiedNameWithoutTypeParameters();
                validateFields.append(", ").append(getGeneratedTargetName(domainEntityName, false, false)).append("::").append(CodegenUtils.getGetterMethodName(fieldName, fieldType));
            }
        }
        return validateFields.toString();
    }

    @Override
    public String getDomainObjectName() {
        return getDomainEntityName();
    }

    @Override
    public String getDomainObjectTitle() {
        return getDomainEntityTitle();
    }

    @Override
    public String getDomainObjectAlias() {
        return getDomainEntityAlias();
    }

    @Override
    public String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix) {
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + (includeSuffix ? ".java" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainEntityConfig)) return false;
        DomainEntityConfig that = (DomainEntityConfig) o;
        return domainEntityName.equals(that.domainEntityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domainEntityName);
    }

}