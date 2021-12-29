package com.penglecode.xmodule.common.support;

import com.penglecode.xmodule.common.exception.ApplicationValidationException;
import com.penglecode.xmodule.common.util.ValidatorUtils;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * 基于javax.validation.Validator的JavaBean校验辅助类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/19 20:18
 */
@SuppressWarnings("unchecked")
public class BeanValidator {

    private BeanValidator() {}

    /**
     * 对指定的Bean进行数据校验
     *
     * @param bean      - 被校验的bean实例
     * @param props     - 指定校验bean中的特定属性字段，如果为空则校验全部属性字段
     * @param <T>       - bean的类型
     * @param <P>       - 指定字段的类型
     */
    @SafeVarargs
    public static <T,P> void validateBean(T bean, SerializableFunction<T,P>... props) throws ApplicationValidationException {
        validateBean(bean, new Class<?>[0], props);
    }

    /**
     * 对指定的Bean进行数据校验
     *
     * @param bean      - 被校验的bean实例
     * @param groups    - 验证组
     * @param props     - 指定校验bean中的特定属性字段，如果为空则校验全部属性字段
     * @param <T>       - bean的类型
     * @param <P>       - 指定字段的类型
     */
    @SafeVarargs
    public static <T,P> void validateBean(T bean, Class<?>[] groups, SerializableFunction<T,P>... props) throws ApplicationValidationException {
        groups = groups == null ? new Class<?>[0] : groups;
        Validator validator = ValidatorUtils.getDefaultValidator();
        if(props != null && props.length > 0) {
            for(SerializableFunction<T,P> prop : props) {
                Method getterMethod = BeanIntrospector.introspectMethod(prop);
                String propName = BeanIntrospector.introspectFieldName(getterMethod);
                Set<ConstraintViolation<T>> validationResults = validator.validateProperty(bean, propName, groups);
                checkValidationResults(validationResults);
            }
        } else {
            Set<ConstraintViolation<T>> validationResults = validator.validate(bean, groups);
            checkValidationResults(validationResults);
        }

    }

    /**
     * 对指定的Bean进行数据校验
     *
     * @param map      - 被校验的map实例
     * @param props     - 指定校验bean中的特定属性字段，如果为空则校验全部属性字段
     * @param <T>       - bean的类型
     * @param <P>       - 指定字段的类型
     */
    @SafeVarargs
    public static <T,P> void validateMap(Map<String,Object> map, SerializableFunction<T,P>... props) throws ApplicationValidationException {
        validateMap(map, new Class<?>[0], props);
    }

    /**
     * 对指定的Bean进行数据校验
     *
     * @param map      - 被校验的map实例
     * @param groups    - 验证组
     * @param props     - 指定校验bean中的特定属性字段，如果为空则校验全部属性字段
     * @param <T>       - bean的类型
     * @param <P>       - 指定字段的类型
     */
    @SafeVarargs
    public static <T,P> void validateMap(Map<String,Object> map, Class<?>[] groups, SerializableFunction<T,P>... props) throws ApplicationValidationException {
        groups = groups == null ? new Class<?>[0] : groups;
        Validator validator = ValidatorUtils.getDefaultValidator();
        if(props != null && props.length > 0) {
            for(SerializableFunction<T,P> prop : props) {
                Method getterMethod = BeanIntrospector.introspectMethod(prop);
                String propName = BeanIntrospector.introspectFieldName(getterMethod);
                Object propValue = map.get(propName);
                Set<ConstraintViolation<T>> validationResults = validator.validateValue((Class<T>)getterMethod.getDeclaringClass(), propName, propValue, groups);
                checkValidationResults(validationResults);
            }
        }
    }

    /**
     * 应用某个具体Bean字段上的校验规则对某个具体值进行校验
     * @param propValue     - 属性值
     * @param prop          - 属性引用
     * @param <T>       - bean的类型
     * @param <P>       - 指定字段的类型
     */
    public static <T,P> void validateProperty(P propValue, SerializableFunction<T,P> prop) throws ApplicationValidationException {
        validateProperty(propValue, new Class<?>[0], prop);
    }

    /**
     * 应用某个具体Bean字段上的校验规则对某个具体值进行校验
     * @param propValue     - 属性值
     * @param groups        - 分组
     * @param prop          - 属性引用
     * @param <T>
     * @param <P>
     * @throws ApplicationValidationException
     */
    public static <T,P> void validateProperty(P propValue, Class<?>[] groups, SerializableFunction<T,P> prop) throws ApplicationValidationException {
        groups = groups == null ? new Class<?>[0] : groups;
        Validator validator = ValidatorUtils.getDefaultValidator();
        Method getterMethod = BeanIntrospector.introspectMethod(prop);
        String propName = BeanIntrospector.introspectFieldName(getterMethod);
        Set<ConstraintViolation<T>> validationResults = validator.validateValue((Class<T>)getterMethod.getDeclaringClass(), propName, propValue, groups);
        checkValidationResults(validationResults);
    }

    protected static <T> void checkValidationResults(Set<ConstraintViolation<T>> validationResults) {
        if(!CollectionUtils.isEmpty(validationResults)) {
            ConstraintViolation<T> firstResult = validationResults.iterator().next();
            ErrorCode errorCode = GlobalErrorCode.ERR400; //参数校验类错误统一归属到HTTP(400)下面
            throw new ApplicationValidationException(errorCode.getCode(), firstResult.getMessage());
        }
    }

}
