package com.penglecode.xmodule.common.domain;

import com.penglecode.xmodule.common.support.BeanIntrospector;
import com.penglecode.xmodule.common.support.SerializableFunction;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * 通用排序对象
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 15:15
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 排序字段名 */
	private String property;

	/** 排序顺序：asc|desc */
	private String direction;

	Order() {
		super();
	}

	Order(String property, String direction) {
		super();
		if(direction != null) {
			direction = Direction.DESC.toString().equalsIgnoreCase(direction) ? Direction.DESC.toString() : Direction.ASC.toString();
		} else {
			direction = Direction.ASC.toString();
		}
		this.property = property;
		this.direction = direction;
	}

	public static Order by(String property, Direction direction) {
		return new Order(property, direction.toString());
	}

	public static Order asc(String property) {
		return new Order(property, Direction.ASC.toString());
	}

	public static Order desc(String property) {
		return new Order(property, Direction.DESC.toString());
	}

	public static <T,R> Order by(SerializableFunction<T,R> getterReference, String order) {
		Field field = BeanIntrospector.introspectField(getterReference);
		return new Order(field.getName(), order);
	}

	public static <T,R> Order asc(SerializableFunction<T,R> getterReference) {
		Field field = BeanIntrospector.introspectField(getterReference);
		return new Order(field.getName(), Direction.ASC.toString());
	}

	public static <T,R> Order desc(SerializableFunction<T,R> getterReference) {
		Field field = BeanIntrospector.introspectField(getterReference);
		return new Order(field.getName(), Direction.DESC.toString());
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	/**
	 * Used by SpringMVC @RequestParam and JAX-RS @QueryParam
	 * @param order
	 * @return
	 */
	public static Order valueOf(String order) {
		if(order != null && !order.isEmpty()) {
			String[] orders = order.trim().split(":");
			String prop = orders[0] == null ? null : orders[0].trim();
			String dir = null;
			if(orders.length == 2) {
				dir = orders[1] == null ? null : orders[1].trim();
			}
			if(prop != null && prop.length() > 0) {
				return new Order(prop, dir);
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return property + ":" + direction;
	}

	public enum Direction {

		ASC, DESC
	}

}