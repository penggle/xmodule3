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

<#list domainServices as domainService>
    @Resource(name="${domainService.domainServiceBeanName}")
    private ${domainService.domainServiceName} ${domainService.domainServiceInstanceName};

</#list>
<#if createDomainObject.activated>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void create${createDomainObject.domainObjectParameter.domainObjectAlias}(${createDomainObject.domainObjectParameter.domainObjectName} ${createDomainObject.domainObjectParameter.lowerDomainObjectName}) {
    <#list createDomainObject.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if modifyDomainObjectById.activated>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void modify${modifyDomainObjectById.domainObjectParameter.domainObjectAlias}ById(${modifyDomainObjectById.domainObjectParameter.domainObjectName} ${modifyDomainObjectById.domainObjectParameter.lowerDomainObjectName}) {
    <#list modifyDomainObjectById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if removeDomainObjectById.activated>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void remove${removeDomainObjectById.domainObjectParameter.domainObjectAlias}ById(${removeDomainObjectById.domainObjectParameter.domainObjectIdType} ${removeDomainObjectById.domainObjectParameter.domainObjectIdName}) {
    <#list removeDomainObjectById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if removeDomainObjectsByIds.activated>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void remove${removeDomainObjectsByIds.domainObjectParameter.domainObjectsAlias}ByIds(List<${removeDomainObjectsByIds.domainObjectParameter.domainObjectIdType}> ${removeDomainObjectsByIds.domainObjectParameter.domainObjectIdsName}) {
    <#list removeDomainObjectsByIds.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if getDomainObjectById.activated>

    @Override
    public ${getDomainObjectById.domainObjectParameter.domainObjectName} get${getDomainObjectById.domainObjectParameter.domainObjectAlias}ById(${getDomainObjectById.domainObjectParameter.domainObjectIdType} ${getDomainObjectById.domainObjectParameter.domainObjectIdName}, boolean cascade) {
    <#list getDomainObjectById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if getDomainObjectsByIds.activated>

    @Override
    public List<${getDomainObjectsByIds.domainObjectParameter.domainObjectName}> get${getDomainObjectsByIds.domainObjectParameter.domainObjectsAlias}ByIds(List<${getDomainObjectsByIds.domainObjectParameter.domainObjectIdType}> ${getDomainObjectsByIds.domainObjectParameter.domainObjectIdsName}, boolean cascade) {
    <#list getDomainObjectByIds.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if getDomainObjectsByPage.activated>

    @Override
    public List<${getDomainObjectsByPage.domainObjectParameter.domainObjectName}> get${getDomainObjectsByPage.domainObjectParameter.domainObjectsAlias}ByPage(${getDomainObjectsByPage.domainObjectParameter.domainObjectName} condition, Page page, boolean cascade) {
    <#list getDomainObjectsByPage.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if prepareAggregateObjects.activated>

    protected List<${prepareAggregateObjects.domainObjectParameter.domainObjectName}> prepare${prepareAggregateObjects.domainObjectParameter.lowerDomainObjectName}List(List<${prepareAggregateObjects.masterDomainObjectParameter.domainObjectName}> ${prepareAggregateObjects.masterDomainObjectParameter.lowerDomainObjectsName}, boolean cascade) {
    <#list prepareAggregateObjects.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if getDomainObjectTotalCount.activated>

    @Override
    public int get${getDomainObjectTotalCount.domainObjectParameter.domainObjectAlias}TotalCount() {
    <#list getDomainObjectTotalCount.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if forEachDomainObject1.activated>

    @Override
    public void forEach${forEachDomainObject1.domainObjectParameter.domainObjectAlias}(Consumer<${forEachDomainObject1.domainObjectParameter.domainObjectName}> consumer) {
    <#list forEachDomainObject1.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if forEachDomainObject2.activated>

    @Override
    public void forEach${forEachDomainObject2.domainObjectParameter.domainObjectAlias}(ObjIntConsumer<${forEachDomainObject2.domainObjectParameter.domainObjectName}> consumer) {
    <#list forEachDomainObject2.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>

}