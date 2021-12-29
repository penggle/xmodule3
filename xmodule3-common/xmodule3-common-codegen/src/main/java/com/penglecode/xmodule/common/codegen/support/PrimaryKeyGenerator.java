package com.penglecode.xmodule.common.codegen.support;

/**
 * 主键生成器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/8/21 14:03
 */
public class PrimaryKeyGenerator {

    private final PrimaryKeyGenStrategy strategy;

    private final String parameter;

    public PrimaryKeyGenerator(PrimaryKeyGenStrategy strategy, String parameter) {
        this.strategy = strategy;
        this.parameter = parameter;
    }

    public PrimaryKeyGenStrategy getStrategy() {
        return strategy;
    }

    public String getParameter() {
        return parameter;
    }
}
