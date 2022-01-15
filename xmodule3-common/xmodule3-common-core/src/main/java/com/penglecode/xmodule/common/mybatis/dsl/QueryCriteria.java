package com.penglecode.xmodule.common.mybatis.dsl;

import com.penglecode.xmodule.common.domain.DomainObject;
import com.penglecode.xmodule.common.model.OrderBy;
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
    private final List<OrderBy> orderBys;

    /**
     * 查询限制返回行数
     */
    private Integer limit;

    /**
     * 冻结条件
     * 例如在调用dynamic()方法之后就需要冻结条件
     */
    private boolean frozenCriteria;

    QueryCriteria(E example) {
        Assert.notNull(example, "Parameter 'example' can not be null!");
        this.example = example;
        this.criteria1 = new LinkedHashSet<>();
        this.orderBys = new ArrayList<>();
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
     * @param orderBys
     * @return
     */
    protected QueryCriteria<E> orderBy(OrderBy... orderBys) {
        Assert.notEmpty(orderBys, "Parameter 'orderBys' can not be null!");
        this.orderBys.addAll(Stream.of(orderBys).collect(Collectors.toList()));
        return this;
    }

    /**
     * 应用指定的OrderBy
     * @param orderBys
     * @return
     */
    protected QueryCriteria<E> orderBy(List<OrderBy> orderBys) {
        if(orderBys != null) {
            this.orderBys.addAll(orderBys);
        }
        return this;
    }

    /**
     * 应用指定的查询限制返回行数
     * 需要底层数据库支持：
     *  1、对于MySQL则使用limit字句实现
     *  2、对于Oracle则使用rownum隐藏列来实现
     *  n、对于不支持的数据库不会报错，即没有任何效果
     * 注意如果当前是分页查询，则忽略该limit设置
     * @param limit - 该值必须大于0，否则忽略
     * @return
     */
    protected QueryCriteria<E> limit(int limit) {
        Assert.isTrue(limit > 0, "Parameter 'limit' must be > 0!");
        this.limit = limit;
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

    protected void addCriterion(Criterion abstractCriterion) {
        criteria1.add(abstractCriterion);
    }

    protected E getExample() {
        return example;
    }

    protected Set<Criterion> getCriteria1() {
        return criteria1;
    }

    protected List<OrderBy> getOrders() {
        return orderBys;
    }

    public Integer getLimit() {
        return limit;
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
