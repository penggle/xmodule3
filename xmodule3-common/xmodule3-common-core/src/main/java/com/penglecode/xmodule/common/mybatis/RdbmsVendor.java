package com.penglecode.xmodule.common.mybatis;

/**
 * 关系型数据库厂商枚举
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/8/15 14:50
 */
public enum RdbmsVendor {

    MYSQL, ORACLE;

    public static RdbmsVendor of(String name) {
        for(RdbmsVendor em : values()) {
            if(em.name().equalsIgnoreCase(name)) {
                return em;
            }
        }
        return null;
    }

}
