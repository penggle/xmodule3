package com.penglecode.xmodule.common.util;

import java.util.function.BinaryOperator;

/**
 * Java8 Stream工具类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/8/6 17:12
 */
public class StreamUtils {

    private StreamUtils() {}

    /**
     * 针对Collectors.toMap(Function keyMapper, Function valueMapper, BinaryOperator mergeFunction)方法的第三个参数mergeFunction的工具类,
     * 该preferNewMergeFunction()方法返回的Lambda表达式返回倾向于新的那个value值
     *
     * @param <U>
     * @return
     */
    public static <U> BinaryOperator<U> preferNew() {
        return (oldValue, newValue) -> newValue;
    }

    /**
     * 针对Collectors.toMap(Function keyMapper, Function valueMapper, BinaryOperator mergeFunction)方法的第三个参数mergeFunction的工具类,
     * 该preferOldMergeFunction()方法返回的Lambda表达式返回倾向于旧的那个value值
     *
     * @param <U>
     * @return
     */
    public static <U> BinaryOperator<U> preferOld() {
        return (oldValue, newValue) -> newValue;
    }

}
