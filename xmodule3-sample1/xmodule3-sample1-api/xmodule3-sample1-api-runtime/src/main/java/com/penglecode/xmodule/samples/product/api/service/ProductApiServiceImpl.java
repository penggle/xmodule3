package com.penglecode.xmodule.samples.product.api.service;

import com.penglecode.xmodule.common.model.Page;
import com.penglecode.xmodule.common.model.PageResult;
import com.penglecode.xmodule.common.model.Result;
import com.penglecode.xmodule.common.support.BeanCopier;
import com.penglecode.xmodule.samples.product.api.request.CreateProductRequest;
import com.penglecode.xmodule.samples.product.api.request.ModifyProductRequest;
import com.penglecode.xmodule.samples.product.api.request.QueryProductRequest;
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
 * @since 2021/4/10 21:32
 */
@RestController
public class ProductApiServiceImpl implements ProductApiService {

    @Resource(name="productAppService")
    private ProductAppService productAppService;

    @Override
    public Result<Long> createProduct(CreateProductRequest createRequest) {
        ProductAggregate product = BeanCopier.copy(createRequest, ProductAggregate::new);
        productAppService.createProduct(product);
        return Result.success().data(product.getProductId()).build();
    }

    @Override
    public Result<Void> modifyProduct(ModifyProductRequest modifyRequest) {
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
    public PageResult<QueryProductResponse> getProductsByPage(QueryProductRequest queryRequest, Boolean cascade) {
        ProductAggregate condition = BeanCopier.copy(queryRequest, ProductAggregate::new);
        Page page = Page.copyOf(queryRequest);
        List<ProductAggregate> productList = productAppService.getProductsByPage(condition, page, cascade);
        List<QueryProductResponse> queryResponses = BeanCopier.copy(productList, QueryProductResponse::new);
        return PageResult.success().data(queryResponses).totalRowCount(page.getTotalRowCount()).build();
    }

}
