package com.penglecode.xmodule.common.dto;

/**
 * 排序请求DTO
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/15 23:12
 */
public class OrderDTO implements BaseDTO {

    private static final long serialVersionUID = 1L;

    /** 排序字段名 */
    private String property;

    /** 排序顺序：asc|desc */
    private String direction;

    public OrderDTO() {
    }

    public OrderDTO(String property, String direction) {
        this.property = property;
        this.direction = direction;
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

    @Override
    public String toString() {
        return "{property='" + property + '\'' + ", direction='" + direction + '\'' + '}';
    }
}