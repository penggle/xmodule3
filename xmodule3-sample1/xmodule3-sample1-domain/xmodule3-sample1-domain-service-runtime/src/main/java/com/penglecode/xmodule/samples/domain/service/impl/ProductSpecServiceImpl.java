package com.penglecode.xmodule.samples.domain.service.impl;

import com.penglecode.xmodule.common.domain.ID;
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
import com.penglecode.xmodule.samples.dal.mapper.ProductSpecMapper;
import com.penglecode.xmodule.samples.domain.model.ProductSpec;
import com.penglecode.xmodule.samples.domain.service.ProductSpecService;
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
 * 商品销售规格信息 领域服务实现
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月25日 下午 23:29
 */
@Service("productSpecService")
public class ProductSpecServiceImpl implements ProductSpecService {

    @Resource(name="productProductSpecMapper")
    private ProductSpecMapper productSpecMapper;

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void createProductSpec(ProductSpec productSpec) {
        ValidationAssert.notNull(productSpec, MessageSupplier.of("message.parameter.required", "productSpec"));
        productSpec.setCreateTime(StringUtils.defaultIfBlank(productSpec.getCreateTime(), DateTimeUtils.formatNow()));
        productSpec.setUpdateTime(productSpec.getCreateTime());
        BeanValidator.validateBean(productSpec, ProductSpec::getProductId, ProductSpec::getSpecNo, ProductSpec::getSpecName, ProductSpec::getSpecIndex, ProductSpec::getCreateTime, ProductSpec::getUpdateTime);
        productSpecMapper.insertModel(productSpec);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void batchCreateProductSpec(List<ProductSpec> productSpecList) {
        ValidationAssert.notEmpty(productSpecList, MessageSupplier.of("message.parameter.required", "productSpecList"));
        MybatisHelper.bulkUpdateDomainObjects(productSpecList, this::createProductSpec, productSpecMapper);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void modifyProductSpecById(ProductSpec productSpec) {
        ValidationAssert.notNull(productSpec, MessageSupplier.of("message.parameter.required", "productSpec"));
        productSpec.setUpdateTime(StringUtils.defaultIfBlank(productSpec.getUpdateTime(), DateTimeUtils.formatNow()));
        BeanValidator.validateBean(productSpec, ProductSpec::getProductId, ProductSpec::getSpecNo, ProductSpec::getSpecName, ProductSpec::getSpecIndex, ProductSpec::getUpdateTime);
        Map<String,Object> updateColumns = MapLambdaBuilder.of(productSpec)
                .with(ProductSpec::getSpecName)
                .with(ProductSpec::getSpecIndex)
                .with(ProductSpec::getRemark)
                .with(ProductSpec::getUpdateTime)
                .build();
        productSpecMapper.updateModelById(productSpec.identity(), updateColumns);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void batchModifyProductSpecById(List<ProductSpec> productSpecList) {
        ValidationAssert.notEmpty(productSpecList, MessageSupplier.of("message.parameter.required", "productSpecList"));
        MybatisHelper.bulkUpdateDomainObjects(productSpecList, this::modifyProductSpecById, productSpecMapper);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void removeProductSpecById(ID id) {
        BeanValidator.validateMap(id, ProductSpec::getProductId, ProductSpec::getSpecNo);
        productSpecMapper.deleteModelById(id);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void removeProductSpecByIds(List<ID> ids) {
        ValidationAssert.notEmpty(ids, MessageSupplier.of("message.parameter.required", "ids"));
        MybatisHelper.bulkDeleteDomainObjects(ids, productSpecMapper);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", rollbackFor=Exception.class)
    public void removeProductSpecListByProductId(Long productId) {
        BeanValidator.validateProperty(productId, ProductSpec::getProductId);
        QueryCriteria<ProductSpec> criteria = LambdaQueryCriteria.ofEmpty(ProductSpec::new)
            .eq(ProductSpec::getProductId, productId);
        productSpecMapper.deleteModelByCriteria(criteria);
    }

    @Override
    public ProductSpec getProductSpecById(ID id) {
        return ObjectUtils.isEmpty(id) ? null : productSpecMapper.selectModelById(id);
    }

    @Override
    public List<ProductSpec> getProductSpecListByIds(List<ID> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : productSpecMapper.selectModelListByIds(ids);
    }

    @Override
    public List<ProductSpec> getProductSpecListByProductId(Long productId) {
        if(!ObjectUtils.isEmpty(productId)) {
            QueryCriteria<ProductSpec> criteria = LambdaQueryCriteria.ofEmpty(ProductSpec::new)
                .eq(ProductSpec::getProductId, productId);
            return productSpecMapper.selectModelListByCriteria(criteria);
        }
        return Collections.emptyList();
    }

    @Override
    public Map<Long,List<ProductSpec>> getProductSpecListByProductIds(List<Long> productIds) {
        if(!CollectionUtils.isEmpty(productIds)) {
            QueryCriteria<ProductSpec> criteria = LambdaQueryCriteria.ofEmpty(ProductSpec::new)
                .in(ProductSpec::getProductId, productIds.toArray());
            List<ProductSpec> productSpecList = productSpecMapper.selectModelListByCriteria(criteria);
            if(!CollectionUtils.isEmpty(productSpecList)) {
                return productSpecList.stream().collect(Collectors.groupingBy(ProductSpec::getProductId, Collectors.toList()));
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public List<ProductSpec> getProductSpecListByPage(ProductSpec condition, Page page) {
        QueryCriteria<ProductSpec> criteria = LambdaQueryCriteria.of(condition)
                .eq(ProductSpec::getProductId)
                .eq(ProductSpec::getSpecNo)
                .eq(ProductSpec::getSpecIndex)
                .dynamic(true)
                .orderBy(page.getOrders());
        return MybatisHelper.selectDomainObjectListByPage(productSpecMapper, criteria, page);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", readOnly=true)
    public void forEachProductSpec(Consumer<ProductSpec> consumer) {
        productSpecMapper.selectAllModelList().forEach(consumer);
    }

    @Override
    @Transactional(transactionManager="productDataSourceTransactionManager", readOnly=true)
    public void forEachProductSpec(ObjIntConsumer<ProductSpec> consumer) {
        Cursor<ProductSpec> cursor = productSpecMapper.selectAllModelList();
        int index = 0;
        for (ProductSpec item : cursor) {
            consumer.accept(item, index++);
        }
    }

}