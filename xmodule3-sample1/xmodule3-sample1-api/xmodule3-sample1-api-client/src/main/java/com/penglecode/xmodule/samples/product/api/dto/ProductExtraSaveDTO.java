package com.penglecode.xmodule.samples.product.api.dto;

import com.penglecode.xmodule.common.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 商品额外信息保存(入站)DTO
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @created 2021年10月21日 下午 23:18
 */
@Schema(description="商品额外信息保存(入站)DTO")
public class ProductExtraSaveDTO implements BaseDTO {

    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @Schema(description="商品ID(修改时必填)")
    private Long productId;

    /** 商品详情(HTML片段) */
    @Schema(description="商品详情(HTML片段)")
    private String productDetails;

    /** 商品规则参数(HTML片段) */
    @Schema(description="商品规则参数(HTML片段)")
    private String productSpecifications;

    /** 商品服务(HTML片段) */
    @Schema(description="商品服务(HTML片段)")
    private String productServices;

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

}
