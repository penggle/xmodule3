package com.penglecode.xmodule.common.codegen.mybatis;

import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.mybatis.DatabaseType;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;

import java.sql.Types;

/**
 * 基于ORACLE数据库的Mybatis模块代码生成方言
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/9/20 0:28
 */
public class OracleMybatisCodegenDialect implements MybatisCodegenDialect {

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.ORACLE;
    }

    @Override
    public String getSelectColumnClause(CodegenContext codegenContext) {
        String columnSelects = QueryCriteria.TABLE_ALIAS_NAME + "." + codegenContext.getDomainObjectColumnConfig().getColumnName();
        int jdbcType = codegenContext.getDomainObjectColumnConfig().getIntrospectedColumn().getJdbcType();
        if(Types.TIMESTAMP == jdbcType) {
            return "TO_CHAR(" + columnSelects + ", 'yyyy-mm-dd hh24:mi:ss')";
        } else if (Types.DATE == jdbcType) {
            return "TO_CHAR(" + columnSelects + ", 'yyyy-mm-dd')";
        }
        return columnSelects;
    }

}
