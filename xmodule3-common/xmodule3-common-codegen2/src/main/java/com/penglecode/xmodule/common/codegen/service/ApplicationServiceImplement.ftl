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
    public void create${domainObjectParameter.domainObjectAlias}(${domainObjectParameter.domainObjectName} ${domainObjectParameter.lowerDomainObjectName}) {
    <#list createDomainObject.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if modifyDomainObjectById.activated>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void modify${domainObjectParameter.domainObjectAlias}ById(${domainObjectParameter.domainObjectName} ${domainObjectParameter.lowerDomainObjectName}) {
    <#list modifyDomainObjectById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if removeDomainObjectById.activated>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void remove${domainObjectParameter.domainObjectAlias}ById(${domainObjectParameter.domainObjectIdType} ${domainObjectParameter.domainObjectIdName}) {
    <#list removeDomainObjectById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if removeDomainObjectsByIds.activated>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void remove${domainObjectParameter.domainObjectsAlias}ByIds(List<${domainObjectParameter.domainObjectIdType}> ${domainObjectParameter.domainObjectIdsName}) {
    <#list removeDomainObjectsByIds.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if getDomainObjectById.activated>

    @Override
    public ${domainObjectParameter.domainObjectName} get${domainObjectParameter.domainObjectAlias}ById(${domainObjectParameter.domainObjectIdType} ${domainObjectParameter.domainObjectIdName}, boolean cascade) {
    <#list getDomainObjectById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if getDomainObjectsByIds.activated>

    @Override
    public List<${domainObjectParameter.domainObjectName}> get${domainObjectParameter.domainObjectsAlias}ByIds(List<${domainObjectParameter.domainObjectIdType}> ${domainObjectParameter.domainObjectIdsName}, boolean cascade) {
    <#list getDomainObjectByIds.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if getDomainObjectsByPage.activated>

    @Override
    public List<${domainObjectParameter.domainObjectName}> get${domainObjectParameter.domainObjectsAlias}ByPage(${domainObjectParameter.domainObjectName} condition, Page page, boolean cascade) {
    <#list getDomainObjectsByPage.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if prepareAggregateObjects.activated>

    protected List<${domainObjectParameter.domainObjectName}> prepare${domainObjectParameter.lowerDomainObjectName}List(List<${prepareAggregateObjects.masterDomainObjectParameter.domainObjectName}> ${prepareAggregateObjects.masterDomainObjectParameter.lowerDomainObjectsName}, boolean cascade) {
    <#list prepareAggregateObjects.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if getDomainObjectTotalCount.activated>

    @Override
    public int get${domainObjectParameter.domainObjectAlias}TotalCount() {
    <#list getDomainObjectTotalCount.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if forEachDomainObject1.activated>

    @Override
    public void forEach${domainObjectParameter.domainObjectAlias}(Consumer<${domainObjectParameter.domainObjectName}> consumer) {
    <#list forEachDomainObject1.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if forEachDomainObject2.activated>

    @Override
    public void forEach${domainObjectParameter.domainObjectAlias}(ObjIntConsumer<${domainObjectParameter.domainObjectName}> consumer) {
    <#list forEachDomainObject2.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>

}