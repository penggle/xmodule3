package com.penglecode.xmodule.samples.product.service.impl;

import com.penglecode.xmodule.common.domain.Page;
import com.penglecode.xmodule.common.support.*;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.samples.product.domain.model.ProductAggregate;
import com.penglecode.xmodule.samples.product.domain.model.ProductBaseInfo;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleSpec;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleStock;
import com.penglecode.xmodule.samples.product.domain.service.ProductBaseInfoService;
import com.penglecode.xmodule.samples.product.domain.service.ProductExtraInfoService;
import com.penglecode.xmodule.samples.product.domain.service.ProductSaleSpecService;
import com.penglecode.xmodule.samples.product.domain.service.ProductSaleStockService;
import com.penglecode.xmodule.samples.product.service.ProductApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/10/23 14:42
 */
@Service("productApplicationService")
public class ProductApplicationServiceImpl implements ProductApplicationService {

    @Resource(name="productBaseInfoService")
    private ProductBaseInfoService productBaseInfoService;

    @Resource(name="productExtraInfoService")
    private ProductExtraInfoService productExtraInfoService;

    @Resource(name="productSaleSpecService")
    private ProductSaleSpecService productSaleSpecService;

    @Resource(name="productSaleStockService")
    private ProductSaleStockService productSaleStockService;

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void createProduct(ProductAggregate product) {
        ValidationAssert.notNull(product, MessageSupplier.ofRequiredParameter("product"));
        BeanValidator.validateBean(product, ProductAggregate::getProductExtraInfo, ProductAggregate::getProductSaleSpecs, ProductAggregate::getProductSaleStocks);
        productBaseInfoService.createProductBase(product);
        productExtraInfoService.createProductExtra(product.getProductExtraInfo());
        productSaleSpecService.batchCreateProductSaleSpec(product.getProductSaleSpecs());
        productSaleStockService.batchCreateProductSaleStock(product.getProductSaleStocks());
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void modifyProductById(ProductAggregate product) {
        ValidationAssert.notNull(product, MessageSupplier.ofRequiredParameter("product"));
        BeanValidator.validateBean(product, ProductAggregate::getProductExtraInfo, ProductAggregate::getProductSaleSpecs, ProductAggregate::getProductSaleStocks);
        productBaseInfoService.modifyProductBaseById(product);
        productExtraInfoService.modifyProductExtraById(product.getProductExtraInfo());
        List<ProductSaleSpec> persistedProductSaleSpecs = productSaleSpecService.getProductSaleSpecsByProductId(product.getProductId());
        ApplicationServiceHelper.batchSaveEntityObjects(product.getProductSaleSpecs(), persistedProductSaleSpecs, ProductSaleSpec::identity, productSaleSpecService::batchCreateProductSaleSpec, productSaleSpecService::batchModifyProductSaleSpecById, productSaleSpecService::removeProductSaleSpecByIds);
        List<ProductSaleStock> persistedProductSaleStocks = productSaleStockService.getProductSaleStocksByProductId(product.getProductId());
        ApplicationServiceHelper.batchSaveEntityObjects(product.getProductSaleStocks(), persistedProductSaleStocks, ProductSaleStock::identity, productSaleStockService::batchCreateProductSaleStock, productSaleStockService::batchModifyProductSaleStockById, productSaleStockService::removeProductSaleStockByIds);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void removeProductById(Long id) {
        productBaseInfoService.removeProductBaseById(id);
        productExtraInfoService.removeProductExtraById(id);
        productSaleSpecService.removeProductSaleSpecsByProductId(id);
        productSaleStockService.removeProductSaleStocksByProductId(id);
    }

    @Override
    public ProductAggregate getProductById(Long id, boolean cascade) {
        ProductBaseInfo productInfo = productBaseInfoService.getProductBaseById(id);
        if(productInfo != null) {
            ProductAggregate product = BeanMapper.map(productInfo, ProductAggregate::new);
            if(cascade) {
                product.setProductExtraInfo(productExtraInfoService.getProductExtraById(id));
                product.setProductSaleSpecs(productSaleSpecService.getProductSaleSpecsByProductId(id));
                product.setProductSaleStocks(productSaleStockService.getProductSaleStocksByProductId(id));
            }
            return product;
        }
        return null;
    }

    @Override
    public List<ProductAggregate> getProductListByIds(List<Long> ids, boolean cascade) {
        List<ProductBaseInfo> productBases = productBaseInfoService.getProductBasesByIds(ids);
        return prepareProductList(productBases, cascade);
    }

    @Override
    public List<ProductAggregate> getProductListByPage(ProductAggregate condition, Page page, boolean cascade) {
        List<ProductBaseInfo> productBases = productBaseInfoService.getProductBasesByPage(condition, page);
        return prepareProductList(productBases, cascade);
    }

    protected List<ProductAggregate> prepareProductList(List<ProductBaseInfo> productBases, boolean cascade) {
        if(!CollectionUtils.isEmpty(productBases)) {
            List<ProductAggregate> products = BeanMapper.map(productBases, ProductAggregate::new);
            if(cascade) {
                List<Long> productIds = productBases.stream().map(ProductBaseInfo::getProductId).collect(Collectors.toList());
                Map<Long,List<ProductSaleSpec>> productSaleSpecs = productSaleSpecService.getProductSaleSpecsByProductIds(productIds);
                Map<Long,List<ProductSaleStock>> productSaleStocks = productSaleStockService.getProductSaleStocksByProductIds(productIds);
                for(ProductAggregate product : products) {
                    product.setProductSaleSpecs(productSaleSpecs.get(product.getProductId()));
                    product.setProductSaleStocks(productSaleStocks.get(product.getProductId()));
                }
            }
            return products;
        }
        return Collections.emptyList();
    }

    @Override
    public void forEachProduct(Consumer<ProductAggregate> consumer) {
        productBaseInfoService.forEachProductBase(productInfo -> consumer.accept(BeanMapper.map(productInfo, ProductAggregate::new)));
    }

    @Override
    public void forEachProduct(ObjIntConsumer<ProductAggregate> consumer) {
        productBaseInfoService.forEachProductBase((productInfo, index) -> consumer.accept(BeanMapper.map(productInfo, ProductAggregate::new), index));
    }

}
