package com.penglecode.xmodule.samples.product.api.controller;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品API接口服务
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/4/7 16:15
 */
@RestController
@RequestMapping("/api/product")
@Tag(name="ProductApiService", description="商品API接口")
public class ProductController extends ServletHttpApiSupport {

    @Resource(name="productAppService")
    private ProductAppService productAppService;

    /**
     * 创建商品
     *
     * @param createRequest - 请求参数
     * @return
     */
    @Operation(summary="创建商品")
    @PostMapping(value="/create", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<Long> createProduct(@RequestBody SaveProductRequest createRequest) {
        ProductAggregate product = BeanCopier.copy(createRequest, ProductAggregate::new);
        productAppService.createProduct(product);
        return Result.success().data(product.getProductId()).build();
    }

    /**
     * 修改商品
     *
     * @param modifyRequest - 请求参数
     * @return
     */
    @Operation(summary="修改商品")
    @PostMapping(value="/modify", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<Void> modifyProduct(@RequestBody SaveProductRequest modifyRequest) {
        ProductAggregate product = BeanCopier.copy(modifyRequest, ProductAggregate::new);
        productAppService.modifyProductById(product);
        return Result.success().build();
    }

    /**
     * 根据商品ID删除商品
     *
     * @param id    - 商品ID
     * @return
     */
    @Operation(summary="根据商品ID删除商品")
    @PostMapping(value="/remove/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<Void> removeProductById(@PathVariable("id") Long id) {
        productAppService.removeProductById(id);
        return Result.success().build();
    }

    /**
     * 根据多个商品ID删除商品
     *
     * @param ids    - 商品ID
     * @return
     */
    @Operation(summary="根据多个商品ID删除商品")
    @PostMapping(value="/remove/ids", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<Void> removeProductByIds(@RequestBody List<Long> ids) {
        productAppService.removeProductByIds(ids);
        return Result.success().build();
    }

    /**
     * 根据商品ID获取商品
     *
     * @param id        - 商品ID
     * @param cascade   - 是否级联带出其他信息
     * @return
     */
    @Operation(summary="根据商品ID获取商品")
    @GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<QueryProductResponse> getProductById(@PathVariable("id") Long id, @RequestParam(name="cascade", defaultValue="false") Boolean cascade) {
        ProductAggregate product = productAppService.getProductById(id, cascade);
        QueryProductResponse queryResponse = BeanCopier.copy(product, QueryProductResponse::new);
        return Result.success().data(queryResponse).build();
    }

    /**
     * 根据商品ID获取商品
     *
     * @param ids       - 商品ID
     * @param cascade   - 是否级联带出其他信息
     * @return
     */
    @Operation(summary="根据多个商品ID获取商品")
    @PostMapping(value="/ids", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<List<QueryProductResponse>> getProductByIds(@RequestBody List<Long> ids, @RequestParam(name="cascade", defaultValue="false") Boolean cascade) {
        List<ProductAggregate> productList = productAppService.getProductsByIds(ids, cascade);
        List<QueryProductResponse> queryResponses = BeanCopier.copy(productList, QueryProductResponse::new);
        return Result.success().data(queryResponses).build();
    }

    /**
     * 根据条件分页查询商品
     *
     * @param queryRequest  - 查询参数
     * @param cascade       - 是否级联带出其他信息
     * @return
     */
    @Operation(summary="根据条件分页查询商品")
    @PostMapping(value="/list", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public PageResult<List<QueryProductResponse>> getProductsByPage(@RequestBody QueryProductRequest queryRequest, @RequestParam(name="cascade", defaultValue="false") Boolean cascade) {
        ProductAggregate condition = BeanCopier.copy(queryRequest, ProductAggregate::new);
        Page page = Page.copyOf(queryRequest);
        List<ProductAggregate> productList = productAppService.getProductsByPage(condition, page, cascade);
        List<QueryProductResponse> queryResponses = BeanCopier.copy(productList, QueryProductResponse::new);
        return PageResult.success().data(queryResponses).totalRowCount(page.getTotalRowCount()).build();
    }

}
