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
	private Integer pageIndex = 1;
	
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
	private List<OrderBy> orderBys = new ArrayList<>();
	
	Page() {
		super();
	}

	Page(Integer pageIndex, Integer pageSize) {
		super();
		if(pageIndex != null && pageIndex > 0){
			this.pageIndex = pageIndex;
		}
		if(pageSize != null && pageSize > 0){
			this.pageSize = pageSize;
		}
	}

	Page(Integer pageIndex, Integer pageSize, List<OrderBy> orderBys) {
		this(pageIndex, pageSize);
		if(orderBys != null){
			this.orderBys = orderBys;
		}
	}

	Page(Integer pageIndex, Integer pageSize, Integer totalRowCount) {
		this(pageIndex, pageSize);
		if(totalRowCount != null){
			this.totalRowCount = totalRowCount;
		}
	}

	public static Page ofDefault() {
		return new Page();
	}

	public static Page of(Integer currentPage, Integer pageSize) {
		return new Page(currentPage, pageSize);
	}
	
	public static Page of(Integer currentPage, Integer pageSize, Integer totalRowCount) {
		return new Page(currentPage, pageSize, totalRowCount);
	}

	public static Page of(Integer currentPage, Integer pageSize, OrderBy... orderBys) {
		return new Page(currentPage, pageSize, Stream.of(orderBys).collect(Collectors.toList()));
	}
	
	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
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

	public List<OrderBy> getOrderBys() {
		return orderBys;
	}

	public void setOrderBys(List<OrderBy> orderBys) {
		this.orderBys = orderBys;
	}

	public void addOrder(OrderBy orderBy) {
		this.orderBys.add(orderBy);
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
		return (pageIndex - 1) * pageSize;
	}
	
	public Integer getLimit() {
		return getPageSize();
	}

	public String toString() {
		return "Page [pageIndex=" + pageIndex + ", pageSize=" + pageSize
				+ ", totalRowCount=" + totalRowCount + ", totalPageCount="
				+ totalPageCount + ", orderBys=" + orderBys + "]";
	}

}
