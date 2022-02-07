package ${targetPackage};

<#if (targetProjectImports?size > 0 || targetThirdImports?size > 0)>
<#list targetProjectImports as targetImport>
import ${targetImport};
</#list>
<#list targetThirdImports as targetImport>
import ${targetImport};
</#list>

</#if>
<#if (targetJdkImports?size > 0)>
<#list targetJdkImports as targetImport>
import ${targetImport};
</#list>

</#if>
/**
 * ${targetComment}
 *
 * @author ${targetAuthor}
 * @version ${targetVersion}
 * @since ${targetCreated}
 */
public interface ${targetClass} {
<#if createDomainObject["methodActivated"]>

    /**
     * 创建${createDomainObject["domainObjectTitle"]}
     *
     * @param ${createDomainObject["domainObjectVariable"]}     - 被保存的领域对象
     */
    void create${createDomainObject["domainObjectAlias"]}(${createDomainObject["domainObjectName"]} ${createDomainObject["domainObjectVariable"]});
</#if>
<#if batchCreateDomainObjects["methodActivated"]>

    /**
     * 批量创建${batchCreateDomainObjects["domainObjectTitle"]}
     *
     * @param ${batchCreateDomainObjects["domainObjectVariables"]}    - 被保存的领域对象集合
     */
    void batchCreate${batchCreateDomainObjects["domainObjectAliases"]}(List<${batchCreateDomainObjects["domainObjectName"]}> ${batchCreateDomainObjects["domainObjectVariables"]});
</#if>
<#if modifyDomainObjectById["methodActivated"]>

    /**
     * 根据ID修改${modifyDomainObjectById["domainObjectTitle"]}
     *
     * @param ${modifyDomainObjectById["domainObjectVariable"]}     - 被保存的领域对象(其id字段必须有值)
     */
    void modify${modifyDomainObjectById["domainObjectAlias"]}ById(${modifyDomainObjectById["domainObjectName"]} ${modifyDomainObjectById["domainObjectVariable"]});
</#if>
<#if batchModifyDomainObjectsById["methodActivated"]>

    /**
     * 根据ID批量修改${batchModifyDomainObjectsById["domainObjectTitle"]}
     *
     * @param ${batchModifyDomainObjectsById["domainObjectVariables"]}    - 被保存的领域对象集合(其id字段必须有值)
     */
    void batchModify${batchModifyDomainObjectsById["domainObjectAliases"]}ById(List<${batchModifyDomainObjectsById["domainObjectName"]}> ${batchModifyDomainObjectsById["domainObjectVariables"]});
</#if>
<#if removeDomainObjectById["methodActivated"]>

    /**
     * 根据ID删除${removeDomainObjectById["domainObjectTitle"]}
     *
     * @param ${removeDomainObjectById["domainObjectIdName"]}    - ID主键
     */
    void remove${removeDomainObjectById["domainObjectAlias"]}ById(${removeDomainObjectById["domainObjectIdType"]} ${removeDomainObjectById["domainObjectIdName"]});
</#if>
<#if removeDomainObjectsByIds["methodActivated"]>

    /**
     * 根据多个ID删除${removeDomainObjectsByIds["domainObjectTitle"]}
     *
     * @param ${removeDomainObjectsByIds["domainObjectIdsName"]}    - ID主键列表
     */
    void remove${removeDomainObjectsByIds["domainObjectAliases"]}ByIds(List<${removeDomainObjectsByIds["domainObjectIdType"]}> ${removeDomainObjectsByIds["domainObjectIdsName"]});
</#if>
<#if removeDomainObjectsByXxxMasterId["methodActivated"]>
<#list removeDomainObjectsByXxxMasterId["removeByMasterIdMethods"] as removeDomainObjectsByMasterId>
<#if removeDomainObjectsByMasterId["methodActivated"]>

    /**
     * 根据${removeDomainObjectsByMasterId["masterDomainObjectTitle"]}ID删除${removeDomainObjectsByMasterId["domainObjectTitle"]}
     *
     * @param ${removeDomainObjectsByMasterId["masterDomainObjectIdName"]}   - ${removeDomainObjectsByMasterId["masterDomainObjectTitle"]}ID
     */
    void remove${removeDomainObjectsByMasterId["domainObjectAliases"]}By${removeDomainObjectsByMasterId["upperMasterDomainObjectIdName"]}(${removeDomainObjectsByMasterId["masterDomainObjectIdType"]} ${removeDomainObjectsByMasterId["masterDomainObjectIdName"]});
</#if>
</#list>
</#if>
<#if getDomainObjectById["methodActivated"]>

    /**
     * 根据ID获取${getDomainObjectById["domainObjectTitle"]}
     *
     * @param ${getDomainObjectById["domainObjectIdName"]}    - ID主键
     * @return 返回完整的领域对象信息
     */
    ${getDomainObjectById["domainObjectName"]} get${getDomainObjectById["domainObjectAlias"]}ById(${getDomainObjectById["domainObjectIdType"]} ${getDomainObjectById["domainObjectIdName"]});
</#if>
<#if getDomainObjectsByIds["methodActivated"]>

    /**
     * 根据多个ID获取${getDomainObjectsByIds["domainObjectTitle"]}
     *
     * @param ${getDomainObjectsByIds["domainObjectIdsName"]}   - ID主键列表
     * @return 返回完整的领域对象信息列表
     */
    List<${getDomainObjectsByIds["domainObjectName"]}> get${getDomainObjectsByIds["domainObjectAliases"]}ByIds(List<${getDomainObjectsByIds["domainObjectIdType"]}> ${getDomainObjectsByIds["domainObjectIdsName"]});
</#if>
<#if getDomainObjectsByXxxMasterId["methodActivated"]>
<#list getDomainObjectsByXxxMasterId["getByMasterIdMethods"] as getDomainObjectsByMasterId>
<#if getDomainObjectsByMasterId["methodActivated"]>

    /**
     * 根据${getDomainObjectsByMasterId["masterDomainObjectTitle"]}ID获取${getDomainObjectsByMasterId["domainObjectTitle"]}
     *
     * @param ${getDomainObjectsByMasterId["masterDomainObjectIdName"]}   - ${getDomainObjectsByMasterId["masterDomainObjectTitle"]}ID
     * @return 返回完整的领域对象信息
     */
    ${getDomainObjectsByMasterId["methodDynamicReturnType"]} get${getDomainObjectsByMasterId["domainObjectAliases"]}By${getDomainObjectsByMasterId["upperMasterDomainObjectIdName"]}(${getDomainObjectsByMasterId["masterDomainObjectIdType"]} ${getDomainObjectsByMasterId["masterDomainObjectIdName"]});
</#if>
</#list>
</#if>
<#if getDomainObjectsByXxxMasterIds["methodActivated"]>
<#list getDomainObjectsByXxxMasterIds["getByMasterIdsMethods"] as getDomainObjectsByMasterIds>
<#if getDomainObjectsByMasterIds["methodActivated"]>

    /**
     * 根据多个${getDomainObjectsByMasterIds["masterDomainObjectTitle"]}ID获取${getDomainObjectsByMasterIds["domainObjectTitle"]}列表
     *
     * @param ${getDomainObjectsByMasterIds["masterDomainObjectIdsName"]}   - ${getDomainObjectsByMasterIds["masterDomainObjectTitle"]}ID列表
     * @return 返回完整的领域对象信息
     */
    ${getDomainObjectsByMasterIds["methodDynamicReturnType"]} get${getDomainObjectsByMasterIds["domainObjectsAliases"]}By${getDomainObjectsByMasterIds["upperMasterDomainObjectIdsName"]}(List<${getDomainObjectsByMasterIds["masterDomainObjectIdType"]}> ${getDomainObjectsByMasterIds["masterDomainObjectIdsName"]});
</#if>
</#list>
</#if>
<#if getDomainObjectsByPage["methodActivated"]>

    /**
     * 根据条件查询${getDomainObjectsByPage["domainObjectTitle"]}列表(排序、分页)
     *
     * @param condition		- 查询条件
     * @param page			- 分页/排序参数
     * @return 返回完整的领域对象列表
     */
    List<${getDomainObjectsByPage["domainObjectName"]}> get${getDomainObjectsByPage["domainObjectAliases"]}ByPage(${getDomainObjectsByPage["domainObjectName"]} condition, Page page);
</#if>
<#if getDomainObjectTotalCount["methodActivated"]>

    /**
     * 获取${getDomainObjectTotalCount["domainObjectTitle"]}总记录数
     *
     * @return 返回领域对象总记录数
     */
    int get${getDomainObjectTotalCount["domainObjectAlias"]}TotalCount();
</#if>
<#if forEachDomainObject1["methodActivated"]>

    /**
     * 基于Mybatis游标操作，遍历所有${forEachDomainObject1["domainObjectTitle"]}
     * (在数据量大的情况下，避免一次加载出所有数据而引起内存溢出)
     * 典型示例1、(数据量不大的情况下一次获取所有元素)：
     *      List&lt;MyModel&gt; allModels = new ArrayList<>(); //承接所有元素的集合
     *      myModelService.forEach${forEachDomainObject1["domainObjectAlias"]}(allModels::add); //加载所有元素进入集合
     *
     * 典型示例2、(数据量大的情况需要逐步迭代处理每个元素)：
     *      myModelService.forEach${forEachDomainObject1["domainObjectAlias"]}(System.out::println); //逐步迭代处理每个元素
     *
     * @param consumer  - 遍历元素的Consumer
     */
    void forEach${forEachDomainObject1["domainObjectAlias"]}(Consumer<${forEachDomainObject1["domainObjectName"]}> consumer);
</#if>
<#if forEachDomainObject2["methodActivated"]>
    /**
     * 基于Mybatis游标操作，遍历所有${forEachDomainObject2["domainObjectTitle"]}
     * (在数据量大的情况下，避免一次加载出所有数据而引起内存溢出)
     *
     * 典型示例、(数据量大的情况需要逐步迭代处理每个元素)：
     *      myModelService.forEach${forEachDomainObject2["domainObjectAlias"]}((item, index) -> {
     *          System.out.println("index = " + index + ", item = " + item);
     *      }); //逐步迭代处理每个元素
     *
     * @param consumer  - 遍历元素的ObjIntConsumer
     */
    void forEach${forEachDomainObject2["domainObjectAlias"]}(ObjIntConsumer<${forEachDomainObject2["domainObjectName"]}> consumer);
</#if>

}