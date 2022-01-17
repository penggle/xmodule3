package com.penglecode.xmodule.common.support;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.ReflectionUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.cglib.core.Converter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 基于CGLIB-BeanCopier + SpringCore-Converter的JavaBean之间属性拷贝工具类，
 * 由于引入了SpringCore-Converter性能接近CGLIB-BeanCopier(与get/set差距不大)
 * 支持深度拷贝(deep-copy)，灵活性很大，只要字段名匹配，甚至能将JSON字符串类型转换成POJO对象
 *
 * 使用限制：
 *      1、该实现要求目标bean与源bean的字段名必须得完全匹配，本实现对于字段名对不上的无能为力!!!
 *      2、深度拷贝的字段(自定义bean)必须实现{@link Convertible}标记接口
 *
 * @author pengpeng
 * @version 1.0
 * @since 2020/3/14 10:49
 */
public class BeanCopier {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanCopier.class);

    private static final ConversionService CONVERSION_SERVICE = new DefaultConversionService();

    private static final ConcurrentMap<String, ImmutablePair<org.springframework.cglib.beans.BeanCopier,Converter>> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();

    private BeanCopier() {}

    /**
     * 将源对象属性拷贝到指定的目标对象
     *
     * @param source        - 源对象
     * @param targetType    - 目标类型
     * @return 如果source为null则返回null,否则执行bean映射拷贝
     */
    public static <T,S> T copy(S source, Class<T> targetType) {
        if(source != null) {
            Assert.notNull(targetType, "Parameter 'targetType' can not be null!");
            return copy(source, BeanUtils.instantiateClass(targetType));
        }
        return null;
    }

    /**
     * 将源对象属性拷贝到指定的目标对象
     *
     * @param source            - 源对象
     * @param targetSupplier    - 目标Supplier
     * @return 如果source为null则返回null,否则执行bean映射拷贝
     */
    public static <T,S> T copy(S source, Supplier<T> targetSupplier) {
        if(source != null) {
            Assert.notNull(targetSupplier, "Parameter 'targetSupplier' can not be null!");
            return copy(source, targetSupplier.get());
        }
        return null;
    }

    /**
     * 将源对象属性拷贝到指定的目标对象
     *
     * @param source        - 源对象
     * @param target        - 目标对象
     * @return 如果source为null则返回null,否则执行bean映射拷贝
     */
    @SuppressWarnings("unchecked")
    public static <T,S> T copy(S source, T target) {
        if(source != null) {
            Assert.notNull(target, "Parameter 'target' can not be null!");
            Class<S> sourceType = (Class<S>) source.getClass();
            Class<T> targetType = (Class<T>) target.getClass();
            String cacheKey = sourceType + "->" + targetType;
            ImmutablePair<org.springframework.cglib.beans.BeanCopier,Converter> pair = BEAN_COPIER_CACHE.computeIfAbsent(cacheKey, key -> {
                org.springframework.cglib.beans.BeanCopier beanCopier = org.springframework.cglib.beans.BeanCopier.create(sourceType, targetType, true);
                Converter beanConverter = new DefaultCglibConverter(sourceType, targetType);
                return new ImmutablePair<>(beanCopier, beanConverter);
            });
            pair.getLeft().copy(source, target, pair.getRight());
            return target;
        }
        return null;
    }

    /**
     * 将源对象属性拷贝到指定的目标对象
     *
     * @param sources           - 源对象列表
     * @param targetType        - 目标类型
     * @param <T>
     * @param <S>
     * @return 如果sources为null则返回null,否则执行bean映射拷贝
     */
    public static <T,S> List<T> copy(List<S> sources, Class<T> targetType) {
        if(sources != null) {
            Assert.notNull(targetType, "Parameter 'targetType' can not be null!");
            return sources.stream().map(source -> copy(source, targetType)).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 将源对象属性拷贝到指定的目标对象
     *
     * @param sources           - 源对象列表
     * @param targetSupplier    - 目标Supplier
     * @param <T>
     * @param <S>
     * @return 如果sources为null则返回null,否则执行bean映射拷贝
     */
    public static <T,S> List<T> copy(List<S> sources, Supplier<T> targetSupplier) {
        if(sources != null) {
            Assert.notNull(targetSupplier, "Parameter 'targetSupplier' can not be null!");
            return sources.stream().map(source -> copy(source, targetSupplier)).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 默认的CGLIB-BeanCopier的Converter
     */
    static class DefaultCglibConverter implements Converter {
        /** bean拷贝的源类型 */
        private final Class<?> sourceType;

        /** bean拷贝的目标类型 */
        private final Class<?> targetType;

        public DefaultCglibConverter(Class<?> sourceType, Class<?> targetType) {
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        @Override
        public Object convert(Object sourcePropertyValue, Class targetPropertyType, Object targetPropertySetterName) {
            if(sourcePropertyValue != null) {
                String propertyName = resolvePropertyName((String) targetPropertySetterName);
                try {
                    Field sourcePropertyField = ReflectionUtils.findField(sourceType, propertyName); //走缓存
                    Field targetPropertyField = ReflectionUtils.findField(targetType, propertyName); //走缓存
                    if (sourcePropertyField != null && targetPropertyField != null) {
                        TypeDescriptor sourceTypeDescriptor = new TypeDescriptor(sourcePropertyField);
                        TypeDescriptor targetTypeDescriptor = new TypeDescriptor(targetPropertyField);
                        if (CONVERSION_SERVICE.canConvert(sourceTypeDescriptor, targetTypeDescriptor)) {
                            return CONVERSION_SERVICE.convert(sourcePropertyValue, sourceTypeDescriptor, targetTypeDescriptor);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error(String.format("bean转换出错：%s", e.getMessage()), e);
                }
            }
            return null;
        }

        protected String resolvePropertyName(String propertySetterName) {
            if(propertySetterName.startsWith("set")) {
                propertySetterName = propertySetterName.substring(3);
            } else if(propertySetterName.startsWith("is")) {
                propertySetterName = propertySetterName.substring(2);
            }
            return StringUtils.lowerCaseFirstChar(propertySetterName);
        }

    }

    /**
     * bean拷贝的默认ConversionService
     */
    static class DefaultConversionService extends FormattingConversionService {

        public DefaultConversionService() {
            super();
            ApplicationConversionService.configure(this);
            ApplicationConstants.DEFAULT_DATETIME_FORMATTER_REGISTRAR.registerFormatters(this);
            addApplicationConverters();
        }

        protected void addApplicationConverters() {
            addConverter(new DefaultXXO2XXOConverter()); //XXO -> XXO
            addConverter(new DefaultXXO2StringConverter()); //XXO -> String
            addConverter(new DefaultString2XXOConverter()); //String -> XXO
        }

    }

    /**
     * 默认XXO到XXO之间的拷贝
     * 例如DO -> DTO、PO -> DO、DTO -> VO等
     */
    static class DefaultXXO2XXOConverter implements GenericConverter {

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(new ConvertiblePair(Convertible.class, Convertible.class));
        }

        @Override
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            Object target = BeanUtils.instantiateClass(targetType.getType());
            return copy(source, target); //递归深度拷贝
        }

    }

    /**
     * 默认XXO到String的拷贝(只考虑JSON)
     */
    static class DefaultXXO2StringConverter implements GenericConverter {

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(new ConvertiblePair(Convertible.class, String.class));
        }

        @Override
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            return JsonUtils.object2Json(source);
        }

    }

    /**
     * 默认String到XXO的拷贝(只考虑JSON)
     */
    static class DefaultString2XXOConverter implements GenericConverter {

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(new ConvertiblePair(String.class, Convertible.class));
        }

        @Override
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            return JsonUtils.json2Object((String) source, targetType.getType());
        }

    }

}