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
 * @created ${targetCreated}
 */
public interface ${targetClass} {
<#if createDomainObject.activated>

    /**
     * 创建${domainObjectParameter.domainObjectTitle}
     *
     * @param ${domainObjectParameter.lowerDomainObjectName}     - 被保存的领域对象
     */
    void create${domainObjectParameter.domainObjectAlias}(${domainObjectParameter.domainObjectName} ${domainObjectParameter.lowerDomainObjectName});
</#if>
<#if batchCreateDomainObjects.activated>

    /**
     * 批量创建${domainObjectParameter.domainObjectTitle}
     *
     * @param ${domainObjectParameter.lowerDomainObjectsName}    - 被保存的领域对象集合
     */
    void batchCreate${domainObjectParameter.domainObjectsAlias}(List<${domainObjectParameter.domainObjectName}> ${domainObjectParameter.lowerDomainObjectsName});
</#if>
<#if modifyDomainObjectById.activated>

    /**
     * 根据ID修改${domainObjectParameter.domainObjectTitle}
     *
     * @param ${domainObjectParameter.lowerDomainObjectName}     - 被保存的领域对象(其id字段必须有值)
     */
    void modify${domainObjectParameter.domainObjectAlias}ById(${domainObjectParameter.domainObjectName} ${domainObjectParameter.lowerDomainObjectName});
</#if>
<#if batchModifyDomainObjectsById.activated>

    /**
     * 根据ID批量修改${domainObjectParameter.domainObjectTitle}
     *
     * @param ${domainObjectParameter.lowerDomainObjectsName}    - 被保存的领域对象集合(其id字段必须有值)
     */
    void batchModify${domainObjectParameter.domainObjectsAlias}ById(List<${domainObjectParameter.domainObjectName}> ${domainObjectParameter.lowerDomainObjectsName});
</#if>
<#if removeDomainObjectById.activated>

    /**
     * 根据ID删除${domainObjectParameter.domainObjectTitle}
     *
     * @param ${domainObjectParameter.domainObjectIdName}    - ID主键
     */
    int remove${domainObjectParameter.domainObjectAlias}ById(${domainObjectParameter.domainObjectIdType} ${domainObjectParameter.domainObjectIdName});
</#if>
<#if removeDomainObjectsByIds.activated>

    /**
     * 根据多个ID删除${domainObjectParameter.domainObjectTitle}
     *
     * @param ${domainObjectParameter.domainObjectIdsName}    - ID主键列表
     */
    int remove${domainObjectParameter.domainObjectsAlias}ByIds(List<${domainObjectParameter.domainObjectIdType}> ${domainObjectParameter.domainObjectIdsName});
</#if>
<#list removeDomainObjectsByXxxMasterId?values as removeDomainObjectsByMasterId>
<#if removeDomainObjectsByMasterId.activated>

    /**
     * 根据${removeDomainObjectsByMasterId.masterDomainObjectParameter.domainObjectTitle}ID删除${domainObjectParameter.domainObjectTitle}
     *
     * @param ${removeDomainObjectsByMasterId.masterIdNameOfSlave}   - ${removeDomainObjectsByMasterId.masterDomainObjectParameter.domainObjectTitle}ID
     */
    int remove${domainObjectParameter.domainObjectsAlias}By${removeDomainObjectsByMasterId.upperMasterIdNameOfSlave}(${removeDomainObjectsByMasterId.masterDomainObjectParameter.domainObjectIdType} ${removeDomainObjectsByMasterId.masterIdNameOfSlave});
</#if>
</#list>
<#if getDomainObjectById.activated>

    /**
     * 根据ID获取${domainObjectParameter.domainObjectTitle}
     *
     * @param ${domainObjectParameter.domainObjectIdName}    - ID主键
     * @return 返回完整的领域对象信息
     */
    ${domainObjectParameter.domainObjectName} get${domainObjectParameter.domainObjectAlias}ById(${domainObjectParameter.domainObjectIdType} ${domainObjectParameter.domainObjectIdName});
</#if>
<#if getDomainObjectsByIds.activated>

    /**
     * 根据多个ID获取${domainObjectParameter.domainObjectTitle}
     *
     * @param ${domainObjectParameter.domainObjectIdsName}   - ID主键列表
     * @return 返回完整的领域对象信息列表
     */
    List<${domainObjectParameter.domainObjectName}> get${domainObjectParameter.domainObjectsAlias}ByIds(List<${domainObjectParameter.domainObjectIdType}> ${domainObjectParameter.domainObjectIdsName});
</#if>
<#list getDomainObjectsByXxxMasterId?values as getDomainObjectsByMasterId>
<#if getDomainObjectsByMasterId.activated>

    /**
     * 根据${getDomainObjectsByMasterId.masterDomainObjectParameter.domainObjectTitle}ID获取${domainObjectParameter.domainObjectTitle}
     *
     * @param ${getDomainObjectsByMasterId.masterIdNameOfSlave}   - ${getDomainObjectsByMasterId.masterDomainObjectParameter.domainObjectTitle}ID
     * @return 返回完整的领域对象信息
     */
    ${getDomainObjectsByMasterId.methodReturnType} get${domainObjectParameter.domainObjectsAlias}By${getDomainObjectsByMasterId.upperMasterIdNameOfSlave}(${getDomainObjectsByMasterId.masterDomainObjectParameter.domainObjectIdType} ${getDomainObjectsByMasterId.masterIdNameOfSlave});
</#if>
</#list>
<#list getDomainObjectsByXxxMasterIds?values as getDomainObjectsByMasterIds>
<#if getDomainObjectsByMasterIds.activated>

    /**
     * 根据多个${getDomainObjectsByMasterIds.masterDomainObjectParameter.domainObjectTitle}ID获取${domainObjectParameter.domainObjectTitle}列表
     *
     * @param ${getDomainObjectsByMasterIds.masterIdsNameOfSlave}   - ${getDomainObjectsByMasterIds.masterDomainObjectParameter.domainObjectTitle}ID列表
     * @return 返回完整的领域对象信息
     */
    ${getDomainObjectsByMasterIds.methodReturnType} get${domainObjectParameter.domainObjectsAlias}By${getDomainObjectsByMasterIds.upperMasterIdsNameOfSlave}(List<${getDomainObjectsByMasterIds.masterDomainObjectParameter.domainObjectIdType}> ${getDomainObjectsByMasterIds.masterIdsNameOfSlave});
</#if>
</#list>
<#if getDomainObjectsByPage.activated>

    /**
     * 根据条件查询${domainObjectParameter.domainObjectTitle}列表(排序、分页)
     *
     * @param condition		- 查询条件
     * @param page			- 分页/排序参数
     * @return 返回完整的领域对象列表
     */
    List<${domainObjectParameter.domainObjectName}> get${domainObjectParameter.domainObjectsAlias}ByPage(${domainObjectParameter.domainObjectName} condition, Page page);
</#if>
<#if getDomainObjectTotalCount.activated>

    /**
     * 获取${domainObjectParameter.domainObjectTitle}总记录数
     *
     * @return 返回领域对象总记录数
     */
    int get${domainObjectParameter.domainObjectAlias}TotalCount();
</#if>
<#if forEachDomainObject1.activated>

    /**
     * 基于Mybatis游标操作，遍历所有${domainObjectParameter.domainObjectTitle}
     * (在数据量大的情况下，避免一次加载出所有数据而引起内存溢出)
     * 典型示例1、(数据量不大的情况下一次获取所有元素)：
     *      List&lt;MyModel&gt; allModels = new ArrayList<>(); //承接所有元素的集合
     *      myModelService.forEach${domainObjectParameter.domainObjectAlias}(allModels::add); //加载所有元素进入集合
     *
     * 典型示例2、(数据量大的情况需要逐步迭代处理每个元素)：
     *      myModelService.forEach${domainObjectParameter.domainObjectAlias}(System.out::println); //逐步迭代处理每个元素
     *
     * @param consumer  - 遍历元素的Consumer
     */
    void forEach${domainObjectParameter.domainObjectAlias}(Consumer<${domainObjectParameter.domainObjectName}> consumer);
</#if>
<#if forEachDomainObject2.activated>

    /**
     * 基于Mybatis游标操作，遍历所有${domainObjectParameter.domainObjectTitle}
     * (在数据量大的情况下，避免一次加载出所有数据而引起内存溢出)
     *
     * 典型示例、(数据量大的情况需要逐步迭代处理每个元素)：
     *      myModelService.forEach${domainObjectParameter.domainObjectAlias}((item, index) -> {
     *          System.out.println("index = " + index + ", item = " + item);
     *      }); //逐步迭代处理每个元素
     *
     * @param consumer  - 遍历元素的ObjIntConsumer
     */
    void forEach${domainObjectParameter.domainObjectAlias}(ObjIntConsumer<${domainObjectParameter.domainObjectName}> consumer);
</#if>

}