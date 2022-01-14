package com.penglecode.xmodule.samples.product.api.dto;

import com.penglecode.xmodule.common.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 商品销售库存信息出站DTO
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月21日 下午 23:18
 */
@Schema(description="商品销售库存信息出站DTO")
public class ProductSaleStockODTO implements BaseDTO {

    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @Schema(description="商品ID")
    private Long productId;

    /** 商品规格编号,多个t_product_spec.spec_no按顺序拼凑 */
    @Schema(description="商品规格编号")
    private String specNo;

    /** 商品规格编号,多个t_product_spec.spec_name按顺序拼凑 */
    @Schema(description="商品规格编号")
    private String specName;

    /** 商品售价(单位分) */
    @Schema(description="商品售价")
    private Long sellPrice;

    /** 库存量 */
    @Schema(description="库存量")
    private Integer stock;

    /** 创建时间 */
    @Schema(description="创建时间")
    private String createTime;

    /** 最近修改时间 */
    @Schema(description="最近修改时间")
    private String updateTime;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSpecNo() {
        return specNo;
    }

    public void setSpecNo(String specNo) {
        this.specNo = specNo;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Long getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Long sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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

}
