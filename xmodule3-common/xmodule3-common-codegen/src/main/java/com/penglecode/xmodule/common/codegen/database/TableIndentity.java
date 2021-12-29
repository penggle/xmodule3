package com.penglecode.xmodule.common.codegen.database;

import java.util.Objects;

/**
 * 数据库表唯一身份
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/9/5 18:57
 */
public class TableIndentity {

    /** 数据库所属目录 */
    private final String catalog;

    /** 表所属的数据库名称 */
    private final String schema;

    /** 表名 */
    private final String tableName;

    public TableIndentity(String catalog, String schema, String tableName) {
        this.catalog = catalog;
        this.schema = schema;
        this.tableName = tableName;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableIndentity)) return false;

        TableIndentity that = (TableIndentity) o;

        if (!Objects.equals(catalog, that.catalog)) return false;
        if (!Objects.equals(schema, that.schema)) return false;
        return tableName.equals(that.tableName);
    }

    @Override
    public int hashCode() {
        int result = catalog != null ? catalog.hashCode() : 0;
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        result = 31 * result + tableName.hashCode();
        return result;
    }

}
