package com.penglecode.xmodule.samples.product.domain.service.test;

import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.samples.boot.Sample1Application;
import com.penglecode.xmodule.samples.product.domain.service.ProductBaseInfoService;
import com.penglecode.xmodule.samples.product.domain.service.ProductSaleStockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/24 14:33
 */
@SpringBootTest(classes= Sample1Application.class)
public class ProductDomainServiceTest {

    @Autowired
    private ProductBaseInfoService productBaseInfoService;

    @Autowired
    private ProductSaleStockService productSaleStockService;

    @Test
    public void getProductBaseById() {
        System.out.println(productBaseInfoService.getProductBaseById(1L));
    }

    @Test
    public void forEachProductStock() {
        productSaleStockService.forEachProductSaleStock((item, index) -> {
            System.out.println("【" + index + "】>>> " + JsonUtils.object2Json(item));
        });
    }

}