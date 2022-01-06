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
import com.penglecode.xmodule.samples.product.dal.mapper.ProductExtraInfoMapper;
import com.penglecode.xmodule.samples.product.domain.model.ProductExtraInfo;
import com.penglecode.xmodule.samples.product.domain.service.ProductExtraInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

/**
 * 商品额外信息 领域服务实现
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月25日 下午 23:29
 */
@Service("productExtraInfoService")
public class ProductExtraInfoServiceImpl implements ProductExtraInfoService {

    @Resource(name="productProductExtraInfoMapper")
    private ProductExtraInfoMapper productExtraInfoMapper;

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void createProductExtra(ProductExtraInfo productExtra) {
        ValidationAssert.notNull(productExtra, MessageSupplier.ofRequiredParameter("productExtra"));
        productExtra.setCreateTime(StringUtils.defaultIfBlank(productExtra.getCreateTime(), DateTimeUtils.formatNow()));
        productExtra.setUpdateTime(productExtra.getCreateTime());
        BeanValidator.validateBean(productExtra, ProductExtraInfo::getProductId, ProductExtraInfo::getProductDetails, ProductExtraInfo::getCreateTime, ProductExtraInfo::getUpdateTime);
        productExtraInfoMapper.insertModel(productExtra);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void modifyProductExtraById(ProductExtraInfo productExtra) {
        ValidationAssert.notNull(productExtra, MessageSupplier.ofRequiredParameter("productExtra"));
        productExtra.setUpdateTime(StringUtils.defaultIfBlank(productExtra.getUpdateTime(), DateTimeUtils.formatNow()));
        BeanValidator.validateBean(productExtra, ProductExtraInfo::getProductId, ProductExtraInfo::getProductDetails, ProductExtraInfo::getUpdateTime);
        Map<String,Object> updateColumns = MapLambdaBuilder.of(productExtra)
                .with(ProductExtraInfo::getProductDetails)
                .with(ProductExtraInfo::getProductSpecifications)
                .with(ProductExtraInfo::getProductServices)
                .with(ProductExtraInfo::getUpdateTime)
                .build();
        productExtraInfoMapper.updateModelById(productExtra.identity(), updateColumns);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void removeProductExtraById(Long id) {
        BeanValidator.validateProperty(id, ProductExtraInfo::getProductId);
        productExtraInfoMapper.deleteModelById(id);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void removeProductExtraByIds(List<Long> ids) {
        ValidationAssert.notEmpty(ids, MessageSupplier.ofRequiredParameter( "ids"));
        MybatisHelper.batchDeleteDomainObjects(ids, productExtraInfoMapper);
    }

    @Override
    public ProductExtraInfo getProductExtraById(Long id) {
        return ObjectUtils.isEmpty(id) ? null : productExtraInfoMapper.selectModelById(id);
    }

    @Override
    public List<ProductExtraInfo> getProductExtrasByIds(List<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : productExtraInfoMapper.selectModelListByIds(ids);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", readOnly=true)
    public List<ProductExtraInfo> getProductExtrasByPage(ProductExtraInfo condition, Page page) {
        QueryCriteria<ProductExtraInfo> criteria = LambdaQueryCriteria.of(condition)
                .eq(ProductExtraInfo::getProductId)
                .dynamic(true)
                .orderBy(page.getOrders());
        return MybatisHelper.selectDomainObjectListByPage(productExtraInfoMapper, criteria, page);
    }

    @Override
    public int getProductTotalCount() {
        return productExtraInfoMapper.selectAllModelCount();
    }

    @Override
    public void forEachProductExtra(Consumer<ProductExtraInfo> consumer) {
        productExtraInfoMapper.selectAllModelList().forEach(consumer);
    }

    @Override
    public void forEachProductExtra(ObjIntConsumer<ProductExtraInfo> consumer) {
        Cursor<ProductExtraInfo> cursor = productExtraInfoMapper.selectAllModelList();
        int index = 0;
        for (ProductExtraInfo item : cursor) {
            consumer.accept(item, index++);
        }
    }

}