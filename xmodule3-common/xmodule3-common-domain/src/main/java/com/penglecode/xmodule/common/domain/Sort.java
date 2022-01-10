package com.penglecode.xmodule.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用排序对象
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 15:15
 */
public class Sort implements Serializable {

	private static final long serialVersionUID = 1L;

	private final List<OrderBy> orderBys;
	
	Sort() {
		this(new ArrayList<>());
	}
	
	Sort(List<OrderBy> orderBys) {
		super();
		this.orderBys = orderBys;
	}
	
	public static Sort by(OrderBy... orderBys) {
		return new Sort(Arrays.asList(orderBys));
	}

	public static Sort by(List<OrderBy> orderBys) {
		return new Sort(orderBys);
	}

	public boolean addOrderBy(OrderBy orderBy) {
		return orderBys.add(orderBy);
	}

	public List<OrderBy> getOrderBys() {
		return orderBys;
	}

	public OrderBy first() {
		if(orderBys != null && !orderBys.isEmpty()) {
			return orderBys.get(0);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "Sort " + orderBys + "";
	}

}
