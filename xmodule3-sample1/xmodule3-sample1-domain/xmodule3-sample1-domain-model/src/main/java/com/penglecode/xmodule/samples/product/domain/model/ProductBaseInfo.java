package com.penglecode.xmodule.samples.product.domain.model;

import com.penglecode.xmodule.common.domain.EntityObject;
import com.penglecode.xmodule.samples.product.domain.enums.ProductAuditStatusEnum;
import com.penglecode.xmodule.samples.product.domain.enums.ProductOnlineStatusEnum;
import com.penglecode.xmodule.samples.product.domain.enums.ProductTypeEnum;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Optional;

/**
 * 商品基础信息实体
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @created 2021年10月21日 下午 23:18
 */
public class ProductBaseInfo implements EntityObject {

    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @NotNull(message="商品ID不能为空!")
    private Long productId;

    /** 商品名称 */
    @NotBlank(message="商品名称不能为空!")
    private String productName;

    /** 商品URL */
    @NotBlank(message="商品详情URL不能为空!")
    private String productUrl;

    /** 商品标签 */
    @NotBlank(message="商品标签不能为空!")
    private String productTags;

    /** 商品类型：0-虚拟商品,1-实物商品 */
    @NotNull(message="商品类型不能为空!")
    private Integer productType;

    /** 审核状态：0-待审核,1-审核通过,2-审核不通过 */
    @NotNull(message="审核状态不能为空!")
    private Integer auditStatus;

    /** 上下架状态：0-已下架,1-已上架 */
    @NotNull(message="上下架状态不能为空!")
    private Integer onlineStatus;

    /** 所属店铺ID */
    @NotNull(message="所属店铺ID不能为空!")
    private Long shopId;

    /** 商品备注 */
    private String remark;

    /** 创建时间 */
    @NotBlank(message="创建时间不能为空!")
    private String createTime;

    /** 最近修改时间 */
    @NotBlank(message="最近更新时间不能为空!")
    private String updateTime;

    //以下属于辅助字段

    /** productType的查询结果辅助字段 */
    private String productTypeName;

    /** auditStatus的IN查询条件辅助字段 */
    private Integer[] auditStatuses;

    /** auditStatus的查询结果辅助字段 */
    private String auditStatusName;

    /** onlineStatus的查询结果辅助字段 */
    private String onlineStatusName;

    /** createTime的范围查询条件辅助字段 */
    private String startCreateTime;

    /** createTime的范围查询条件辅助字段 */
    private String endCreateTime;

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

    public Integer[] getAuditStatuses() {
        return auditStatuses;
    }

    public void setAuditStatuses(Integer[] auditStatuses) {
        this.auditStatuses = auditStatuses;
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

    @Override
    public Long identity() {
        return productId;
    }

    @Override
    public ProductBaseInfo beforeOutbound() {
        Optional.ofNullable(ProductTypeEnum.of(productType)).ifPresent(em -> setProductTypeName(em.getTypeName()));
        Optional.ofNullable(ProductAuditStatusEnum.of(auditStatus)).ifPresent(em -> setAuditStatusName(em.getStatusName()));
        Optional.ofNullable(ProductOnlineStatusEnum.of(onlineStatus)).ifPresent(em -> setOnlineStatusName(em.getStatusName()));
        return this;
    }
}
