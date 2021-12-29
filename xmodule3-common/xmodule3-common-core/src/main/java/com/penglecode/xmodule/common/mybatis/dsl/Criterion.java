package com.penglecode.xmodule.common.mybatis.dsl;

import java.io.Serializable;

/**
 * 抽象的过滤条件
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/10 20:02
 */
public abstract class Criterion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前过滤条件是否为逻辑嵌套?
     */
    private final boolean nested;

    /**
     * 当前过滤条件的逻辑运算符(AND/OR)
     */
    private final String logicOp;

    Criterion(boolean nested, String logicOp) {
        this.nested = nested;
        this.logicOp = logicOp;
    }

    public boolean isNested() {
        return nested;
    }

    public String getLogicOp() {
        return logicOp;
    }

}
