package com.penglecode.xmodule.common.mybatis;
/**
 * 数据库分页之mysql
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
public class MySQLPageDialect implements PageDialect {

	public String getPageSql(String sql, int offset, int limit) {
		return sql + " LIMIT " + offset + ", " + limit;
	}

}
