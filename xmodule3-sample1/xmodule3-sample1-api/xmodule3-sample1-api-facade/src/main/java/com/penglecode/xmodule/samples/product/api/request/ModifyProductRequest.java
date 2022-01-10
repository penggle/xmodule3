package com.penglecode.xmodule.samples.product.api.request;

import com.penglecode.xmodule.common.dto.BaseDTO;
import com.penglecode.xmodule.samples.product.api.dto.ProductBaseInfoIDTO;
import com.penglecode.xmodule.samples.product.api.dto.ProductExtraInfoIDTO;
import com.penglecode.xmodule.samples.product.api.dto.ProductSaleSpecIDTO;
import com.penglecode.xmodule.samples.product.api.dto.ProductSaleStockIDTO;

import java.util.List;

/**
 * 修改商品请求DTO
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021年10月21日 下午 23:18
 */
public class ModifyProductRequest extends ProductBaseInfoIDTO implements BaseDTO {

    private static final long serialVersionUID = 1L;

    /** 商品额外信息 */
    private ProductExtraInfoIDTO productExtra;

    /** 商品销售规格信息 */
    private List<ProductSaleSpecIDTO> productSaleSpecs;

    /** 商品销售库存信息 */
    private List<ProductSaleStockIDTO> productSaleStocks;

    public ProductExtraInfoIDTO getProductExtra() {
        return productExtra;
    }

    public void setProductExtra(ProductExtraInfoIDTO productExtra) {
        this.productExtra = productExtra;
    }

    public List<ProductSaleSpecIDTO> getProductSaleSpecs() {
        return productSaleSpecs;
    }

    public void setProductSaleSpecs(List<ProductSaleSpecIDTO> productSaleSpecs) {
        this.productSaleSpecs = productSaleSpecs;
    }

    public List<ProductSaleStockIDTO> getProductSaleStocks() {
        return productSaleStocks;
    }

    public void setProductSaleStocks(List<ProductSaleStockIDTO> productSaleStocks) {
        this.productSaleStocks = productSaleStocks;
    }
    
}
