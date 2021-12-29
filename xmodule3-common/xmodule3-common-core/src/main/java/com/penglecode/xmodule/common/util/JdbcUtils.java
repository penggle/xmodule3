package com.penglecode.xmodule.common.util;

import org.springframework.util.ClassUtils;

import java.sql.Driver;
import java.sql.SQLException;

/**
 * JDBC工具类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
public class JdbcUtils extends org.springframework.jdbc.support.JdbcUtils {

	private static Boolean mysqlDriverVersion6;

    /**
     * 根据jdbcUrl来解析其驱动类名
     * @param jdbcUrl
     * @return
     * @throws SQLException
     */
	public static String getDriverClassName(String jdbcUrl) throws SQLException {
        if (jdbcUrl == null) {
            return null;
        }
        
        if (jdbcUrl.startsWith("jdbc:derby:")) {
            return "org.apache.derby.jdbc.EmbeddedDriver";
        } else if (jdbcUrl.startsWith("jdbc:mysql:")) {
            if (mysqlDriverVersion6 == null) {
                Class<?> driverClass = null;
                try {
                    driverClass = ClassUtils.forName("com.mysql.cj.jdbc.Driver", ClassUtils.getDefaultClassLoader());
                } catch (Exception e) {
                    //ignore
                }
                mysqlDriverVersion6 = driverClass != null;
            }

            if (mysqlDriverVersion6) {
                return "com.mysql.cj.jdbc.Driver";
            } else {
                return "com.mysql.jdbc.Driver";
            }
        } else if (jdbcUrl.startsWith("jdbc:log4jdbc:")) {
            return "net.sf.log4jdbc.DriverSpy";
        } else if (jdbcUrl.startsWith("jdbc:mariadb:")) {
            return "org.mariadb.jdbc.Driver";
        } else if (jdbcUrl.startsWith("jdbc:oracle:") //
                   || jdbcUrl.startsWith("JDBC:oracle:")) {
            return "oracle.jdbc.OracleDriver";
        } else if (jdbcUrl.startsWith("jdbc:microsoft:")) {
            return "com.microsoft.jdbc.sqlserver.SQLServerDriver";
        } else if (jdbcUrl.startsWith("jdbc:sqlserver:")) {
            return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        } else if (jdbcUrl.startsWith("jdbc:postgresql:")) {
            return "org.postgresql.Driver";
        } else if (jdbcUrl.startsWith("jdbc:edb:")) {
            return "org.postgresql.Driver";
        } else if (jdbcUrl.startsWith("jdbc:hsqldb:")) {
            return "org.hsqldb.jdbcDriver";
        } else if (jdbcUrl.startsWith("jdbc:db2:")) {
            return "com.ibm.db2.jcc.DB2Driver";
        } else if (jdbcUrl.startsWith("jdbc:sqlite:")) {
            return "org.sqlite.JDBC";
        } else if (jdbcUrl.startsWith("jdbc:ingres:")) {
            return "com.ingres.jdbc.IngresDriver";
        } else if (jdbcUrl.startsWith("jdbc:h2:")) {
            return "org.h2.Driver";
        } else if (jdbcUrl.startsWith("jdbc:hive:")) {
            return "org.apache.hive.jdbc.HiveDriver";
        } else if (jdbcUrl.startsWith("jdbc:hive2:")) {
            return "org.apache.hive.jdbc.HiveDriver";
        } else if (jdbcUrl.startsWith("jdbc:clickhouse:")) {
            return "ru.yandex.clickhouse.ClickHouseDriver";
        } else {
            throw new SQLException("unknown jdbc driver : " + jdbcUrl);
        }
    }

    /**
     * 根据驱动类名获取其数据库类型
     * @param jdbcUrl
     * @return
     */
    public static String getDbType(String jdbcUrl) {
        if (jdbcUrl == null) {
            return null;
        }

        if (jdbcUrl.startsWith("jdbc:derby:") || jdbcUrl.startsWith("jdbc:log4jdbc:derby:")) {
            return "derby";
        } else if (jdbcUrl.startsWith("jdbc:mysql:") || jdbcUrl.startsWith("jdbc:cobar:")
                   || jdbcUrl.startsWith("jdbc:log4jdbc:mysql:")) {
            return "mysql";
        } else if (jdbcUrl.startsWith("jdbc:mariadb:")) {
            return "mariadb";
        } else if (jdbcUrl.startsWith("jdbc:oracle:") || jdbcUrl.startsWith("jdbc:log4jdbc:oracle:")) {
            return "oracle";
        } else if (jdbcUrl.startsWith("jdbc:microsoft:") || jdbcUrl.startsWith("jdbc:log4jdbc:microsoft:")) {
            return "sqlserver";
        } else if (jdbcUrl.startsWith("jdbc:sqlserver:") || jdbcUrl.startsWith("jdbc:log4jdbc:sqlserver:")) {
            return "sqlserver";
        } else if (jdbcUrl.startsWith("jdbc:postgresql:") || jdbcUrl.startsWith("jdbc:log4jdbc:postgresql:")) {
            return "postgresql";
        } else if (jdbcUrl.startsWith("jdbc:hsqldb:") || jdbcUrl.startsWith("jdbc:log4jdbc:hsqldb:")) {
            return "hsql";
        } else if (jdbcUrl.startsWith("jdbc:db2:")) {
            return "db2";
        } else if (jdbcUrl.startsWith("jdbc:sqlite:")) {
            return "sqlite";
        } else if (jdbcUrl.startsWith("jdbc:ingres:")) {
            return "ingres";
        } else if (jdbcUrl.startsWith("jdbc:h2:") || jdbcUrl.startsWith("jdbc:log4jdbc:h2:")) {
            return "h2";
        } else if (jdbcUrl.startsWith("jdbc:hive:")) {
            return "hive";
        } else if (jdbcUrl.startsWith("jdbc:hive2:")) {
            return "hive";
        } else if (jdbcUrl.startsWith("jdbc:clickhouse:")) {
            return "clickhouse";
        } else {
            return null;
        }
    }

    /**
     * 创建JDBC驱动
     * @param driverClassName
     * @return
     * @throws SQLException
     */
    public static Driver createDriver(String driverClassName) throws SQLException {
        return createDriver(null, driverClassName);
    }

    /**
     * 创建JDBC驱动
     * @param classLoader
     * @param driverClassName
     * @return
     * @throws SQLException
     */
    public static Driver createDriver(ClassLoader classLoader, String driverClassName) throws SQLException {
        Class<?> clazz = null;
        if (classLoader != null) {
            try {
                clazz = classLoader.loadClass(driverClassName);
            } catch (ClassNotFoundException e) {
                // skip
            }
        }

        if (clazz == null) {
            try {
                ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
                if (contextLoader != null) {
                    clazz = contextLoader.loadClass(driverClassName);
                }
            } catch (ClassNotFoundException e) {
                // skip
            }
        }

        if (clazz == null) {
            try {
                clazz = Class.forName(driverClassName);
            } catch (ClassNotFoundException e) {
                throw new SQLException(e.getMessage(), e);
            }
        }

        try {
            return (Driver) clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }
	
}
