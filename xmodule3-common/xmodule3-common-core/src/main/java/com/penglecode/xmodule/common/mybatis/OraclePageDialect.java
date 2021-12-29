package com.penglecode.xmodule.common.mybatis;
/**
 * oracle数据库分页实现类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
public class OraclePageDialect implements PageDialect {

	public static final String DEFAULT_ROWNUM_COLUMN_NAME = "rn_";
	
	private static final String DEFAULT_PAGING_SQL_FORMAT = "SELECT *"
														   + " FROM (SELECT rownum rn_, page_inner_tab.*"
														           + " FROM (%s) page_inner_tab"
														          + " WHERE rownum <= %s) page_outer_tab"
														  + " WHERE page_outer_tab.rn_ > %s";
	
	public String getPageSql(String sql, int offset, int limit) {
		return String.format(DEFAULT_PAGING_SQL_FORMAT, sql, offset + limit, offset);
	}

}
