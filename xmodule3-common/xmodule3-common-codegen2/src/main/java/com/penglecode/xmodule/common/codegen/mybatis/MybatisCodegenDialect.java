package com.penglecode.xmodule.common.codegen.mybatis;

import com.penglecode.xmodule.common.codegen.config.DomainEntityConfig;
import com.penglecode.xmodule.common.codegen.config.MybatisCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.MybatisXmlMapperConfig;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.mybatis.SupportedDatabaseType;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import org.springframework.core.Ordered;

/**
 * Mybatis代码生成方言
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/26 21:19
 */
public interface MybatisCodegenDialect extends Ordered {

    /**
     * 返回方言对应的数据库类型
     * @return
     */
    SupportedDatabaseType getDatabaseType();

    /**
     * 获取指定数据库列的select列子句,例如a.user_name、DATE_FORMAT(a.create_time, '%Y-%m-%d %T')等
     *
     * @param columnName        - 列名
     * @param codegenContext    - 代码生成上下文
     * @return
     */
    default String getSelectColumnClause(String columnName, CodegenContext<MybatisCodegenConfigProperties,MybatisXmlMapperConfig,DomainEntityConfig> codegenContext) {
        return QueryCriteria.TABLE_ALIAS_NAME + "." + codegenContext.getDomainObjectConfig().getDomainEntityColumns().get(columnName).getColumnName();
    }

    /**
     * 获取DELETE语句中目标表的别名,例如DELETE ${deleteTargetAliasName} FROM t_test t WHERE 1=1;在MySQL中deleteTargetAliasName为t
     *
     * @param codegenContext
     * @return
     */
    default String getDeleteTargetAliasName(CodegenContext<MybatisCodegenConfigProperties,MybatisXmlMapperConfig,DomainEntityConfig> codegenContext) {
        return "";
    }

    /**
     * 返回游标查询的fetchSize(默认1000)
     *
     * @param codegenContext
     * @return
     */
    default int getCursorFetchSize(CodegenContext<MybatisCodegenConfigProperties,MybatisXmlMapperConfig,DomainEntityConfig> codegenContext) {
        return 1000;
    }

    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
