package com.penglecode.xmodule.samples.product.dal.mapper.test;

import com.penglecode.xmodule.common.domain.ID;
import com.penglecode.xmodule.common.domain.OrderBy;
import com.penglecode.xmodule.common.mybatis.dsl.LambdaQueryCriteria;
import com.penglecode.xmodule.common.mybatis.dsl.QueryColumns;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import com.penglecode.xmodule.common.mybatis.executor.JdbcBatchOperation;
import com.penglecode.xmodule.common.support.MapLambdaBuilder;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.samples.boot.Sample1Application;
import com.penglecode.xmodule.samples.product.ProductTestHelper;
import com.penglecode.xmodule.samples.product.TestProduct;
import com.penglecode.xmodule.samples.product.dal.mapper.ProductBaseInfoMapper;
import com.penglecode.xmodule.samples.product.dal.mapper.ProductExtraInfoMapper;
import com.penglecode.xmodule.samples.product.dal.mapper.ProductSaleSpecMapper;
import com.penglecode.xmodule.samples.product.dal.mapper.ProductSaleStockMapper;
import com.penglecode.xmodule.samples.product.domain.enums.ProductAuditStatusEnum;
import com.penglecode.xmodule.samples.product.domain.enums.ProductOnlineStatusEnum;
import com.penglecode.xmodule.samples.product.domain.model.ProductBaseInfo;
import com.penglecode.xmodule.samples.product.domain.model.ProductExtraInfo;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleSpec;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleStock;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/24 14:33
 */
@SuppressWarnings("unchecked")
@SpringBootTest(classes=Sample1Application.class)
public class ProductModuleMapperTest {

    @Autowired
    private ProductBaseInfoMapper productBaseInfoMapper;

    @Autowired
    private ProductExtraInfoMapper productExtraInfoMapper;

    @Autowired
    private ProductSaleSpecMapper productSaleSpecMapper;

    @Autowired
    private ProductSaleStockMapper productSaleStockMapper;

    @Resource(name="productTransactionManager")
    private DataSourceTransactionManager productTransactionManager;

    @Test
    public void createProduct() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(productTransactionManager);
        transactionTemplate.execute(this::doCreateProduct);
    }

    protected Object doCreateProduct(TransactionStatus status) {
        TestProduct testProduct = ProductTestHelper.genTestProduct4Create();
        ProductBaseInfo productBase = testProduct.getProductBase();
        //System.out.println(JsonUtils.object2Json(productBase));
        productBaseInfoMapper.insertModel(productBase);

        ProductExtraInfo productExtra = testProduct.getProductExtra();
        productExtra.setProductId(productBase.getProductId());
        productExtraInfoMapper.insertModel(productExtra);

        List<ProductSaleSpec> productSaleSpecs = testProduct.getProductSaleSpecs();
        try(JdbcBatchOperation batchOperation = new JdbcBatchOperation()) {
            for(ProductSaleSpec productSaleSpec : productSaleSpecs) {
                productSaleSpec.setProductId(productBase.getProductId());
                productSaleSpecMapper.insertModel(productSaleSpec);
            }
            productSaleSpecMapper.flushStatements(); //批量提交
        }

        List<ProductSaleStock> productSaleStocks = testProduct.getProductSaleStocks();
        try(JdbcBatchOperation batchOperation = new JdbcBatchOperation()) {
            for(ProductSaleStock productSaleStock : productSaleStocks) {
                productSaleStock.setProductId(productBase.getProductId());
                productSaleStockMapper.insertModel(productSaleStock);
            }
            productSaleStockMapper.flushStatements(); //批量提交
        }
        return null;
    }

    @Test
    public void updateProduct() {
        Long productId = 1L;
        ProductBaseInfo productBase = productBaseInfoMapper.selectModelById(productId);
        System.out.println(JsonUtils.object2Json(productBase));

        QueryCriteria<ProductSaleStock> criteria = LambdaQueryCriteria.ofEmpty(ProductSaleStock::new)
                .eq(ProductSaleStock::getProductId, productId)
                .likeRight(ProductSaleStock::getSpecNo, "00");
        List<ProductSaleStock> productStocks = productSaleStockMapper.selectModelListByCriteria(criteria);

        if(!CollectionUtils.isEmpty(productStocks)) {
            productStocks.forEach(stock -> System.out.println(JsonUtils.object2Json(stock)));
        }

        String nowTime = DateTimeUtils.formatNow();
        productBase.setAuditStatus(ProductAuditStatusEnum.AUDIT_PASSED.getStatusCode());
        productBase.setOnlineStatus(ProductOnlineStatusEnum.ONLINE.getStatusCode());
        productBase.setUpdateTime(nowTime);
        Map<String,Object> paramMap1 = MapLambdaBuilder.of(productBase)
                .with(ProductBaseInfo::getAuditStatus)
                .with(ProductBaseInfo::getOnlineStatus)
                .with(ProductBaseInfo::getUpdateTime)
                .build();
        productBaseInfoMapper.updateModelById(productBase.getProductId(), paramMap1);

        ProductExtraInfo productExtra = productExtraInfoMapper.selectModelById(productId);
        productExtra.setProductServices("商品服务111");
        productExtra.setUpdateTime(nowTime);
        Map<String,Object> paramMap2 = MapLambdaBuilder.of(productExtra)
                .with(ProductExtraInfo::getProductServices)
                .with(ProductExtraInfo::getUpdateTime)
                .build();
        productExtraInfoMapper.updateModelById(productExtra.getProductId(), paramMap2);

        Map<String,Object> paramMap3 = MapLambdaBuilder.of(new ProductSaleStock())
                .with(ProductSaleStock::getSellPrice, 599700)
                .with(ProductSaleStock::getUpdateTime, nowTime)
                .build();
        productSaleStockMapper.updateModelByCriteria(criteria, paramMap3);
    }

    @Test
    public void queryProduct() {
        Long productId = 1L;
        List<ProductBaseInfo> productBases = productBaseInfoMapper.selectModelListByIds(Arrays.asList(5L, 6L, 7L, 8L, 9L),
                new QueryColumns(ProductBaseInfo::getProductId, ProductBaseInfo::getProductName, ProductBaseInfo::getProductType));
        if(!CollectionUtils.isEmpty(productBases)) {
            productBases.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }

        System.out.println("-----------------------------------------");

        QueryCriteria<ProductBaseInfo> criteria1 = LambdaQueryCriteria.ofEmpty(ProductBaseInfo::new)
                .eq(ProductBaseInfo::getProductType, 1)
                .orderBy(OrderBy.desc(ProductBaseInfo::getCreateTime))
                .limit(10);
        productBases = productBaseInfoMapper.selectModelListByCriteria(criteria1);
        if(!CollectionUtils.isEmpty(productBases)) {
            productBases.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }

        System.out.println("-----------------------------------------");

        QueryCriteria<ProductSaleStock> criteria2 = LambdaQueryCriteria.ofEmpty(ProductSaleStock::new)
                .eq(ProductSaleStock::getProductId, productId)
                .likeRight(ProductSaleStock::getSpecNo, "00")
                .orderBy(OrderBy.desc(ProductSaleStock::getSellPrice));
        List<ProductSaleStock> productStocks = productSaleStockMapper.selectModelListByCriteria(criteria2, new QueryColumns(ProductSaleStock::getProductId, ProductSaleStock::getSpecNo, ProductSaleStock::getSellPrice, ProductSaleStock::getStock));
        if(!CollectionUtils.isEmpty(productStocks)) {
            productStocks.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }

        System.out.println("-----------------------------------------");

        List<ID> idList = new ArrayList<>();
        idList.add(new ID().addKey("productId", productId).addKey("specNo", "00:10:20"));
        idList.add(new ID().addKey("productId", productId).addKey("specNo", "00:10:21"));
        productStocks = productSaleStockMapper.selectModelListByIds(idList, new QueryColumns(column -> true));
        if(!CollectionUtils.isEmpty(productStocks)) {
            productStocks.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }

        System.out.println("-----------------------------------------");
        ProductSaleStock productStock = productSaleStockMapper.selectModelById(new ID().addKey("productId", productId).addKey("specNo", "01:10:20"));
        System.out.println(JsonUtils.object2Json(productStock));
    }

    @Test
    public void queryProductByPage() {
        Long productId = 1L;
        QueryCriteria<ProductSaleStock> criteria = LambdaQueryCriteria.ofEmpty(ProductSaleStock::new)
                .eq(ProductSaleStock::getProductId, productId)
                .orderBy(OrderBy.desc(ProductSaleStock::getSellPrice))
                .limit(5); //limit与在分页查询(selectModelPageListByCriteria)时会失效
        System.out.println("totalCount = " + productSaleStockMapper.selectModelPageCountByCriteria(criteria));
        System.out.println("--------------------------第1页------------------------");
        List<ProductSaleStock> productStocks1 = productSaleStockMapper.selectModelPageListByCriteria(criteria, new RowBounds(0, 10));
        if(!CollectionUtils.isEmpty(productStocks1)) {
            productStocks1.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }
        System.out.println("--------------------------第2页------------------------");
        List<ProductSaleStock> productStocks2 = productSaleStockMapper.selectModelPageListByCriteria(criteria, new RowBounds(10, 10), new QueryColumns(ProductSaleStock::getProductId, ProductSaleStock::getSpecNo, ProductSaleStock::getSellPrice, ProductSaleStock::getStock));
        if(!CollectionUtils.isEmpty(productStocks2)) {
            productStocks2.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }
    }

    @Test
    public void deleteProduct() {
        Long productId = 1L;
        productSaleStockMapper.deleteModelById(new ID().addKey("productId", productId).addKey("specNo", "00:10:20"));

        List<ID> idList = new ArrayList<>();
        idList.add(new ID().addKey("productId", productId).addKey("specNo", "00:11:20"));
        idList.add(new ID().addKey("productId", productId).addKey("specNo", "00:11:21"));
        productSaleStockMapper.deleteModelByIds(idList);

        QueryCriteria<ProductSaleStock> criteria = LambdaQueryCriteria.ofEmpty(ProductSaleStock::new)
                .eq(ProductSaleStock::getProductId, productId)
                .likeRight(ProductSaleStock::getSpecNo, "00");
        productSaleStockMapper.deleteModelByCriteria(criteria);

        productBaseInfoMapper.deleteModelByIds(Arrays.asList(1L, 2L, 3L, 4L, 5L));
    }

}