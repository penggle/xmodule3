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

    /** 当前页码 */
    private Integer pageIndex = 1;

    /** 每页显示多少条 */
    private Integer pageSize = 10;

    /** 排序列表 */
    private List<OrderByDTO> orderBys = new ArrayList<>();

    public PageDTO() {
    }

    public PageDTO(Integer pageIndex, Integer pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public PageDTO(Integer pageIndex, Integer pageSize, List<OrderByDTO> orderBys) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.orderBys = orderBys;
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

    public List<OrderByDTO> getOrderBys() {
        return orderBys;
    }

    public void setOrderBys(List<OrderByDTO> orderBys) {
        this.orderBys = orderBys;
    }

    @Override
    public String toString() {
        return "{pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", orderBys=" + orderBys + "}";
    }
}
