package com.penglecode.xmodule.common.mybatis.dsl;

import com.penglecode.xmodule.common.domain.DomainObject;
import com.penglecode.xmodule.common.support.BeanIntrospector;
import com.penglecode.xmodule.common.support.SerializableFunction;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 用于指定查询语句返回的列(这里的column指定就是领域对象字段名，例如userName而不是user_name)，例如：
 *
 *      1、只选择特定的列：userMapper.selectModelById(id, new QueryColumns<User>(User::getUserId, User::getUserName));
 *      2、根据条件选择列：orderMapper.selectModelById(id, new QueryColumns<Order>(column -> column.startWith("product")));
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/26 22:26
 */
public class QueryColumns {

    /**
     * 被选择的select列对应的领域对象字段名
     */
    private final Set<String> selectColumns;

    /**
     * 根据条件选择列
     */
    private final Predicate<String> selectPredicate;

    @SafeVarargs
    public <T extends DomainObject> QueryColumns(SerializableFunction<T,?>... selectColumns) {
        Set<String> propertyNames = new LinkedHashSet<>();
        if(selectColumns != null && selectColumns.length > 0) {
            for(SerializableFunction<T,?> selectColumn : selectColumns) {
                Field selectField = BeanIntrospector.introspectField(selectColumn);
                propertyNames.add(selectField.getName());
            }
        }
        this.selectColumns = Collections.unmodifiableSet(propertyNames);
        this.selectPredicate = null;
    }

    public QueryColumns(Predicate<String> selectPredicate) {
        this.selectPredicate = selectPredicate;
        this.selectColumns = Collections.unmodifiableSet(Collections.emptySet());
    }

    public Set<String> getSelectColumns() {
        return selectColumns;
    }

    public Predicate<String> getSelectPredicate() {
        return selectPredicate;
    }

}
