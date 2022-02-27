package com.penglecode.xmodule.common.codegen.database;

import com.penglecode.xmodule.common.codegen.config.DomainConfig;
import com.penglecode.xmodule.common.codegen.config.DomainEntityColumnConfig;
import com.penglecode.xmodule.common.codegen.config.DomainIntrospectConfig;
import com.penglecode.xmodule.common.codegen.config.DomainEntityConfig;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.boot.autoconfigure.dal.DalComponentUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据库自省器
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/22 11:16
 */
public class DatabaseIntrospector {

    private final DomainConfig domainConfig;

    private final Map<Integer, JdbcTypeInformation> jdbcTypes;

    public DatabaseIntrospector(DomainConfig domainConfig) {
        Assert.notNull(domainConfig, "参数'domainConfig'不能为空!");
        this.domainConfig = domainConfig;
        this.jdbcTypes = new HashMap<>();
        this.jdbcTypes.put(Types.ARRAY, new JdbcTypeInformation("ARRAY", new FullyQualifiedJavaType(Object.class.getName())));
        this.jdbcTypes.put(Types.BIGINT, new JdbcTypeInformation("BIGINT", new FullyQualifiedJavaType(Long.class.getName())));
        this.jdbcTypes.put(Types.BINARY, new JdbcTypeInformation("BINARY", new FullyQualifiedJavaType("byte[]")));
        this.jdbcTypes.put(Types.BIT, new JdbcTypeInformation("BIT", new FullyQualifiedJavaType(Boolean.class.getName())));
        this.jdbcTypes.put(Types.BLOB, new JdbcTypeInformation("BLOB", new FullyQualifiedJavaType("byte[]")));
        this.jdbcTypes.put(Types.BOOLEAN, new JdbcTypeInformation("BOOLEAN", new FullyQualifiedJavaType(Boolean.class.getName())));
        this.jdbcTypes.put(Types.CHAR, new JdbcTypeInformation("CHAR", new FullyQualifiedJavaType(String.class.getName())));
        this.jdbcTypes.put(Types.CLOB, new JdbcTypeInformation("CLOB", new FullyQualifiedJavaType(String.class.getName())));
        this.jdbcTypes.put(Types.DATALINK, new JdbcTypeInformation("DATALINK", new FullyQualifiedJavaType(Object.class.getName())));
        this.jdbcTypes.put(Types.DATE, new JdbcTypeInformation("DATE", new FullyQualifiedJavaType(LocalDate.class.getName())));
        this.jdbcTypes.put(Types.DECIMAL, new JdbcTypeInformation("DECIMAL", new FullyQualifiedJavaType(BigDecimal.class.getName())));
        this.jdbcTypes.put(Types.DISTINCT, new JdbcTypeInformation("DISTINCT", new FullyQualifiedJavaType(Object.class.getName())));
        this.jdbcTypes.put(Types.DOUBLE, new JdbcTypeInformation("DOUBLE", new FullyQualifiedJavaType(Double.class.getName())));
        this.jdbcTypes.put(Types.FLOAT, new JdbcTypeInformation("FLOAT", new FullyQualifiedJavaType(Double.class.getName())));
        this.jdbcTypes.put(Types.INTEGER, new JdbcTypeInformation("INTEGER", new FullyQualifiedJavaType(Integer.class.getName())));
        this.jdbcTypes.put(Types.JAVA_OBJECT, new JdbcTypeInformation("JAVA_OBJECT", new FullyQualifiedJavaType(Double.class.getName())));
        this.jdbcTypes.put(Types.LONGNVARCHAR, new JdbcTypeInformation("LONGNVARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        this.jdbcTypes.put(Types.LONGVARBINARY, new JdbcTypeInformation("LONGVARBINARY", new FullyQualifiedJavaType("byte[]")));
        this.jdbcTypes.put(Types.LONGVARCHAR, new JdbcTypeInformation("LONGVARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        this.jdbcTypes.put(Types.NCHAR, new JdbcTypeInformation("NCHAR", new FullyQualifiedJavaType(String.class.getName())));
        this.jdbcTypes.put(Types.NCLOB, new JdbcTypeInformation("NCLOB", new FullyQualifiedJavaType(String.class.getName())));
        this.jdbcTypes.put(Types.NVARCHAR, new JdbcTypeInformation("NVARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        this.jdbcTypes.put(Types.NULL, new JdbcTypeInformation("NULL", new FullyQualifiedJavaType(Object.class.getName())));
        this.jdbcTypes.put(Types.NUMERIC, new JdbcTypeInformation("NUMERIC", new FullyQualifiedJavaType(BigDecimal.class.getName())));
        this.jdbcTypes.put(Types.OTHER, new JdbcTypeInformation("OTHER", new FullyQualifiedJavaType(Object.class.getName())));
        this.jdbcTypes.put(Types.REAL, new JdbcTypeInformation("REAL", new FullyQualifiedJavaType(Float.class.getName())));
        this.jdbcTypes.put(Types.REF, new JdbcTypeInformation("REF", new FullyQualifiedJavaType(Object.class.getName())));
        this.jdbcTypes.put(Types.SMALLINT, new JdbcTypeInformation("SMALLINT", new FullyQualifiedJavaType(Short.class.getName())));
        this.jdbcTypes.put(Types.STRUCT, new JdbcTypeInformation("STRUCT", new FullyQualifiedJavaType(Object.class.getName())));
        this.jdbcTypes.put(Types.TIME, new JdbcTypeInformation("TIME", new FullyQualifiedJavaType(LocalTime.class.getName())));
        this.jdbcTypes.put(Types.TIMESTAMP, new JdbcTypeInformation("TIMESTAMP", new FullyQualifiedJavaType(LocalDateTime.class.getName())));
        this.jdbcTypes.put(Types.TINYINT, new JdbcTypeInformation("TINYINT", new FullyQualifiedJavaType(Integer.class.getName())));
        this.jdbcTypes.put(Types.VARBINARY, new JdbcTypeInformation("VARBINARY", new FullyQualifiedJavaType("byte[]")));
        this.jdbcTypes.put(Types.VARCHAR, new JdbcTypeInformation("VARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        // JDK 1.8 types
        this.jdbcTypes.put(Types.TIME_WITH_TIMEZONE, new JdbcTypeInformation("TIME_WITH_TIMEZONE", new FullyQualifiedJavaType(OffsetTime.class.getName())));
        this.jdbcTypes.put(Types.TIMESTAMP_WITH_TIMEZONE, new JdbcTypeInformation("TIMESTAMP_WITH_TIMEZONE", new FullyQualifiedJavaType(OffsetDateTime.class.getName())));
        this.overrideDefaultJdbcTypes();
    }

    protected void overrideDefaultJdbcTypes() {
        DomainIntrospectConfig introspectConfig = getDomainConfig().getDomainCommons().getIntrospectConfig();
        if(introspectConfig.isForceDateTimeAsString()) {
            addJdbcTypeMapping(Types.DATE, new JdbcTypeInformation("VARCHAR", new FullyQualifiedJavaType(String.class.getName())));
            addJdbcTypeMapping(Types.TIME, new JdbcTypeInformation("VARCHAR", new FullyQualifiedJavaType(String.class.getName())));
            addJdbcTypeMapping(Types.TIMESTAMP, new JdbcTypeInformation("VARCHAR", new FullyQualifiedJavaType(String.class.getName())));
        }
        if(introspectConfig.isForceDecimalNumericAsDouble()) {
            addJdbcTypeMapping(Types.DECIMAL, new JdbcTypeInformation("DECIMAL", new FullyQualifiedJavaType(Double.class.getName())));
            addJdbcTypeMapping(Types.NUMERIC, new JdbcTypeInformation("NUMERIC", new FullyQualifiedJavaType(Double.class.getName())));
        }
    }

    /**
     * 自省codegenConfig所指定的数据库表
     * @return
     * @throws SQLException
     */
    public Set<IntrospectedTable> introspectedTables() throws SQLException {
        Set<IntrospectedTable> introspectedTables = new LinkedHashSet<>();
        Map<String,DomainEntityConfig> domainEntityConfigs = getDomainConfig().getDomainEntities();
        for(Map.Entry<String,DomainEntityConfig> entry : domainEntityConfigs.entrySet()) {
            DomainEntityConfig domainEntityConfig = entry.getValue();
            introspectedTables.add(introspectedTable(domainEntityConfig));
        }
        return introspectedTables;
    }

    /**
     * 内省domainEntityConfig所指定的数据库表
     * @param domainEntityConfig
     * @return
     * @throws SQLException
     */
    public IntrospectedTable introspectedTable(DomainEntityConfig domainEntityConfig) throws SQLException {
        String introspectDataSourceName = StringUtils.defaultIfBlank(domainEntityConfig.getIntrospectConfig().getIntrospectDataSource(), getDomainConfig().getDomainCommons().getIntrospectDataSource());
        DataSource introspectDataSource = DalComponentUtils.getBeanOfType(introspectDataSourceName, DataSource.class);
        try(Connection connection = introspectDataSource.getConnection()) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String targetTableName = domainEntityConfig.getDomainEntityTable();
            if(databaseMetaData.storesLowerCaseIdentifiers()) {
                targetTableName = targetTableName.toLowerCase();
            } else if(databaseMetaData.storesUpperCaseIdentifiers()) {
                targetTableName = targetTableName.toUpperCase();
            }
            TableIndentity tableIndentity = new TableIndentity(null, null, targetTableName);
            ImmutablePair<TableIndentity,List<IntrospectedColumn>> tableAndColumns = extractTableColumns(domainEntityConfig, databaseMetaData, tableIndentity);
            tableIndentity = tableAndColumns.getLeft();
            List<IntrospectedColumn> allColumns = tableAndColumns.getRight();
            IntrospectedTable introspectedTable = createIntrospectedTable(domainEntityConfig, databaseMetaData, tableIndentity);
            introspectedTable.setAllColumns(allColumns);
            introspectedTable.setPkColumns(extractPkColumns(domainEntityConfig, databaseMetaData, tableIndentity, allColumns));
            for(IntrospectedColumn introspectedColumn : allColumns) {
                completeIntrospect(domainEntityConfig, introspectedColumn, introspectedTable);
            }
            return introspectedTable;
        }
    }

    /**
     * 提取当前领域对象对应的数据库表的所有列
     * @param domainEntityConfig
     * @param databaseMetaData
     * @param tableIndentity
     * @return
     * @throws SQLException
     */
    protected ImmutablePair<TableIndentity,List<IntrospectedColumn>> extractTableColumns(DomainEntityConfig domainEntityConfig, DatabaseMetaData databaseMetaData, TableIndentity tableIndentity) throws SQLException {
        try(ResultSet rs = databaseMetaData.getColumns(StringUtils.defaultIfBlank(tableIndentity.getCatalog(), (String) null), StringUtils.defaultIfBlank(tableIndentity.getSchema(), (String) null), tableIndentity.getTableName(), "%")) {
            List<IntrospectedColumn> tableColumns = new ArrayList<>();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            boolean supportsIsAutoIncrement = false;
            for(int i = 1; i <= columnCount; i++) {
                if("IS_AUTOINCREMENT".equals(rsmd.getColumnName(i))) {
                    supportsIsAutoIncrement = true;
                }
            }
            while (rs.next()) {
                String catalog = rs.getString("TABLE_CAT");
                String schema = rs.getString("TABLE_SCHEM");
                String tableName = rs.getString("TABLE_NAME");
                if(tableIndentity.getTableName().equalsIgnoreCase(tableName)) {
                    tableIndentity = new TableIndentity(catalog, schema, tableName);
                    IntrospectedColumn introspectedColumn = new IntrospectedColumn();
                    introspectedColumn.setColumnName(rs.getString("COLUMN_NAME"));
                    introspectedColumn.setColumnType(rs.getString("TYPE_NAME"));
                    introspectedColumn.setJdbcType(rs.getInt("DATA_TYPE"));
                    introspectedColumn.setLength(rs.getInt("COLUMN_SIZE"));
                    introspectedColumn.setNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                    introspectedColumn.setScale(rs.getInt("DECIMAL_DIGITS"));
                    introspectedColumn.setColumnComment(rs.getString("REMARKS"));
                    introspectedColumn.setDefaultValue(rs.getString("COLUMN_DEF"));
                    if (supportsIsAutoIncrement) {
                        introspectedColumn.setAutoIncrement("YES".equals(rs.getString("IS_AUTOINCREMENT")));
                    }
                    tableColumns.add(introspectedColumn);
                }
            }
            Assert.notEmpty(tableColumns, String.format("未找到表(%s)的元数据信息!", tableIndentity.getTableName()));
            return new ImmutablePair<>(tableIndentity, tableColumns);
        }
    }

    /**
     * 创建IntrospectedTable
     * @param domainEntityConfig
     * @param databaseMetaData
     * @param tableIndentity
     * @return
     * @throws SQLException
     */
    protected IntrospectedTable createIntrospectedTable(DomainEntityConfig domainEntityConfig, DatabaseMetaData databaseMetaData, TableIndentity tableIndentity) throws SQLException {
        IntrospectedTable introspectedTable = new IntrospectedTable(tableIndentity.getCatalog(), tableIndentity.getSchema(), tableIndentity.getTableName());
        introspectedTable.setDomainEntityName(domainEntityConfig.getDomainEntityName());
        try(ResultSet rs = databaseMetaData.getTables(tableIndentity.getCatalog(), tableIndentity.getSchema(), tableIndentity.getTableName(), null)) {
            if (rs.next()) {
                introspectedTable.setTableComment(rs.getString("REMARKS"));
                introspectedTable.setTableType(rs.getString("TABLE_TYPE"));
            }
        }
        return introspectedTable;
    }

    /**
     * 提取当前领域实体对应的数据库表的主键列
     * @param domainEntityConfig
     * @param databaseMetaData
     * @param tableIndentity
     * @param allColumns
     * @return
     * @throws SQLException
     */
    protected List<IntrospectedColumn> extractPkColumns(DomainEntityConfig domainEntityConfig, DatabaseMetaData databaseMetaData, TableIndentity tableIndentity, List<IntrospectedColumn> allColumns) throws SQLException {
        try(ResultSet rs = databaseMetaData.getPrimaryKeys(tableIndentity.getCatalog(), tableIndentity.getSchema(), tableIndentity.getTableName())) {
            Map<Short,String> keyColumns = new TreeMap<>();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                short keySeq = rs.getShort("KEY_SEQ");
                keyColumns.put(keySeq, columnName);
            }
            List<IntrospectedColumn> pkColumns = new ArrayList<>();
            for(String columnName : keyColumns.values()) {
                for(IntrospectedColumn column : allColumns) {
                    if(column.getColumnName().equalsIgnoreCase(columnName)) {
                        pkColumns.add(column);
                    }
                }
            }
            Assert.notEmpty(pkColumns, String.format("表(%s)中没有发现主键!", tableIndentity.getTableName()));
            return pkColumns;
        }
    }

    /**
     * 提取当前领域实体对应的数据库表的所以唯一键列
     * @param domainEntityConfig
     * @param databaseMetaData
     * @param tableIndentity
     * @param allColumns
     * @return
     * @throws SQLException
     */
    protected Map<String,List<IntrospectedColumn>> extractUkColumns(DomainEntityConfig domainEntityConfig, DatabaseMetaData databaseMetaData, TableIndentity tableIndentity, List<IntrospectedColumn> allColumns) throws SQLException {
        try(ResultSet rs = databaseMetaData.getIndexInfo(tableIndentity.getCatalog(), tableIndentity.getSchema(), tableIndentity.getTableName(), true, false)) {
            Map<String,Map<Short,String>> ukMap = new HashMap<>();
            while (rs.next()) {
                String indexName = rs.getString("INDEX_NAME");
                String indexColumnName = rs.getString("COLUMN_NAME");
                Short indexColumnIndex = rs.getShort("ORDINAL_POSITION");
                if(!ukMap.containsKey(indexName)) {
                    ukMap.put(indexName, new TreeMap<>());
                }
                ukMap.get(indexName).put(indexColumnIndex, indexColumnName);
            }
            Map<String,List<IntrospectedColumn>> ukColumns = new HashMap<>();
            for(Map.Entry<String,Map<Short,String>> entry : ukMap.entrySet()) {
                List<IntrospectedColumn> keyColumns = new ArrayList<>();
                for(String columnName : entry.getValue().values()) {
                    for(IntrospectedColumn column : allColumns) {
                        if(column.getColumnName().equalsIgnoreCase(columnName)) {
                            keyColumns.add(column);
                        }
                    }
                }
                if(!keyColumns.isEmpty()) {
                    ukColumns.put(entry.getKey(), keyColumns);
                }
            }
            return ukColumns;
        }
    }

    protected void completeIntrospect(DomainEntityConfig domainEntityConfig, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable) {
        introspectedColumn.setIntrospectedTable(introspectedTable);
        introspectedColumn.setJdbcTypeName(resolveJdbcTypeName(introspectedColumn));
        introspectedColumn.setBlobType(isBlobType(introspectedColumn.getJdbcTypeName()));
        introspectedColumn.setJavaFieldType(resolveJavaType(introspectedColumn));
        introspectedColumn.setJavaFieldName(StringUtils.snakeNamingToCamel(introspectedColumn.getColumnName()));

        DomainEntityColumnConfig domainEntityColumn = domainEntityConfig.getDomainEntityColumns().get(introspectedColumn.getColumnName());
        if(domainEntityColumn != null) {
            if(domainEntityColumn.getJavaTypeOverride() != null) { //处理javaTypeOverride配置
                FullyQualifiedJavaType javaFieldType = new FullyQualifiedJavaType(domainEntityColumn.getJavaTypeOverride().getName());
                introspectedColumn.setJavaFieldType(javaFieldType); //设置为指定的javaTypeOverride
                for(Map.Entry<Integer,JdbcTypeInformation> entry : jdbcTypes.entrySet()) {
                    JdbcTypeInformation jdbcTypeInformation = entry.getValue();
                    if(jdbcTypeInformation.getJavaType().equals(javaFieldType)) { //根据javaTypeOverride反向设置其jdbcType
                        introspectedColumn.setJdbcType(entry.getKey());
                        introspectedColumn.setJdbcTypeName(jdbcTypeInformation.getJdbcTypeName());
                    }
                }
            }
        }
    }

    public void addJdbcTypeMapping(int jdbcType, JdbcTypeInformation jdbcTypeInformation) {
        this.jdbcTypes.put(jdbcType, jdbcTypeInformation);
    }

    protected FullyQualifiedJavaType resolveJavaType(IntrospectedColumn column) {
        JdbcTypeInformation defaultJdbcType = jdbcTypes.get(column.getJdbcType());
        if(defaultJdbcType != null) {
            if(Types.BIT == column.getJdbcType()) {
                return resolveBitReplacement(column, defaultJdbcType);
            } else if (Types.DECIMAL == column.getJdbcType() || Types.NUMERIC == column.getJdbcType()) {
                return resolveBigDecimalReplacement(column, defaultJdbcType);
            } else if (Types.INTEGER == column.getJdbcType() || Types.TINYINT == column.getJdbcType() || Types.SMALLINT == column.getJdbcType()) {
                return resolveNumberReplacement(column, defaultJdbcType);
            } else if (Types.DATE == column.getJdbcType() || Types.TIME == column.getJdbcType() || Types.TIMESTAMP == column.getJdbcType()) {
                return resolveDateTimeReplacement(column, defaultJdbcType);
            } else {
                return defaultJdbcType.getJavaType();
            }
        }
        return null;
    }

    protected FullyQualifiedJavaType resolveBitReplacement(IntrospectedColumn column, JdbcTypeInformation defaultJdbcType) {
        if(column.getLength() > 1) {
            new FullyQualifiedJavaType("byte[]");
        }
        return defaultJdbcType.getJavaType();
    }

    protected FullyQualifiedJavaType resolveNumberReplacement(IntrospectedColumn column, JdbcTypeInformation defaultJdbcType) {
        boolean forceNumber1AsBoolean = ObjectUtils.defaultIfNull(getDomainConfig().getDomainEntities().get(column.getIntrospectedTable().getDomainEntityName()).getIntrospectConfig().isForceNumber1AsBoolean(), getDomainConfig().getDomainCommons().getIntrospectConfig().isForceNumber1AsBoolean());
        if (column.getLength() == 1 && forceNumber1AsBoolean) {
            return new FullyQualifiedJavaType(Boolean.class.getName());
        }
        return defaultJdbcType.getJavaType();
    }

    protected FullyQualifiedJavaType resolveDateTimeReplacement(IntrospectedColumn column, JdbcTypeInformation defaultJdbcType) {
        boolean forceNumber1AsBoolean = ObjectUtils.defaultIfNull(getDomainConfig().getDomainEntities().get(column.getIntrospectedTable().getDomainEntityName()).getIntrospectConfig().isForceDateTimeAsString(), getDomainConfig().getDomainCommons().getIntrospectConfig().isForceDateTimeAsString());
        if (forceNumber1AsBoolean) {
            return new FullyQualifiedJavaType(String.class.getName());
        }
        return defaultJdbcType.getJavaType();
    }

    protected FullyQualifiedJavaType resolveBigDecimalReplacement(IntrospectedColumn column, JdbcTypeInformation defaultJdbcType) {
        if(column.getLength() > 18) { //长度太大则强制使用BigDecimal
            return new FullyQualifiedJavaType(BigDecimal.class.getName());
        }
        boolean forceDecimalNumericAsDouble = ObjectUtils.defaultIfNull(getDomainConfig().getDomainEntities().get(column.getIntrospectedTable().getDomainEntityName()).getIntrospectConfig().isForceDecimalNumericAsDouble(), getDomainConfig().getDomainCommons().getIntrospectConfig().isForceDecimalNumericAsDouble());
        if(forceDecimalNumericAsDouble) {
            return new FullyQualifiedJavaType(Double.class.getName());
        }
        return defaultJdbcType.getJavaType();
    }

    protected String resolveJdbcTypeName(IntrospectedColumn column) {
        JdbcTypeInformation jdbcTypeInformation = jdbcTypes.get(column.getJdbcType());
        return jdbcTypeInformation == null ? null : jdbcTypeInformation.getJdbcTypeName();
    }

    protected boolean isBlobType(String jdbcTypeName) {
        return "BINARY".equals(jdbcTypeName) || "BLOB".equals(jdbcTypeName)
                || "CLOB".equals(jdbcTypeName) || "LONGNVARCHAR".equals(jdbcTypeName)
                || "LONGVARBINARY".equals(jdbcTypeName) || "LONGVARCHAR".equals(jdbcTypeName)
                || "NCLOB".equals(jdbcTypeName) || "VARBINARY".equals(jdbcTypeName);
    }

    protected Map<Integer, JdbcTypeInformation> getJdbcTypes() {
        return jdbcTypes;
    }

    protected DomainConfig getDomainConfig() {
        return domainConfig;
    }
}
