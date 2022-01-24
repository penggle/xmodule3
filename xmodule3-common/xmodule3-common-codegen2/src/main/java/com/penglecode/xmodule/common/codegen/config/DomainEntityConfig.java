package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.database.IntrospectedTable;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 领域实体配置，领域实体直接对应着数据库中的表
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 13:30
 */
public class DomainEntityConfig extends GenerableTargetConfig {

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
     * 返回领域对象的ID字段
     * @return
     */
    public List<DomainEntityFieldConfig> getIdFields() {
        return domainEntityFields.values().stream().filter(DomainEntityFieldConfig::isIdField).collect(Collectors.toList());
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