package com.penglecode.xmodule.samples.product.domain.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;

/**
 * 商品聚合根
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月21日 下午 23:18
 */
public class ProductAggregate extends ProductBaseInfo {

    private static final long serialVersionUID = 1L;

    /** 商品额外信息 */
    @NotNull(message="商品额外信息不能为空!")
    private ProductExtraInfo productExtraInfo;

    /** 商品销售规格信息 */
    @NotEmpty(message="商品销售规格信息不能为空!")
    private List<ProductSaleSpec> productSaleSpecs;

    /** 商品销售库存信息 */
    @NotEmpty(message="商品销售库存信息不能为空!")
    private List<ProductSaleStock> productSaleStocks;

    public ProductExtraInfo getProductExtraInfo() {
        return productExtraInfo;
    }

    public void setProductExtraInfo(ProductExtraInfo productExtraInfo) {
        this.productExtraInfo = productExtraInfo;
    }

    public List<ProductSaleSpec> getProductSaleSpecs() {
        return productSaleSpecs;
    }

    public void setProductSaleSpecs(List<ProductSaleSpec> productSaleSpecs) {
        this.productSaleSpecs = productSaleSpecs;
    }

    public List<ProductSaleStock> getProductSaleStocks() {
        return productSaleStocks;
    }

    public void setProductSaleStocks(List<ProductSaleStock> productSaleStocks) {
        this.productSaleStocks = productSaleStocks;
    }

}
