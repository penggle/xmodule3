package com.penglecode.xmodule.samples.product.domain.service.impl;

import com.penglecode.xmodule.common.domain.ID;
import com.penglecode.xmodule.common.model.Page;
import com.penglecode.xmodule.common.mybatis.MybatisHelper;
import com.penglecode.xmodule.common.mybatis.dsl.LambdaQueryCriteria;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import com.penglecode.xmodule.common.support.BeanValidator;
import com.penglecode.xmodule.common.support.MapLambdaBuilder;
import com.penglecode.xmodule.common.support.MessageSupplier;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import javax.annotation.Resource;

import com.penglecode.xmodule.samples.product.dal.mapper.ProductSaleSpecMapper;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleSpec;
import com.penglecode.xmodule.samples.product.domain.service.ProductSaleSpecService;
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

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

/**
 * 商品销售规格信息 领域服务实现
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月25日 下午 23:29
 */
@Service("productSaleSpecService")
public class ProductSaleSpecServiceImpl implements ProductSaleSpecService {

    @Resource(name="productProductSaleSpecMapper")
    private ProductSaleSpecMapper productSaleSpecMapper;

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void createProductSaleSpec(ProductSaleSpec productSaleSpec) {
        ValidationAssert.notNull(productSaleSpec, MessageSupplier.ofRequiredParameter("productSaleSpec"));
        productSaleSpec.setCreateTime(StringUtils.defaultIfBlank(productSaleSpec.getCreateTime(), DateTimeUtils.formatNow()));
        productSaleSpec.setUpdateTime(productSaleSpec.getCreateTime());
        BeanValidator.validateBean(productSaleSpec, ProductSaleSpec::getProductId, ProductSaleSpec::getSpecNo, ProductSaleSpec::getSpecName, ProductSaleSpec::getSpecIndex, ProductSaleSpec::getCreateTime, ProductSaleSpec::getUpdateTime);
        productSaleSpecMapper.insertModel(productSaleSpec);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void batchCreateProductSaleSpec(List<ProductSaleSpec> productSaleSpecs) {
        ValidationAssert.notEmpty(productSaleSpecs, MessageSupplier.ofRequiredParameter("productSaleSpecs"));
        MybatisHelper.batchUpdateDomainObjects(productSaleSpecs, this::createProductSaleSpec, productSaleSpecMapper);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void modifyProductSaleSpecById(ProductSaleSpec productSaleSpec) {
        ValidationAssert.notNull(productSaleSpec, MessageSupplier.ofRequiredParameter("productSaleSpec"));
        productSaleSpec.setUpdateTime(StringUtils.defaultIfBlank(productSaleSpec.getUpdateTime(), DateTimeUtils.formatNow()));
        BeanValidator.validateBean(productSaleSpec, ProductSaleSpec::getProductId, ProductSaleSpec::getSpecNo, ProductSaleSpec::getSpecName, ProductSaleSpec::getSpecIndex, ProductSaleSpec::getUpdateTime);
        Map<String,Object> updateColumns = MapLambdaBuilder.of(productSaleSpec)
                .with(ProductSaleSpec::getSpecName)
                .with(ProductSaleSpec::getSpecIndex)
                .with(ProductSaleSpec::getRemark)
                .with(ProductSaleSpec::getUpdateTime)
                .build();
        productSaleSpecMapper.updateModelById(productSaleSpec.identity(), updateColumns);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void batchModifyProductSaleSpecById(List<ProductSaleSpec> productSaleSpecs) {
        ValidationAssert.notEmpty(productSaleSpecs, MessageSupplier.ofRequiredParameter("productSaleSpecs"));
        MybatisHelper.batchUpdateDomainObjects(productSaleSpecs, this::modifyProductSaleSpecById, productSaleSpecMapper);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void removeProductSaleSpecById(ID id) {
        BeanValidator.validateMap(id, ProductSaleSpec::getProductId, ProductSaleSpec::getSpecNo);
        productSaleSpecMapper.deleteModelById(id);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void removeProductSaleSpecByIds(List<ID> ids) {
        ValidationAssert.notEmpty(ids, MessageSupplier.ofRequiredParameter("ids"));
        MybatisHelper.batchDeleteDomainObjects(ids, productSaleSpecMapper);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void removeProductSaleSpecsByProductId(Long productId) {
        BeanValidator.validateProperty(productId, ProductSaleSpec::getProductId);
        QueryCriteria<ProductSaleSpec> criteria = LambdaQueryCriteria.ofEmpty(ProductSaleSpec::new)
            .eq(ProductSaleSpec::getProductId, productId);
        productSaleSpecMapper.deleteModelByCriteria(criteria);
    }

    @Override
    public ProductSaleSpec getProductSaleSpecById(ID id) {
        return ObjectUtils.isEmpty(id) ? null : productSaleSpecMapper.selectModelById(id);
    }

    @Override
    public List<ProductSaleSpec> getProductSaleSpecsByIds(List<ID> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : productSaleSpecMapper.selectModelListByIds(ids);
    }

    @Override
    public List<ProductSaleSpec> getProductSaleSpecsByProductId(Long productId) {
        if(!ObjectUtils.isEmpty(productId)) {
            QueryCriteria<ProductSaleSpec> criteria = LambdaQueryCriteria.ofEmpty(ProductSaleSpec::new)
                .eq(ProductSaleSpec::getProductId, productId);
            return productSaleSpecMapper.selectModelListByCriteria(criteria);
        }
        return Collections.emptyList();
    }

    @Override
    public Map<Long,List<ProductSaleSpec>> getProductSaleSpecsByProductIds(List<Long> productIds) {
        if(!CollectionUtils.isEmpty(productIds)) {
            QueryCriteria<ProductSaleSpec> criteria = LambdaQueryCriteria.ofEmpty(ProductSaleSpec::new)
                .in(ProductSaleSpec::getProductId, productIds.toArray());
            List<ProductSaleSpec> productSaleSpecs = productSaleSpecMapper.selectModelListByCriteria(criteria);
            if(!CollectionUtils.isEmpty(productSaleSpecs)) {
                return productSaleSpecs.stream().collect(Collectors.groupingBy(ProductSaleSpec::getProductId, Collectors.toList()));
            }
        }
        return Collections.emptyMap();
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", propagation=REQUIRES_NEW, readOnly=true, isolation=REPEATABLE_READ)
    public List<ProductSaleSpec> getProductSaleSpecsByPage(ProductSaleSpec condition, Page page) {
        QueryCriteria<ProductSaleSpec> criteria = LambdaQueryCriteria.of(condition)
                .eq(ProductSaleSpec::getProductId)
                .eq(ProductSaleSpec::getSpecNo)
                .eq(ProductSaleSpec::getSpecIndex)
                .dynamic(true)
                .orderBy(page.getOrderBys());
        return MybatisHelper.selectDomainObjectListByPage(productSaleSpecMapper, criteria, page);
    }

    @Override
    public int getProductTotalCount() {
        return productSaleSpecMapper.selectAllModelCount();
    }

    @Override
    public void forEachProductSaleSpec(Consumer<ProductSaleSpec> consumer) {
        productSaleSpecMapper.selectAllModelList().forEach(consumer);
    }

    @Override
    public void forEachProductSaleSpec(ObjIntConsumer<ProductSaleSpec> consumer) {
        Cursor<ProductSaleSpec> cursor = productSaleSpecMapper.selectAllModelList();
        int index = 0;
        for (ProductSaleSpec item : cursor) {
            consumer.accept(item, index++);
        }
    }

}