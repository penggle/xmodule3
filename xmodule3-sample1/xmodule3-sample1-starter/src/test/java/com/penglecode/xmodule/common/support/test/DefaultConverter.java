package com.penglecode.xmodule.common.support.test;

import org.springframework.cglib.core.Converter;
import org.springframework.util.ClassUtils;

import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.*;

public class DefaultConverter implements Converter {

    /**
     * 处理深拷贝的类型
     */
    private final Set<Class<?>> deepCopyTypes;

    public DefaultConverter() {
        this(Collections.emptySet());
    }

    public DefaultConverter(Set<Class<?>> deepCopyTypes) {
        this.deepCopyTypes = deepCopyTypes == null ? Collections.emptySet() : deepCopyTypes;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object convert(Object sourcePropertyValue, Class targetPropertyType, Object targetPropertySetterName) {
        if(sourcePropertyValue != null) {
            Class sourcePropertyType = sourcePropertyValue.getClass();
            //转换原始类型为包装类型，避免isAssignableFrom()在原始类型上调用时失效问题
            if(sourcePropertyType.isPrimitive()) {
                sourcePropertyType = ClassUtils.resolvePrimitiveIfNecessary(sourcePropertyType);
            }
            if(targetPropertyType.isPrimitive()) {
                targetPropertyType = ClassUtils.resolvePrimitiveIfNecessary(targetPropertyType);
            }
            if(Collection.class.isAssignableFrom(sourcePropertyType) && Collection.class.isAssignableFrom(targetPropertyType)) {
                Collection collectionSourceValue = (Collection) sourcePropertyValue;
                if(!collectionSourceValue.isEmpty()) {
                    Object itemValue = collectionSourceValue.iterator().next();
                    Class itemType = itemValue.getClass();
                    if(itemValue != null && isSimpleType(itemType)) {
                    }
                }
            }


            //1、类型相同或者是父子类型(考虑到类型擦除，需要排除集合类型)
            if(targetPropertyType.isAssignableFrom(sourcePropertyType) && !isCollectionType(targetPropertyType) && !isCollectionType(sourcePropertyType)) {
                return sourcePropertyValue;
            }
            //2、如果指定了deepCopyTypes，且source/target都不是简单类型则需要处理深拷贝
            else if(!deepCopyTypes.isEmpty() && !isSimpleType(sourcePropertyType) && !isSimpleType(targetPropertyType)) {
                for(Class<?> deepCopyType : deepCopyTypes) {
                    if(deepCopyType.isAssignableFrom(targetPropertyType)) {

                    }
                }
            }
        }
        return null;
    }

    /**
     * 判断指定类型是否是JAVA集合类型(包括Collection和Map)
     * @param type
     * @return
     */
    protected boolean isCollectionType(Class<?> type) {
        return Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type);
    }

    /**
     * 判断指定类型是否是简单类型
     * @param type
     * @return
     */
    protected boolean isSimpleType(Class<?> type) {
        if(ClassUtils.isPrimitiveOrWrapper(type)) { //八大基本类型及其包装类
            return true;
        } else if(Number.class.isAssignableFrom(type)){ //数字类型(如BigDecimal,AtomicInteger)
            return true;
        } else if(CharSequence.class.isAssignableFrom(type)) { //字符串类型
            return true;
        } else if(Date.class.isAssignableFrom(type)) { //java.util.Date及其子类(java.sql.Date|Time|Timestamp)
            return true;
        } else if(TemporalAccessor.class.isAssignableFrom(type) || TemporalAmount.class.isAssignableFrom(type)) { //JAVA8日期时间类型
            return true;
        }
        return false; //否则认为是复杂类型
    }

}