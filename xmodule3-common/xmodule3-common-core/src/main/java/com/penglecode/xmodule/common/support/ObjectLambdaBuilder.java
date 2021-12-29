package com.penglecode.xmodule.common.support;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 基于JAVA8方法引用(lambda的一种特殊形式)方式来快速构造普通JavaBean的通用Builder
 * 使用示例：
 *      Account account = ObjectLambdaBuilder.of(Account::new)
 *                 .with(Account::setAcctNo, "27182821221122219")
 *                 .with(Account::setCustNo, "71283828182823291929")
 *                 .with(Account::setIdCode, null)
 *                 .with(Account::setCustName, "阿三")
 *                 .with(Account::setMobile, "13812345678")
 *                 .build();
 *         System.out.println(account);
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/27 11:19
 */
public class ObjectLambdaBuilder<T> {

    private final Supplier<T> instantiator;

    private final List<Consumer<T>> modifiers = new ArrayList<>();

    private ObjectLambdaBuilder(Supplier<T> instantiator) {
        Assert.notNull(instantiator, "Parameter 'instantiator' can not be null!");
        this.instantiator = instantiator;
    }

    public static <T> ObjectLambdaBuilder<T> of(Supplier<T> instantiator) {
        return new ObjectLambdaBuilder<>(instantiator);
    }

    public <P> ObjectLambdaBuilder<T> with(Consumer2<T,P> consumer2, P p) {
        Consumer<T> consumer = instance -> consumer2.accept(instance, p);
        modifiers.add(consumer);
        return this;
    }

    public T build() {
        T instance = instantiator.get();
        modifiers.forEach(modifier -> modifier.accept(instance));
        modifiers.clear();
        return instance;
    }

    /**
     * 具有两个参数的Consumer
     * @param <T>
     * @param <P>
     */
    public interface Consumer2<T,P> {

        void accept(T t, P p);

    }

}
