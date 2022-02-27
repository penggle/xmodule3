package com.penglecode.xmodule.samples.product.api.dto;

import com.penglecode.xmodule.common.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 商品销售规格信息保存(入站)DTO
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @created 2021年10月21日 下午 23:18
 */
@Schema(description="商品销售规格信息保存(入站)DTO")
public class ProductSaleSpecSaveDTO implements BaseDTO {

    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @Schema(description="商品ID")
    private Long productId;

    /** 商品规格编号,两位数字组成 */
    @Schema(description="商品规格编号")
    private String specNo;

    /** 商品规格名称 */
    @Schema(description="商品规格名称")
    private String specName;

    /** 商品规格顺序 */
    @Schema(description="商品规格顺序")
    private Integer specIndex;

    /** 商品规格备注 */
    @Schema(description="商品规格备注")
    private String remark;

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

    public Integer getSpecIndex() {
        return specIndex;
    }

    public void setSpecIndex(Integer specIndex) {
        this.specIndex = specIndex;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}