package com.penglecode.xmodule.samples.product.api.service;

import com.penglecode.xmodule.common.domain.Page;
import com.penglecode.xmodule.common.dto.PageResult;
import com.penglecode.xmodule.common.dto.Result;
import com.penglecode.xmodule.common.support.BeanMapper;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.samples.product.api.request.CreateProductRequest;
import com.penglecode.xmodule.samples.product.api.request.ModifyProductRequest;
import com.penglecode.xmodule.samples.product.api.request.QueryProductRequest;
import com.penglecode.xmodule.samples.product.api.request.QueryProductResponse;
import com.penglecode.xmodule.samples.product.application.service.ProductAppService;
import com.penglecode.xmodule.samples.product.domain.model.ProductAggregate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品API接口服务(实现)
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
        ProductAggregate product = BeanMapper.map(createRequest, ProductAggregate::new);
        productAppService.createProduct(product);
        return Result.success().data(product.getProductId()).build();
    }

    @Override
    public Result<Void> modifyProduct(ModifyProductRequest modifyRequest) {
        ProductAggregate product = BeanMapper.map(modifyRequest, ProductAggregate::new);
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
        System.out.println(JsonUtils.object2Json(product));
        QueryProductResponse queryResponse = BeanMapper.map(product, QueryProductResponse::new);
        return Result.success().data(queryResponse).build();
    }

    @Override
    public PageResult<QueryProductResponse> getProductsByPage(QueryProductRequest queryRequest, Boolean cascade) {
        ProductAggregate condition = BeanMapper.map(queryRequest, ProductAggregate::new);
        Page page = BeanMapper.map(queryRequest, Page.ofDefault());
        List<ProductAggregate> productList = productAppService.getProductsByPage(condition, page, cascade);
        List<QueryProductResponse> queryResponses = BeanMapper.map(productList, QueryProductResponse::new);
        return PageResult.success().data(queryResponses).totalRowCount(page.getTotalRowCount()).build();
    }

}
