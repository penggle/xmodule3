package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.database.IntrospectedColumn;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.codegen.support.PrimaryKeyGenStrategy;
import com.penglecode.xmodule.common.codegen.support.PrimaryKeyGenerator;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 领域对象的列配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/8/20 21:47
 */
public class DomainObjectColumnConfigProperties {

    /** 列名 */
    private String columnName;

    /** 列中文名 */
    private String columnTitle;

    /** 是否是注解 */
    private boolean primaryKey;

    /** 主键生成策略 */
    private PrimaryKeyGenerator primaryKeyGenerator;

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

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public PrimaryKeyGenerator getPrimaryKeyGenerator() {
        return primaryKeyGenerator;
    }

    /**
     * 目前仅支持两种方式的数据库主键生成器：
     * 1、IDENTITY : 自增主键生成
     * 2、SEQUENCE(t_test_sequence)
     *
     * @param primaryKeyGenerator
     */
    public void setPrimaryKeyGenerator(String primaryKeyGenerator) {
        if(StringUtils.isNotBlank(primaryKeyGenerator)) {
            Pattern pattern = Pattern.compile("([0-9A-Z_]+)\\((\\w*)\\)");
            Matcher matcher = pattern.matcher(primaryKeyGenerator);
            if(matcher.matches()) {
                String strategy = matcher.group(1);
                String parameter = StringUtils.defaultIfBlank(matcher.group(2), "");
                if(PrimaryKeyGenStrategy.IDENTITY.name().equals(strategy)) {
                    this.primaryKeyGenerator = new PrimaryKeyGenerator(PrimaryKeyGenStrategy.IDENTITY, "");
                } else if(PrimaryKeyGenStrategy.SEQUENCE.name().equals(strategy)) {
                    this.primaryKeyGenerator = new PrimaryKeyGenerator(PrimaryKeyGenStrategy.SEQUENCE, parameter);
                }
            }
        }
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

    public void prepareValidateExpressions(DomainConfigProperties domainConfigProperties) {
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
                .map(expression -> CodegenUtils.parseAnnotationExpression(expression, domainConfigProperties))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        this.setValidateExpressions(finalValidateExpressions);
    }

}
