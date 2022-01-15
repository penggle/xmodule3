package com.penglecode.xmodule.samples.service;

import com.penglecode.xmodule.common.model.Page;
import com.penglecode.xmodule.common.support.*;
import com.penglecode.xmodule.common.util.AppServiceUtils;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.samples.domain.model.Product;
import com.penglecode.xmodule.samples.domain.model.ProductInfo;
import com.penglecode.xmodule.samples.domain.model.ProductSpec;
import com.penglecode.xmodule.samples.domain.model.ProductStock;
import com.penglecode.xmodule.samples.domain.service.ProductInfoService;
import com.penglecode.xmodule.samples.domain.service.ProductSpecService;
import com.penglecode.xmodule.samples.domain.service.ProductStockService;
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
@Service("productAppService")
public class ProductAppServiceImpl implements ProductAppService {

    @Resource(name="productInfoService")
    private ProductInfoService productInfoService;

    @Resource(name="productSpecService")
    private ProductSpecService productSpecService;

    @Resource(name="productStockService")
    private ProductStockService productStockService;

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void createProduct(Product product) {
        ValidationAssert.notNull(product, MessageSupplier.of("message.parameter.required", "product"));
        BeanValidator.validateBean(product, Product::getProductSpecList, Product::getProductStockList);
        productInfoService.createProduct(product);
        productSpecService.batchCreateProductSpec(product.getProductSpecList());
        productStockService.batchCreateProductStock(product.getProductStockList());
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void modifyProductById(Product product) {
        ValidationAssert.notNull(product, MessageSupplier.of("message.parameter.required", "product"));
        BeanValidator.validateBean(product, Product::getProductSpecList, Product::getProductStockList);
        productInfoService.modifyProductById(product);
        List<ProductSpec> persistentProductSpecList = productSpecService.getProductSpecListByProductId(product.getProductId());
        AppServiceUtils.batchMergeEntityObjects(product.getProductSpecList(), persistentProductSpecList, ProductSpec::identity, productSpecService::batchCreateProductSpec, productSpecService::batchModifyProductSpecById, productSpecService::removeProductSpecByIds);
        List<ProductStock> persistentProductStockList = productStockService.getProductStockListByProductId(product.getProductId());
        AppServiceUtils.batchMergeEntityObjects(product.getProductStockList(), persistentProductStockList, ProductStock::identity, productStockService::batchCreateProductStock, productStockService::batchModifyProductStockById, productStockService::removeProductStockByIds);
    }

    @Override
    @Transactional(transactionManager="productTransactionManager", rollbackFor=Exception.class)
    public void removeProductById(Long id) {
        productInfoService.removeProductById(id);
        productSpecService.removeProductSpecListByProductId(id);
        productStockService.removeProductStockListByProductId(id);
    }

    @Override
    public Product getProductById(Long id, boolean cascade) {
        ProductInfo productInfo = productInfoService.getProductById(id);
        if(productInfo != null) {
            Product product = BeanMapper.map(productInfo, Product::new);
            if(cascade) {
                product.setProductSpecList(productSpecService.getProductSpecListByProductId(id));
                product.setProductStockList(productStockService.getProductStockListByProductId(id));
            }
            return product;
        }
        return null;
    }

    @Override
    public List<Product> getProductListByIds(List<Long> ids, boolean cascade) {
        List<ProductInfo> productInfoList = productInfoService.getProductListByIds(ids);
        return prepareProductList(productInfoList, cascade);
    }

    @Override
    public List<Product> getProductListByPage(Product condition, Page page, boolean cascade) {
        List<ProductInfo> productInfoList = productInfoService.getProductListByPage(condition, page);
        return prepareProductList(productInfoList, cascade);
    }

    protected List<Product> prepareProductList(List<ProductInfo> productInfoList, boolean cascade) {
        if(!CollectionUtils.isEmpty(productInfoList)) {
            List<Product> productList = BeanMapper.map(productInfoList, Product::new);
            if(cascade) {
                List<Long> productIds = productInfoList.stream().map(ProductInfo::getProductId).collect(Collectors.toList());
                Map<Long,List<ProductSpec>> productSpecs = productSpecService.getProductSpecListByProductIds(productIds);
                Map<Long,List<ProductStock>> productStocks = productStockService.getProductStockListByProductIds(productIds);
                for(Product product : productList) {
                    product.setProductSpecList(productSpecs.get(product.getProductId()));
                    product.setProductStockList(productStocks.get(product.getProductId()));
                }
            }
            return productList;
        }
        return Collections.emptyList();
    }

    @Override
    public void forEachProduct(Consumer<Product> consumer) {
        productInfoService.forEachProduct(productInfo -> consumer.accept(BeanMapper.map(productInfo, Product::new)));
    }

    @Override
    public void forEachProduct(ObjIntConsumer<Product> consumer) {
        productInfoService.forEachProduct((productInfo, index) -> consumer.accept(BeanMapper.map(productInfo, Product::new), index));
    }

}
