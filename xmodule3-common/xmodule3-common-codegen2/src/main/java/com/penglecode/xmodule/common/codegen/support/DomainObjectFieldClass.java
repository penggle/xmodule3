package com.penglecode.xmodule.common.codegen.support;

/**
 * 领域对象字段分类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/23 15:11
 */
public enum DomainObjectFieldClass {

    /**
     * 领域实体对象字段
     */
    DOMAIN_ENTITY_FIELD {
        @Override
        public boolean isSupportField() {
            return false;
        }
    },

    /**
     * 领域聚合对象字段
     */
    DOMAIN_AGGREGATE_FIELD {
        @Override
        public boolean isSupportField() {
            return false;
        }
    },

    /**
     * 领域实体辅助字段(查询场景入站参数)
     */
    DOMAIN_ENTITY_SUPPORTS_QUERY_INBOUND_FIELD {
        @Override
        public boolean isSupportField() {
            return true;
        }
    },
    /**
     * 领域实体辅助字段(查询场景出站参数)
     */
    DOMAIN_ENTITY_SUPPORTS_QUERY_OUTBOUND_FIELD {
        @Override
        public boolean isSupportField() {
            return true;
        }
    };

    public abstract boolean isSupportField();

}
