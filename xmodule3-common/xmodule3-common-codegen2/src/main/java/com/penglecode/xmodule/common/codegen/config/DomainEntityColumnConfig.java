package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.database.IntrospectedColumn;
import com.penglecode.xmodule.common.codegen.support.IdGenerator;

import java.util.Set;

/**
 * 领域实体对应的数据库表列配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 13:36
 */
public class DomainEntityColumnConfig {

    /** 列名 */
    private String columnName;

    /** 列中文名 */
    private String columnTitle;

    /** 是否是ID列 */
    private boolean idColumn;

    /** 主键生成策略 */
    private IdGenerator idGenerator;

    /** 是否是插入列 */
    private boolean columnOnInsert;

    /** 插入时是否需要校验 */
    private boolean validateOnInsert;

    /** 是否是更新列 */
    private boolean columnOnUpdate;

    /** 更新时是否需要校验 */
    private boolean validateOnUpdate;

    /** 校验表达式 */
    private Set<String> validateExpressions;

    /** 不为空则当前列为查询条件列，该条件运算符支持eq,like|likeLeft|likeRight,gt|lt|gte|lte,in等 */
    private String operatorOnQuery;

    /** 当前字段decode所需的枚举类型 */
    private String decodeEnumType;

    /** 覆盖当前列的自省Java类型 */
    private Class<?> javaTypeOverride;

    /** 对应的自省列 */
    private IntrospectedColumn introspectedColumn;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public boolean isIdColumn() {
        return idColumn;
    }

    public void setIdColumn(boolean idColumn) {
        this.idColumn = idColumn;
    }

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public boolean isColumnOnInsert() {
        return columnOnInsert;
    }

    public void setColumnOnInsert(boolean columnOnInsert) {
        this.columnOnInsert = columnOnInsert;
    }

    public boolean isValidateOnInsert() {
        return validateOnInsert;
    }

    public void setValidateOnInsert(boolean validateOnInsert) {
        this.validateOnInsert = validateOnInsert;
    }

    public boolean isColumnOnUpdate() {
        return columnOnUpdate;
    }

    public void setColumnOnUpdate(boolean columnOnUpdate) {
        this.columnOnUpdate = columnOnUpdate;
    }

    public boolean isValidateOnUpdate() {
        return validateOnUpdate;
    }

    public void setValidateOnUpdate(boolean validateOnUpdate) {
        this.validateOnUpdate = validateOnUpdate;
    }

    public Set<String> getValidateExpressions() {
        return validateExpressions;
    }

    public void setValidateExpressions(Set<String> validateExpressions) {
        this.validateExpressions = validateExpressions;
    }

    public String getOperatorOnQuery() {
        return operatorOnQuery;
    }

    public void setOperatorOnQuery(String operatorOnQuery) {
        this.operatorOnQuery = operatorOnQuery;
    }

    public String getDecodeEnumType() {
        return decodeEnumType;
    }

    public void setDecodeEnumType(String decodeEnumType) {
        this.decodeEnumType = decodeEnumType;
    }

    public Class<?> getJavaTypeOverride() {
        return javaTypeOverride;
    }

    public void setJavaTypeOverride(Class<?> javaTypeOverride) {
        this.javaTypeOverride = javaTypeOverride;
    }

    public IntrospectedColumn getIntrospectedColumn() {
        return introspectedColumn;
    }

    public void setIntrospectedColumn(IntrospectedColumn introspectedColumn) {
        this.introspectedColumn = introspectedColumn;
    }

}