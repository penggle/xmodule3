package com.penglecode.xmodule.samples.product.domain.service;

import com.penglecode.xmodule.common.model.Page;
import com.penglecode.xmodule.samples.product.domain.model.ProductExtraInfo;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

/**
 * 商品额外信息 领域服务接口
 *
 * @author AutoCodeGenerator
 * @version 1.0
 * @created 2021年10月25日 下午 23:29
 */
public interface ProductExtraInfoService {

    /**
     * 创建商品额外信息
     *
     * @param productExtra     - 被保存的领域对象
     */
    void createProductExtra(ProductExtraInfo productExtra);

    /**
     * 根据ID修改商品额外信息
     *
     * @param productExtra     - 被保存的领域对象(id字段必须有值)
     */
    void modifyProductExtraById(ProductExtraInfo productExtra);

    /**
     * 根据ID删除商品额外信息
     *
     * @param id    - ID主键
     */
    void removeProductExtraById(Long id);

    /**
     * 根据多个ID删除商品额外信息
     *
     * @param ids    - ID主键列表
     */
    void removeProductExtraByIds(List<Long> ids);

    /**
     * 根据ID获取商品额外信息
     *
     * @param id    - ID主键
     * @return 返回完整的领域对象信息
     */
    ProductExtraInfo getProductExtraById(Long id);

    /**
     * 根据多个ID获取商品额外信息
     *
     * @param ids   - ID主键列表
     * @return 返回完整的领域对象信息
     */
    Map<Long,ProductExtraInfo> getProductExtrasByIds(List<Long> ids);

    /**
     * 根据条件查询商品额外信息列表(排序、分页)
     *
     * @param condition		- 查询条件
     * @param page			- 分页/排序参数
     * @return 返回完整的领域对象列表
     */
    List<ProductExtraInfo> getProductExtrasByPage(ProductExtraInfo condition, Page page);

    /**
     * 获取商品额外信息总记录数
     *
     * @return 返回商品额外信息总记录数
     */
    int getProductTotalCount();

    /**
     * 基于Mybatis游标操作，遍历所有商品额外信息
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
    void forEachProductExtra(Consumer<ProductExtraInfo> consumer);

    /**
     * 基于Mybatis游标操作，遍历所有商品额外信息
     * (在数据量大的情况下，避免一次加载出所有数据而引起内存溢出)
     *
     * 典型示例、(数据量大的情况需要逐步迭代处理每个元素)：
     *      myModelService.forEachProduct((item, index) -> {
     *          System.out.println("index = " + index + ", item = " + item);
     *      }); //逐步迭代处理每个元素
     *
     * @param consumer  - 遍历元素的Consumer
     */
    void forEachProductExtra(ObjIntConsumer<ProductExtraInfo> consumer);

}