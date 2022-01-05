package com.penglecode.xmodule.samples.product.app.service.test;

import com.penglecode.xmodule.common.support.BeanMapper;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.samples.boot.Sample1Application;
import com.penglecode.xmodule.samples.product.ProductTestHelper;
import com.penglecode.xmodule.samples.product.app.service.ProductAppService;
import com.penglecode.xmodule.samples.product.domain.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2022/1/4 15:39
 */
@SuppressWarnings("unchecked")
@SpringBootTest(classes=Sample1Application.class)
public class ProductAppServiceTest {

    @Autowired
    private ProductAppService productAppService;

    @Test
    public void createProduct() {
        Object[] productTests = ProductTestHelper.genProductTests4Create();
        ProductBaseInfo productBase = (ProductBaseInfo) productTests[0];
        ProductExtraInfo productExtra = (ProductExtraInfo) productTests[1];
        List<ProductSaleSpec> productSaleSpecs = (List<ProductSaleSpec>) productTests[2];
        List<ProductSaleStock> productSaleStocks = (List<ProductSaleStock>) productTests[3];

        ProductAggregate product = BeanMapper.map(productBase, ProductAggregate::new);
        product.setProductExtraInfo(productExtra);
        product.setProductSaleSpecs(productSaleSpecs);
        product.setProductSaleStocks(productSaleStocks);
        productAppService.createProduct(product);
    }

    @Test
    public void modifyProductById() {
        Object[] productTests = ProductTestHelper.genProductTests4Modify(1L);
        ProductBaseInfo productBase = (ProductBaseInfo) productTests[0];
        ProductExtraInfo productExtra = (ProductExtraInfo) productTests[1];
        List<ProductSaleSpec> productSaleSpecs = (List<ProductSaleSpec>) productTests[2];
        List<ProductSaleStock> productSaleStocks = (List<ProductSaleStock>) productTests[3];

        ProductAggregate product = BeanMapper.map(productBase, ProductAggregate::new);
        product.setProductExtraInfo(productExtra);
        product.setProductSaleSpecs(productSaleSpecs);
        product.setProductSaleStocks(productSaleStocks);
        productAppService.modifyProductById(product);
    }

    @Test
    public void getProductById() {
        ProductAggregate product1 = productAppService.getProductById(1L, false);
        System.out.println(JsonUtils.object2Json(product1));
        System.out.println("----------------------------------------------");
        ProductAggregate product2 = productAppService.getProductById(1L, true);
        System.out.println(JsonUtils.object2Json(product2));
    }

    @Test
    public void removeProductById() {
        productAppService.removeProductById(1L);
    }

}
