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
    @Resource(name="${domainService["domainServiceBeanName"]}")
    private ${domainService["domainServiceName"]} ${domainService["domainServiceInstanceName"]};

</#list>
<#if createDomainObject["methodActivated"]>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void create${createDomainObject["domainObjectAlias"]}(${createDomainObject["domainObjectName"]} ${createDomainObject["domainObjectVariable"]}) {
    <#list createDomainObject["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
    }
</#if>
<#if modifyDomainObjectById["methodActivated"]>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void modify${modifyDomainObjectById["domainObjectAlias"]}ById(${modifyDomainObjectById["domainObjectName"]} ${modifyDomainObjectById["domainObjectVariable"]}) {
    <#list modifyDomainObjectById["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
    }
</#if>
<#if removeDomainObjectById["methodActivated"]>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void remove${removeDomainObjectById["domainObjectAlias"]}ById(${removeDomainObjectById["domainObjectIdType"]} ${removeDomainObjectById["domainObjectIdName"]}) {
    <#list removeDomainObjectById["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
    }
</#if>
<#if removeDomainObjectsByIds["methodActivated"]>

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void remove${removeDomainObjectsByIds["domainObjectAliases"]}ByIds(List<${removeDomainObjectsByIds["domainObjectIdType"]}> ${removeDomainObjectsByIds["domainObjectIdsName"]}) {
    <#list removeDomainObjectsByIds["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
    }
</#if>
<#if getDomainObjectById["methodActivated"]>

    @Override
    public ${getDomainObjectById["domainObjectName"]} get${getDomainObjectById["domainObjectAlias"]}ById(${getDomainObjectById["domainObjectIdType"]} ${getDomainObjectById["domainObjectIdName"]}, boolean cascade) {
    <#list getDomainObjectById["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
    }
</#if>
<#if getDomainObjectByIds["methodActivated"]>

    @Override
    public List<${getDomainObjectsByIds["domainObjectName"]}> get${getDomainObjectsByIds["domainObjectAliases"]}ByIds(List<${getDomainObjectsByIds["domainObjectIdType"]}> ${getDomainObjectsByIds["domainObjectIdsName"]}, boolean cascade) {
    <#list getDomainObjectByIds["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
    }
</#if>
<#if getDomainObjectsByPage["methodActivated"]>

    @Override
    public List<${getDomainObjectsByPage["domainObjectName"]}> get${getDomainObjectsByPage["domainObjectAliases"]}ByPage(${getDomainObjectsByPage["domainObjectName"]} condition, Page page, boolean cascade) {
    <#list getDomainObjectByIds["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
    }
</#if>
<#if getDomainObjectTotalCount["methodActivated"]>

    @Override
    public int get${getDomainObjectTotalCount["domainObjectAlias"]}TotalCount() {
    <#list getDomainObjectTotalCount["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
    }
</#if>
<#if forEachDomainObject1["methodActivated"]>

    @Override
    public void forEach${forEachDomainObject1["domainObjectAlias"]}(Consumer<${forEachDomainObject1["domainObjectName"]}> consumer) {
    <#list forEachDomainObject1["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
    }
</#if>
<#if forEachDomainObject2["methodActivated"]>

    @Override
    public void forEach${forEachDomainObject2["domainObjectAlias"]}(ObjIntConsumer<${forEachDomainObject2["domainObjectName"]}> consumer) {
    <#list forEachDomainObject2["methodCodeLines"] as methodCodeLine>
        ${methodCodeLine}
    </#list>
    }
</#if>

}