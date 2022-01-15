package com.penglecode.xmodule.samples.domain.service.impl;

import com.penglecode.xmodule.common.domain.ID;
import com.penglecode.xmodule.common.model.Page;
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
import com.penglecode.xmodule.samples.dal.mapper.ProductStockMapper;
import com.penglecode.xmodule.samples.domain.model.ProductStock;
import com.penglecode.xmodule.samples.domain.service.ProductStockService;
import javax.annotation.Resource;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service("productStockService")
public class ProductStockServiceImpl implements ProductStockService {

    @Resource(name="productProductStockMapper")
    private ProductStockMapper productStockMapper;

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void createProductStock(ProductStock productStock) {
        ValidationAssert.notNull(productStock, MessageSupplier.of("message.parameter.required", "productStock"));
        productStock.setCreateTime(StringUtils.defaultIfBlank(productStock.getCreateTime(), DateTimeUtils.formatNow()));
        productStock.setUpdateTime(productStock.getCreateTime());
        BeanValidator.validateBean(productStock, ProductStock::getProductId, ProductStock::getSpecNo, ProductStock::getSpecName, ProductStock::getSellPrice, ProductStock::getStock, ProductStock::getCreateTime, ProductStock::getUpdateTime);
        productStockMapper.insertModel(productStock);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void batchCreateProductStock(List<ProductStock> productStockList) {
        ValidationAssert.notEmpty(productStockList, MessageSupplier.of("message.parameter.required", "productStockList"));
        MybatisHelper.batchUpdateDomainObjects(productStockList, this::createProductStock, productStockMapper);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void modifyProductStockById(ProductStock productStock) {
        ValidationAssert.notNull(productStock, MessageSupplier.of("message.parameter.required", "productStock"));
        productStock.setUpdateTime(StringUtils.defaultIfBlank(productStock.getUpdateTime(), DateTimeUtils.formatNow()));
        BeanValidator.validateBean(productStock, ProductStock::getProductId, ProductStock::getSpecNo, ProductStock::getSpecName, ProductStock::getSellPrice, ProductStock::getStock, ProductStock::getUpdateTime);
        Map<String,Object> updateColumns = MapLambdaBuilder.of(productStock)
                .with(ProductStock::getSpecName)
                .with(ProductStock::getSellPrice)
                .with(ProductStock::getStock)
                .with(ProductStock::getUpdateTime)
                .build();
        productStockMapper.updateModelById(productStock.identity(), updateColumns);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void batchModifyProductStockById(List<ProductStock> productStockList) {
        ValidationAssert.notEmpty(productStockList, MessageSupplier.of("message.parameter.required", "productStockList"));
        MybatisHelper.batchUpdateDomainObjects(productStockList, this::modifyProductStockById, productStockMapper);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void removeProductStockById(ID id) {
        BeanValidator.validateMap(id, ProductStock::getProductId, ProductStock::getSpecNo);
        productStockMapper.deleteModelById(id);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void removeProductStockByIds(List<ID> ids) {
        ValidationAssert.notEmpty(ids, MessageSupplier.of("message.parameter.required", "ids"));
        MybatisHelper.batchDeleteDomainObjects(ids, productStockMapper);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void removeProductStockListByProductId(Long productId) {
        BeanValidator.validateProperty(productId, ProductStock::getProductId);
        QueryCriteria<ProductStock> criteria = LambdaQueryCriteria.ofEmpty(ProductStock::new)
            .eq(ProductStock::getProductId, productId);
        productStockMapper.deleteModelByCriteria(criteria);
    }

    @Override
    public ProductStock getProductStockById(ID id) {
        return ObjectUtils.isEmpty(id) ? null : productStockMapper.selectModelById(id);
    }

    @Override
    public List<ProductStock> getProductStockListByIds(List<ID> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : productStockMapper.selectModelListByIds(ids);
    }

    @Override
    public List<ProductStock> getProductStockListByProductId(Long productId) {
        if(!ObjectUtils.isEmpty(productId)) {
            QueryCriteria<ProductStock> criteria = LambdaQueryCriteria.ofEmpty(ProductStock::new)
                .eq(ProductStock::getProductId, productId);
            return productStockMapper.selectModelListByCriteria(criteria);
        }
        return Collections.emptyList();
    }

    @Override
    public Map<Long,List<ProductStock>> getProductStockListByProductIds(List<Long> productIds) {
        if(!CollectionUtils.isEmpty(productIds)) {
            QueryCriteria<ProductStock> criteria = LambdaQueryCriteria.ofEmpty(ProductStock::new)
                .in(ProductStock::getProductId, productIds.toArray());
            List<ProductStock> productStockList = productStockMapper.selectModelListByCriteria(criteria);
            if(!CollectionUtils.isEmpty(productStockList)) {
                return productStockList.stream().collect(Collectors.groupingBy(ProductStock::getProductId, Collectors.toList()));
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public List<ProductStock> getProductStockListByPage(ProductStock condition, Page page) {
        QueryCriteria<ProductStock> criteria = LambdaQueryCriteria.of(condition)
                .eq(ProductStock::getProductId)
                .eq(ProductStock::getSpecNo)
                .eq(ProductStock::getSellPrice)
                .eq(ProductStock::getStock)
                .gte(ProductStock::getSellPrice, condition.getMinSellPrice())
                .lte(ProductStock::getSellPrice, condition.getMaxSellPrice())
                .gte(ProductStock::getStock, condition.getMinStock())
                .lte(ProductStock::getStock, condition.getMaxStock())
                .dynamic(true)
                .orderBy(page.getOrderBys());
        return MybatisHelper.selectDomainObjectListByPage(productStockMapper, criteria, page);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", readOnly=true)
    public void forEachProductStock(Consumer<ProductStock> consumer) {
        productStockMapper.selectAllModelList().forEach(consumer);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", readOnly=true)
    public void forEachProductStock(ObjIntConsumer<ProductStock> consumer) {
        Cursor<ProductStock> cursor = productStockMapper.selectAllModelList();
        int index = 0;
        for (ProductStock item : cursor) {
            consumer.accept(item, index++);
        }
    }

}