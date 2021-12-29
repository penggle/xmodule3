package com.penglecode.xmodule.samples.domain.service;

import com.penglecode.xmodule.common.domain.Page;
import com.penglecode.xmodule.samples.domain.model.ProductInfo;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

/**
 * 商品信息 领域服务接口
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月25日 下午 23:29
 */
public interface ProductInfoService {

    /**
     * 创建商品信息
     *
     * @param product     - 被保存的领域对象
     */
    void createProduct(ProductInfo product);

    /**
     * 批量创建商品信息
     *
     * @param productList     - 被保存的领域对象集合
     */
    void batchCreateProduct(List<ProductInfo> productList);

    /**
     * 根据ID修改商品信息
     *
     * @param product     - 被保存的领域对象(id字段必须有值)
     */
    void modifyProductById(ProductInfo product);

    /**
     * 根据ID批量修改商品信息
     *
     * @param productList     - 被保存的领域对象集合(id字段必须有值)
     */
    void batchModifyProductById(List<ProductInfo> productList);

    /**
     * 根据ID删除商品信息
     *
     * @param id    - ID主键
     */
    void removeProductById(Long id);

    /**
     * 根据多个ID删除商品信息
     *
     * @param ids    - ID主键列表
     */
    void removeProductByIds(List<Long> ids);

    /**
     * 根据ID获取商品信息
     *
     * @param id    - ID主键
     * @return 返回完整的领域对象信息
     */
    ProductInfo getProductById(Long id);

    /**
     * 根据多个ID获取商品信息
     *
     * @param ids   - ID主键列表
     * @return 返回完整的领域对象信息
     */
    List<ProductInfo> getProductListByIds(List<Long> ids);

    /**
     * 根据条件查询商品信息列表(排序、分页)
     *
     * @param condition		- 查询条件
     * @param page			- 分页/排序参数
     * @return 返回完整的领域对象列表
     */
    List<ProductInfo> getProductListByPage(ProductInfo condition, Page page);

    /**
     * 基于Mybatis游标操作，遍历所有商品信息
     * (在数据量大的情况下，避免一次加载出所有数据而引起内存溢出)
     * 典型示例1、(数据量不大的情况下一次获取所有元素)：
     *      List<MyModel> allModels = new ArrayList<>(); //承接所有元素的集合
     *      myModelService.forEachProduct(allModels::add); //加载所有元素进入集合
     *
     * 典型示例2、(数据量大的情况需要逐步迭代处理每个元素)：
     *      myModelService.forEachProduct(System.out::println); //逐步迭代处理每个元素
     *
     * @param consumer  - 遍历元素的Consumer
     */
    void forEachProduct(Consumer<ProductInfo> consumer);

    /**
     * 基于Mybatis游标操作，遍历所有商品信息
     * (在数据量大的情况下，避免一次加载出所有数据而引起内存溢出)
     *
     * 典型示例、(数据量大的情况需要逐步迭代处理每个元素)：
     *      myModelService.forEachProduct((item, index) -> {
     *          System.out.println("index = " + index + ", item = " + item);
     *      }); //逐步迭代处理每个元素
     *
     * @param consumer  - 遍历元素的Consumer
     */
    void forEachProduct(ObjIntConsumer<ProductInfo> consumer);

}