package com.penglecode.xmodule.common.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 排序请求DTO
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/15 23:19
 */
public class SortDTO implements BaseDTO {

    private static final long serialVersionUID = 1L;

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
