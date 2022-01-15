package com.penglecode.xmodule.samples.product.api.request;

import com.penglecode.xmodule.common.model.BaseDTO;
import com.penglecode.xmodule.samples.product.api.dto.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 查询商品响应DTO
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021年10月21日 下午 23:18
 */
@Schema(description="查询商品响应DTO")
public class QueryProductResponse extends ProductBaseInfoODTO implements BaseDTO {

    private static final long serialVersionUID = 1L;

    /** 商品额外信息 */
    @Schema(description="商品额外信息")
    private ProductExtraInfoODTO productExtra;

    /** 商品销售规格信息列表 */
    @Schema(description="商品销售规格信息列表")
    private List<ProductSaleSpecODTO> productSaleSpecs;

    /** 商品销售库存信息列表 */
    @Schema(description="商品销售库存信息列表")
    private List<ProductSaleStockODTO> productSaleStocks;

    public ProductExtraInfoODTO getProductExtra() {
        return productExtra;
    }

    public void setProductExtra(ProductExtraInfoODTO productExtra) {
        this.productExtra = productExtra;
    }

    public List<ProductSaleSpecODTO> getProductSaleSpecs() {
        return productSaleSpecs;
    }

    public void setProductSaleSpecs(List<ProductSaleSpecODTO> productSaleSpecs) {
        this.productSaleSpecs = productSaleSpecs;
    }

    public List<ProductSaleStockODTO> getProductSaleStocks() {
        return productSaleStocks;
    }

    public void setProductSaleStocks(List<ProductSaleStockODTO> productSaleStocks) {
        this.productSaleStocks = productSaleStocks;
    }

}
