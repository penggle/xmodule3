package com.penglecode.xmodule.samples.product.api.dto;

import com.penglecode.xmodule.common.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 商品基础信息入站DTO
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月21日 下午 23:18
 */
@Schema(description="商品基础信息入站DTO")
public class ProductBaseInfoIDTO implements BaseDTO {

    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @Schema(description="商品ID(修改时必填)")
    private Long productId;

    /** 商品名称 */
    @Schema(description="商品名称")
    private String productName;

    /** 商品URL */
    @Schema(description="商品URL")
    private String productUrl;

    /** 商品标签 */
    @Schema(description="商品标签")
    private String productTags;

    /** 商品类型：0-虚拟商品,1-实物商品 */
    @Schema(description="商品类型(0-虚拟,1-实物)")
    private Integer productType;

    /** 审核状态：0-待审核,1-审核通过,2-审核不通过 */
    @Schema(description="审核状态(0-待审核,1-审核通过,2-审核不通过)")
    private Integer auditStatus;

    /** 上下架状态：0-已下架,1-已上架 */
    @Schema(description="上下架状态(0-已下架,1-已上架)")
    private Integer onlineStatus;

    /** 所属店铺ID */
    @Schema(description="所属店铺ID")
    private Long shopId;

    /** 商品备注 */
    @Schema(description="商品备注")
    private String remark;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductTags() {
        return productTags;
    }

    public void setProductTags(String productTags) {
        this.productTags = productTags;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
