package com.penglecode.xmodule.samples.product.domain.model;

import com.penglecode.xmodule.common.domain.EntityObject;
import com.penglecode.xmodule.common.domain.ID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 商品销售规格信息实体
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @created 2021年10月21日 下午 23:18
 */
public class ProductSaleSpec implements EntityObject {

    private static final long serialVersionUID = 1L;

    /** 商品ID */
    @NotNull(message="商品ID不能为空!")
    private Long productId;

    /** 商品规格编号,两位数字组成 */
    @NotBlank(message="规格编号不能为空!")
    private String specNo;

    /** 商品规格名称 */
    @NotBlank(message="规格名称不能为空!")
    private String specName;

    /** 商品规格顺序 */
    @NotNull(message="规格顺序不能为空!")
    private Integer specIndex;

    /** 商品规格备注 */
    private String remark;

    /** 创建时间 */
    @NotBlank(message="创建时间不能为空!")
    private String createTime;

    /** 最近修改时间 */
    @NotBlank(message="最近更新时间不能为空!")
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

    @Override
    public ID identity() {
        return new ID().addKey("productId", productId).addKey("specNo", specNo);
    }

}
