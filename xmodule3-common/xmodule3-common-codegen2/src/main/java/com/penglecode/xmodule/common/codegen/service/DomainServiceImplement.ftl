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
<#list targetAnnotations as targetAnnotation>
${targetAnnotation}
</#list>
public class ${targetClass} implements <#list targetImplements as targetImplement>${targetImplement}<#if (item_has_next)>, </#if></#list> {

    @Resource(name="${mapperBeanName}")
    private ${mapperInterfaceName} ${mapperInstanceName};
<#if createDomainObject["methodActivated"]>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void create${createDomainObject["domainObjectAlias"]}(${createDomainObject["domainObjectName"]} ${createDomainObject["domainObjectVariable"]}) {
    <#list createDomainObject["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
        ${mapperInstanceName}.insertModel(${createDomainObject["domainObjectVariable"]});
    }
</#if>
<#if batchCreateDomainObjects["methodActivated"]>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void batchCreate${batchCreateDomainObjects["domainObjectAliases"]}(List<${batchCreateDomainObjects["domainObjectName"]}> ${batchCreateDomainObjects["domainObjectVariables"]}) {
    <#list batchCreateDomainObjects["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
        MybatisHelper.batchUpdateDomainObjects(${batchCreateDomainObjects["domainObjectVariables"]}, this::create${createDomainObject["domainObjectAlias"]}, ${mapperInstanceName});
    }
</#if>
<#if modifyDomainObjectById["methodActivated"]>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void modify${modifyDomainObjectById["domainObjectAlias"]}ById(${modifyDomainObjectById["domainObjectName"]} ${modifyDomainObjectById["domainObjectVariable"]}) {
    <#list modifyDomainObjectById["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
        ${mapperInstanceName}.updateModelById(${modifyDomainObjectById["domainObjectVariable"]}.identity(), updateColumns);
    }
</#if>
<#if batchModifyDomainObjectsById["methodActivated"]>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void batchModify${batchModifyDomainObjectsById["domainObjectAliases"]}ById(List<${batchModifyDomainObjectsById["domainObjectName"]}> ${batchModifyDomainObjectsById["domainObjectVariables"]}) {
    <#list batchModifyDomainObjectsById["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
        MybatisHelper.batchUpdateDomainObjects(${batchModifyDomainObjectsById["domainObjectVariables"]}, this::modify${batchModifyDomainObjectsById["domainObjectAlias"]}ById, ${mapperInstanceName});
    }
</#if>
<#if removeDomainObjectById["methodActivated"]>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void remove${removeDomainObjectById["domainObjectAlias"]}ById(${removeDomainObjectById["domainObjectIdType"]} ${removeDomainObjectById["domainObjectIdName"]}) {
    <#list removeDomainObjectById["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
        ${mapperInstanceName}.deleteModelById(${removeDomainObjectById["domainObjectIdName"]});
    }
</#if>
<#if removeDomainObjectsByIds["methodActivated"]>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void remove${removeDomainObjectsByIds["domainObjectAliases"]}ByIds(List<${removeDomainObjectsByIds["domainObjectIdType"]}> ${removeDomainObjectsByIds["domainObjectIdsName"]}) {
    <#list removeDomainObjectsByIds["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
        MybatisHelper.batchDeleteDomainObjects(${removeDomainObjectsByIds["domainObjectIdsName"]}, ${mapperInstanceName});
    }
</#if>
<#if removeDomainObjectsByXxxMasterId["methodActivated"]>
<#list removeDomainObjectsByXxxMasterId["removeByMasterIdMethods"] as removeDomainObjectsByMasterId>
<#if removeDomainObjectsByMasterId["methodActivated"]>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void remove${removeDomainObjectsByMasterId["domainObjectAliases"]}By${removeDomainObjectsByMasterId["upperMasterDomainObjectIdName"]}(${removeDomainObjectsByMasterId["masterDomainObjectIdType"]} ${removeDomainObjectsByMasterId["masterDomainObjectIdName"]}) {
    <#list removeDomainObjectsByMasterId["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
        ${mapperInstanceName}.deleteModelByCriteria(criteria);
    }
</#if>
</#list>
</#if>
<#if getDomainObjectById["methodActivated"]>

    @Override
    public ${getDomainObjectById["domainObjectName"]} get${getDomainObjectById["domainObjectAlias"]}ById(${getDomainObjectById["domainObjectIdType"]} ${getDomainObjectById["domainObjectIdName"]}) {
        return ObjectUtils.isEmpty(${getDomainObjectById["domainObjectIdName"]}) ? null : ${mapperInstanceName}.selectModelById(${getDomainObjectById["domainObjectIdName"]});
    }
</#if>
<#if getDomainObjectsByIds["methodActivated"]>

    @Override
    public List<${getDomainObjectsByIds["domainObjectName"]}> get${getDomainObjectsByIds["domainObjectAliases"]}ByIds(List<${getDomainObjectsByIds["domainObjectIdType"]}> ${getDomainObjectsByIds["domainObjectIdsName"]}) {
        return CollectionUtils.isEmpty(${getDomainObjectsByIds["domainObjectIdsName"]}) ? Collections.emptyList() : ${mapperInstanceName}.selectModelListByIds(${getDomainObjectsByIds["domainObjectIdsName"]});
    }
</#if>
<#if getDomainObjectsByXxxMasterId["methodActivated"]>
<#list getDomainObjectsByXxxMasterId["getByMasterIdMethods"] as getDomainObjectsByMasterId>
<#if getDomainObjectsByMasterId["methodActivated"]>

    @Override
    public ${getDomainObjectsByMasterId["methodDynamicReturnType"]} get${getDomainObjectsByMasterId["domainObjectAliases"]}By${getDomainObjectsByMasterId["upperMasterDomainObjectIdName"]}(${getDomainObjectsByMasterId["masterDomainObjectIdType"]} ${getDomainObjectsByMasterId["masterDomainObjectIdName"]}) {
    <#list getDomainObjectsByMasterId["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
    }
</#if>
</#list>
</#if>
<#if getDomainObjectsByXxxMasterIds["methodActivated"]>
<#list getDomainObjectsByXxxMasterIds["getByMasterIdsMethods"] as getDomainObjectsByMasterIds>
<#if getDomainObjectsByMasterIds["methodActivated"]>

    @Override
    public ${getDomainObjectsByMasterIds["methodDynamicReturnType"]} get${getDomainObjectsByMasterIds["domainObjectsAliases"]}By${getDomainObjectsByMasterIds["upperMasterDomainObjectIdsName"]}(List<${getDomainObjectsByMasterIds["masterDomainObjectIdType"]}> ${getDomainObjectsByMasterIds["masterDomainObjectIdsName"]}) {
    <#list getDomainObjectsByMasterIds["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
    }
</#if>
</#list>
</#if>
<#if getDomainObjectsByPage["methodActivated"]>

    @Override
    public List<${getDomainObjectsByPage["domainObjectName"]}> get${getDomainObjectsByPage["domainObjectAliases"]}ByPage(${getDomainObjectsByPage["domainObjectName"]} condition, Page page) {
    <#list getDomainObjectsByPage["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
        return MybatisHelper.selectDomainObjectListByPage(${mapperInstanceName}, criteria, page);
    }
</#if>
<#if getDomainObjectTotalCount["methodActivated"]>

    @Override
    public int get${getDomainObjectTotalCount["domainObjectAlias"]}TotalCount() {
        return ${mapperInstanceName}.selectAllModelCount();
    }
</#if>

<#if forEachDomainObject1["methodActivated"]>

    @Override
    public void forEach${forEachDomainObject1["domainObjectAlias"]}(Consumer<${forEachDomainObject1["domainObjectName"]}> consumer) {
        ${mapperInstanceName}.selectAllModelList().forEach(consumer);
    }
</#if>
<#if forEachDomainObject2["methodActivated"]>

    @Override
    public void forEach${forEachDomainObject2["domainObjectAlias"]}(ObjIntConsumer<${forEachDomainObject2["domainObjectName"]}> consumer) {
        Cursor<${forEachDomainObject2["domainObjectName"]}> cursor = ${mapperInstanceName}.selectAllModelList();
        int index = 0;
        for (${forEachDomainObject2["domainObjectName"]} item : cursor) {
            consumer.accept(item, index++);
        }
    }
</#if>

}