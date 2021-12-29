package com.penglecode.xmodule.common.codegen.support;

/**
 * 领域对象聚合关系枚举
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/23 17:51
 */
public enum DomainMasterSlaveRelation {

    RELATION_11("1:1", "一对一关系"), RELATION_1N("1:N", "一对多关系");

    private final String code;

    private final String desc;

    DomainMasterSlaveRelation(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static DomainMasterSlaveRelation of(String code) {
        for(DomainMasterSlaveRelation em : values()) {
            if(em.getCode().equalsIgnoreCase(code)) {
                return em;
            }
        }
        return null;
    }

}
