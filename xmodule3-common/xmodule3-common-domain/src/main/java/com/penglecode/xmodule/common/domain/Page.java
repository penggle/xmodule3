package com.penglecode.xmodule.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 通用分页Page对象
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/5/15 15:15
 */
public class Page implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 当前页码
	 */
	private Integer currentPage = 1;
	
	/**
	 * 每页显示多少条
	 */
	private Integer pageSize = 10;
	
	/**
	 * 查询总记录数
	 */
	private Integer totalRowCount = 0;
	
	/**
	 * 可分多少页
	 */
	private Integer totalPageCount = 0;

	/**
	 * 分页排序
	 */
	private List<Order> orders = new ArrayList<>();
	
	Page() {
		super();
	}

	Page(Integer currentPage, Integer pageSize) {
		super();
		if(currentPage != null && currentPage > 0){
			this.currentPage = currentPage;
		}
		if(pageSize != null && pageSize > 0){
			this.pageSize = pageSize;
		}
	}

	Page(Integer currentPage, Integer pageSize, List<Order> orders) {
		this(currentPage, pageSize);
		if(orders != null){
			this.orders = orders;
		}
	}

	Page(Integer currentPage, Integer pageSize, Integer totalRowCount) {
		this(currentPage, pageSize);
		if(totalRowCount != null){
			this.totalRowCount = totalRowCount;
		}
	}

	public static Page of(Integer currentPage, Integer pageSize) {
		return new Page(currentPage, pageSize);
	}
	
	public static Page of(Integer currentPage, Integer pageSize, Integer totalRowCount) {
		return new Page(currentPage, pageSize, totalRowCount);
	}

	public static Page of(Integer currentPage, Integer pageSize, Order... orders) {
		return new Page(currentPage, pageSize, Stream.of(orders).collect(Collectors.toList()));
	}
	
	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(Integer totalRowCount) {
		this.totalRowCount = totalRowCount;
		getTotalPageCount(); //计算totalPageCount
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public void addOrder(Order order) {
		this.orders.add(order);
	}

	public Integer getTotalPageCount() {
		if(totalRowCount <= 0){
			totalPageCount = 0;
		}else{
			totalPageCount = totalRowCount % pageSize == 0 ? totalRowCount / pageSize : (totalRowCount / pageSize) + 1;
		}
		return totalPageCount;
	}

	public Integer getOffset() {
		return (currentPage - 1) * pageSize;
	}
	
	public Integer getLimit() {
		return getPageSize();
	}

	public String toString() {
		return "Page [currentPage=" + currentPage + ", pageSize=" + pageSize
				+ ", totalRowCount=" + totalRowCount + ", totalPageCount="
				+ totalPageCount + ", orders=" + orders + "]";
	}

}
