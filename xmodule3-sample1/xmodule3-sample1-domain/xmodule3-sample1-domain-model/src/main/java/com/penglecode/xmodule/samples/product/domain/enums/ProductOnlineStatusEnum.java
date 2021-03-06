package com.penglecode.xmodule.samples.product.domain.enums;

/**
 * 商品上下架状态枚举
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @created 2021年10月21日 下午 23:18
 */
public enum ProductOnlineStatusEnum {

    OFFLINE(0, "已下架"),
    ONLINE(1, "已上架");

    private final Integer statusCode;

    private final String statusName;

    ProductOnlineStatusEnum(Integer statusCode, String statusName) {
        this.statusCode = statusCode;
        this.statusName = statusName;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public static ProductOnlineStatusEnum of(Integer statusCode) {
        if(statusCode != null) {
            for(ProductOnlineStatusEnum em : values()) {
                if(em.getStatusCode().equals(statusCode)) {
                    return em;
                }
            }
        }
        return null;
    }
}
