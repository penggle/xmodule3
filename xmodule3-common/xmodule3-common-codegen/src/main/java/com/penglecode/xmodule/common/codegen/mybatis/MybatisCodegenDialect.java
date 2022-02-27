package com.penglecode.xmodule.common.codegen.mybatis;

import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.mybatis.SupportedDatabaseType;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import org.springframework.core.Ordered;

/**
 * Mybatis代码生成方言
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/9/19 21:19
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
     * @param codegenContext
     * @return
     */
    default String getSelectColumnClause(CodegenContext codegenContext) {
        return QueryCriteria.TABLE_ALIAS_NAME + "." + codegenContext.getDomainObjectColumnConfig().getColumnName();
    }

    /**
     * 获取DELETE语句中目标表的别名,例如DELETE ${deleteTableAliasName} FROM t_test t WHERE 1=1;在MySQL中deleteTableAliasName为t
     *
     * @param codegenContext
     * @return
     */
    default String getDeleteTableAliasName(CodegenContext codegenContext) {
        return "";
    }

    /**
     * 返回游标查询的fetchSize(默认1000)
     *
     * @param codegenContext
     * @return
     */
    default int getCursorFetchSize(CodegenContext codegenContext) {
        return 1000;
    }

    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
