package com.penglecode.xmodule.samples.product.api.request;

import com.penglecode.xmodule.common.dto.PageDTO;

/**
 * 查询商品请求DTO
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021年10月21日 下午 23:18
 */
public class QueryProductRequest extends PageDTO {

    /** 商品名称 */
    private String productName;

    /** 商品类型：0-虚拟商品,1-实物商品 */
    private Integer productType;

    /** 商品审核状态：0-待审核,1-审核通过,2-审核不通过 */
    private Integer auditStatus;

    /** 上下架状态：0-已下架,1-已上架 */
    private Integer onlineStatus;

    /** 所属店铺ID */
    private Long shopId;

    /** createTime的范围查询条件辅助字段 */
    private String startCreateTime;

    /** createTime的范围查询条件辅助字段 */
    private String endCreateTime;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(String startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

}