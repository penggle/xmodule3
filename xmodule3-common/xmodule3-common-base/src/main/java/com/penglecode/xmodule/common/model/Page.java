package com.penglecode.xmodule.common.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
@Schema(description="通用分页对象")
public class Page implements BaseDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 当前页码
	 */
	@Schema(description="当前页码(默认1)", defaultValue="1")
	@NotNull(message="当前页码(pageIndex)不能为空!")
	@Min(value=1, message="当前页码(pageIndex)不能小于1")
	private Integer pageIndex = 1;
	
	/**
	 * 每页显示条数
	 */
	@Schema(description="每页显示条数(默认10)", defaultValue="10")
	@NotNull(message="每页显示条数(pageSize)不能为空!")
	@Min(value=1, message="每页显示条数(pageSize)不能小于1")
	private Integer pageSize = 10;
	
	/**
	 * 总记录数
	 */
	@Schema(description="总记录数(默认0)", hidden=true)
	private Integer totalRowCount = 0;
	
	/**
	 * 总分页数
	 */
	@Schema(description="总分页数(默认0)", hidden=true)
	private Integer totalPageCount = 0;

	/**
	 * 分页排序列表
	 */
	@Valid
	@Schema(description="分页排序列表")
	private List<OrderBy> orderBys = new ArrayList<>();
	
	protected Page() {
		super();
	}

	protected Page(Integer pageIndex, Integer pageSize) {
		super();
		if(pageIndex != null && pageIndex > 0){
			this.pageIndex = pageIndex;
		}
		if(pageSize != null && pageSize > 0){
			this.pageSize = pageSize;
		}
	}

	protected Page(Integer pageIndex, Integer pageSize, List<OrderBy> orderBys) {
		this(pageIndex, pageSize);
		if(orderBys != null){
			this.orderBys = orderBys;
		}
	}

	protected Page(Integer pageIndex, Integer pageSize, Integer totalRowCount) {
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

	public static Page of(Integer currentPage, Integer pageSize, List<OrderBy> orderBys) {
		return new Page(currentPage, pageSize, orderBys);
	}

	public static Page copyOf(Page page) {
		Page newPage = new Page();
		newPage.setPageIndex(page.getPageIndex());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalRowCount(page.getTotalRowCount());
		newPage.setOrderBys(page.getOrderBys());
		return newPage;
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
		calcTotalPageCount(); //计算totalPageCount
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
		return totalPageCount;
	}

	public Integer offset() {
		return (pageIndex - 1) * pageSize;
	}
	
	public Integer limit() {
		return getPageSize();
	}

	protected void calcTotalPageCount() {
		if(totalRowCount <= 0){
			totalPageCount = 0;
		}else{
			totalPageCount = totalRowCount % pageSize == 0 ? totalRowCount / pageSize : (totalRowCount / pageSize) + 1;
		}
	}

	public String toString() {
		return "Page [pageIndex=" + pageIndex + ", pageSize=" + pageSize
				+ ", totalRowCount=" + totalRowCount + ", totalPageCount="
				+ totalPageCount + ", orderBys=" + orderBys + "]";
	}

}
