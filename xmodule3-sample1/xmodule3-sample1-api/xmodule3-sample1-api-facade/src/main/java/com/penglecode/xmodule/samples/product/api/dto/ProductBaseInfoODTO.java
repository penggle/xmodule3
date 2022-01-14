package com.penglecode.xmodule.samples.product.api.dto;

import com.penglecode.xmodule.common.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 商品基础信息出站DTO
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月21日 下午 23:18
 */
@Schema(description="商品基础信息出站DTO")
public class ProductBaseInfoODTO implements BaseDTO {

    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @Schema(description="商品ID")
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
    @Schema(description="商品类型")
    private Integer productType;

    /** 审核状态：0-待审核,1-审核通过,2-审核不通过 */
    @Schema(description="审核状态")
    private Integer auditStatus;

    /** 上下架状态：0-已下架,1-已上架 */
    @Schema(description="上下架状态")
    private int onlineStatus;

    /** 所属店铺ID */
    @Schema(description="所属店铺ID")
    private Long shopId;

    /** 商品备注 */
    @Schema(description="商品备注")
    private CharSequence remark;

    /** 创建时间 */
    @Schema(description="创建时间")
    private String createTime;

    /** 最近修改时间 */
    @Schema(description="最近修改时间")
    private String updateTime;

    /** productType的查询结果辅助字段 */
    @Schema(description="商品类型名称")
    private String productTypeName;

    /** auditStatus的查询结果辅助字段 */
    @Schema(description="审核状态名称")
    private String auditStatusName;

    /** onlineStatus的查询结果辅助字段 */
    @Schema(description="上下架状态名称")
    private String onlineStatusName;

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

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public CharSequence getRemark() {
        return remark;
    }

    public void setRemark(CharSequence remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getAuditStatusName() {
        return auditStatusName;
    }

    public void setAuditStatusName(String auditStatusName) {
        this.auditStatusName = auditStatusName;
    }

    public String getOnlineStatusName() {
        return onlineStatusName;
    }

    public void setOnlineStatusName(String onlineStatusName) {
        this.onlineStatusName = onlineStatusName;
    }

}
