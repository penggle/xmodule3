package com.penglecode.xmodule.common.support;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 基于CGLIB-BeanCopier的JavaBean之间映射的转换工具类
 * 经JMH压测，性能接近get/set
 *
 * (注意，该实现要求目标bean与源bean的字段必须得完全匹配，本实现对于字段对不上的无能为力!!!)
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/14 10:49
 */
public class BeanMapper {

    private static final ConcurrentMap<String,BeanCopier> CACHE = new ConcurrentHashMap<>();

    private BeanMapper() {}

    /**
     * 将源对象转换成指定的目标对象
     * 默认严格按源对象与目标对象的字段一一对应
     * @param source        - 源对象
     * @param targetSupplier- 目标Supplier
     * @return 如果source为null则返回null,否则执行bean映射转换
     */
    public static <T,S> T map(S source, Supplier<T> targetSupplier) {
        if(source != null) {
            Assert.notNull(targetSupplier, "targetSupplier 'targetType' can not be null!");
            return map(source, targetSupplier.get());
        }
        return null;
    }

    /**
     * 将源对象转换成指定的目标对象
     * 默认严格按源对象与目标对象的字段一一对应
     * @param source        - 源对象
     * @param target        - 目标对象
     * @return 如果source为null则返回null,否则执行bean映射转换
     */
    @SuppressWarnings("unchecked")
    public static <T,S> T map(S source, T target) {
        if(source != null) {
            Assert.notNull(target, "Parameter 'target' can not be null!");
            Class<S> sourceType = (Class<S>) source.getClass();
            Class<T> targetType = (Class<T>) target.getClass();
            String cacheKey = sourceType + "->" + targetType;
            BeanCopier beanCopier = CACHE.computeIfAbsent(cacheKey, key -> BeanCopier.create(sourceType, targetType, false));
            beanCopier.copy(source, target, null);
            return target;
        }
        return null;
    }

    /**
     * 将源对象转换成指定的目标对象
     * 默认严格按源对象与目标对象的字段一一对应
     * @param sources
     * @param targetSupplier
     * @param <T>
     * @param <S>
     * @return 如果sources为null则返回null,否则执行bean映射转换
     */
    public static <T,S> List<T> map(List<S> sources, Supplier<T> targetSupplier) {
        if(sources != null) {
            Assert.notNull(targetSupplier, "Parameter 'targetSupplier' can not be null!");
            return sources.stream().map(source -> map(source, targetSupplier)).collect(Collectors.toList());
        }
        return null;
    }

}
