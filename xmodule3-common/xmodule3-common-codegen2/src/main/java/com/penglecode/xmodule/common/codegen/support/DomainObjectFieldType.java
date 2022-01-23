package com.penglecode.xmodule.common.codegen.support;

/**
 * 领域对象字段类型
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/23 15:11
 */
public enum DomainObjectFieldType {

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
     * 领域实体辅助字段(查询场景输入参数)
     */
    DOMAIN_ENTITY_SUPPORTS_QUERY_INPUT_FIELD {
        @Override
        public boolean isSupportField() {
            return true;
        }
    },
    /**
     * 领域实体辅助字段(查询场景输出参数)
     */
    DOMAIN_ENTITY_SUPPORTS_QUERY_OUTPUT_FIELD {
        @Override
        public boolean isSupportField() {
            return true;
        }
    };

    public abstract boolean isSupportField();

}
