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

	private List<OrderBy> orderBys = new ArrayList<>();
	
	Sort() {
		super();
	}
	
	Sort(List<OrderBy> orderBys) {
		super();
		if(orderBys != null) {
			this.orderBys = orderBys;
		}
	}
	
	public static Sort orderBy(OrderBy... orderBys) {
		return new Sort(Arrays.asList(orderBys));
	}

	public static Sort orderBy(List<OrderBy> orderBys) {
		return new Sort(orderBys);
	}

	public boolean addOrderBy(OrderBy orderBy) {
		return orderBys.add(orderBy);
	}

	public List<OrderBy> getOrderBys() {
		return orderBys;
	}

	public void setOrderBys(List<OrderBy> orderBys) {
		this.orderBys = orderBys;
	}

	@Override
	public String toString() {
		return "Sort " + orderBys + "";
	}

}
