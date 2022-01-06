package com.penglecode.xmodule.samples.product;

import com.penglecode.xmodule.samples.product.domain.model.ProductBaseInfo;
import com.penglecode.xmodule.samples.product.domain.model.ProductExtraInfo;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleSpec;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleStock;

import java.util.List;

public class TestProduct {

    private final ProductBaseInfo productBase;

    private final ProductExtraInfo productExtra;

    private final List<ProductSaleSpec> productSaleSpecs;

    private final List<ProductSaleStock> productSaleStocks;

    public TestProduct(ProductBaseInfo productBase, ProductExtraInfo productExtra, List<ProductSaleSpec> productSaleSpecs, List<ProductSaleStock> productSaleStocks) {
        this.productBase = productBase;
        this.productExtra = productExtra;
        this.productSaleSpecs = productSaleSpecs;
        this.productSaleStocks = productSaleStocks;
    }

    public ProductBaseInfo getProductBase() {
        return productBase;
    }

    public ProductExtraInfo getProductExtra() {
        return productExtra;
    }

    public List<ProductSaleSpec> getProductSaleSpecs() {
        return productSaleSpecs;
    }

    public List<ProductSaleStock> getProductSaleStocks() {
        return productSaleStocks;
    }
}