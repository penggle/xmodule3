package com.penglecode.xmodule.common.util;

import org.springframework.core.ResolvableType;

import java.net.*;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.*;

/**
 * Class工具类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/9/11 21:01
 */
@SuppressWarnings({"unchecked"})
public class ClassUtils extends org.springframework.util.ClassUtils {

    /**
     * 默认的简单类型的祖先集合(但不包括八大基本类型及其包装类)
     */
    private static final Set<Class<?>> defaultAncestorsOfSimpleType = new HashSet<>();

    /**
     * 基本类型的默认值
     */
    private static final Map<Class<?>,Object> defaultValuesOfPrimitive = new HashMap<>();

    static {
        defaultAncestorsOfSimpleType.add(Number.class);
        defaultAncestorsOfSimpleType.add(CharSequence.class);
        defaultAncestorsOfSimpleType.add(Date.class);
        defaultAncestorsOfSimpleType.add(Calendar.class);
        defaultAncestorsOfSimpleType.add(TemporalAccessor.class);
        defaultAncestorsOfSimpleType.add(TemporalAmount.class);
        defaultAncestorsOfSimpleType.add(SocketAddress.class);
        defaultAncestorsOfSimpleType.add(InetAddress.class);
        defaultAncestorsOfSimpleType.add(URL.class);
        defaultAncestorsOfSimpleType.add(URI.class);

        defaultValuesOfPrimitive.put(boolean.class, false);
        defaultValuesOfPrimitive.put(char.class, '\u0000');
        defaultValuesOfPrimitive.put(byte.class, 0);
        defaultValuesOfPrimitive.put(short.class, 0);
        defaultValuesOfPrimitive.put(int.class, 0);
        defaultValuesOfPrimitive.put(long.class, 0L);
        defaultValuesOfPrimitive.put(float.class, 0.0f);
        defaultValuesOfPrimitive.put(double.class, 0.0d);
    }

    private ClassUtils() {}

    /**
     * 获取指定泛型父类的具体子类的实际泛型声明类型
     *
     * 例如：
     * class SuperClass<T> { ... }
     * class SubClass extends SuperClass<String> { ... }
     * 使用该方法能得到具体的泛型类型: java.lang.String
     *
     * @param subClass      - 子类(当前类)
     * @param superClass    - 父类Class
     * @param index         - zero based
     * @return
     */
    public static <T,G> Class<G> getSuperClassGenericType(Class<? extends T> subClass, Class<T> superClass, int index) {
        ResolvableType rt = ResolvableType.forClass(subClass);
        while(!rt.equals(ResolvableType.NONE) && !superClass.equals(rt.getRawClass())) {
            rt = rt.getSuperType();
        }
        return (Class<G>) rt.getGeneric(index).getType();
    }

    /**
     * 判断指定目标类型targetType是否是简单类型
     *
     * @param targetType    - 目标类型
     * @return
     */
    public static boolean isSimpleType(Class<?> targetType) {
        return isSimpleType(targetType, null);
    }

    /**
     * 判断指定目标类型targetType是否是简单类型
     *
     * @param targetType    - 目标类型
     * @param additional    - 附加的认为是简单类型的类型集合
     * @return
     */
    public static boolean isSimpleType(Class<?> targetType, Set<Class<?>> additional) {
        //八大基本类型及其包装类
        if(isPrimitiveOrWrapper(targetType)) {
            return true;
        }
        //默认的被认为是简单类型的超类集合
        for(Class<?> simpleType : defaultAncestorsOfSimpleType) {
            if(isAssignable(simpleType, targetType)) {
                return true;
            }
        }
        //附加补充的认为是简单类型的集合
        if(additional != null) {
            for(Class<?> simpleType : additional) {
                if(isAssignable(simpleType, targetType)) {
                    return true;
                }
            }
        }
        return false; //否则认为是复杂类型
    }

    /**
     * 获取原始类型的默认值
     *
     * @param primitiveType
     * @param <T>
     * @return
     */
    public static <T> T getDefaultValueOfPrimitive(Class<T> primitiveType) {
        return (T) defaultValuesOfPrimitive.get(primitiveType);
    }

}
