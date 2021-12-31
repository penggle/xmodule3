package com.penglecode.xmodule.samples.product.domain.service;

import com.penglecode.xmodule.common.domain.ID;
import com.penglecode.xmodule.common.domain.Page;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleStock;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

/**
 * 商品销售库存信息 领域服务接口
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @since 2021年10月25日 下午 23:29
 */
public interface ProductSaleStockService {

    /**
     * 创建商品销售库存信息
     *
     * @param productSaleStock     - 被保存的领域对象
     */
    void createProductSaleStock(ProductSaleStock productSaleStock);

    /**
     * 批量创建商品销售库存信息
     *
     * @param productSaleStocks     - 被保存的领域对象集合
     */
    void batchCreateProductSaleStock(List<ProductSaleStock> productSaleStocks);

    /**
     * 根据ID修改商品销售库存信息
     *
     * @param productSaleStock     - 被保存的领域对象(id字段必须有值)
     */
    void modifyProductSaleStockById(ProductSaleStock productSaleStock);

    /**
     * 根据ID批量修改商品销售库存信息
     *
     * @param productSaleStocks     - 被保存的领域对象集合(id字段必须有值)
     */
    void batchModifyProductSaleStockById(List<ProductSaleStock> productSaleStocks);

    /**
     * 根据ID删除商品销售库存信息
     *
     * @param id    - ID主键
     */
    void removeProductSaleStockById(ID id);

    /**
     * 根据多个ID删除商品销售库存信息
     *
     * @param ids    - ID主键列表
     */
    void removeProductSaleStockByIds(List<ID> ids);

    /**
     * 根据商品信息ID删除商品销售库存信息列表
     *
     * @param productId   - 商品信息ID
     */
    void removeProductSaleStocksByProductId(Long productId);

    /**
     * 根据ID获取商品销售库存信息
     *
     * @param id    - ID主键
     * @return 返回完整的领域对象信息
     */
    ProductSaleStock getProductSaleStockById(ID id);

    /**
     * 根据多个ID获取商品销售库存信息
     *
     * @param ids   - ID主键列表
     * @return 返回完整的领域对象信息
     */
    List<ProductSaleStock> getProductSaleStocksByIds(List<ID> ids);

    /**
     * 根据商品信息ID获取商品销售库存信息列表
     *
     * @param productId   - 商品信息ID
     * @return 返回完整的领域对象信息
     */
    List<ProductSaleStock> getProductSaleStocksByProductId(Long productId);

    /**
     * 根据多个商品信息ID获取商品销售库存信息列表
     *
     * @param productIds   - 商品信息ID列表
     * @return 返回完整的领域对象信息
     */
    Map<Long,List<ProductSaleStock>> getProductSaleStocksByProductIds(List<Long> productIds);

    /**
     * 根据条件查询商品销售库存信息列表(排序、分页)
     *
     * @param condition		- 查询条件
     * @param page			- 分页/排序参数
     * @return 返回完整的领域对象列表
     */
    List<ProductSaleStock> getProductSaleStocksByPage(ProductSaleStock condition, Page page);

    /**
     * 基于Mybatis游标操作，遍历所有商品销售库存信息
     * (在数据量大的情况下，避免一次加载出所有数据而引起内存溢出)
     * 典型示例1、(数据量不大的情况下一次获取所有元素)：
     *      List<MyModel> allModels = new ArrayList<>(); //承接所有元素的集合
     *      myModelService.forEachProductSaleStock(allModels::add); //加载所有元素进入集合
     *
     * 典型示例2、(数据量大的情况需要逐步迭代处理每个元素)：
     *      myModelService.forEachProductSaleStock(System.out::println); //逐步迭代处理每个元素
     *
     * @param consumer  - 遍历元素的Consumer
     */
    void forEachProductSaleStock(Consumer<ProductSaleStock> consumer);

    /**
     * 基于Mybatis游标操作，遍历所有商品销售库存信息
     * (在数据量大的情况下，避免一次加载出所有数据而引起内存溢出)
     *
     * 典型示例、(数据量大的情况需要逐步迭代处理每个元素)：
     *      myModelService.forEachProductSaleStock((item, index) -> {
     *          System.out.println("index = " + index + ", item = " + item);
     *      }); //逐步迭代处理每个元素
     *
     * @param consumer  - 遍历元素的Consumer
     */
    void forEachProductSaleStock(ObjIntConsumer<ProductSaleStock> consumer);

}