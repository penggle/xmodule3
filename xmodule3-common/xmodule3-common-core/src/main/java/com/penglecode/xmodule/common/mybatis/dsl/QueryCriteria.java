package com.penglecode.xmodule.common.mybatis.dsl;

import com.penglecode.xmodule.common.domain.DomainObject;
import com.penglecode.xmodule.common.domain.Order;
import com.penglecode.xmodule.common.util.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 查询、更新、删除等SQL查询条件
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/10 16:43
 */
public abstract class QueryCriteria<E extends DomainObject> implements DomainObject {

    private static final long serialVersionUID = 1L;

    public static final String TABLE_ALIAS_NAME = "t";

    /**
     * 查询条件的example，它是一个对应数据库表的实体数据模型
     */
    private final E example;

    /**
     * 查询条件列表
     */
    private final Set<Criterion> criteria1;

    /**
     * 排序对象
     */
    private final List<Order> orders;

    /**
     * 冻结条件
     * 例如在调用dynamic()方法之后就需要冻结条件
     */
    private boolean frozenCriteria;

    QueryCriteria(E example) {
        Assert.notNull(example, "Parameter 'example' can not be null!");
        this.example = example;
        this.criteria1 = new LinkedHashSet<>();
        this.orders = new ArrayList<>();
    }

    /**
     * 动态过滤查询条件，即过滤掉value为空值(null,空串,空数组,空集合)的查询条件,
     * 即实现如下的动态语句：
     * &lt;if test="example.xxx != null && example.xxx != ''"&gt;
     *     AND|OR xxx = #{example.xxx}
     * &lt;/if&gt;
     * @param frozenCriteria - 是否冻结查询条件?为true则在调用dynamic()方法之后就不能再添加新的查询条件了，否则报错
     * @return
     */
    protected QueryCriteria<E> dynamic(boolean frozenCriteria) {
        trimEmptyCriterion(criteria1);
        this.frozenCriteria = frozenCriteria;
        return this;
    }

    /**
     * 应用指定的OrderBy
     * @param orders
     * @return
     */
    protected QueryCriteria<E> orderBy(Order... orders) {
        Assert.notEmpty(orders, "Parameter 'orders' can not be null!");
        this.orders.addAll(Stream.of(orders).collect(Collectors.toList()));
        return this;
    }

    /**
     * 应用指定的OrderBy
     * @param orders
     * @return
     */
    protected QueryCriteria<E> orderBy(List<Order> orders) {
        Assert.notEmpty(orders, "Parameter 'orders' can not be null!");
        this.orders.addAll(orders);
        return this;
    }

    protected void checkCriteriaFrozen() {
        Assert.state(!isFrozenCriteria(), "All query criteria is frozen, can not add new query criteria!");
    }

    /**
     * 递归剔除value为空的条件
     * @param criteria
     */
    protected void trimEmptyCriterion(Set<? extends Criterion> criteria) {
        for(Iterator<? extends Criterion> iterator = criteria.iterator(); iterator.hasNext();) {
            Criterion criterion = iterator.next();
            if(criterion instanceof NestedCriterion) {
                Set<? extends Criterion> nestedCriteria = ((NestedCriterion) criterion).getCriteria2();
                trimEmptyCriterion(nestedCriteria);
                if(nestedCriteria.isEmpty()) { //通过对嵌套逻辑条件的过滤,如果当前嵌套逻辑组为空,则删除之
                    iterator.remove();
                }
            } else if(criterion instanceof ColumnCriterion) {
                Object value = ((ColumnCriterion) criterion).getRawValue();
                if(ObjectUtils.isEmpty(value)) { //过滤掉值为空值(null,空串,空数组,空集合)的条件
                    iterator.remove();
                }
            }
        }
    }

    protected boolean addCriterion(Criterion abstractCriterion) {
        return criteria1.add(abstractCriterion);
    }

    protected E getExample() {
        return example;
    }

    protected Set<Criterion> getCriteria1() {
        return criteria1;
    }

    protected List<Order> getOrders() {
        return orders;
    }

    protected boolean isFrozenCriteria() {
        return frozenCriteria;
    }

    protected String getColumnName(Field field) {
        return getColumnName(field.getName());
    }

    protected String getColumnName(String property) {
        return StringUtils.camelNamingToSnake(property);
    }

}
