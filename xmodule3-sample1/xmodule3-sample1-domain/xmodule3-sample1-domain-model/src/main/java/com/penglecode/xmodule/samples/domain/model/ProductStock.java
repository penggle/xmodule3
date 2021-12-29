package com.penglecode.xmodule.samples.domain.model;

import com.penglecode.xmodule.common.domain.EntityObject;
import com.penglecode.xmodule.common.domain.ID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 商品销售库存信息Domain-Object
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月21日 下午 23:18
 */
public class ProductStock implements EntityObject {

    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @NotNull(message="商品ID不能为空!")
    private Long productId;

    /** 商品规格编号,多个t_product_spec.spec_no按顺序拼凑 */
    @NotBlank(message="规格编号不能为空!")
    private String specNo;

    /** 商品规格编号,多个t_product_spec.spec_name按顺序拼凑 */
    @NotBlank(message="规格名称不能为空!")
    private String specName;

    /** 商品售价(单位分) */
    @NotNull(message="售价不能为空!")
    private Long sellPrice;

    /** 库存量 */
    @NotNull(message="库存不能为空!")
    private Integer stock;

    /** 创建时间 */
    @NotBlank(message="创建时间不能为空!")
    private String createTime;

    /** 最近修改时间 */
    @NotBlank(message="最近更新时间不能为空!")
    private String updateTime;

    //以下属于辅助字段

    /** sellPrice的范围查询条件辅助字段 */
    private Long minSellPrice;

    /** sellPrice的范围查询条件辅助字段 */
    private Long maxSellPrice;

    /** stock的范围查询条件辅助字段 */
    private Integer minStock;

    /** stock的范围查询条件辅助字段 */
    private Integer maxStock;

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

    public Long getMinSellPrice() {
        return minSellPrice;
    }

    public void setMinSellPrice(Long minSellPrice) {
        this.minSellPrice = minSellPrice;
    }

    public Long getMaxSellPrice() {
        return maxSellPrice;
    }

    public void setMaxSellPrice(Long maxSellPrice) {
        this.maxSellPrice = maxSellPrice;
    }

    public Integer getMinStock() {
        return minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }

    public Integer getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(Integer maxStock) {
        this.maxStock = maxStock;
    }

    @Override
    public ID identity() {
        return new ID().addKey("productId", productId).addKey("specNo", specNo);
    }

}
