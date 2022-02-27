package com.penglecode.xmodule.common.codegen.mybatis;

import com.penglecode.xmodule.common.codegen.support.CodegenParameter;

import java.util.List;

/**
 * 领域实体的Mybatis XML-Mapper代码生成参数
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/6 21:48
 */
public class MybatisXmlMapperCodegenParameter extends CodegenParameter {

    /** 领域对象名称 */
    private String domainObjectName;

    /** Mapper的Namespace */
    private String mapperNamespace;

    /** MapperHelper类名 */
    private String mapperHelperClass;

    /** 领域实体对应的表名 */
    private String domainEntityTable;

    /** 领域实体对应表的别名 */
    private String tableAliasName;

    /** 删除操作时目标表的别名 */
    private String deleteTargetAliasName;

    /** 游标操作的fetchSize */
    private Integer cursorFetchSize;

    /** INSERT列 */
    private List<QueryColumn> insertColumns;

    /** UPDATE列 */
    private List<QueryColumn> updateColumns;

    /** SELECT列 */
    private List<QueryColumn> queryColumns;

    /** ID字段类型 */
    private String idFieldType;

    /** ID列名 */
    private String idColumnName;

    /** ID字段名 */
    private String idFieldName;

    /** ID字段JDBC类型 */
    private String idJdbcTypeName;

    /** ID生成策略 */
    private String idGenStrategy;

    /** ID生成参数,例如序列名称 */
    private String idGenParameter;

    /** ID字段列表 */
    private List<IdColumn> idColumns;

    public MybatisXmlMapperCodegenParameter(String targetTemplateName) {
        super(targetTemplateName);
    }

    public String getDomainObjectName() {
        return domainObjectName;
    }

    public void setDomainObjectName(String domainObjectName) {
        this.domainObjectName = domainObjectName;
    }

    public String getMapperNamespace() {
        return mapperNamespace;
    }

    public void setMapperNamespace(String mapperNamespace) {
        this.mapperNamespace = mapperNamespace;
    }

    public String getMapperHelperClass() {
        return mapperHelperClass;
    }

    public void setMapperHelperClass(String mapperHelperClass) {
        this.mapperHelperClass = mapperHelperClass;
    }

    public String getDomainEntityTable() {
        return domainEntityTable;
    }

    public void setDomainEntityTable(String domainEntityTable) {
        this.domainEntityTable = domainEntityTable;
    }

    public String getTableAliasName() {
        return tableAliasName;
    }

    public void setTableAliasName(String tableAliasName) {
        this.tableAliasName = tableAliasName;
    }

    public String getDeleteTargetAliasName() {
        return deleteTargetAliasName;
    }

    public void setDeleteTargetAliasName(String deleteTargetAliasName) {
        this.deleteTargetAliasName = deleteTargetAliasName;
    }

    public Integer getCursorFetchSize() {
        return cursorFetchSize;
    }

    public void setCursorFetchSize(Integer cursorFetchSize) {
        this.cursorFetchSize = cursorFetchSize;
    }

    public List<QueryColumn> getInsertColumns() {
        return insertColumns;
    }

    public void setInsertColumns(List<QueryColumn> insertColumns) {
        this.insertColumns = insertColumns;
    }

    public List<QueryColumn> getUpdateColumns() {
        return updateColumns;
    }

    public void setUpdateColumns(List<QueryColumn> updateColumns) {
        this.updateColumns = updateColumns;
    }

    public List<QueryColumn> getQueryColumns() {
        return queryColumns;
    }

    public void setQueryColumns(List<QueryColumn> queryColumns) {
        this.queryColumns = queryColumns;
    }

    public String getIdFieldType() {
        return idFieldType;
    }

    public void setIdFieldType(String idFieldType) {
        this.idFieldType = idFieldType;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public void setIdColumnName(String idColumnName) {
        this.idColumnName = idColumnName;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    public String getIdJdbcTypeName() {
        return idJdbcTypeName;
    }

    public void setIdJdbcTypeName(String idJdbcTypeName) {
        this.idJdbcTypeName = idJdbcTypeName;
    }

    public String getIdGenStrategy() {
        return idGenStrategy;
    }

    public void setIdGenStrategy(String idGenStrategy) {
        this.idGenStrategy = idGenStrategy;
    }

    public String getIdGenParameter() {
        return idGenParameter;
    }

    public void setIdGenParameter(String idGenParameter) {
        this.idGenParameter = idGenParameter;
    }

    public List<IdColumn> getIdColumns() {
        return idColumns;
    }

    public void setIdColumns(List<IdColumn> idColumns) {
        this.idColumns = idColumns;
    }

    public static class QueryColumn {

        /** 列名 */
        private String columnName;

        /** 对应的实体字段名 */
        private String fieldName;

        /** 对应的实体字段类型 */
        private String fieldType;

        /** 字段对应的JDBC类型 */
        private String jdbcTypeName;

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldType() {
            return fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }

        public String getJdbcTypeName() {
            return jdbcTypeName;
        }

        public void setJdbcTypeName(String jdbcTypeName) {
            this.jdbcTypeName = jdbcTypeName;
        }
    }

    public static class IdColumn {

        /** ID字段类型 */
        private String idFieldType;

        /** ID列名 */
        private String idColumnName;

        /** ID字段名 */
        private String idFieldName;

        /** ID字段JDBC类型 */
        private String idJdbcTypeName;

        public String getIdFieldType() {
            return idFieldType;
        }

        public void setIdFieldType(String idFieldType) {
            this.idFieldType = idFieldType;
        }

        public String getIdColumnName() {
            return idColumnName;
        }

        public void setIdColumnName(String idColumnName) {
            this.idColumnName = idColumnName;
        }

        public String getIdFieldName() {
            return idFieldName;
        }

        public void setIdFieldName(String idFieldName) {
            this.idFieldName = idFieldName;
        }

        public String getIdJdbcTypeName() {
            return idJdbcTypeName;
        }

        public void setIdJdbcTypeName(String idJdbcTypeName) {
            this.idJdbcTypeName = idJdbcTypeName;
        }
    }

}
