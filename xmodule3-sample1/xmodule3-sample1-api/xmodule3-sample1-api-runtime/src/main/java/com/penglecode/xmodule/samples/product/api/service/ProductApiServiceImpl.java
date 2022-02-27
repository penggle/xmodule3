package com.penglecode.xmodule.samples.product.api.service;

import com.penglecode.xmodule.common.model.Page;
import com.penglecode.xmodule.common.model.PageResult;
import com.penglecode.xmodule.common.model.Result;
import com.penglecode.xmodule.common.support.BeanCopier;
import com.penglecode.xmodule.common.web.servlet.support.ServletHttpApiSupport;
import com.penglecode.xmodule.samples.product.api.request.QueryProductRequest;
import com.penglecode.xmodule.samples.product.api.request.SaveProductRequest;
import com.penglecode.xmodule.samples.product.api.response.QueryProductResponse;
import com.penglecode.xmodule.samples.product.application.service.ProductAppService;
import com.penglecode.xmodule.samples.product.domain.model.ProductAggregate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品API接口(实现)
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/4/10 21:32
 */
@RestController
public class ProductApiServiceImpl extends ServletHttpApiSupport implements ProductApiService {

    @Resource(name="productAppService")
    private ProductAppService productAppService;

    @Override
    public Result<Long> createProduct(SaveProductRequest createRequest) {
        ProductAggregate product = BeanCopier.copy(createRequest, ProductAggregate::new);
        productAppService.createProduct(product);
        return Result.success().data(product.getProductId()).build();
    }

    @Override
    public Result<Void> modifyProduct(SaveProductRequest modifyRequest) {
        ProductAggregate product = BeanCopier.copy(modifyRequest, ProductAggregate::new);
        productAppService.modifyProductById(product);
        return Result.success().build();
    }

    @Override
    public Result<Void> removeProductById(Long id) {
        productAppService.removeProductById(id);
        return Result.success().build();
    }

    @Override
    public Result<Void> removeProductByIds(List<Long> ids) {
        productAppService.removeProductByIds(ids);
        return Result.success().build();
    }

    @Override
    public Result<QueryProductResponse> getProductById(Long id, Boolean cascade) {
        ProductAggregate product = productAppService.getProductById(id, cascade);
        QueryProductResponse queryResponse = BeanCopier.copy(product, QueryProductResponse::new);
        return Result.success().data(queryResponse).build();
    }

    @Override
    public Result<List<QueryProductResponse>> getProductByIds(List<Long> ids, Boolean cascade) {
        List<ProductAggregate> products = productAppService.getProductsByIds(ids, cascade);
        List<QueryProductResponse> queryResponses = BeanCopier.copy(products, QueryProductResponse::new);
        return Result.success().data(queryResponses).build();
    }

    @Override
    public PageResult<List<QueryProductResponse>> getProductsByPage(QueryProductRequest queryRequest, Boolean cascade) {
        ProductAggregate condition = BeanCopier.copy(queryRequest, ProductAggregate::new);
        Page page = Page.copyOf(queryRequest);
        List<ProductAggregate> products = productAppService.getProductsByPage(condition, page, cascade);
        List<QueryProductResponse> queryResponses = BeanCopier.copy(products, QueryProductResponse::new);
        return PageResult.success().data(queryResponses).totalRowCount(page.getTotalRowCount()).build();
    }

}
