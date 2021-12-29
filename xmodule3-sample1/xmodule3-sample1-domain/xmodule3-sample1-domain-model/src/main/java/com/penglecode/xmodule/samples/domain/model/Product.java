package com.penglecode.xmodule.samples.domain.model;

import javax.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 商品Aggregate
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月21日 下午 23:18
 */
public class Product extends ProductInfo {

    private static final long serialVersionUID = 1L;

    /** 商品销售规格信息 */
    @NotEmpty(message="商品销售规格信息不能为空!")
    private List<ProductSpec> productSpecList;

    /** 商品销售库存信息 */
    @NotEmpty(message="商品销售库存信息不能为空!")
    private List<ProductStock> productStockList;

    public List<ProductSpec> getProductSpecList() {
        return productSpecList;
    }

    public void setProductSpecList(List<ProductSpec> productSpecList) {
        this.productSpecList = productSpecList;
    }

    public List<ProductStock> getProductStockList() {
        return productStockList;
    }

    public void setProductStockList(List<ProductStock> productStockList) {
        this.productStockList = productStockList;
    }

}
