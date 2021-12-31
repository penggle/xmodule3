package com.penglecode.xmodule.samples.domain.service.impl;

import com.penglecode.xmodule.common.domain.Page;
import com.penglecode.xmodule.common.mybatis.MybatisHelper;
import com.penglecode.xmodule.common.mybatis.dsl.LambdaQueryCriteria;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import com.penglecode.xmodule.common.support.BeanValidator;
import com.penglecode.xmodule.common.support.MapLambdaBuilder;
import com.penglecode.xmodule.common.support.MessageSupplier;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.samples.dal.mapper.ProductInfoMapper;
import com.penglecode.xmodule.samples.domain.model.ProductInfo;
import com.penglecode.xmodule.samples.domain.service.ProductInfoService;
import javax.annotation.Resource;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

/**
 * 商品信息 领域服务实现
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月25日 下午 23:29
 */
@Service("productInfoService")
public class ProductInfoServiceImpl implements ProductInfoService {

    @Resource(name="productProductInfoMapper")
    private ProductInfoMapper productInfoMapper;

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void createProduct(ProductInfo product) {
        ValidationAssert.notNull(product, MessageSupplier.of("message.parameter.required", "product"));
        product.setCreateTime(StringUtils.defaultIfBlank(product.getCreateTime(), DateTimeUtils.formatNow()));
        product.setUpdateTime(product.getCreateTime());
        BeanValidator.validateBean(product, ProductInfo::getProductName, ProductInfo::getProductUrl, ProductInfo::getProductTags, ProductInfo::getProductType, ProductInfo::getAuditStatus, ProductInfo::getOnlineStatus, ProductInfo::getShopId, ProductInfo::getCreateTime, ProductInfo::getUpdateTime);
        productInfoMapper.insertModel(product);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void batchCreateProduct(List<ProductInfo> productList) {
        ValidationAssert.notEmpty(productList, MessageSupplier.of("message.parameter.required", "productList"));
        MybatisHelper.batchUpdateDomainObjects(productList, this::createProduct, productInfoMapper);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void modifyProductById(ProductInfo product) {
        ValidationAssert.notNull(product, MessageSupplier.of("message.parameter.required", "product"));
        product.setUpdateTime(StringUtils.defaultIfBlank(product.getUpdateTime(), DateTimeUtils.formatNow()));
        BeanValidator.validateBean(product, ProductInfo::getProductId, ProductInfo::getProductName, ProductInfo::getProductUrl, ProductInfo::getProductTags, ProductInfo::getProductType, ProductInfo::getAuditStatus, ProductInfo::getOnlineStatus, ProductInfo::getUpdateTime);
        Map<String,Object> updateColumns = MapLambdaBuilder.of(product)
                .with(ProductInfo::getProductName)
                .with(ProductInfo::getProductUrl)
                .with(ProductInfo::getProductTags)
                .with(ProductInfo::getProductType)
                .with(ProductInfo::getAuditStatus)
                .with(ProductInfo::getOnlineStatus)
                .with(ProductInfo::getRemark)
                .with(ProductInfo::getUpdateTime)
                .build();
        productInfoMapper.updateModelById(product.identity(), updateColumns);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void batchModifyProductById(List<ProductInfo> productList) {
        ValidationAssert.notEmpty(productList, MessageSupplier.of("message.parameter.required", "productList"));
        MybatisHelper.batchUpdateDomainObjects(productList, this::modifyProductById, productInfoMapper);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void removeProductById(Long id) {
        BeanValidator.validateProperty(id, ProductInfo::getProductId);
        productInfoMapper.deleteModelById(id);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void removeProductByIds(List<Long> ids) {
        ValidationAssert.notEmpty(ids, MessageSupplier.of("message.parameter.required", "ids"));
        MybatisHelper.batchDeleteDomainObjects(ids, productInfoMapper);
    }

    @Override
    public ProductInfo getProductById(Long id) {
        return ObjectUtils.isEmpty(id) ? null : productInfoMapper.selectModelById(id);
    }

    @Override
    public List<ProductInfo> getProductListByIds(List<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : productInfoMapper.selectModelListByIds(ids);
    }

    @Override
    public List<ProductInfo> getProductListByPage(ProductInfo condition, Page page) {
        QueryCriteria<ProductInfo> criteria = LambdaQueryCriteria.of(condition)
                .like(ProductInfo::getProductTags)
                .eq(ProductInfo::getProductType)
                .eq(ProductInfo::getAuditStatus)
                .eq(ProductInfo::getOnlineStatus)
                .eq(ProductInfo::getShopId)
                .eq(ProductInfo::getCreateTime)
                .in(ProductInfo::getAuditStatus, condition.getAuditStatuses())
                .gte(ProductInfo::getCreateTime, condition.getStartCreateTime())
                .lte(ProductInfo::getCreateTime, condition.getEndCreateTime())
                .dynamic(true)
                .orderBy(page.getOrders());
        return MybatisHelper.selectDomainObjectListByPage(productInfoMapper, criteria, page);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", readOnly=true)
    public void forEachProduct(Consumer<ProductInfo> consumer) {
        productInfoMapper.selectAllModelList().forEach(consumer);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", readOnly=true)
    public void forEachProduct(ObjIntConsumer<ProductInfo> consumer) {
        Cursor<ProductInfo> cursor = productInfoMapper.selectAllModelList();
        int index = 0;
        for (ProductInfo item : cursor) {
            consumer.accept(item, index++);
        }
    }

}