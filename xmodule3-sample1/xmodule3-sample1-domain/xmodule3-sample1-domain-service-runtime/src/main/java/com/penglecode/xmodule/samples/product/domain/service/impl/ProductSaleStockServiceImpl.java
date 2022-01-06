package com.penglecode.xmodule.samples.product.domain.service.impl;

import com.penglecode.xmodule.common.domain.ID;
import com.penglecode.xmodule.common.domain.Page;
import com.penglecode.xmodule.common.mybatis.MybatisHelper;
import com.penglecode.xmodule.common.mybatis.dsl.LambdaQueryCriteria;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import com.penglecode.xmodule.common.support.BeanValidator;
import com.penglecode.xmodule.common.support.MapLambdaBuilder;
import com.penglecode.xmodule.common.support.MessageSupplier;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import javax.annotation.Resource;

import com.penglecode.xmodule.samples.product.dal.mapper.ProductSaleStockMapper;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleStock;
import com.penglecode.xmodule.samples.product.domain.service.ProductSaleStockService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;

/**
 * 商品销售库存信息 领域服务实现
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月25日 下午 23:29
 */
@Service("productSaleStockService")
public class ProductSaleStockServiceImpl implements ProductSaleStockService {

    @Resource(name="productProductSaleStockMapper")
    private ProductSaleStockMapper productSaleStockMapper;

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void createProductSaleStock(ProductSaleStock productStock) {
        ValidationAssert.notNull(productStock, MessageSupplier.ofRequiredParameter("productStock"));
        productStock.setCreateTime(StringUtils.defaultIfBlank(productStock.getCreateTime(), DateTimeUtils.formatNow()));
        productStock.setUpdateTime(productStock.getCreateTime());
        BeanValidator.validateBean(productStock, ProductSaleStock::getProductId, ProductSaleStock::getSpecNo, ProductSaleStock::getSpecName, ProductSaleStock::getSellPrice, ProductSaleStock::getStock, ProductSaleStock::getCreateTime, ProductSaleStock::getUpdateTime);
        productSaleStockMapper.insertModel(productStock);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void batchCreateProductSaleStock(List<ProductSaleStock> productStocks) {
        ValidationAssert.notEmpty(productStocks, MessageSupplier.ofRequiredParameter("productStocks"));
        MybatisHelper.batchUpdateDomainObjects(productStocks, this::createProductSaleStock, productSaleStockMapper);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void modifyProductSaleStockById(ProductSaleStock productStock) {
        ValidationAssert.notNull(productStock, MessageSupplier.ofRequiredParameter("productStock"));
        productStock.setUpdateTime(StringUtils.defaultIfBlank(productStock.getUpdateTime(), DateTimeUtils.formatNow()));
        BeanValidator.validateBean(productStock, ProductSaleStock::getProductId, ProductSaleStock::getSpecNo, ProductSaleStock::getSpecName, ProductSaleStock::getSellPrice, ProductSaleStock::getStock, ProductSaleStock::getUpdateTime);
        Map<String,Object> updateColumns = MapLambdaBuilder.of(productStock)
                .with(ProductSaleStock::getSpecName)
                .with(ProductSaleStock::getSellPrice)
                .with(ProductSaleStock::getStock)
                .with(ProductSaleStock::getUpdateTime)
                .build();
        productSaleStockMapper.updateModelById(productStock.identity(), updateColumns);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void batchModifyProductSaleStockById(List<ProductSaleStock> productStocks) {
        ValidationAssert.notEmpty(productStocks, MessageSupplier.ofRequiredParameter("productStocks"));
        MybatisHelper.batchUpdateDomainObjects(productStocks, this::modifyProductSaleStockById, productSaleStockMapper);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void removeProductSaleStockById(ID id) {
        BeanValidator.validateMap(id, ProductSaleStock::getProductId, ProductSaleStock::getSpecNo);
        productSaleStockMapper.deleteModelById(id);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void removeProductSaleStockByIds(List<ID> ids) {
        ValidationAssert.notEmpty(ids, MessageSupplier.ofRequiredParameter("ids"));
        MybatisHelper.batchDeleteDomainObjects(ids, productSaleStockMapper);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void removeProductSaleStocksByProductId(Long productId) {
        BeanValidator.validateProperty(productId, ProductSaleStock::getProductId);
        QueryCriteria<ProductSaleStock> criteria = LambdaQueryCriteria.ofEmpty(ProductSaleStock::new)
            .eq(ProductSaleStock::getProductId, productId);
        productSaleStockMapper.deleteModelByCriteria(criteria);
    }

    @Override
    public ProductSaleStock getProductSaleStockById(ID id) {
        return ObjectUtils.isEmpty(id) ? null : productSaleStockMapper.selectModelById(id);
    }

    @Override
    public List<ProductSaleStock> getProductSaleStocksByIds(List<ID> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : productSaleStockMapper.selectModelListByIds(ids);
    }

    @Override
    public List<ProductSaleStock> getProductSaleStocksByProductId(Long productId) {
        if(!ObjectUtils.isEmpty(productId)) {
            QueryCriteria<ProductSaleStock> criteria = LambdaQueryCriteria.ofEmpty(ProductSaleStock::new)
                .eq(ProductSaleStock::getProductId, productId);
            return productSaleStockMapper.selectModelListByCriteria(criteria);
        }
        return Collections.emptyList();
    }

    @Override
    public Map<Long,List<ProductSaleStock>> getProductSaleStocksByProductIds(List<Long> productIds) {
        if(!CollectionUtils.isEmpty(productIds)) {
            QueryCriteria<ProductSaleStock> criteria = LambdaQueryCriteria.ofEmpty(ProductSaleStock::new)
                .in(ProductSaleStock::getProductId, productIds.toArray());
            List<ProductSaleStock> productStocks = productSaleStockMapper.selectModelListByCriteria(criteria);
            if(!CollectionUtils.isEmpty(productStocks)) {
                return productStocks.stream().collect(Collectors.groupingBy(ProductSaleStock::getProductId, Collectors.toList()));
            }
        }
        return Collections.emptyMap();
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", readOnly=true)
    public List<ProductSaleStock> getProductSaleStocksByPage(ProductSaleStock condition, Page page) {
        QueryCriteria<ProductSaleStock> criteria = LambdaQueryCriteria.of(condition)
                .eq(ProductSaleStock::getProductId)
                .eq(ProductSaleStock::getSpecNo)
                .eq(ProductSaleStock::getSellPrice)
                .eq(ProductSaleStock::getStock)
                .gte(ProductSaleStock::getSellPrice, condition.getMinSellPrice())
                .lte(ProductSaleStock::getSellPrice, condition.getMaxSellPrice())
                .gte(ProductSaleStock::getStock, condition.getMinStock())
                .lte(ProductSaleStock::getStock, condition.getMaxStock())
                .dynamic(true)
                .orderBy(page.getOrders());
        return MybatisHelper.selectDomainObjectListByPage(productSaleStockMapper, criteria, page);
    }

    @Override
    public int getProductTotalCount() {
        return productSaleStockMapper.selectAllModelCount();
    }

    @Override
    public void forEachProductSaleStock(Consumer<ProductSaleStock> consumer) {
        productSaleStockMapper.selectAllModelList().forEach(consumer);
    }

    @Override
    public void forEachProductSaleStock(ObjIntConsumer<ProductSaleStock> consumer) {
        Cursor<ProductSaleStock> cursor = productSaleStockMapper.selectAllModelList();
        int index = 0;
        for (ProductSaleStock item : cursor) {
            consumer.accept(item, index++);
        }
    }

}