package com.penglecode.xmodule.common.mybatis.interceptor;

import com.penglecode.xmodule.common.mybatis.DatabaseType;
import com.penglecode.xmodule.common.mybatis.MySQLDialect;
import com.penglecode.xmodule.common.mybatis.OracleDialect;
import com.penglecode.xmodule.common.mybatis.SqlDialect;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import com.penglecode.xmodule.common.util.CollectionUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

/**
 * 处理Mybatis分页及基于QueryCriteria#limit(int)实现数据条数限制的拦截器
 * 因为这两者是互斥的，所以可以放在一个拦截器中处理
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
@Intercepts({@Signature(type= StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageLimitInterceptor implements Interceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PageLimitInterceptor.class);

	/**
	 * 这里需要保持与BaseXxxMapper中的@Param参数名一致
	 */
	private static final String DEFAULT_QUERY_CRITERIA_PARAM_NAME = "criteria";

	private String dialectType;

	private SqlDialect sqlDialect;

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
		BoundSql boundSql = statementHandler.getBoundSql(); //获取绑定sql
		MetaObject statementHandlerMeta = MetaObject.forObject(statementHandler, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
		RowBounds rowBounds = (RowBounds) statementHandlerMeta.getValue("delegate.rowBounds");
		if(rowBounds == null || rowBounds == RowBounds.DEFAULT) { //如果当前不分页则需要处理QueryCriteria#limit(int)条件
			//开始处理QueryCriteria.limit(xx)逻辑
			Integer limit = getLimitOfCriteria(boundSql);
			if(limit != null && limit > 0) {
				String originalSql = boundSql.getSql();
				statementHandlerMeta.setValue("delegate.boundSql.sql", sqlDialect.getLimitSql(originalSql, limit));
			}
			return invocation.proceed();
		}
		//反之，如果当前存在分页，则忽略QueryCriteria#limit(int)条件
		//开始处理分页逻辑
		SqlDialect sqlDialect = getSqlDialect();
		if(sqlDialect != null) {
			String originalSql = boundSql.getSql();
			statementHandlerMeta.setValue("delegate.boundSql.sql", sqlDialect.getPageSql(originalSql, rowBounds.getOffset(), rowBounds.getLimit()));
			statementHandlerMeta.setValue("delegate.rowBounds", RowBounds.DEFAULT);
		} else {
			LOGGER.warn("No SqlDialect implementation available for dialect: {}", dialectType);
		}
		return invocation.proceed();
	}

	protected void initPageDialect(Properties properties) {
		dialectType = properties.getProperty("dialect");
		sqlDialect = determinePageDialect(dialectType);
	}

	protected SqlDialect determinePageDialect(String dialectType) {
		DatabaseType databaseType = DatabaseType.of(dialectType);
		SqlDialect dialect = null;
		if (DatabaseType.MYSQL.equals(databaseType)) { // MySQL分页
			dialect = new MySQLDialect();
		} else if(DatabaseType.ORACLE.equals(databaseType)) { // Oracle分页
			dialect = new OracleDialect();
		}
		return dialect;
	}

	/**
	 * 如果绑定参数中存在QueryCriteria
	 * @param boundSql
	 * @return
	 */
	protected Integer getLimitOfCriteria(BoundSql boundSql) {
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if(!CollectionUtils.isEmpty(parameterMappings)) {
			for(ParameterMapping parameterMapping : parameterMappings) {
				if(parameterMapping.getMode() != ParameterMode.OUT) {
					if(boundSql.hasAdditionalParameter(DEFAULT_QUERY_CRITERIA_PARAM_NAME)) {
						Object paramValue = boundSql.getAdditionalParameter(DEFAULT_QUERY_CRITERIA_PARAM_NAME);
						if(paramValue instanceof QueryCriteria) {
							return ((QueryCriteria<?>) paramValue).getLimit();
						}
					}
				}
			}
		}
		return null;
	}

	protected SqlDialect getSqlDialect() {
		return sqlDialect;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		initPageDialect(properties);
	}

}
