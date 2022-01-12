package com.penglecode.xmodule.samples.product.api.service;

import com.penglecode.xmodule.common.dto.PageResult;
import com.penglecode.xmodule.common.dto.Result;
import com.penglecode.xmodule.samples.product.api.request.CreateProductRequest;
import com.penglecode.xmodule.samples.product.api.request.ModifyProductRequest;
import com.penglecode.xmodule.samples.product.api.request.QueryProductRequest;
import com.penglecode.xmodule.samples.product.api.request.QueryProductResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品API接口服务
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/4/7 16:15
 */
@RequestMapping("/api/product")
public interface ProductApiService {

    /**
     * 创建商品
     *
     * @param createRequest - 请求参数
     * @return
     */
    @PostMapping(value="/create", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    Result<Long> createProduct(@RequestBody CreateProductRequest createRequest);

    /**
     * 修改商品
     *
     * @param modifyRequest - 请求参数
     * @return
     */
    @PutMapping(value="/modify", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    Result<Void> modifyProduct(@RequestBody ModifyProductRequest modifyRequest);

    /**
     * 根据商品ID删除商品
     *
     * @param id    - 商品ID
     * @return
     */
    @DeleteMapping(value="/remove/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    Result<Void> removeProductById(@PathVariable("id") Long id);

    /**
     * 根据多个商品ID删除商品
     *
     * @param ids    - 商品ID
     * @return
     */
    @PostMapping(value="/remove/batch", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    Result<Void> removeProductByIds(@RequestBody List<Long> ids);

    /**
     * 根据商品ID获取商品
     *
     * @param id        - 商品ID
     * @param cascade   - 是否级联带出其他信息
     * @return
     */
    @GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    Result<QueryProductResponse> getProductById(@PathVariable("id") Long id, @RequestParam(name="cascade", defaultValue="false") Boolean cascade);

    /**
     * 根据条件分页查询商品
     *
     * @param queryRequest  - 查询参数
     * @param cascade       - 是否级联带出其他信息
     * @return
     */
    @GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
    PageResult<QueryProductResponse> getProductsByPage(QueryProductRequest queryRequest, @RequestParam(name="cascade", defaultValue="false") Boolean cascade);

}
