package com.penglecode.xmodule.samples.product.dal.mapper;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.samples.product.domain.model.ProductBaseInfo;
import org.springframework.boot.autoconfigure.dal.NamedDatabase;

/**
 * 商品基础信息 Mybatis-Mapper接口
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月28日 下午 23:11
 */
@NamedDatabase("product")
public interface ProductBaseInfoMapper extends BaseMybatisMapper<ProductBaseInfo> {

}