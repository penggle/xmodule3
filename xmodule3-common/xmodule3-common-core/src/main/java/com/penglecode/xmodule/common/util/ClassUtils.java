package com.penglecode.xmodule.common.util;

import org.springframework.core.ResolvableType;

/**
 * Class工具类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/9/11 21:01
 */
@SuppressWarnings({"unchecked"})
public class ClassUtils extends org.springframework.util.ClassUtils {

    private ClassUtils() {}

    /**
     * 获取指定泛型父类的具体子类的实际泛型声明类型
     *
     * 例如：
     * class SuperClass<T> { ... }
     * class SubClass extends SuperClass<String> { ... }
     * 使用该方法能得到具体的泛型类型: java.lang.String
     *
     * @param subClass
     * @param superClass
     * @param index
     * @return
     */
    public static <T,G> Class<G> getSuperClassGenericType(Class<? extends T> subClass, Class<T> superClass, int index) {
        ResolvableType rt = ResolvableType.forClass(subClass);
        while(!rt.equals(ResolvableType.NONE) && !superClass.equals(rt.getRawClass())) {
            rt = rt.getSuperType();
        }
        return (Class<G>) rt.getGeneric(index).getType();
    }

}
