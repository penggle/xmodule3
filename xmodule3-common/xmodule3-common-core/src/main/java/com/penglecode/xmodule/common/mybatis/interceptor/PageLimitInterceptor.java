package com.penglecode.xmodule.common.mybatis.interceptor;

import com.penglecode.xmodule.common.mybatis.SupportedDatabaseType;
import com.penglecode.xmodule.common.mybatis.MySQLDialect;
import com.penglecode.xmodule.common.mybatis.OracleDialect;
import com.penglecode.xmodule.common.mybatis.SqlDialect;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.Map;

/**
 * 处理Mybatis分页及基于QueryCriteria#limit(int)实现数据条数限制的拦截器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageLimitInterceptor implements Interceptor {

	/**
	 * 这里需要保持与BaseXxxMapper中的@Param参数名一致
	 */
	private static final String DEFAULT_QUERY_CRITERIA_PARAM_NAME = "criteria";

	private volatile SqlDialect sqlDialect;

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
		BoundSql boundSql = statementHandler.getBoundSql(); //获取绑定sql
		MetaObject metaObject = MetaObject.forObject(statementHandler, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
		SqlDialect dialect = getSqlDialect(metaObject);
		RowBounds rowBounds = (RowBounds) metaObject.getValue("delegate.rowBounds");
		if(rowBounds == null || rowBounds == RowBounds.DEFAULT) { //如果当前不分页则需要处理QueryCriteria#limit(int)条件
			//开始处理QueryCriteria.limit(xx)逻辑
			Integer limit = getLimitOfCriteria(boundSql);
			if(limit != null && limit > 0) {
				String originalSql = boundSql.getSql();
				metaObject.setValue("delegate.boundSql.sql", dialect.getLimitSql(originalSql, limit));
			}
			return invocation.proceed();
		}
		//反之，如果当前存在分页，则忽略QueryCriteria#limit(int)条件
		//开始处理分页逻辑
		String originalSql = boundSql.getSql();
		metaObject.setValue("delegate.boundSql.sql", dialect.getPageSql(originalSql, rowBounds.getOffset(), rowBounds.getLimit()));
		//metaObject.setValue("delegate.rowBounds", RowBounds.DEFAULT); //不能重置rowBounds引用为DEFAULT(应该使用下面方式设置offset和limit)，否则会出现结果集为0的问题
		metaObject.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
		metaObject.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
		return invocation.proceed();
	}

	/**
	 * 如果绑定参数中存在QueryCriteria
	 * @param boundSql
	 * @return
	 */
	protected Integer getLimitOfCriteria(BoundSql boundSql) {
		String paramName = DEFAULT_QUERY_CRITERIA_PARAM_NAME;
		Object parameterObject = boundSql.getParameterObject();
		if(parameterObject instanceof QueryCriteria) {
			return ((QueryCriteria<?>) parameterObject).getLimit();
		}
		if(parameterObject instanceof Map && ((Map<?, ?>) parameterObject).containsKey(paramName)) {
			Object paramValue = ((Map<?, ?>) parameterObject).get(paramName);
			if(paramValue instanceof QueryCriteria) {
				return ((QueryCriteria<?>) paramValue).getLimit();
			}
		}
		return null;
	}

	protected SqlDialect getSqlDialect(MetaObject metaObject) {
		if(sqlDialect == null) {
			synchronized (this) {
				if(sqlDialect == null) {
					sqlDialect = initSqlDialect(metaObject);
				}
			}
		}
		return sqlDialect;
	}

	protected SqlDialect initSqlDialect(MetaObject metaObject) {
		Configuration configuration = (Configuration) metaObject.getValue("delegate.configuration");
		String dialect = configuration.getVariables().getProperty("dialect");
		SupportedDatabaseType supportedDatabaseType = SupportedDatabaseType.of(dialect);
		if (SupportedDatabaseType.MYSQL.equals(supportedDatabaseType)) { // MySQL分页
			return new MySQLDialect();
		} else if(SupportedDatabaseType.ORACLE.equals(supportedDatabaseType)) { // Oracle分页
			return new OracleDialect();
		} else {
			throw new IllegalStateException("No 'dialect' property found in Mybatis Configuration!");
		}
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

}
