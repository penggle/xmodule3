package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.database.IntrospectedColumn;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.codegen.support.IdGenerator;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 领域实体对应的数据库表列配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/22 13:36
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

    public void setIdGenerator(String idGenerator) {
        this.idGenerator = IdGenerator.parseGenerator(idGenerator);
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

    public void setColumnOnUpsert(boolean columnOnUpsert) {
        this.setColumnOnInsert(true);
        this.setColumnOnUpdate(true);
    }

    public void setValidateOnUpsert(boolean validateOnUpsert) {
        this.setValidateOnInsert(true);
        this.setValidateOnUpdate(true);
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

    public void initValidateExpressions(DomainConfig domainConfig) {
        Set<String> finalValidateExpressions = Optional.ofNullable(validateExpressions).orElseGet(() -> {
                    Set<String> expressions = new HashSet<>();
                    if(validateOnInsert || validateOnUpdate) {
                        Class<?> validatorType = FullyQualifiedJavaType.getStringInstance().equals(introspectedColumn.getJavaFieldType()) ? NotBlank.class : NotNull.class;
                        expressions.add(String.format("@%s(message=\"%s\")", validatorType.getName(), getColumnTitle() + "不能为空!"));
                    }
                    return expressions;
                })
                .stream()
                .filter(StringUtils::isNotBlank)
                .map(expression -> CodegenUtils.parseAnnotationExpression(expression, domainConfig))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        this.setValidateExpressions(finalValidateExpressions);
    }

}