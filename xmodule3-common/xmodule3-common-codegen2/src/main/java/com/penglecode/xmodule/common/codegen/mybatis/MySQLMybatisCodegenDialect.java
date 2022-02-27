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
 * 基于MySQL数据库的Mybatis模块代码生成方言
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/26 14:28
 */
public class MySQLMybatisCodegenDialect implements MybatisCodegenDialect {

    @Override
    public SupportedDatabaseType getDatabaseType() {
        return SupportedDatabaseType.MYSQL;
    }

    @Override
    public String getSelectColumnClause(String columnName, CodegenContext<MybatisCodegenConfigProperties, MybatisXmlMapperConfig, DomainEntityConfig> codegenContext) {
        DomainEntityColumnConfig domainEntityColumnConfig = codegenContext.getDomainObjectConfig().getDomainEntityColumns().get(columnName);
        String columnSelects = QueryCriteria.TABLE_ALIAS_NAME + "." + domainEntityColumnConfig.getColumnName();
        int jdbcType = domainEntityColumnConfig.getIntrospectedColumn().getJdbcType();
        if(Types.TIMESTAMP == jdbcType) {
            return "DATE_FORMAT(" + columnSelects + ", '%Y-%m-%d %T')";
        } else if (Types.DATE == jdbcType) {
            return "DATE_FORMAT(" + columnSelects + ", '%Y-%m-%d')";
        }
        return columnSelects;
    }

    @Override
    public String getDeleteTargetAliasName(CodegenContext<MybatisCodegenConfigProperties,MybatisXmlMapperConfig,DomainEntityConfig> codegenContext) {
        return QueryCriteria.TABLE_ALIAS_NAME;
    }

}
