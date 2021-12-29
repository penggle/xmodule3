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

	private final List<Order> orders;
	
	Sort() {
		this(new ArrayList<>());
	}
	
	Sort(List<Order> orders) {
		super();
		this.orders = orders;
	}
	
	public static Sort by(Order... orders) {
		return new Sort(Arrays.asList(orders));
	}

	public static Sort by(List<Order> orders) {
		return new Sort(orders);
	}

	public boolean addOrder(Order order) {
		return orders.add(order);
	}

	public List<Order> getOrders() {
		return orders;
	}

	public Order first() {
		if(orders != null && !orders.isEmpty()) {
			return orders.get(0);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "Sort " + orders + "";
	}

}
