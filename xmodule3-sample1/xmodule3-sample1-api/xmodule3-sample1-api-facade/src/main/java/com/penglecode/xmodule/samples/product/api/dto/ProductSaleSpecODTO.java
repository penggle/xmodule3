package com.penglecode.xmodule.samples.product.api.dto;

import com.penglecode.xmodule.common.dto.BaseDTO;

/**
 * 商品销售规格信息入站DTO
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月21日 下午 23:18
 */
public class ProductSaleSpecODTO implements BaseDTO {

    private static final long serialVersionUID = 1L;

    /** 商品ID */
    private Long productId;

    /** 商品规格编号,两位数字组成 */
    private String specNo;

    /** 商品规格名称 */
    private String specName;

    /** 商品规格顺序 */
    private Integer specIndex;

    /** 商品规格备注 */
    private String remark;

    /** 创建时间 */
    private String createTime;

    /** 最近修改时间 */
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
