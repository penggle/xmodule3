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
import com.penglecode.xmodule.samples.domain.model.ProductBaseInfo;
import com.penglecode.xmodule.samples.domain.service.ProductBaseInfoService;
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
public class ProductInfoServiceImpl implements ProductBaseInfoService {

    @Resource(name="productProductInfoMapper")
    private ProductInfoMapper productInfoMapper;

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void createProduct(ProductBaseInfo product) {
        ValidationAssert.notNull(product, MessageSupplier.of("message.parameter.required", "product"));
        product.setCreateTime(StringUtils.defaultIfBlank(product.getCreateTime(), DateTimeUtils.formatNow()));
        product.setUpdateTime(product.getCreateTime());
        BeanValidator.validateBean(product, ProductBaseInfo::getProductName, ProductBaseInfo::getProductUrl, ProductBaseInfo::getProductTags, ProductBaseInfo::getProductType, ProductBaseInfo::getAuditStatus, ProductBaseInfo::getOnlineStatus, ProductBaseInfo::getShopId, ProductBaseInfo::getCreateTime, ProductBaseInfo::getUpdateTime);
        productInfoMapper.insertModel(product);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void batchCreateProduct(List<ProductBaseInfo> productList) {
        ValidationAssert.notEmpty(productList, MessageSupplier.of("message.parameter.required", "productList"));
        MybatisHelper.batchUpdateDomainObjects(productList, this::createProduct, productInfoMapper);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void modifyProductById(ProductBaseInfo product) {
        ValidationAssert.notNull(product, MessageSupplier.of("message.parameter.required", "product"));
        product.setUpdateTime(StringUtils.defaultIfBlank(product.getUpdateTime(), DateTimeUtils.formatNow()));
        BeanValidator.validateBean(product, ProductBaseInfo::getProductId, ProductBaseInfo::getProductName, ProductBaseInfo::getProductUrl, ProductBaseInfo::getProductTags, ProductBaseInfo::getProductType, ProductBaseInfo::getAuditStatus, ProductBaseInfo::getOnlineStatus, ProductBaseInfo::getUpdateTime);
        Map<String,Object> updateColumns = MapLambdaBuilder.of(product)
                .with(ProductBaseInfo::getProductName)
                .with(ProductBaseInfo::getProductUrl)
                .with(ProductBaseInfo::getProductTags)
                .with(ProductBaseInfo::getProductType)
                .with(ProductBaseInfo::getAuditStatus)
                .with(ProductBaseInfo::getOnlineStatus)
                .with(ProductBaseInfo::getRemark)
                .with(ProductBaseInfo::getUpdateTime)
                .build();
        productInfoMapper.updateModelById(product.identity(), updateColumns);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void batchModifyProductById(List<ProductBaseInfo> productList) {
        ValidationAssert.notEmpty(productList, MessageSupplier.of("message.parameter.required", "productList"));
        MybatisHelper.batchUpdateDomainObjects(productList, this::modifyProductById, productInfoMapper);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void removeProductById(Long id) {
        BeanValidator.validateProperty(id, ProductBaseInfo::getProductId);
        productInfoMapper.deleteModelById(id);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void removeProductByIds(List<Long> ids) {
        ValidationAssert.notEmpty(ids, MessageSupplier.of("message.parameter.required", "ids"));
        MybatisHelper.batchDeleteDomainObjects(ids, productInfoMapper);
    }

    @Override
    public ProductBaseInfo getProductById(Long id) {
        return ObjectUtils.isEmpty(id) ? null : productInfoMapper.selectModelById(id);
    }

    @Override
    public List<ProductBaseInfo> getProductListByIds(List<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : productInfoMapper.selectModelListByIds(ids);
    }

    @Override
    public List<ProductBaseInfo> getProductListByPage(ProductBaseInfo condition, Page page) {
        QueryCriteria<ProductBaseInfo> criteria = LambdaQueryCriteria.of(condition)
                .like(ProductBaseInfo::getProductTags)
                .eq(ProductBaseInfo::getProductType)
                .eq(ProductBaseInfo::getAuditStatus)
                .eq(ProductBaseInfo::getOnlineStatus)
                .eq(ProductBaseInfo::getShopId)
                .eq(ProductBaseInfo::getCreateTime)
                .in(ProductBaseInfo::getAuditStatus, condition.getAuditStatuses())
                .gte(ProductBaseInfo::getCreateTime, condition.getStartCreateTime())
                .lte(ProductBaseInfo::getCreateTime, condition.getEndCreateTime())
                .dynamic(true)
                .orderBy(page.getOrders());
        return MybatisHelper.selectDomainObjectListByPage(productInfoMapper, criteria, page);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", readOnly=true)
    public void forEachProduct(Consumer<ProductBaseInfo> consumer) {
        productInfoMapper.selectAllModelList().forEach(consumer);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", readOnly=true)
    public void forEachProduct(ObjIntConsumer<ProductBaseInfo> consumer) {
        Cursor<ProductBaseInfo> cursor = productInfoMapper.selectAllModelList();
        int index = 0;
        for (ProductBaseInfo item : cursor) {
            consumer.accept(item, index++);
        }
    }

}