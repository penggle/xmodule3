package com.penglecode.xmodule.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 排序请求DTO
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/15 23:12
 */
@Schema
public class OrderByDTO implements BaseDTO {

    private static final long serialVersionUID = 1L;

    /** 排序字段名 */
    @Schema(description="排序字段名(与结果集中字段一致)", defaultValue="createTime")
    private String property;

    /** 排序顺序：asc|desc */
    @Schema(description="排序顺序(asc|desc)", defaultValue="desc")
    private String direction;

    public OrderByDTO() {
    }

    public OrderByDTO(String property, String direction) {
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
