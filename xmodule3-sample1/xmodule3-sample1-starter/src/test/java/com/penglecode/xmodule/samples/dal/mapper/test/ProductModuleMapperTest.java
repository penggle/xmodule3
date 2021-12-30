package com.penglecode.xmodule.samples.dal.mapper.test;

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
import com.penglecode.xmodule.samples.boot.SamplesApplication;
import com.penglecode.xmodule.samples.dal.mapper.ProductInfoMapper;
import com.penglecode.xmodule.samples.dal.mapper.ProductSpecMapper;
import com.penglecode.xmodule.samples.dal.mapper.ProductStockMapper;
import com.penglecode.xmodule.samples.domain.enums.ProductAuditStatusEnum;
import com.penglecode.xmodule.samples.domain.enums.ProductOnlineStatusEnum;
import com.penglecode.xmodule.samples.domain.enums.ProductTypeEnum;
import com.penglecode.xmodule.samples.domain.model.ProductBaseInfo;
import com.penglecode.xmodule.samples.domain.model.ProductSpec;
import com.penglecode.xmodule.samples.domain.model.ProductStock;
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
@SpringBootTest(classes= SamplesApplication.class)
public class ProductModuleMapperTest {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private ProductSpecMapper productSpecMapper;

    @Autowired
    private ProductStockMapper productStockMapper;

    @Resource(name="productDataSourceTransactionManager")
    private DataSourceTransactionManager productDataSourceTransactionManager;

    @Test
    public void createProduct() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(productDataSourceTransactionManager);
        transactionTemplate.execute(this::doCreateProduct);
    }

    protected Object doCreateProduct(TransactionStatus status) {
        String nowTime = DateTimeUtils.formatNow();
        ProductBaseInfo productInfo = new ProductBaseInfo();
        productInfo.setProductName("24期免息【当天发】Huawei/华为Mate40 5G手机官方旗舰店50pro直降mate40e官网30正品4G鸿蒙正品30全网通40");
        productInfo.setProductUrl("https://detail.tmall.com/item.htm?id=633658852628");
        productInfo.setProductTags("华为手机 5G mate40pro");
        productInfo.setProductType(ProductTypeEnum.PHYSICAL_PRODUCT.getTypeCode());
        productInfo.setAuditStatus(ProductAuditStatusEnum.WAIT_AUDIT.getStatusCode());
        productInfo.setOnlineStatus(ProductOnlineStatusEnum.OFFLINE.getStatusCode());
        productInfo.setShopId(111212422L);
        productInfo.setRemark("当天发货 保修3年 送影视会员 咨询客服");
        productInfo.setCreateTime(nowTime);
        productInfo.setUpdateTime(nowTime);
        //System.out.println(JsonUtils.object2Json(productInfo));
        productInfoMapper.insertModel(productInfo);

        List<String> nets = Arrays.asList("4G全网通", "5G全网通");
        List<String> colors = Arrays.asList("亮黑色", "釉白色", "秘银色", "夏日胡杨", "秋日胡杨");
        List<String> storages = Arrays.asList("8+128GB", "8+256GB");
        List<List<String>> specs = Arrays.asList(nets, colors, storages);

        List<ProductSpec> productSpecs = new ArrayList<>();
        try(JdbcBatchOperation batchOperation = new JdbcBatchOperation()) {
            for(int i = 0, len1 = specs.size(); i < len1; i++) {
                for(int j = 0, len2 = specs.get(i).size(); j < len2; j++) {
                    String specNo = i + "" + j;
                    ProductSpec productSpec = new ProductSpec();
                    productSpec.setProductId(productInfo.getProductId());
                    productSpec.setSpecName(specs.get(i).get(j));
                    productSpec.setSpecNo(specNo);
                    productSpec.setSpecIndex(i + j);
                    productSpec.setCreateTime(nowTime);
                    productSpec.setUpdateTime(nowTime);
                    //System.out.println(JsonUtils.object2Json(productSpec));
                    productSpecs.add(productSpec);
                    productSpecMapper.insertModel(productSpec);
                }
            }
            productSpecMapper.flushStatements(); //批量提交
        }

        Map<Character,List<ProductSpec>> groupedProductSpecs = productSpecs.stream().collect(Collectors.groupingBy(spec -> spec.getSpecNo().charAt(0)));

        List<ProductSpec> productSpecs0 = groupedProductSpecs.get('0');
        List<ProductSpec> productSpecs1 = groupedProductSpecs.get('1');
        List<ProductSpec> productSpecs2 = groupedProductSpecs.get('2');

        List<List<ProductSpec>> cartesians = Lists.cartesianProduct(productSpecs0, productSpecs1, productSpecs2); //笛卡尔积

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
                ProductStock productStock = new ProductStock();
                productStock.setProductId(productInfo.getProductId());
                productStock.setSpecNo(specNos.toString());
                productStock.setSpecName(specNames.toString());
                productStock.setSellPrice(619900L);
                productStock.setStock(999);
                productStock.setCreateTime(nowTime);
                productStock.setUpdateTime(nowTime);
                //System.out.println(JsonUtils.object2Json(productStock));
                productStockMapper.insertModel(productStock);
            }
            productStockMapper.flushStatements(); //批量提交
        }
        return null;
    }

    @Test
    public void updateProduct() {
        Long productId = 7L;
        ProductBaseInfo productInfo = productInfoMapper.selectModelById(productId);
        System.out.println(JsonUtils.object2Json(productInfo));

        QueryCriteria<ProductStock> criteria = LambdaQueryCriteria.ofEmpty(ProductStock::new)
                .eq(ProductStock::getProductId, productId)
                .likeRight(ProductStock::getSpecNo, "00");
        List<ProductStock> productStocks = productStockMapper.selectModelListByCriteria(criteria);

        if(!CollectionUtils.isEmpty(productStocks)) {
            productStocks.forEach(stock -> System.out.println(JsonUtils.object2Json(stock)));
        }

        String nowTime = DateTimeUtils.formatNow();
        productInfo.setAuditStatus(ProductAuditStatusEnum.AUDIT_PASSED.getStatusCode());
        productInfo.setOnlineStatus(ProductOnlineStatusEnum.ONLINE.getStatusCode());
        productInfo.setUpdateTime(nowTime);
        Map<String,Object> paramMap1 = MapLambdaBuilder.of(productInfo)
                .with(ProductBaseInfo::getAuditStatus)
                .with(ProductBaseInfo::getOnlineStatus)
                .with(ProductBaseInfo::getUpdateTime)
                .build();
        productInfoMapper.updateModelById(productInfo.getProductId(), paramMap1);

        Map<String,Object> paramMap2 = MapLambdaBuilder.of(new ProductStock())
                .with(ProductStock::getSellPrice, 599700)
                .with(ProductStock::getUpdateTime, nowTime)
                .build();
        productStockMapper.updateModelByCriteria(criteria, paramMap2);
    }

    @Test
    public void queryProduct() {
        Long productId = 7L;
        List<ProductBaseInfo> productInfos = productInfoMapper.selectModelListByIds(Arrays.asList(5L, 6L, 7L, 8L, 9L),
                new QueryColumns(ProductBaseInfo::getProductId, ProductBaseInfo::getProductName, ProductBaseInfo::getProductType));
        if(!CollectionUtils.isEmpty(productInfos)) {
            productInfos.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }

        System.out.println("-----------------------------------------");

        QueryCriteria<ProductStock> criteria = LambdaQueryCriteria.ofEmpty(ProductStock::new)
                .eq(ProductStock::getProductId, productId)
                .likeRight(ProductStock::getSpecNo, "00")
                .orderBy(Order.desc(ProductStock::getSellPrice));
        List<ProductStock> productStocks = productStockMapper.selectModelListByCriteria(criteria, new QueryColumns(ProductStock::getProductId, ProductStock::getSpecNo, ProductStock::getSellPrice, ProductStock::getStock));
        if(!CollectionUtils.isEmpty(productStocks)) {
            productStocks.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }

        System.out.println("-----------------------------------------");

        List<ID> idList = new ArrayList<>();
        idList.add(new ID().addKey("productId", productId).addKey("specNo", "00:10:20"));
        idList.add(new ID().addKey("productId", productId).addKey("specNo", "00:10:21"));
        productStocks = productStockMapper.selectModelListByIds(idList, new QueryColumns(column -> true));
        if(!CollectionUtils.isEmpty(productStocks)) {
            productStocks.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }

        System.out.println("-----------------------------------------");
        ProductStock productStock = productStockMapper.selectModelById(new ID().addKey("productId", productId).addKey("specNo", "01:10:20"));
        System.out.println(JsonUtils.object2Json(productStock));
    }

    @Test
    public void queryProductByPage() {
        Long productId = 7L;
        QueryCriteria<ProductStock> criteria = LambdaQueryCriteria.ofEmpty(ProductStock::new)
                .eq(ProductStock::getProductId, productId)
                .orderBy(Order.desc(ProductStock::getSellPrice));
        System.out.println("totalCount = " + productStockMapper.selectModelPageCountByCriteria(criteria));
        System.out.println("--------------------------第1页------------------------");
        List<ProductStock> productStocks1 = productStockMapper.selectModelPageListByCriteria(criteria, new RowBounds(0, 10));
        if(!CollectionUtils.isEmpty(productStocks1)) {
            productStocks1.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }
        System.out.println("--------------------------第2页------------------------");
        List<ProductStock> productStocks2 = productStockMapper.selectModelPageListByCriteria(criteria, new RowBounds(10, 10), new QueryColumns(ProductStock::getProductId, ProductStock::getSpecNo, ProductStock::getSellPrice, ProductStock::getStock));
        if(!CollectionUtils.isEmpty(productStocks2)) {
            productStocks2.forEach(item -> System.out.println(JsonUtils.object2Json(item)));
        }
    }

    @Test
    public void deleteProduct() {
        Long productId = 7L;
        productStockMapper.deleteModelById(new ID().addKey("productId", productId).addKey("specNo", "00:10:20"));

        List<ID> idList = new ArrayList<>();
        idList.add(new ID().addKey("productId", productId).addKey("specNo", "00:11:20"));
        idList.add(new ID().addKey("productId", productId).addKey("specNo", "00:11:21"));
        productStockMapper.deleteModelByIds(idList);

        QueryCriteria<ProductStock> criteria = LambdaQueryCriteria.ofEmpty(ProductStock::new)
                .eq(ProductStock::getProductId, productId)
                .likeRight(ProductStock::getSpecNo, "00");
        productStockMapper.deleteModelByCriteria(criteria);

        productInfoMapper.deleteModelByIds(Arrays.asList(1L, 2L, 3L, 4L, 5L));
    }

}