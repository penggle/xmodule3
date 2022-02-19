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
<#if createDomainObject.activated>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void create${createDomainObject.domainObjectParameter.domainObjectAlias}(${createDomainObject.domainObjectParameter.domainObjectName} ${createDomainObject.domainObjectParameter.lowerDomainObjectName}) {
    <#list createDomainObject.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        ${mapperInstanceName}.insertModel(${createDomainObject.domainObjectParameter.lowerDomainObjectName});
    }
</#if>
<#if batchCreateDomainObjects.activated>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void batchCreate${batchCreateDomainObjects.domainObjectParameter.domainObjectsAlias}(List<${batchCreateDomainObjects.domainObjectParameter.domainObjectName}> ${batchCreateDomainObjects.domainObjectParameter.lowerDomainObjectsName}) {
    <#list batchCreateDomainObjects.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        MybatisHelper.batchUpdateDomainObjects(${batchCreateDomainObjects.domainObjectParameter.lowerDomainObjectsName}, this::create${createDomainObject.domainObjectParameter.domainObjectAlias}, ${mapperInstanceName});
    }
</#if>
<#if modifyDomainObjectById.activated>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void modify${modifyDomainObjectById.domainObjectParameter.domainObjectAlias}ById(${modifyDomainObjectById.domainObjectParameter.domainObjectName} ${modifyDomainObjectById.domainObjectParameter.lowerDomainObjectName}) {
    <#list modifyDomainObjectById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        ${mapperInstanceName}.updateModelById(${modifyDomainObjectById.domainObjectParameter.lowerDomainObjectName}.identity(), updateColumns);
    }
</#if>
<#if batchModifyDomainObjectsById.activated>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void batchModify${batchModifyDomainObjectsById.domainObjectParameter.domainObjectsAlias}ById(List<${batchModifyDomainObjectsById.domainObjectParameter.domainObjectName}> ${batchModifyDomainObjectsById.domainObjectParameter.lowerDomainObjectsName}) {
    <#list batchModifyDomainObjectsById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        MybatisHelper.batchUpdateDomainObjects(${batchModifyDomainObjectsById.domainObjectParameter.lowerDomainObjectsName}, this::modify${batchModifyDomainObjectsById.domainObjectParameter.domainObjectAlias}ById, ${mapperInstanceName});
    }
</#if>
<#if removeDomainObjectById.activated>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public int remove${removeDomainObjectById.domainObjectParameter.domainObjectAlias}ById(${removeDomainObjectById.domainObjectParameter.domainObjectIdType} ${removeDomainObjectById.domainObjectParameter.domainObjectIdName}) {
    <#list removeDomainObjectById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        return ${mapperInstanceName}.deleteModelById(${removeDomainObjectById.domainObjectParameter.domainObjectIdName});
    }
</#if>
<#if removeDomainObjectsByIds.activated>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public int remove${removeDomainObjectsByIds.domainObjectParameter.domainObjectsAlias}ByIds(List<${removeDomainObjectsByIds.domainObjectParameter.domainObjectIdType}> ${removeDomainObjectsByIds.domainObjectParameter.domainObjectIdsName}) {
    <#list removeDomainObjectsByIds.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        return MybatisHelper.batchDeleteDomainObjects(${removeDomainObjectsByIds.domainObjectParameter.domainObjectIdsName}, ${mapperInstanceName});
    }
</#if>
<#list removeDomainObjectsByXxxMasterId?values as removeDomainObjectsByMasterId>
<#if removeDomainObjectsByMasterId.activated>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public int remove${removeDomainObjectsByMasterId.domainObjectParameter.domainObjectsAlias}By${removeDomainObjectsByMasterId.upperMasterIdNameOfSlave}(${removeDomainObjectsByMasterId.masterDomainObjectParameter.domainObjectIdType} ${removeDomainObjectsByMasterId.masterIdNameOfSlave}) {
    <#list removeDomainObjectsByMasterId.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        ${mapperInstanceName}.deleteModelByCriteria(criteria);
    }
</#if>
</#list>
<#if getDomainObjectById.activated>

    @Override
    public ${getDomainObjectById.domainObjectParameter.domainObjectName} get${getDomainObjectById.domainObjectParameter.domainObjectAlias}ById(${getDomainObjectById.domainObjectParameter.domainObjectIdType} ${getDomainObjectById.domainObjectParameter.domainObjectIdName}) {
        return ObjectUtils.isEmpty(${getDomainObjectById.domainObjectParameter.domainObjectIdName}) ? null : ${mapperInstanceName}.selectModelById(${getDomainObjectById.domainObjectParameter.domainObjectIdName});
    }
</#if>
<#if getDomainObjectsByIds.activated>

    @Override
    List<${getDomainObjectsByIds.domainObjectParameter.domainObjectName}> get${getDomainObjectsByIds.domainObjectParameter.domainObjectsAlias}ByIds(List<${getDomainObjectsByIds.domainObjectParameter.domainObjectIdType}> ${getDomainObjectsByIds.domainObjectParameter.domainObjectIdsName}) {
        return CollectionUtils.isEmpty(${getDomainObjectsByIds.domainObjectParameter.domainObjectIdsName}) ? Collections.emptyList() : ${mapperInstanceName}.selectModelListByIds(${getDomainObjectsByIds.domainObjectParameter.domainObjectIdsName});
    }
</#if>
<#list getDomainObjectsByXxxMasterId?values as getDomainObjectsByMasterId>
<#if getDomainObjectsByMasterId.activated>

    @Override
    public ${getDomainObjectsByMasterId.methodReturnType} get${getDomainObjectsByMasterId.domainObjectParameter.domainObjectsAlias}By${getDomainObjectsByMasterId.upperMasterIdNameOfSlave}(${getDomainObjectsByMasterId.masterDomainObjectParameter.domainObjectIdType} ${getDomainObjectsByMasterId.masterIdNameOfSlave}) {
    <#list getDomainObjectsByMasterId.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
</#list>
<#list getDomainObjectsByXxxMasterIds?values as getDomainObjectsByMasterIds>
<#if getDomainObjectsByMasterIds.activated>

    @Override
    public ${getDomainObjectsByMasterIds.methodReturnType} get${getDomainObjectsByMasterIds.domainObjectParameter.domainObjectsAlias}By${getDomainObjectsByMasterIds.upperMasterIdsNameOfSlave}(List<${getDomainObjectsByMasterIds.masterDomainObjectParameter.domainObjectIdType}> ${getDomainObjectsByMasterIds.masterIdsNameOfSlave}) {
    <#list getDomainObjectsByMasterIds.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
</#list>
<#if getDomainObjectsByPage.activated>

    @Override
    public List<${getDomainObjectsByPage.domainObjectParameter.domainObjectName}> get${getDomainObjectsByPage.domainObjectParameter.domainObjectsAlias}ByPage(${getDomainObjectsByPage.domainObjectParameter.domainObjectName} condition, Page page) {
    <#list getDomainObjectsByPage.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
        return MybatisHelper.selectDomainObjectListByPage(${mapperInstanceName}, criteria, page);
    }
</#if>
<#if getDomainObjectTotalCount.activated>

    @Override
    public int get${getDomainObjectTotalCount.domainObjectParameter.domainObjectAlias}TotalCount() {
        return ${mapperInstanceName}.selectAllModelCount();
    }
</#if>

<#if forEachDomainObject1.activated>

    @Override
    public void forEach${forEachDomainObject1.domainObjectParameter.domainObjectAlias}(Consumer<${forEachDomainObject1.domainObjectParameter.domainObjectName}> consumer) {
        ${mapperInstanceName}.selectAllModelList().forEach(consumer);
    }
</#if>
<#if forEachDomainObject2.activated>

    @Override
    public void forEach${forEachDomainObject2.domainObjectParameter.domainObjectAlias}(ObjIntConsumer<${forEachDomainObject2.domainObjectParameter.domainObjectName}> consumer) {
        Cursor<${forEachDomainObject2.domainObjectParameter.domainObjectName}> cursor = ${mapperInstanceName}.selectAllModelList();
        int index = 0;
        for (${forEachDomainObject2.domainObjectParameter.domainObjectName} item : cursor) {
            consumer.accept(item, index++);
        }
    }
</#if>

}