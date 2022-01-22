package com.penglecode.xmodule.common.codegen.support;

/**
 * 主键生成器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 14:15
 */
public class IdGenerator {

    private final IdGenStrategy strategy;

    private final String parameter;

    public IdGenerator(IdGenStrategy strategy, String parameter) {
        this.strategy = strategy;
        this.parameter = parameter;
    }

    public IdGenStrategy getStrategy() {
        return strategy;
    }

    public String getParameter() {
        return parameter;
    }
}
