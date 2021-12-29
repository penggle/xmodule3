package com.penglecode.xmodule.common.consts;

import java.lang.reflect.ParameterizedType;
import java.util.function.Supplier;

/**
 * 常量
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 14:02
 */
@SuppressWarnings("unchecked")
public abstract class Constant<T> implements Supplier<T> {

    protected static ConstantPool<Object> constantPool = new DefaultEmptyConstantPool<>();

    protected final String name;

    protected final Class<T> type;

    protected final T defaultValue;

    protected Constant(String name) {
        this(name, null);
    }

    protected Constant(String name, T defaultValue) {
        super();
        this.defaultValue = defaultValue;
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.name = name;
    }

    /**
     * 返回常量值
     * @return
     */
    @Override
    public T get() {
        return (T) constantPool.get((Constant<Object>) this);
    }

    /**
     * 此处需要在应用启动时设置对应的ConstantPool
     * @param constantPool
     */
    public static void setConstantPool(ConstantPool<Object> constantPool) {
        Constant.constantPool = constantPool;
    }

    private static class DefaultEmptyConstantPool<T> implements ConstantPool<T> {

        @Override
        public T get(Constant<T> constant) {
            return constant.defaultValue;
        }

    }

}
