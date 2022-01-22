package com.penglecode.xmodule.common.codegen.database;

import java.util.ArrayList;
import java.util.List;

/**
 * 自省数据库表元数据
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 11:16
 */
public class IntrospectedTable extends TableIndentity {

    /** 对应的领域实体名称 */
    private String domainEntityName;

    /** 表的注释 */
    private String tableComment;

    /** 表类型 */
    private String tableType;

    /** ID列 */
    private List<IntrospectedColumn> idColumns = new ArrayList<>();

    /** 所有列 */
    private List<IntrospectedColumn> allColumns = new ArrayList<>();

    public IntrospectedTable(String catalog, String schema, String tableName) {
        super(catalog, schema, tableName);
    }

    public String getDomainEntityName() {
        return domainEntityName;
    }

    public void setDomainEntityName(String domainEntityName) {
        this.domainEntityName = domainEntityName;
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

    public List<IntrospectedColumn> getIdColumns() {
        return idColumns;
    }

    public void setIdColumns(List<IntrospectedColumn> idColumns) {
        this.idColumns = idColumns;
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
