package com.penglecode.xmodule.samples.dal.mapper;

import com.penglecode.xmodule.common.mybatis.mapper.BaseCrudMapper;
import com.penglecode.xmodule.samples.domain.model.ProductStock;
import org.springframework.boot.autoconfigure.dal.NamedDatabase;

/**
 * 商品销售库存信息 Mybatis-Mapper接口
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月28日 下午 23:11
 */
@NamedDatabase("product")
public interface ProductStockMapper extends BaseCrudMapper<ProductStock> {

}