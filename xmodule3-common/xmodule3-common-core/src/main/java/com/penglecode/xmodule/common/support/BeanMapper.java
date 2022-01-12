package com.penglecode.xmodule.common.support;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.penglecode.xmodule.common.exception.ApplicationException;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.List;
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

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        //去掉默认的时间戳格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //单引号处理,允许单引号
        objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
    }

    private BeanMapper() {}

    /**
     * 将源对象转换成指定的目标对象
     * 默认严格按源对象与目标对象的字段一一对应
     * @param source            - 源对象
     * @param targetSupplier    - 目标Supplier
     * @param <T> 目标类型
     * @param <S> 源类型
     * @return 如果source为null则返回null,否则执行bean映射转换
     */
    public static <T,S> T map(S source, Supplier<T> targetSupplier) {
        if(source != null) {
            Assert.notNull(targetSupplier, "Parameter 'targetSupplier' can not be null!");
            return map(source, targetSupplier.get());
        }
        return null;
    }

    /**
     * 将源对象转换成指定的目标对象
     * 默认严格按源对象与目标对象的字段一一对应
     * @param source        - 源对象
     * @param targetType    - 目标类型
     * @param <T> 目标类型
     * @param <S> 源类型
     * @return 如果source为null则返回null,否则执行bean映射转换
     */
    public static <T,S> T map(S source, Class<T> targetType) {
        if(source != null) {
            Assert.notNull(targetType, "Parameter 'targetType' can not be null!");
            try {
                String jsonSource = objectMapper.writeValueAsString(source);
                return objectMapper.readValue(jsonSource, targetType);
            } catch (Exception e) {
                throw new BeanMappingException(String.format("对象转换出错: %s", e.getMessage()), e);
            }
        }
        return null;
    }

    /**
     * 将源对象转换成指定的目标对象
     * 默认严格按源对象与目标对象的字段一一对应
     * @param source        - 源对象
     * @param target        - 目标对象
     * @param <T> 目标类型
     * @param <S> 源类型
     * @return 如果source为null则返回null,否则执行bean映射转换
     */
    public static <T,S> T map(S source, T target) {
        if(source != null) {
            Assert.notNull(target, "Parameter 'target' can not be null!");
            try {
                String jsonSource = objectMapper.writeValueAsString(source);
                ObjectReader objectReader = objectMapper.readerForUpdating(target);
                return objectReader.readValue(jsonSource);
            } catch (Exception e) {
                throw new BeanMappingException(String.format("对象转换出错: %s", e.getMessage()), e);
            }
        }
        return null;
    }

    /**
     * 将源对象转换成指定的目标对象
     * 默认严格按源对象与目标对象的字段一一对应
     * @param sources           - 源对象
     * @param targetSupplier    - 目标Supplier
     * @param <T> 目标类型
     * @param <S> 源类型
     * @return 如果sources为null则返回null,否则执行bean映射转换
     */
    public static <T,S> List<T> map(List<S> sources, Supplier<T> targetSupplier) {
        if(sources != null) {
            Assert.notNull(targetSupplier, "Parameter 'targetSupplier' can not be null!");
            return sources.stream().map(source -> map(source, targetSupplier)).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 将源对象转换成指定的目标对象
     * 默认严格按源对象与目标对象的字段一一对应
     * @param sources           - 源对象
     * @param targetType        - 目标类型
     * @param <T> 目标类型
     * @param <S> 源类型
     * @return 如果sources为null则返回null,否则执行bean映射转换
     */
    public static <T,S> List<T> map(List<S> sources, Class<T> targetType) {
        if(sources != null) {
            Assert.notNull(targetType, "Parameter 'targetType' can not be null!");
            return sources.stream().map(source -> map(source, targetType)).collect(Collectors.toList());
        }
        return null;
    }

    public static class BeanMappingException extends ApplicationException {

        private static final long serialVersionUID = 1L;

        public BeanMappingException(Throwable cause) {
            super(cause);
        }

        public BeanMappingException(String message, Throwable cause) {
            super(message, cause);
        }

    }

}
