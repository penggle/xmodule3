package com.penglecode.xmodule.samples.product.application.service.test;

import com.penglecode.xmodule.common.domain.Page;
import com.penglecode.xmodule.common.support.BeanMapper;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.samples.boot.Sample1Application;
import com.penglecode.xmodule.samples.product.ProductTestHelper;
import com.penglecode.xmodule.samples.product.TestProduct;
import com.penglecode.xmodule.samples.product.application.service.ProductAppService;
import com.penglecode.xmodule.samples.product.domain.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        TestProduct testProduct = ProductTestHelper.genTestProduct4Create();
        ProductBaseInfo productBase = testProduct.getProductBase();
        ProductExtraInfo productExtra = testProduct.getProductExtra();
        List<ProductSaleSpec> productSaleSpecs = testProduct.getProductSaleSpecs();
        List<ProductSaleStock> productSaleStocks = testProduct.getProductSaleStocks();

        ProductAggregate product = BeanMapper.map(productBase, ProductAggregate::new);
        product.setProductExtra(productExtra);
        product.setProductSaleSpecs(productSaleSpecs);
        product.setProductSaleStocks(productSaleStocks);
        productAppService.createProduct(product);
    }

    @Test
    public void createProductFromJD() throws Exception {
        TestProduct testProduct = ProductTestHelper.getTestProductFromJD(10026626397761L, 37900L);
        ProductBaseInfo productBase = testProduct.getProductBase();
        ProductExtraInfo productExtra = testProduct.getProductExtra();
        List<ProductSaleSpec> productSaleSpecs = testProduct.getProductSaleSpecs();
        List<ProductSaleStock> productSaleStocks = testProduct.getProductSaleStocks();

        ProductAggregate product = BeanMapper.map(productBase, ProductAggregate::new);
        product.setProductExtra(productExtra);
        product.setProductSaleSpecs(productSaleSpecs);
        product.setProductSaleStocks(productSaleStocks);
        productAppService.createProduct(product);
    }

    @Test
    public void modifyProductById() {
        TestProduct testProduct = ProductTestHelper.genTestProduct4Modify(1L);
        ProductBaseInfo productBase = testProduct.getProductBase();
        ProductExtraInfo productExtra = testProduct.getProductExtra();
        List<ProductSaleSpec> productSaleSpecs = testProduct.getProductSaleSpecs();
        List<ProductSaleStock> productSaleStocks = testProduct.getProductSaleStocks();

        ProductAggregate product = BeanMapper.map(productBase, ProductAggregate::new);
        product.setProductExtra(productExtra);
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
    public void getProductListByIds() {
        List<Long> ids = Arrays.asList(100004325476L, 100009077475L, 100011647215L, 100011662793L, 100012720924L);
        List<ProductAggregate> productList = productAppService.getProductsByIds(ids, true);
        if(!CollectionUtils.isEmpty(productList)) {
            for(ProductAggregate product : productList) {
                System.out.println(JsonUtils.object2Json(product));
            }
        }
    }

    @Test
    public void getProductListByPage() {
        ProductAggregate condition = new ProductAggregate();
        condition.setProductType(1);
        condition.setOnlineStatus(0);
        int totalCount = productAppService.getProductTotalCount();
        int pageSize = 10;
        int totalPageCount = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        for(int currentPage = 1; currentPage <= totalPageCount; currentPage++) {
            Page page = Page.of(currentPage, pageSize);
            List<ProductAggregate> productList = productAppService.getProductsByPage(condition, page, false);
            System.out.println("==========> page = " + page);
            if(!CollectionUtils.isEmpty(productList)) {
                for(ProductAggregate product : productList) {
                    System.out.println(JsonUtils.object2Json(product));
                }
            }
            if(Objects.equals(page.getPageIndex(), page.getTotalPageCount())) {
                break;
            }
        }

    }

    @Test
    public void removeProductById() {
        productAppService.removeProductById(100018640796L);
    }

    @Test
    public void forEachProduct() {
        productAppService.forEachProduct((item, index) -> {
            System.out.printf("【%s】==> %s%n", index, JsonUtils.object2Json(item));
        });
    }

}
