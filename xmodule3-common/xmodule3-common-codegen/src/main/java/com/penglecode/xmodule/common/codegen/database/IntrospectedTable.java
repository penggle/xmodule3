package com.penglecode.xmodule.common.codegen.database;

import java.util.ArrayList;
import java.util.List;

/**
 * 自省数据库表元数据
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/9/3 21:58
 */
public class IntrospectedTable extends TableIndentity {

    /** 对应的领域对象名称 */
    private String domainObjectName;

    /** 表的注释 */
    private String tableComment;

    /** 表类型 */
    private String tableType;

    /** 主键列 */
    private List<IntrospectedColumn> pkColumns = new ArrayList<>();

    /** 所有列 */
    private List<IntrospectedColumn> allColumns = new ArrayList<>();

    public IntrospectedTable(String catalog, String schema, String tableName) {
        super(catalog, schema, tableName);
    }

    public String getDomainObjectName() {
        return domainObjectName;
    }

    public void setDomainObjectName(String domainObjectName) {
        this.domainObjectName = domainObjectName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public List<IntrospectedColumn> getPkColumns() {
        return pkColumns;
    }

    public void setPkColumns(List<IntrospectedColumn> pkColumns) {
        this.pkColumns = pkColumns;
    }

    public List<IntrospectedColumn> getAllColumns() {
        return allColumns;
    }

    public void setAllColumns(List<IntrospectedColumn> allColumns) {
        this.allColumns = allColumns;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
