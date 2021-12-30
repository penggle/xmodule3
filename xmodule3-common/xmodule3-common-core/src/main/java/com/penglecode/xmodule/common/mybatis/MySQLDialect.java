package com.penglecode.xmodule.common.mybatis;
/**
 * 数据库分页之mysql
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
public class MySQLDialect implements SqlDialect {

	@Override
	public String getPageSql(String sql, int offset, int limit) {
		return sql + " LIMIT " + offset + ", " + limit;
	}

	@Override
	public String getLimitSql(String sql, int limit) {
		String upperSql = sql.toUpperCase();
		if(upperSql.startsWith("SELECT") || upperSql.startsWith("UPDATE") || upperSql.startsWith("DELETE")) {
			return sql + " LIMIT " + limit;
		}
		return sql;
	}

}
