package com.penglecode.xmodule.samples.product.domain.service.impl;

import com.penglecode.xmodule.common.domain.Page;
import com.penglecode.xmodule.common.mybatis.MybatisHelper;
import com.penglecode.xmodule.common.mybatis.dsl.LambdaQueryCriteria;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import com.penglecode.xmodule.common.support.BeanValidator;
import com.penglecode.xmodule.common.support.MapLambdaBuilder;
import com.penglecode.xmodule.common.support.MessageSupplier;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.samples.product.domain.service.ProductBaseInfoService;
import javax.annotation.Resource;

import com.penglecode.xmodule.samples.product.dal.mapper.ProductBaseInfoMapper;
import com.penglecode.xmodule.samples.product.domain.model.ProductBaseInfo;
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

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

/**
 * 商品基础信息 领域服务实现
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月25日 下午 23:29
 */
@Service("productBaseInfoService")
public class ProductBaseInfoServiceImpl implements ProductBaseInfoService {

    @Resource(name="productProductBaseInfoMapper")
    private ProductBaseInfoMapper productBaseInfoMapper;

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void createProductBase(ProductBaseInfo productBase) {
        ValidationAssert.notNull(productBase, MessageSupplier.ofRequiredParameter("productBase"));
        productBase.setCreateTime(StringUtils.defaultIfBlank(productBase.getCreateTime(), DateTimeUtils.formatNow()));
        productBase.setUpdateTime(productBase.getCreateTime());
        BeanValidator.validateBean(productBase, ProductBaseInfo::getProductName, ProductBaseInfo::getProductUrl, ProductBaseInfo::getProductTags, ProductBaseInfo::getProductType, ProductBaseInfo::getAuditStatus, ProductBaseInfo::getOnlineStatus, ProductBaseInfo::getShopId, ProductBaseInfo::getCreateTime, ProductBaseInfo::getUpdateTime);
        productBaseInfoMapper.insertModel(productBase);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void modifyProductBaseById(ProductBaseInfo productBase) {
        ValidationAssert.notNull(productBase, MessageSupplier.ofRequiredParameter("productBase"));
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
    public void removeProductBaseById(Long id) {
        BeanValidator.validateProperty(id, ProductBaseInfo::getProductId);
        productBaseInfoMapper.deleteModelById(id);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void removeProductBaseByIds(List<Long> ids) {
        ValidationAssert.notEmpty(ids, MessageSupplier.ofRequiredParameter( "ids"));
        MybatisHelper.batchDeleteDomainObjects(ids, productBaseInfoMapper);
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
    @Transactional(transactionManager="productTransactionManager", propagation=REQUIRES_NEW, readOnly=true, isolation=REPEATABLE_READ)
    public List<ProductBaseInfo> getProductBasesByPage(ProductBaseInfo condition, Page page) {
        QueryCriteria<ProductBaseInfo> criteria = LambdaQueryCriteria.of(condition)
                .like(ProductBaseInfo::getProductTags)
                .eq(ProductBaseInfo::getProductType)
                .eq(ProductBaseInfo::getAuditStatus)
                .eq(ProductBaseInfo::getOnlineStatus)
                .eq(ProductBaseInfo::getShopId)
                .in(ProductBaseInfo::getAuditStatus, condition.getAuditStatuses())
                .gte(ProductBaseInfo::getCreateTime, condition.getStartCreateTime())
                .lte(ProductBaseInfo::getCreateTime, condition.getEndCreateTime())
                .dynamic(true)
                .orderBy(page.getOrders());
        return MybatisHelper.selectDomainObjectListByPage(productBaseInfoMapper, criteria, page);
    }

    @Override
    public int getProductTotalCount() {
        return productBaseInfoMapper.selectAllModelCount();
    }

    @Override
    public void forEachProductBase(Consumer<ProductBaseInfo> consumer) {
        productBaseInfoMapper.selectAllModelList().forEach(consumer);
    }

    @Override
    public void forEachProductBase(ObjIntConsumer<ProductBaseInfo> consumer) {
        Cursor<ProductBaseInfo> cursor = productBaseInfoMapper.selectAllModelList();
        int index = 0;
        for (ProductBaseInfo item : cursor) {
            consumer.accept(item, index++);
        }
    }

}