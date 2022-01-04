package com.penglecode.xmodule.samples.product.dal.mapper.test;

import com.google.common.collect.Lists;
import com.penglecode.xmodule.common.domain.ID;
import com.penglecode.xmodule.common.domain.Order;
import com.penglecode.xmodule.common.mybatis.dsl.LambdaQueryCriteria;
import com.penglecode.xmodule.common.mybatis.dsl.QueryColumns;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import com.penglecode.xmodule.common.mybatis.executor.JdbcBatchOperation;
import com.penglecode.xmodule.common.support.MapLambdaBuilder;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.samples.boot.Sample1Application;
import com.penglecode.xmodule.samples.product.dal.mapper.ProductBaseInfoMapper;
import com.penglecode.xmodule.samples.product.dal.mapper.ProductExtraInfoMapper;
import com.penglecode.xmodule.samples.product.dal.mapper.ProductSaleSpecMapper;
import com.penglecode.xmodule.samples.product.dal.mapper.ProductSaleStockMapper;
import com.penglecode.xmodule.samples.product.domain.enums.ProductAuditStatusEnum;
import com.penglecode.xmodule.samples.product.domain.enums.ProductOnlineStatusEnum;
import com.penglecode.xmodule.samples.product.domain.enums.ProductTypeEnum;
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
import java.util.stream.Collectors;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/24 14:33
 */
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
        String nowTime = DateTimeUtils.formatNow();
        ProductBaseInfo productBase = new ProductBaseInfo();
        productBase.setProductName("24期免息【当天发】Huawei/华为Mate40 5G手机官方旗舰店50pro直降mate40e官网30正品4G鸿蒙正品30全网通40");
        productBase.setProductUrl("https://detail.tmall.com/item.htm?id=633658852628");
        productBase.setProductTags("华为手机 5G mate40pro");
        productBase.setProductType(ProductTypeEnum.PHYSICAL_PRODUCT.getTypeCode());
        productBase.setAuditStatus(ProductAuditStatusEnum.WAIT_AUDIT.getStatusCode());
        productBase.setOnlineStatus(ProductOnlineStatusEnum.OFFLINE.getStatusCode());
        productBase.setShopId(111212422L);
        productBase.setRemark("当天发货 保修3年 送影视会员 咨询客服");
        productBase.setCreateTime(nowTime);
        productBase.setUpdateTime(nowTime);
        //System.out.println(JsonUtils.object2Json(productBase));
        productBaseInfoMapper.insertModel(productBase);

        ProductExtraInfo productExtra = new ProductExtraInfo();
        productExtra.setProductId(productBase.getProductId());
        productExtra.setProductDetails("商品详情");
        productExtra.setProductSpecifications("商品规格参数");
        productExtra.setProductServices("商品服务");
        productExtra.setCreateTime(nowTime);
        productExtra.setUpdateTime(nowTime);
        productExtraInfoMapper.insertModel(productExtra);

        List<String> nets = Arrays.asList("4G全网通", "5G全网通");
        List<String> colors = Arrays.asList("亮黑色", "釉白色", "秘银色", "夏日胡杨", "秋日胡杨");
        List<String> storages = Arrays.asList("8+128GB", "8+256GB");
        List<List<String>> specs = Arrays.asList(nets, colors, storages);

        List<ProductSaleSpec> productSpecs = new ArrayList<>();
        try(JdbcBatchOperation batchOperation = new JdbcBatchOperation()) {
            for(int i = 0, len1 = specs.size(); i < len1; i++) {
                for(int j = 0, len2 = specs.get(i).size(); j < len2; j++) {
                    String specNo = i + "" + j;
                    ProductSaleSpec productSpec = new ProductSaleSpec();
                    productSpec.setProductId(productBase.getProductId());
                    productSpec.setSpecName(specs.get(i).get(j));
                    productSpec.setSpecNo(specNo);
                    productSpec.setSpecIndex(i + j);
                    productSpec.setCreateTime(nowTime);
                    productSpec.setUpdateTime(nowTime);
                    //System.out.println(JsonUtils.object2Json(productSpec));
                    productSpecs.add(productSpec);
                    productSaleSpecMapper.insertModel(productSpec);
                }
            }
            productSaleSpecMapper.flushStatements(); //批量提交
        }

        Map<Character,List<ProductSaleSpec>> groupedProductSpecs = productSpecs.stream().collect(Collectors.groupingBy(spec -> spec.getSpecNo().charAt(0)));

        List<ProductSaleSpec> productSpecs0 = groupedProductSpecs.get('0');
        List<ProductSaleSpec> productSpecs1 = groupedProductSpecs.get('1');
        List<ProductSaleSpec> productSpecs2 = groupedProductSpecs.get('2');

        List<List<ProductSaleSpec>> cartesians = Lists.cartesianProduct(productSpecs0, productSpecs1, productSpecs2); //笛卡尔积

        try(JdbcBatchOperation batchOperation = new JdbcBatchOperation()) {
            for (int i = 0, len1 = cartesians.size(); i < len1; i++) {
                StringBuilder specNos = new StringBuilder();
                StringBuilder specNames = new StringBuilder();
                for (int j = 0, len2 = cartesians.get(i).size(); j < len2; j++) {
                    specNos.append(cartesians.get(i).get(j).getSpecNo());
                    specNames.append(cartesians.get(i).get(j).getSpecName());
                    if (j != len2 - 1) {
                        specNos.append(":");
                        specNames.append(":");
                    }
                }
                ProductSaleStock productStock = new ProductSaleStock();
                productStock.setProductId(productBase.getProductId());
                productStock.setSpecNo(specNos.toString());
                productStock.setSpecName(specNames.toString());
                productStock.setSellPrice(619900L);
                productStock.setStock(999);
                productStock.setCreateTime(nowTime);
                productStock.setUpdateTime(nowTime);
                //System.out.println(JsonUtils.object2Json(productStock));
                productSaleStockMapper.insertModel(productStock);
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
                .orderBy(Order.desc(ProductBaseInfo::getCreateTime))
                .limit(10);
        productBases = productBaseInfoMapper.selectModelListByCriteria(criteria1);
        if(!CollectionUtils.isEmpty(productBases)) {
            productBases.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }

        System.out.println("-----------------------------------------");

        QueryCriteria<ProductSaleStock> criteria2 = LambdaQueryCriteria.ofEmpty(ProductSaleStock::new)
                .eq(ProductSaleStock::getProductId, productId)
                .likeRight(ProductSaleStock::getSpecNo, "00")
                .orderBy(Order.desc(ProductSaleStock::getSellPrice));
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
                .orderBy(Order.desc(ProductSaleStock::getSellPrice))
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