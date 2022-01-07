package com.penglecode.xmodule.samples.product.api.service;

import com.penglecode.xmodule.common.dto.Result;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 商品API接口服务
 *
 * @author pengpeng
 * @version 1.0
 * @since 2022/1/7 16:15
 */
@RequestMapping("/api/product")
public interface ProductApiService {

    Result<Long> createProduct();

}
