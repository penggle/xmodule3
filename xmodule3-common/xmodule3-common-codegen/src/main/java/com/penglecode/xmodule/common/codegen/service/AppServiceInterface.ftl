package ${targetPackage};

<#if (projectImports?size > 0 || thirdImports?size > 0)>
<#list projectImports as ipm>
import ${ipm};
</#list>
<#list thirdImports as ipm>
import ${ipm};
</#list>

</#if>
<#if (jdkImports?size > 0)>
<#list jdkImports as ipm>
import ${ipm};
</#list>

</#if>
/**
 * ${aggregateObjectTitle} 应用服务接口
 *
 * @author ${commentAuthor}
 * @version 1.0
 * @since ${generateTime}
 */
public interface ${targetObjectName} {

    /**
     * 创建${aggregateObjectTitle}(默认会级联保存关联的领域对象)
     *
     * @param ${aggregateObjectVarName}     - 被保存的聚合对象
     */
    void create${aggregateObjectAlias}(${aggregateObjectName} ${aggregateObjectVarName});

    /**
     * 根据ID修改${aggregateObjectTitle}(默认会级联保存关联的领域对象)
     *
     * @param ${aggregateObjectVarName}     - 被保存的聚合对象(id字段必须有值)
     */
    void modify${aggregateObjectAlias}ById(${aggregateObjectName} ${aggregateObjectVarName});

    /**
     * 根据ID删除${aggregateObjectTitle}(默认会级联删除关联的领域对象)
     *
     * @param id    - ID主键
     */
    void remove${aggregateObjectAlias}ById(${aggregateObjectIdType} id);

    /**
     * 根据ID获取${aggregateObjectTitle}
     *
     * @param id            - ID主键
     * @param cascade       - 是否级联加载关联领域对象
     * @return 返回完整的领域对象信息
     */
    ${aggregateObjectName} get${aggregateObjectAlias}ById(${aggregateObjectIdType} id, boolean cascade);

    /**
     * 根据多个ID获取${aggregateObjectTitle}
     *
     * @param ids           - ID主键列表
     * @param cascade       - 是否级联加载关联领域对象
     * @return 返回完整的领域对象信息
     */
    List<${aggregateObjectName}> get${aggregateObjectAlias}ListByIds(List<${aggregateObjectIdType}> ids, boolean cascade);

    /**
     * 根据条件查询${aggregateObjectTitle}列表(排序、分页)
     *
     * @param condition		- 查询条件
     * @param page			- 分页/排序参数
     * @param cascade       - 是否级联加载关联领域对象
     * @return 返回完整的领域对象列表
     */
    List<${aggregateObjectName}> get${aggregateObjectAlias}ListByPage(${aggregateObjectName} condition, Page page, boolean cascade);

    /**
     * 基于Mybatis游标操作，遍历所有${aggregateObjectTitle}
     * (在数据量大的情况下，避免一次加载出所有数据而引起内存溢出)
     * 典型示例1、(数据量不大的情况下一次获取所有元素)：
     *      List<MyModel> allModels = new ArrayList<>(); //承接所有元素的集合
     *      myModelService.forEach${aggregateObjectAlias}(allModels::add); //加载所有元素进入集合
     *
     * 典型示例2、(数据量大的情况需要逐步迭代处理每个元素)：
     *      myModelService.forEach${aggregateObjectAlias}(System.out::println); //逐步迭代处理每个元素
     *
     * @param consumer  - 遍历元素的Consumer
     */
    void forEach${aggregateObjectAlias}(Consumer<${aggregateObjectName}> consumer);

    /**
     * 基于Mybatis游标操作，遍历所有${domainObjectTitle}
     * (在数据量大的情况下，避免一次加载出所有数据而引起内存溢出)
     *
     * 典型示例、(数据量大的情况需要逐步迭代处理每个元素)：
     *      myModelService.forEach${domainObjectAlias}((item, index) -> {
     *          System.out.println("index = " + index + ", item = " + item);
     *      }); //逐步迭代处理每个元素
     *
     * @param consumer  - 遍历元素的Consumer
     */
    void forEach${aggregateObjectAlias}(ObjIntConsumer<${aggregateObjectName}> consumer);

}