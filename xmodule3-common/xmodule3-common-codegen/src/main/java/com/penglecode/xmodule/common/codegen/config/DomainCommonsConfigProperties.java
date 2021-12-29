package com.penglecode.xmodule.common.codegen.config;

import org.springframework.boot.autoconfigure.dal.NamedDatabase;

import java.util.*;

/**
 * 领域对象公共配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/25 15:20
 */
public class DomainCommonsConfigProperties extends GeneratedTargetConfigProperties {

    /** 注释作者 */
    private String commentAuthor;

    /** 运行时数据源名称 */
    private String runtimeDataSource;

    /** 自省数据源名称 */
    private String introspectDataSource;

    /** 领域对象数据库自省配置 */
    private DomainIntrospectConfigProperties introspectConfig = new DomainIntrospectConfigProperties();

    /** 领域下面的枚举定义(这个定义建议集中定义在primaryDomainObject=true的领域对象下面) */
    private Set<DomainEnumConfigProperties> domainEnums;

    /** 全局性的类型集合,以便后续省去使用带包名的全名称配置引用 */
    private Map<String,String> globalTypes;

    /** 默认的createTime列名 */
    private String defaultCreateTimeColumn = "create_time";

    /** 默认的updateTime列名 */
    private String defaultUpdateTimeColumn = "update_time";

    public DomainCommonsConfigProperties() {
        this.globalTypes = new HashMap<>();
        this.globalTypes.put(NamedDatabase.class.getSimpleName(), NamedDatabase.class.getName());
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(String commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public String getRuntimeDataSource() {
        return runtimeDataSource;
    }

    public void setRuntimeDataSource(String runtimeDataSource) {
        this.runtimeDataSource = runtimeDataSource;
    }

    public String getIntrospectDataSource() {
        return introspectDataSource;
    }

    public void setIntrospectDataSource(String introspectDataSource) {
        this.introspectDataSource = introspectDataSource;
    }

    public DomainIntrospectConfigProperties getIntrospectConfig() {
        return introspectConfig;
    }

    public void setIntrospectConfig(DomainIntrospectConfigProperties introspectConfig) {
        this.introspectConfig = introspectConfig;
    }

    public Set<DomainEnumConfigProperties> getDomainEnums() {
        return domainEnums;
    }

    public void setDomainEnums(Set<DomainEnumConfigProperties> domainEnums) {
        this.domainEnums = domainEnums;
    }

    public Map<String, String> getGlobalTypes() {
        return globalTypes;
    }

    public void setGlobalTypes(Map<String, String> globalTypes) {
        this.globalTypes = globalTypes;
    }

    public String getDefaultCreateTimeColumn() {
        return defaultCreateTimeColumn;
    }

    public void setDefaultCreateTimeColumn(String defaultCreateTimeColumn) {
        this.defaultCreateTimeColumn = defaultCreateTimeColumn == null ? null : defaultCreateTimeColumn.toLowerCase();
    }

    public String getDefaultUpdateTimeColumn() {
        return defaultUpdateTimeColumn;
    }

    public void setDefaultUpdateTimeColumn(String defaultUpdateTimeColumn) {
        this.defaultUpdateTimeColumn = defaultUpdateTimeColumn == null ? null : defaultUpdateTimeColumn.toLowerCase();
    }

    public String defaultEnumsPackage() {
        return getTargetPackage() + ".enums";
    }

    public String defaultModelPackage() {
        return getTargetPackage() + ".model";
    }

    @Override
    public <T extends GeneratedTargetConfigProperties> String getGeneratedTargetName(DomainBoundedTargetConfigProperties<T> targetConfig, boolean includePackage, boolean includeSuffix) {
        throw new UnsupportedOperationException();
    }

}
