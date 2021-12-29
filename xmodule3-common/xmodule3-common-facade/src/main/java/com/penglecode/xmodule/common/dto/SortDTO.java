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

    private List<OrderDTO> orders = new ArrayList<>();

    public SortDTO() {
    }

    public SortDTO(List<OrderDTO> orders) {
        this.orders = orders;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "{orders=" + orders +"}";
    }
}
