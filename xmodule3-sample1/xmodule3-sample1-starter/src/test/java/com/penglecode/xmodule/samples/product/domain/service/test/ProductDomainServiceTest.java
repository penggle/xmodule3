package com.penglecode.xmodule.samples.product.domain.service.test;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.samples.boot.Sample1Application;
import com.penglecode.xmodule.samples.product.ProductTestHelper;
import com.penglecode.xmodule.samples.product.domain.model.ProductBaseInfo;
import com.penglecode.xmodule.samples.product.domain.model.ProductExtraInfo;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleSpec;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleStock;
import com.penglecode.xmodule.samples.product.domain.service.ProductBaseInfoService;
import com.penglecode.xmodule.samples.product.domain.service.ProductExtraInfoService;
import com.penglecode.xmodule.samples.product.domain.service.ProductSaleSpecService;
import com.penglecode.xmodule.samples.product.domain.service.ProductSaleStockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/24 14:33
 */
@SuppressWarnings("unchecked")
@SpringBootTest(classes= Sample1Application.class)
public class ProductDomainServiceTest {

    @Autowired
    private ProductBaseInfoService productBaseInfoService;

    @Autowired
    private ProductExtraInfoService productExtraInfoService;

    @Autowired
    private ProductSaleSpecService productSaleSpecService;

    @Autowired
    private ProductSaleStockService productSaleStockService;

    @Resource(name="productTransactionManager")
    private DataSourceTransactionManager productTransactionManager;

    @Test
    public void createProduct() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(productTransactionManager);
        transactionTemplate.execute(this::doCreateProduct);
    }

    protected Object doCreateProduct(TransactionStatus status) {
        Object[] productTests = ProductTestHelper.genProductTests4Create();
        ProductBaseInfo productBase = (ProductBaseInfo) productTests[0];
        //System.out.println(JsonUtils.object2Json(productBase));
        productBaseInfoService.createProductBase(productBase);

        ProductExtraInfo productExtra = (ProductExtraInfo) productTests[1];
        productExtra.setProductId(productBase.getProductId());
        productExtraInfoService.createProductExtra(productExtra);

        List<ProductSaleSpec> productSaleSpecs = (List<ProductSaleSpec>) productTests[2];
        List<ProductSaleStock> productSaleStocks = (List<ProductSaleStock>) productTests[3];
        productSaleSpecService.batchCreateProductSaleSpec(productSaleSpecs);
        productSaleStockService.batchCreateProductSaleStock(productSaleStocks);
        return null;
    }

    @Test
    public void getProductBaseById() {
        ProductBaseInfo productBase = productBaseInfoService.getProductBaseById(1L);
        System.out.println(JsonUtils.object2Json(productBase));
    }

    @Test
    public void getProductSaleSpecsByProductId() {
        List<ProductSaleSpec> productSaleSpecs = productSaleSpecService.getProductSaleSpecsByProductId(1L);
        if(!CollectionUtils.isEmpty(productSaleSpecs)) {
            for(ProductSaleSpec productSaleSpec : productSaleSpecs) {
                System.out.println(JsonUtils.object2Json(productSaleSpec));
            }
        }
    }

    @Test
    public void forEachProductSaleStock() {
        productSaleStockService.forEachProductSaleStock((item, index) -> {
            System.out.println("【" + index + "】>>> " + JsonUtils.object2Json(item));
        });
    }

}