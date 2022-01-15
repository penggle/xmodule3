package com.penglecode.xmodule.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页请求DTO
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/15 23:08
 */
@Schema
public class PageDTO implements BaseDTO {

    private static final long serialVersionUID = 1L;

    /** 当前页码 */
    @Schema(description="当前页码(默认1)", defaultValue="1")
    private Integer pageIndex = 1;

    /** 每页显示条数 */
    @Schema(description="每页显示条数(默认10)", defaultValue="10")
    private Integer pageSize = 10;

    /** 排序列表 */
    @Schema(description="排序列表(支持多个字段排序)")
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
