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
 * ${domainObjectTitle} 领域服务接口
 *
 * @author ${commentAuthor}
 * @version 1.0
 * @since ${generateTime}
 */
public interface ${targetObjectName} {

    /**
     * 创建${domainObjectTitle}
     *
     * @param ${domainObjectVarName}     - 被保存的领域对象
     */
    void create${domainObjectAlias}(${domainObjectName} ${domainObjectVarName});

    /**
     * 批量创建${domainObjectTitle}
     *
     * @param ${domainObjectVarName}List     - 被保存的领域对象集合
     */
    void batchCreate${domainObjectAlias}(List<${domainObjectName}> ${domainObjectVarName}List);

    /**
     * 根据ID修改${domainObjectTitle}
     *
     * @param ${domainObjectVarName}     - 被保存的领域对象(id字段必须有值)
     */
    void modify${domainObjectAlias}ById(${domainObjectName} ${domainObjectVarName});

    /**
     * 根据ID批量修改${domainObjectTitle}
     *
     * @param ${domainObjectVarName}List     - 被保存的领域对象集合(id字段必须有值)
     */
    void batchModify${domainObjectAlias}ById(List<${domainObjectName}> ${domainObjectVarName}List);

    /**
     * 根据ID删除${domainObjectTitle}
     *
     * @param id    - ID主键
     */
    void remove${domainObjectAlias}ById(${domainObjectIdType} id);

    /**
     * 根据多个ID删除${domainObjectTitle}
     *
     * @param ids    - ID主键列表
     */
    void remove${domainObjectAlias}ByIds(List<${domainObjectIdType}> ids);

<#if removeSlaveByMasterIdParameter??>
    /**
     * 根据${removeSlaveByMasterIdParameter["masterDomainObjectTitle"]}ID删除${removeSlaveByMasterIdParameter["slaveDomainObjectTitle"]}
     *
     * @param ${removeSlaveByMasterIdParameter["masterDomainObjectId"]}   - ${removeSlaveByMasterIdParameter["masterDomainObjectTitle"]}ID
     */
    void remove${removeSlaveByMasterIdParameter["slaveDomainObjectAlias"]}By${removeSlaveByMasterIdParameter["upperMasterDomainObjectId"]}(${removeSlaveByMasterIdParameter["masterDomainObjectIdType"]} ${removeSlaveByMasterIdParameter["masterDomainObjectId"]});

</#if>
    /**
     * 根据ID获取${domainObjectTitle}
     *
     * @param id    - ID主键
     * @return 返回完整的领域对象信息
     */
    ${domainObjectName} get${domainObjectAlias}ById(${domainObjectIdType} id);

    /**
     * 根据多个ID获取${domainObjectTitle}
     *
     * @param ids   - ID主键列表
     * @return 返回完整的领域对象信息
     */
    List<${domainObjectName}> get${domainObjectAlias}ListByIds(List<${domainObjectIdType}> ids);

<#if getSlaveByMasterIdParameter??>
    /**
     * 根据${getSlaveByMasterIdParameter["masterDomainObjectTitle"]}ID获取${getSlaveByMasterIdParameter["slaveDomainObjectTitle"]}
     *
     * @param ${getSlaveByMasterIdParameter["masterDomainObjectId"]}   - ${getSlaveByMasterIdParameter["masterDomainObjectTitle"]}ID
     * @return 返回完整的领域对象信息
     */
    ${getSlaveByMasterIdParameter["serviceMethodReturnType"]} get${getSlaveByMasterIdParameter["slaveDomainObjectAlias"]}By${getSlaveByMasterIdParameter["upperMasterDomainObjectId"]}(${getSlaveByMasterIdParameter["masterDomainObjectIdType"]} ${getSlaveByMasterIdParameter["masterDomainObjectId"]});

</#if>
<#if getSlaveByMasterIdsParameter??>
    /**
     * 根据多个${getSlaveByMasterIdsParameter["masterDomainObjectTitle"]}ID获取${getSlaveByMasterIdsParameter["slaveDomainObjectTitle"]}列表
     *
     * @param ${getSlaveByMasterIdsParameter["masterDomainObjectIds"]}   - ${getSlaveByMasterIdsParameter["masterDomainObjectTitle"]}ID列表
     * @return 返回完整的领域对象信息
     */
    ${getSlaveByMasterIdsParameter["serviceMethodReturnType"]} get${getSlaveByMasterIdsParameter["slaveDomainObjectsAlias"]}By${getSlaveByMasterIdsParameter["upperMasterDomainObjectIds"]}(${getSlaveByMasterIdsParameter["masterDomainObjectIdsType"]} ${getSlaveByMasterIdsParameter["masterDomainObjectIds"]});

</#if>
    /**
     * 根据条件查询${domainObjectTitle}列表(排序、分页)
     *
     * @param condition		- 查询条件
     * @param page			- 分页/排序参数
     * @return 返回完整的领域对象列表
     */
    List<${domainObjectName}> get${domainObjectAlias}ListByPage(${domainObjectName} condition, Page page);

    /**
     * 基于Mybatis游标操作，遍历所有${domainObjectTitle}
     * (在数据量大的情况下，避免一次加载出所有数据而引起内存溢出)
     * 典型示例1、(数据量不大的情况下一次获取所有元素)：
     *      List<MyModel> allModels = new ArrayList<>(); //承接所有元素的集合
     *      myModelService.forEach${domainObjectAlias}(allModels::add); //加载所有元素进入集合
     *
     * 典型示例2、(数据量大的情况需要逐步迭代处理每个元素)：
     *      myModelService.forEach${domainObjectAlias}(System.out::println); //逐步迭代处理每个元素
     *
     * @param consumer  - 遍历元素的Consumer
     */
    void forEach${domainObjectAlias}(Consumer<${domainObjectName}> consumer);

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
    void forEach${domainObjectAlias}(ObjIntConsumer<${domainObjectName}> consumer);

}