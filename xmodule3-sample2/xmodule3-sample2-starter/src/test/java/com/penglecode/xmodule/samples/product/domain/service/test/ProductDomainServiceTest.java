package com.penglecode.xmodule.samples.product.domain.service.test;

import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.samples.boot.SamplesApplication;
import com.penglecode.xmodule.samples.domain.service.ProductInfoService;
import com.penglecode.xmodule.samples.domain.service.ProductStockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/24 14:33
 */
@SpringBootTest(classes=SamplesApplication.class)
public class ProductDomainServiceTest {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductStockService productStockService;

    @Test
    public void getProductById() {
        System.out.println(productInfoService.getProductById(1L));
    }

    @Test
    public void forEachProductStock() {
        productStockService.forEachProductStock((item, index) -> {
            System.out.println("【" + index + "】>>> " + JsonUtils.object2Json(item));
        });
    }

}