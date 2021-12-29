package com.penglecode.xmodule.common.mybatis;

/**
 * 方言数据库类型
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/8/15 14:50
 */
public enum DatabaseType {

    MYSQL, ORACLE;

    public static DatabaseType of(String name) {
        for(DatabaseType em : values()) {
            if(em.name().equalsIgnoreCase(name)) {
                return em;
            }
        }
        return null;
    }

}
