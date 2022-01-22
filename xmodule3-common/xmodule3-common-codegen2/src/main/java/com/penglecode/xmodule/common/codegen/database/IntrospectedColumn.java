package com.penglecode.xmodule.common.codegen.database;

import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;

/**
 * 自省表的列元数据
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 11:16
 */
public class IntrospectedColumn {

    /** 列名 */
    private String columnName;

    /** 对应数据库列的类型 */
    private String columnType;

    /** 列的注释 */
    private String columnComment;

    /** 是否可为空 */
    private boolean nullable;

    /** 列长度 */
    private int length;

    /** 列精度(当为浮点型类型时) */
    private int scale;

    /** 列的默认值 */
    private String defaultValue;

    /** 是否是自增列 */
    private boolean autoIncrement;

    /** BLOB类型? */
    private boolean blobType;

    /** JDBC类型,见java.sql.Types */
    private int jdbcType;

    /** JDBC类型名称 */
    private String jdbcTypeName;

    /** 对应的Java字段类型 */
    private FullyQualifiedJavaType javaFieldType;

    /** 对应的Java字段名称 */
    private String javaFieldName;

    private IntrospectedTable introspectedTable;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName.toLowerCase();
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isBlobType() {
        return blobType;
    }

    public void setBlobType(boolean blobType) {
        this.blobType = blobType;
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(int jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getJdbcTypeName() {
        return jdbcTypeName;
    }

    public void setJdbcTypeName(String jdbcTypeName) {
        this.jdbcTypeName = jdbcTypeName;
    }

    public FullyQualifiedJavaType getJavaFieldType() {
        return javaFieldType;
    }

    public void setJavaFieldType(FullyQualifiedJavaType javaFieldType) {
        this.javaFieldType = javaFieldType;
    }

    public String getJavaFieldName() {
        return javaFieldName;
    }

    public void setJavaFieldName(String javaFieldName) {
        this.javaFieldName = javaFieldName;
    }

    public IntrospectedTable getIntrospectedTable() {
        return introspectedTable;
    }

    public void setIntrospectedTable(IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntrospectedColumn)) return false;
        IntrospectedColumn that = (IntrospectedColumn) o;
        if (!columnName.equals(that.columnName)) return false;
        return introspectedTable.equals(that.introspectedTable);
    }

    @Override
    public int hashCode() {
        int result = columnName.hashCode();
        result = 31 * result + introspectedTable.hashCode();
        return result;
    }
}
