package com.penglecode.xmodule.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * 排序请求DTO
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/15 23:19
 */
@Schema
public class SortDTO implements BaseDTO {

    private static final long serialVersionUID = 1L;

    /** 排序列表 */
    @Schema(description="排序列表(支持多个字段排序)")
    private List<OrderByDTO> orderBys = new ArrayList<>();

    public SortDTO() {
    }

    public SortDTO(List<OrderByDTO> orderBys) {
        this.orderBys = orderBys;
    }

    public List<OrderByDTO> getOrderBys() {
        return orderBys;
    }

    public void setOrderBys(List<OrderByDTO> orderBys) {
        this.orderBys = orderBys;
    }

    @Override
    public String toString() {
        return "{orderBys=" + orderBys +"}";
    }
}
