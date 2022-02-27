package com.penglecode.xmodule.common.codegen.mybatis;

import com.penglecode.xmodule.common.codegen.config.DomainEntityColumnConfig;
import com.penglecode.xmodule.common.codegen.config.DomainEntityConfig;
import com.penglecode.xmodule.common.codegen.config.MybatisCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.MybatisXmlMapperConfig;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.mybatis.SupportedDatabaseType;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;

import java.sql.Types;

/**
 * 基于ORACLE数据库的Mybatis模块代码生成方言
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/26 14:28
 */
public class OracleMybatisCodegenDialect implements MybatisCodegenDialect {

    @Override
    public SupportedDatabaseType getDatabaseType() {
        return SupportedDatabaseType.ORACLE;
    }

    @Override
    public String getSelectColumnClause(String columnName, CodegenContext<MybatisCodegenConfigProperties, MybatisXmlMapperConfig, DomainEntityConfig> codegenContext) {
        DomainEntityColumnConfig domainEntityColumnConfig = codegenContext.getDomainObjectConfig().getDomainEntityColumns().get(columnName);
        String columnSelects = QueryCriteria.TABLE_ALIAS_NAME + "." + domainEntityColumnConfig.getColumnName();
        int jdbcType = domainEntityColumnConfig.getIntrospectedColumn().getJdbcType();
        if(Types.TIMESTAMP == jdbcType) {
            return "TO_CHAR(" + columnSelects + ", 'yyyy-mm-dd hh24:mi:ss')";
        } else if (Types.DATE == jdbcType) {
            return "TO_CHAR(" + columnSelects + ", 'yyyy-mm-dd')";
        }
        return columnSelects;
    }

}
