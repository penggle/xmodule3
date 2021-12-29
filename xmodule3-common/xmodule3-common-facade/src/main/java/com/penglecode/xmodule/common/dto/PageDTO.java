package com.penglecode.xmodule.common.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页请求DTO
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/15 23:08
 */
public class PageDTO implements BaseDTO {

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
     * 排序列表
     */
    private List<OrderDTO> orders = new ArrayList<>();

    public PageDTO() {
    }

    public PageDTO(Integer currentPage, Integer pageSize, List<OrderDTO> orders) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.orders = orders;
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

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "{currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", orders=" + orders + "}";
    }
}
