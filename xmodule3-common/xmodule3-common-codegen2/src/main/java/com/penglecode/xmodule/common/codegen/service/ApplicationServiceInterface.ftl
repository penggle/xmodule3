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
     * 创建${domainObjectParameter.domainObjectTitle}(默认会级联保存关联的领域对象)
     *
     * @param ${domainObjectParameter.lowerDomainObjectName}     - 被保存的领域对象
     */
    void create${domainObjectParameter.domainObjectAlias}(${domainObjectParameter.domainObjectName} ${domainObjectParameter.lowerDomainObjectName});
</#if>
<#if modifyDomainObjectById.activated>

    /**
     * 根据ID修改${domainObjectParameter.domainObjectTitle}(默认会级联保存关联的领域对象)
     *
     * @param ${domainObjectParameter.lowerDomainObjectName}     - 被保存的领域对象(其id字段必须有值)
     */
    void modify${domainObjectParameter.domainObjectAlias}ById(${domainObjectParameter.domainObjectName} ${domainObjectParameter.lowerDomainObjectName});
</#if>
<#if removeDomainObjectById.activated>

    /**
     * 根据ID删除${domainObjectParameter.domainObjectTitle}(默认会级联删除关联的领域对象)
     *
     * @param ${domainObjectParameter.domainObjectIdName}    - ID主键
     */
    void remove${domainObjectParameter.domainObjectAlias}ById(${domainObjectParameter.domainObjectIdType} ${domainObjectParameter.domainObjectIdName});
</#if>
<#if removeDomainObjectsByIds.activated>

    /**
     * 根据多个ID删除${domainObjectParameter.domainObjectTitle}(默认会级联删除关联的领域对象)
     *
     * @param ${domainObjectParameter.domainObjectIdsName}    - ID主键列表
     */
    void remove${domainObjectParameter.domainObjectsAlias}ByIds(List<${domainObjectParameter.domainObjectIdType}> ${domainObjectParameter.domainObjectIdsName});
</#if>
<#if getDomainObjectById.activated>

    /**
     * 根据ID获取${domainObjectParameter.domainObjectTitle}
     *
     * @param ${domainObjectParameter.domainObjectIdName}          - ID主键
     * @param cascade       - 是否级联加载关联领域对象
     * @return 返回完整的领域对象信息
     */
    ${domainObjectParameter.domainObjectName} get${domainObjectParameter.domainObjectAlias}ById(${domainObjectParameter.domainObjectIdType} ${domainObjectParameter.domainObjectIdName}, boolean cascade);
</#if>
<#if getDomainObjectsByIds.activated>

    /**
     * 根据多个ID获取${domainObjectParameter.domainObjectTitle}
     *
     * @param ${domainObjectParameter.domainObjectIdsName}    - ID主键列表
     * @param cascade       - 是否级联加载关联领域对象
     * @return 返回完整的领域对象信息
     */
    List<${domainObjectParameter.domainObjectName}> get${domainObjectParameter.domainObjectsAlias}ByIds(List<${domainObjectParameter.domainObjectIdType}> ${domainObjectParameter.domainObjectIdsName}, boolean cascade);
</#if>
<#if getDomainObjectsByPage.activated>

    /**
     * 根据条件查询${domainObjectParameter.domainObjectTitle}列表(排序、分页)
     *
     * @param condition		- 查询条件
     * @param page			- 分页/排序参数
     * @param cascade       - 是否级联加载关联领域对象
     * @return 返回完整的领域对象列表
     */
    List<${domainObjectParameter.domainObjectName}> get${domainObjectParameter.domainObjectsAlias}ByPage(${domainObjectParameter.domainObjectName} condition, Page page, boolean cascade);
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