package com.penglecode.xmodule.samples.product.api.service;

import com.penglecode.xmodule.common.model.PageResult;
import com.penglecode.xmodule.common.model.Result;
import com.penglecode.xmodule.samples.product.api.request.QueryProductRequest;
import com.penglecode.xmodule.samples.product.api.request.SaveProductRequest;
import com.penglecode.xmodule.samples.product.api.response.QueryProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品API接口服务
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/4/7 16:15
 */
@RequestMapping("/api/product")
@Tag(name="ProductApiService", description="商品API接口")
public interface ProductApiService {

    /**
     * 创建商品
     *
     * @param createRequest - 请求参数
     * @return
     */
    @Operation(summary="创建商品")
    @PostMapping(value="/create", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    Result<Long> createProduct(@RequestBody SaveProductRequest createRequest);

    /**
     * 修改商品
     *
     * @param modifyRequest - 请求参数
     * @return
     */
    @Operation(summary="修改商品")
    @PostMapping(value="/modify", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    Result<Void> modifyProduct(@RequestBody SaveProductRequest modifyRequest);

    /**
     * 根据商品ID删除商品
     *
     * @param id    - 商品ID
     * @return
     */
    @Operation(summary="根据商品ID删除商品")
    @PostMapping(value="/remove/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    Result<Void> removeProductById(@PathVariable("id") Long id);

    /**
     * 根据多个商品ID删除商品
     *
     * @param ids    - 商品ID
     * @return
     */
    @Operation(summary="根据多个商品ID删除商品")
    @PostMapping(value="/remove/ids", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    Result<Void> removeProductByIds(@RequestBody List<Long> ids);

    /**
     * 根据商品ID获取商品
     *
     * @param id        - 商品ID
     * @param cascade   - 是否级联带出其他信息
     * @return
     */
    @Operation(summary="根据商品ID获取商品")
    @GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    Result<QueryProductResponse> getProductById(@PathVariable("id") Long id, @RequestParam(name="cascade", defaultValue="false") Boolean cascade);

    /**
     * 根据商品ID获取商品
     *
     * @param ids       - 商品ID
     * @param cascade   - 是否级联带出其他信息
     * @return
     */
    @Operation(summary="根据多个商品ID获取商品")
    @PostMapping(value="/ids", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    Result<List<QueryProductResponse>> getProductByIds(@RequestBody List<Long> ids, @RequestParam(name="cascade", defaultValue="false") Boolean cascade);

    /**
     * 根据条件分页查询商品
     *
     * @param queryRequest  - 查询参数
     * @param cascade       - 是否级联带出其他信息
     * @return
     */
    @Operation(summary="根据条件分页查询商品")
    @PostMapping(value="/list", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    PageResult<List<QueryProductResponse>> getProductsByPage(@RequestBody QueryProductRequest queryRequest, @RequestParam(name="cascade", defaultValue="false") Boolean cascade);

}
