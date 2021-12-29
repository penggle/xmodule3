package com.penglecode.xmodule.common.mybatis.interceptor;

import com.penglecode.xmodule.common.mybatis.DatabaseType;
import com.penglecode.xmodule.common.mybatis.MySQLPageDialect;
import com.penglecode.xmodule.common.mybatis.OraclePageDialect;
import com.penglecode.xmodule.common.mybatis.PageDialect;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

/**
 * Mybatis分页插件-在执行sql前将原始sql转换为分页sql
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
@Intercepts({@Signature(type= StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PaginationInterceptor implements Interceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaginationInterceptor.class);

	private String dialectType;

	private PageDialect pageDialect;

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
		MetaObject statementHandlerMeta = MetaObject.forObject(statementHandler, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
		RowBounds rowBounds = (RowBounds)statementHandlerMeta.getValue("delegate.rowBounds");
		if(rowBounds == null || rowBounds == RowBounds.DEFAULT){
			return invocation.proceed();
		}
		PageDialect dialectToUse = getPageDialect();
		if(dialectToUse != null) {
			String originalSql = statementHandlerMeta.getValue("delegate.boundSql.sql").toString();
			statementHandlerMeta.setValue("delegate.boundSql.sql", dialectToUse.getPageSql(originalSql, rowBounds.getOffset(), rowBounds.getLimit()));
			statementHandlerMeta.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
			statementHandlerMeta.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
		} else {
			LOGGER.warn("No PageDialect implementation available for dialect: {}", dialectType);
		}
		return invocation.proceed();
	}

	protected void initPageDialect(Properties properties) {
		dialectType = properties.getProperty("dialect");
		pageDialect = determinePageDialect(dialectType);
	}

	protected PageDialect determinePageDialect(String dialectType) {
		DatabaseType databaseType = DatabaseType.of(dialectType);
		PageDialect dialect = null;
		if (DatabaseType.MYSQL.equals(databaseType)) { // MySQL分页
			dialect = new MySQLPageDialect();
		} else if(DatabaseType.ORACLE.equals(databaseType)) { // Oracle分页
			dialect = new OraclePageDialect();
		}
		return dialect;
	}

	protected PageDialect getPageDialect() {
		return pageDialect;
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
