package com.penglecode.xmodule.common.codegen.mybatis;

import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.mybatis.SupportedDatabaseType;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;

import java.sql.Types;

/**
 * 基于MySQL数据库的Mybatis模块代码生成方言
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/9/20 0:28
 */
public class MySQLMybatisCodegenDialect implements MybatisCodegenDialect {

    @Override
    public SupportedDatabaseType getDatabaseType() {
        return SupportedDatabaseType.MYSQL;
    }

    @Override
    public String getSelectColumnClause(CodegenContext codegenContext) {
        String columnSelects = QueryCriteria.TABLE_ALIAS_NAME + "." + codegenContext.getDomainObjectColumnConfig().getColumnName();
        int jdbcType = codegenContext.getDomainObjectColumnConfig().getIntrospectedColumn().getJdbcType();
        if(Types.TIMESTAMP == jdbcType) {
            return "DATE_FORMAT(" + columnSelects + ", '%Y-%m-%d %T')";
        } else if (Types.DATE == jdbcType) {
            return "DATE_FORMAT(" + columnSelects + ", '%Y-%m-%d')";
        }
        return columnSelects;
    }

    @Override
    public String getDeleteTableAliasName(CodegenContext codegenContext) {
        return QueryCriteria.TABLE_ALIAS_NAME;
    }

}
