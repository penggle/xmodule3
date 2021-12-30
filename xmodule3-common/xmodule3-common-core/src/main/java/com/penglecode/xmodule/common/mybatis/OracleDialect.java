package com.penglecode.xmodule.common.mybatis;
/**
 * oracle数据库分页实现类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
public class OracleDialect implements SqlDialect {

	public static final String DEFAULT_ROWNUM_COLUMN_NAME = "rn_";
	
	private static final String DEFAULT_PAGING_SQL_FORMAT = "SELECT *"
														   + " FROM (SELECT rownum rn_, page_inner_table.*"
														           + " FROM (%s) page_inner_table"
														          + " WHERE rownum <= %s) page_outer_table"
														  + " WHERE page_outer_table.rn_ > %s";

	@Override
	public String getPageSql(String sql, int offset, int limit) {
		return String.format(DEFAULT_PAGING_SQL_FORMAT, sql, offset + limit, offset);
	}

	@Override
	public String getLimitSql(String sql, int limit) {
		String upperSql = sql.toUpperCase();
		if(upperSql.startsWith("SELECT")) {
			return "SELECT * FROM (" + sql + ") WHERE rownum <= " + limit;
		} else if(upperSql.startsWith("UPDATE") || upperSql.startsWith("DELETE")) { //TODO //此分支实现对于复杂SQL可能会存在问题
			if(upperSql.contains(" WHERE ")) {
				return sql + " AND rownum <= " + limit;
			} else {
				return sql + " WHERE rownum <= " + limit;
			}
		}
		return sql;
	}

}
