package com.penglecode.xmodule.common.mybatis;

/**
 * 支持的数据库类型
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/8/15 14:50
 */
public enum SupportedDatabaseType {

    MYSQL, ORACLE;

    public static SupportedDatabaseType of(String name) {
        for(SupportedDatabaseType em : values()) {
            if(em.name().equalsIgnoreCase(name)) {
                return em;
            }
        }
        throw new UnsupportedOperationException("Unsupported Database Type: " + name);
    }

}
