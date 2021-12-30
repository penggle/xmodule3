package com.penglecode.xmodule.samples.product.domain.model;

import com.penglecode.xmodule.common.domain.EntityObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 商品额外信息实体
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月21日 下午 23:18
 */
public class ProductExtraInfo implements EntityObject {

    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @NotNull(message="商品ID不能为空!")
    private Long productId;

    /** 商品详情(HTML片段) */
    @NotBlank(message="商品详情不能为空!")
    private String productDetails;

    /** 商品规则参数(HTML片段) */
    private String productSpecifications;

    /** 商品服务(HTML片段) */
    private String productServices;

    /** 创建时间 */
    @NotBlank(message="创建时间不能为空!")
    private String createTime;

    /** 最近修改时间 */
    @NotBlank(message="最近更新时间不能为空!")
    private String updateTime;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getProductSpecifications() {
        return productSpecifications;
    }

    public void setProductSpecifications(String productSpecifications) {
        this.productSpecifications = productSpecifications;
    }

    public String getProductServices() {
        return productServices;
    }

    public void setProductServices(String productServices) {
        this.productServices = productServices;
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

    @Override
    public Long identity() {
        return productId;
    }

}
