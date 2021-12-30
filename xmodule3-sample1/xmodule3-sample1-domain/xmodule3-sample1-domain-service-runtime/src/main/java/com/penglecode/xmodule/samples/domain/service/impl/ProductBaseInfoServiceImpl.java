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
import com.penglecode.xmodule.samples.domain.service.ProductBaseInfoService;
import javax.annotation.Resource;

import com.penglecode.xmodule.samples.product.dal.mapper.ProductBaseInfoMapper;
import com.penglecode.xmodule.samples.product.domain.model.ProductBaseInfo;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

/**
 * 商品基础信息 领域服务实现
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月25日 下午 23:29
 */
@Service("productInfoService")
public class ProductBaseInfoServiceImpl implements ProductBaseInfoService {

    @Resource(name="productProductBaseInfoMapper")
    private ProductBaseInfoMapper productBaseInfoMapper;

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void createProductBase(ProductBaseInfo productBase) {
        ValidationAssert.notNull(productBase, MessageSupplier.of("message.parameter.required", "productBase"));
        productBase.setCreateTime(StringUtils.defaultIfBlank(productBase.getCreateTime(), DateTimeUtils.formatNow()));
        productBase.setUpdateTime(productBase.getCreateTime());
        BeanValidator.validateBean(productBase, ProductBaseInfo::getProductName, ProductBaseInfo::getProductUrl, ProductBaseInfo::getProductTags, ProductBaseInfo::getProductType, ProductBaseInfo::getAuditStatus, ProductBaseInfo::getOnlineStatus, ProductBaseInfo::getShopId, ProductBaseInfo::getCreateTime, ProductBaseInfo::getUpdateTime);
        productBaseInfoMapper.insertModel(productBase);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void bulkCreateProductBase(List<ProductBaseInfo> productBases) {
        ValidationAssert.notEmpty(productBases, MessageSupplier.of("message.parameter.required", "productBases"));
        MybatisHelper.bulkUpdateDomainObjects(productBases, this::createProductBase, productBaseInfoMapper);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void modifyProductBaseById(ProductBaseInfo productBase) {
        ValidationAssert.notNull(productBase, MessageSupplier.of("message.parameter.required", "productBase"));
        productBase.setUpdateTime(StringUtils.defaultIfBlank(productBase.getUpdateTime(), DateTimeUtils.formatNow()));
        BeanValidator.validateBean(productBase, ProductBaseInfo::getProductId, ProductBaseInfo::getProductName, ProductBaseInfo::getProductUrl, ProductBaseInfo::getProductTags, ProductBaseInfo::getProductType, ProductBaseInfo::getAuditStatus, ProductBaseInfo::getOnlineStatus, ProductBaseInfo::getUpdateTime);
        Map<String,Object> updateColumns = MapLambdaBuilder.of(productBase)
                .with(ProductBaseInfo::getProductName)
                .with(ProductBaseInfo::getProductUrl)
                .with(ProductBaseInfo::getProductTags)
                .with(ProductBaseInfo::getProductType)
                .with(ProductBaseInfo::getAuditStatus)
                .with(ProductBaseInfo::getOnlineStatus)
                .with(ProductBaseInfo::getRemark)
                .with(ProductBaseInfo::getUpdateTime)
                .build();
        productBaseInfoMapper.updateModelById(productBase.identity(), updateColumns);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void bulkModifyProductBaseById(List<ProductBaseInfo> productBases) {
        ValidationAssert.notEmpty(productBases, MessageSupplier.of("message.parameter.required", "productBases"));
        MybatisHelper.bulkUpdateDomainObjects(productBases, this::modifyProductBaseById, productBaseInfoMapper);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void removeProductBaseById(Long id) {
        BeanValidator.validateProperty(id, ProductBaseInfo::getProductId);
        productBaseInfoMapper.deleteModelById(id);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void removeProductBaseByIds(List<Long> ids) {
        ValidationAssert.notEmpty(ids, MessageSupplier.of("message.parameter.required", "ids"));
        MybatisHelper.bulkDeleteDomainObjects(ids, productBaseInfoMapper);
    }

    @Override
    public ProductBaseInfo getProductBaseById(Long id) {
        return ObjectUtils.isEmpty(id) ? null : productBaseInfoMapper.selectModelById(id);
    }

    @Override
    public List<ProductBaseInfo> getProductBasesByIds(List<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : productBaseInfoMapper.selectModelListByIds(ids);
    }

    @Override
    public List<ProductBaseInfo> getProductBasesByPage(ProductBaseInfo condition, Page page) {
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
        return MybatisHelper.selectDomainObjectListByPage(productBaseInfoMapper, criteria, page);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", readOnly=true)
    public void forEachProductBase(Consumer<ProductBaseInfo> consumer) {
        productBaseInfoMapper.selectAllModelList().forEach(consumer);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", readOnly=true)
    public void forEachProductBase(ObjIntConsumer<ProductBaseInfo> consumer) {
        Cursor<ProductBaseInfo> cursor = productBaseInfoMapper.selectAllModelList();
        int index = 0;
        for (ProductBaseInfo item : cursor) {
            consumer.accept(item, index++);
        }
    }

}