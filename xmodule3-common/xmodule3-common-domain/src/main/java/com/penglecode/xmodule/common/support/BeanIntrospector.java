package com.penglecode.xmodule.common.support;

import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 基于JAVA8方法引用的Getter方法自省器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/27 11:44
 */
public class BeanIntrospector {

    private static final ConcurrentMap<SerializableFunction<?,?>, Member[]> CACHE = new ConcurrentHashMap<>();

    private BeanIntrospector() {}

    /**
     * 根据方法引用获取对应的Getter方法
     * @param getterReference   - Getter方法引用，例如：User::getUserName
     * @param <T>               - Getter方法所属实例
     * @param <R>               - Getter方法返回值
     * @return  [Getter方法, Getter方法对应的属性字段]
     */
    public static <T,R> Method introspectMethod(SerializableFunction<T,R> getterReference) {
        Member[] getterMembers = CACHE.computeIfAbsent(getterReference, BeanIntrospector::introspectMembers);
        return (Method) getterMembers[0];
    }

    /**
     * 根据getter方法内省出其字段名
     * @param getterMethod
     * @return 属性名
     */
    public static String introspectFieldName(Method getterMethod) {
        try {
            String expectedFieldName = getterMethod.getName();
            if(expectedFieldName.startsWith("get") && expectedFieldName.length() > 3) {
                return Introspector.decapitalize(expectedFieldName.substring(3));
            } else if(expectedFieldName.startsWith("is") && expectedFieldName.length() > 2) {
                return Introspector.decapitalize(expectedFieldName.substring(2));
            }
            throw new NoSuchFieldException(String.format("No field (%s) found for getter method: %s", expectedFieldName, getterMethod));
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * 根据方法引用获取对应的Getter方法
     * @param getterReference   - Getter方法引用，例如：User::getUserName
     * @param <T>               - Getter方法所属实例
     * @param <R>               - Getter方法返回值
     * @return  [Getter方法, Getter方法对应的属性字段]
     */
    public static <T,R> Field introspectField(SerializableFunction<T,R> getterReference) {
        try {
            Member[] getterMembers = CACHE.computeIfAbsent(getterReference, BeanIntrospector::introspectMembers);
            Method getterMethod = (Method) getterMembers[0];
            Field getterField = (Field) getterMembers[1];
            if(getterField == null) {
                String expectedFieldName = introspectFieldName(getterMethod);
                throw new NoSuchFieldException(String.format("No field (%s) found for getter method: %s", expectedFieldName, getterMethod));
            }
            return getterField;
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * 根据方法引用获取对应的Getter方法及属性字段
     * @param getterReference   - Getter方法引用，例如：User::getUserName
     * @param <T>               - Getter方法所属实例
     * @param <R>               - Getter方法返回值
     * @return  [Getter方法, Getter方法对应的属性字段]
     */
    protected static <T,R> Member[] introspectMembers(SerializableFunction<T,R> getterReference) {
        try {
            //1、获取SerializedLambda
            Method serialMethod = getterReference.getClass().getDeclaredMethod("writeReplace");
            serialMethod.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) serialMethod.invoke(getterReference);
            //2、获取Getter方法名
            String implMethodName = serializedLambda.getImplMethodName();
            String fieldName;
            if(implMethodName.startsWith("get") && implMethodName.length() > 3) {
                fieldName = Introspector.decapitalize(implMethodName.substring(3));
            } else if(implMethodName.startsWith("is") && implMethodName.length() > 2) {
                fieldName = Introspector.decapitalize(implMethodName.substring(2));
            } else if(implMethodName.startsWith("lambda$")) {
                throw new IllegalArgumentException(String.format("Parameter 'getterReference' (implMethodName=%s) only applies to method-references, not lambda expressions!", implMethodName));
            } else {
                throw new IllegalArgumentException(String.format("Parameter 'getterReference' (implMethodName=%s) not a getter method-references!", implMethodName));
            }
            //3、获取Getter方法所属类Class
            String implClassName = serializedLambda.getImplClass().replace("/", ".");
            Class<?> implClass = Class.forName(implClassName, false, ClassUtils.getDefaultClassLoader());
            //4、获取Getter方法所对应的
            Method getterMethod = ReflectionUtils.findMethod(implClass, implMethodName);
            if(getterMethod == null) {
                throw new NoSuchMethodException(String.format("No method (%s) found in %s", implMethodName, implClassName));
            }
            Field getterField = ReflectionUtils.findField(implClass, fieldName);
            return new Member[]{getterMethod, getterField};
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
