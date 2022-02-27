package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.database.IntrospectedTable;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 具体领域对象配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/7/25 13:43
 */
public class DomainObjectConfigProperties extends GeneratedTargetConfigProperties {

    /** 领域对象类名 */
    private String domainObjectName;

    /** 领域对象对应的数据库表名 */
    private String domainTableName;

    /** 领域对象中文名称 */
    private String domainObjectTitle;

    /** 领域对象别名(定义Service/Controller方法名、url等时用到) */
    private String domainObjectAlias;

    /** 运行时数据源名称 */
    private String runtimeDataSource;

    /** 领域对象数据库自省配置 */
    private DomainIntrospectConfigProperties introspectConfig = new DomainIntrospectConfigProperties();

    /** 领域对象对应的数据库表的所有列*/
    private Map<String, DomainObjectColumnConfigProperties> domainObjectColumns;

    /** 领域对象对应的Model类的所有字段*/
    private Map<String, DomainObjectFieldConfigProperties> domainObjectFields;

    private DomainMasterSlaveConfigProperties domainMasterSlaveConfig;

    /** 对应的自省表 */
    private IntrospectedTable introspectedTable;

    public String getDomainObjectName() {
        return domainObjectName;
    }

    public void setDomainObjectName(String domainObjectName) {
        this.domainObjectName = domainObjectName;
    }

    public String getDomainTableName() {
        return domainTableName;
    }

    public void setDomainTableName(String domainTableName) {
        this.domainTableName = domainTableName;
    }

    public String getDomainObjectTitle() {
        return domainObjectTitle;
    }

    public void setDomainObjectTitle(String domainObjectTitle) {
        this.domainObjectTitle = domainObjectTitle;
    }

    public String getDomainObjectAlias() {
        return domainObjectAlias;
    }

    public void setDomainObjectAlias(String domainObjectAlias) {
        this.domainObjectAlias = domainObjectAlias;
    }

    public String getRuntimeDataSource() {
        return runtimeDataSource;
    }

    public void setRuntimeDataSource(String runtimeDataSource) {
        this.runtimeDataSource = runtimeDataSource;
    }

    public DomainIntrospectConfigProperties getIntrospectConfig() {
        return introspectConfig;
    }

    public void setIntrospectConfig(DomainIntrospectConfigProperties introspectConfig) {
        this.introspectConfig = introspectConfig;
    }

    public Map<String, DomainObjectColumnConfigProperties> getDomainObjectColumns() {
        return domainObjectColumns;
    }

    public void setDomainObjectColumns(Set<DomainObjectColumnConfigProperties> domainObjectColumns) {
        this.domainObjectColumns = new LinkedHashMap<>();
        if(!CollectionUtils.isEmpty(domainObjectColumns)) {
            for(DomainObjectColumnConfigProperties domainObjectColumn : domainObjectColumns) {
                this.domainObjectColumns.put(domainObjectColumn.getColumnName().toLowerCase(), domainObjectColumn);
            }
        }
    }

    public Map<String, DomainObjectFieldConfigProperties> getDomainObjectFields() {
        return domainObjectFields;
    }

    public void setDomainObjectFields(Map<String, DomainObjectFieldConfigProperties> domainObjectFields) {
        this.domainObjectFields = domainObjectFields;
    }

    public DomainMasterSlaveConfigProperties getDomainMasterSlaveConfig() {
        return domainMasterSlaveConfig;
    }

    public void setDomainMasterSlaveConfig(DomainMasterSlaveConfigProperties domainMasterSlaveConfig) {
        this.domainMasterSlaveConfig = domainMasterSlaveConfig;
    }

    public IntrospectedTable getIntrospectedTable() {
        return introspectedTable;
    }

    public void setIntrospectedTable(IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }

    /**
     * 返回领域对象的主键列
     * @return
     */
    public List<DomainObjectColumnConfigProperties> getPkColumns() {
        return domainObjectColumns.values().stream().filter(DomainObjectColumnConfigProperties::isPrimaryKey).collect(Collectors.toList());
    }

    /**
     * 返回领域对象的主键字段
     * @return
     */
    public List<DomainObjectFieldConfigProperties> getPkFields() {
        return domainObjectFields.values().stream().filter(DomainObjectFieldConfigProperties::isPkField).collect(Collectors.toList());
    }

    @Override
    public <T extends GeneratedTargetConfigProperties> String getGeneratedTargetName(DomainBoundedTargetConfigProperties<T> targetConfig, boolean includePackage, boolean includeSuffix) {
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + (includeSuffix ? ".java" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainObjectConfigProperties)) return false;
        DomainObjectConfigProperties that = (DomainObjectConfigProperties) o;
        return domainObjectName.equals(that.domainObjectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domainObjectName);
    }

}
