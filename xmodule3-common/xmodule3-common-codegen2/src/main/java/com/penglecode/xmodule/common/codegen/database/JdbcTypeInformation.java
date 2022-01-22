package com.penglecode.xmodule.common.codegen.database;

import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;

/**
 * JDBC类型信息
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 11:16
 */
public class JdbcTypeInformation {

    /** JDBC类型名称 */
    private final String jdbcTypeName;

    /** 对应的Java类型 */
    private final FullyQualifiedJavaType javaType;

    public JdbcTypeInformation(String jdbcTypeName, FullyQualifiedJavaType javaType) {
        this.jdbcTypeName = jdbcTypeName;
        this.javaType = javaType;
    }

    public String getJdbcTypeName() {
        return jdbcTypeName;
    }

    public FullyQualifiedJavaType getJavaType() {
        return javaType;
    }

}
