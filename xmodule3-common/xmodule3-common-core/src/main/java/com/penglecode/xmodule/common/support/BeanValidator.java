package com.penglecode.xmodule.common.support;

import com.penglecode.xmodule.common.exception.ApplicationValidationException;
import com.penglecode.xmodule.common.util.ReflectionUtils;
import com.penglecode.xmodule.common.util.ValidatorUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 基于javax.validation.Validator的JavaBean校验辅助类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2020/3/19 20:18
 */
@SuppressWarnings("unchecked")
public class BeanValidator {

    private BeanValidator() {}

    /**
     * 对指定的Bean进行数据校验
     *
     * @param bean          - 被校验的bean实例
     * @param propGetters   - 指定校验bean中的特定属性字段的Getter方法JAVA8引用，如果为空则校验全部属性字段
     * @param <T>           - bean的类型
     * @param <P>           - 指定字段的类型
     */
    @SafeVarargs
    public static <T,P> void validateBean(T bean, SerializableFunction<T,P>... propGetters) throws ApplicationValidationException {
        validateBean(bean, new Class<?>[0], propGetters);
    }

    /**
     * 对指定的Bean进行数据校验
     *
     * @param bean          - 被校验的bean实例
     * @param groups        - 验证组
     * @param propGetters   - 指定校验bean中的特定属性字段的Getter方法JAVA8引用，如果为空则校验全部属性字段
     * @param <T>           - bean的类型
     * @param <P>           - 指定字段的类型
     */
    @SafeVarargs
    public static <T,P> void validateBean(T bean, Class<?>[] groups, SerializableFunction<T,P>... propGetters) throws ApplicationValidationException {
        groups = groups == null ? new Class<?>[0] : groups;
        Validator validator = ValidatorUtils.getDefaultValidator();
        if(propGetters != null && propGetters.length > 0) {
            for(SerializableFunction<T,P> propGetter : propGetters) {
                Field propField = BeanIntrospector.introspectField(propGetter);
                String propName = propField.getName();
                Set<ConstraintViolation<T>> validationResults = validator.validateProperty(bean, propName, groups);
                checkValidationResults(validationResults);
                //特殊处理@Valid级联校验在Validator.validateProperty(..)中无效的蛋疼问题
                if(propField.isAnnotationPresent(Valid.class)) {
                    Object propValue = ReflectionUtils.getFieldValue(propField, bean);
                    if(!ObjectUtils.isEmpty(propValue)) {
                        validateValidObject(propValue);
                    }
                }
            }
        } else {
            Set<ConstraintViolation<T>> validationResults = validator.validate(bean, groups);
            checkValidationResults(validationResults);
        }

    }

    /**
     * 对指定的Bean进行数据校验
     *
     * @param map           - 被校验的map实例
     * @param propGetters   - 指定属性字段的Getter方法JAVA8引用
     * @param <T>           - bean的类型
     * @param <P>           - 指定字段的类型
     */
    @SafeVarargs
    public static <T,P> void validateMap(Map<String,Object> map, SerializableFunction<T,P>... propGetters) throws ApplicationValidationException {
        validateMap(map, new Class<?>[0], propGetters);
    }

    /**
     * 对指定的Bean进行数据校验
     *
     * @param map           - 被校验的map实例
     * @param groups        - 验证组
     * @param propGetters   - 指定属性字段的Getter方法JAVA8引用
     * @param <T>           - bean的类型
     * @param <P>           - 指定字段的类型
     */
    @SafeVarargs
    public static <T,P> void validateMap(Map<String,Object> map, Class<?>[] groups, SerializableFunction<T,P>... propGetters) throws ApplicationValidationException {
        if(propGetters != null && propGetters.length > 0) {
            for(SerializableFunction<T,P> propGetter : propGetters) {
                Field propField = BeanIntrospector.introspectField(propGetter);
                P propValue = (P) map.get(propField.getName());
                validateProperty(propValue, propGetter, groups);
            }
        }
    }

    /**
     * 应用某个具体Bean字段上的校验规则对某个具体值进行校验
     * @param propValue     - 属性值
     * @param propGetter    - 指定属性字段的Getter方法JAVA8引用
     * @param groups        - 分组
     * @param <T>
     * @param <P>
     * @throws ApplicationValidationException
     */
    public static <T,P> void validateProperty(P propValue, SerializableFunction<T,P> propGetter, Class<?>... groups) throws ApplicationValidationException {
        groups = groups == null ? new Class<?>[0] : groups;
        Validator validator = ValidatorUtils.getDefaultValidator();
        Field propField = BeanIntrospector.introspectField(propGetter);
        String propName = propField.getName();
        Set<ConstraintViolation<T>> validationResults = validator.validateValue((Class<T>)propField.getDeclaringClass(), propName, propValue, groups);
        checkValidationResults(validationResults);
        //特殊处理@Valid级联校验在Validator.validateValue(..)中无效的蛋疼问题
        if(propField.isAnnotationPresent(Valid.class)) {
            if(!ObjectUtils.isEmpty(propValue)) {
                validateValidObject(propValue);
            }
        }
    }

    /**
     * 处理Validator.validateProperty(..)|Validator.validateValue(..)针对 {@link Valid}注解无效的问题
     * 对于具有{@link Valid}注解的字段，此处我们只考虑四种情况：
     *      1、Collection集合
     *      2、Map集合(仅仅考虑其values())
     *      3、数组
     *      4、普通JavaBean
     * @param object
     */
    protected static void validateValidObject(Object object) {
        if(object instanceof Collection) { //校验集合元素
            Collection<?> collection = (Collection<?>) object;
            for(Object item : collection) {
                validateBean(item);
            }
        } else if(object instanceof Map) { //仅仅校验Map的values()
            Collection<?> values = ((Map<?, ?>) object).values();
            for(Object value : values) {
                validateBean(value);
            }
        } else if(ObjectUtils.isArray(object)) { //校验数组元素
            Object[] array = (Object[]) object;
            for(Object item : array) {
                validateBean(item);
            }
        } else { //否则认为是普通JavaBean
            validateBean(object);
        }
    }

    protected static <T> void checkValidationResults(Set<ConstraintViolation<T>> validationResults) {
        if(!CollectionUtils.isEmpty(validationResults)) {
            ConstraintViolation<T> firstResult = validationResults.iterator().next();
            ErrorCode errorCode = GlobalErrorCode.ERR400; //参数校验类错误统一归属到HTTP(400)下面
            throw new ApplicationValidationException(errorCode.getCode(), firstResult.getMessage());
        }
    }

}
