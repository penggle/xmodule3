package com.penglecode.xmodule.common.support;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 基于JAVA8方法引用(lambda的一种特殊形式)方式来快速通过普通JavaBean构造Map的通用Builder
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
 *         Map<String,Object> map = MapLambdaBuilder.of(account)
 *                 .with(Account::getAcctNo)
 *                 .with(Account::getCustNo)
 *                 .with(Account::getCustName)
 *                 .with(Account::getIdCode, "61283291929382912X")
 *                 .with(Account::getMobile)
 *                 .build();
 *         System.out.println(map);
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/27 19:12
 */
@SuppressWarnings("unchecked")
public class MapLambdaBuilder<T> {

    private final Supplier<Map<String,Object>> instantiator;

    private final T object;

    private final Map<String,Object[]> mapEntries = new LinkedHashMap<>();

    private boolean trimEmpty = false;

    private MapLambdaBuilder(Supplier<Map<String,Object>> instantiator, T object) {
        Assert.notNull(instantiator, "Parameter 'instantiator' can not be null!");
        Assert.notNull(object, "Parameter 'object' can not be null!");
        this.instantiator = instantiator;
        this.object = object;
    }

    public static <T> MapLambdaBuilder<T> of(T object) {
        return new MapLambdaBuilder<>(LinkedHashMap::new, object);
    }

    public static <T> MapLambdaBuilder<T> of(Supplier<Map<String,Object>> instantiator, T object) {
        return new MapLambdaBuilder<>(instantiator, object);
    }

    /**
     * 将指定对象字段的name和value放入map中
     * @param getterReference   - 指定对象的字段
     * @param defaultValue      - 如果object的指定字段值为空值(null，空串)则使用defaultValue替换之
     * @param <R>
     * @return
     */
    public <R> MapLambdaBuilder<T> with(SerializableFunction<T,R> getterReference, R... defaultValue) {
        if(getterReference != null && object != null) {
            Field field = BeanIntrospector.introspectField(getterReference);
            String fieldName = field.getName();
            R defaultVal = (defaultValue != null && defaultValue.length == 1) ? defaultValue[0] : null;
            mapEntries.put(fieldName, new Object[] {getterReference, defaultVal});
        }
        return this;
    }

    /**
     * 剔除空值(null, 空串)
     * @return
     */
    public MapLambdaBuilder<T> trimEmpty() {
        this.trimEmpty = true;
        return this;
    }

    public Map<String,Object> build() {
        final Map<String,Object> map = instantiator.get();
        mapEntries.forEach((fieldName, fieldValues) -> {
            Object fieldValue = ((SerializableFunction<T,Object>)fieldValues[0]).apply(object);
            fieldValue = ObjectUtils.isEmpty(fieldValue) ? fieldValues[1] : fieldValue;
            if(trimEmpty) {
                if(!ObjectUtils.isEmpty(fieldValue)) {
                    map.put(fieldName, fieldValue);
                }
            } else {
                map.put(fieldName, fieldValue);
            }
        });
        return map;
    }

}
