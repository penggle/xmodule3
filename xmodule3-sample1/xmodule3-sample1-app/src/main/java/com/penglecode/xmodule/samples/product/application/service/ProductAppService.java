package com.penglecode.xmodule.samples.product.application.service;

import com.penglecode.xmodule.common.domain.Page;
import com.penglecode.xmodule.samples.product.domain.model.ProductAggregate;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

/**
 * 商品 应用服务接口
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月23日 上午 10:38
 */
public interface ProductAppService {

    /**
     * 创建商品(默认会级联保存关联的领域对象)
     *
     * @param product     - 被保存的领域对象
     */
    void createProduct(ProductAggregate product);

    /**
     * 根据ID修改商品(默认会级联保存关联的领域对象)
     *
     * @param product     - 被保存的领域对象(id字段必须有值)
     */
    void modifyProductById(ProductAggregate product);

    /**
     * 根据ID删除商品(默认会级联删除关联的领域对象)
     *
     * @param id    - ID主键
     */
    void removeProductById(Long id);

    /**
     * 根据多个ID删除商品
     *
     * @param ids    - ID主键列表
     */
    void removeProductByIds(List<Long> ids);

    /**
     * 根据ID获取商品
     *
     * @param id            - ID主键
     * @param cascade       - 是否级联加载关联领域对象
     * @return 返回完整的领域对象信息
     */
    ProductAggregate getProductById(Long id, boolean cascade);

    /**
     * 根据多个ID获取商品
     *
     * @param ids           - ID主键列表
     * @param cascade       - 是否级联加载关联领域对象
     * @return 返回完整的领域对象信息
     */
    List<ProductAggregate> getProductsByIds(List<Long> ids, boolean cascade);

    /**
     * 根据条件查询商品列表(排序、分页)
     *
     * @param condition		- 查询条件
     * @param page			- 分页/排序参数
     * @param cascade       - 是否级联加载关联领域对象
     * @return 返回完整的领域对象列表
     */
    List<ProductAggregate> getProductsByPage(ProductAggregate condition, Page page, boolean cascade);

    /**
     * 获取商品总记录数
     *
     * @return 返回商品总记录数
     */
    int getProductTotalCount();

    /**
     * 基于Mybatis游标操作，遍历所有商品
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
    void forEachProduct(Consumer<ProductAggregate> consumer);

    /**
     * 基于Mybatis游标操作，遍历所有商品
     * (在数据量大的情况下，避免一次加载出所有数据而引起内存溢出)
     *
     * 典型示例、(数据量大的情况需要逐步迭代处理每个元素)：
     *      myModelService.forEachProduct((item, index) -> {
     *          System.out.println("index = " + index + ", item = " + item);
     *      }); //逐步迭代处理每个元素
     *
     * @param consumer  - 遍历元素的Consumer
     */
    void forEachProduct(ObjIntConsumer<ProductAggregate> consumer);

}