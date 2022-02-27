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
public class ${targetClass}<#if targetExtends??> extends ${targetExtends}</#if><#if (targetImplements?size > 0)> implements <#list targetImplements as targetImplement>${targetImplement}<#if (item_has_next)>, </#if></#list></#if> {

    @Resource(name="${domainServiceBeanName}")
    private ${domainServiceName} ${domainServiceInstanceName};
<#if createDomainObject.activated>

    @Override
    public ${createDomainObject.methodReturnType} create${domainObjectParameter.domainObjectAlias}(${createDomainObject.inputApiModelName} createRequest) {
    <#list createDomainObject.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if modifyDomainObjectById.activated>

    @Override
    public ${modifyDomainObjectById.methodReturnType} modify${domainObjectParameter.domainObjectAlias}ById(${modifyDomainObjectById.inputApiModelName} modifyRequest) {
    <#list modifyDomainObjectById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if removeDomainObjectById.activated>

    @Override
    public ${removeDomainObjectById.methodReturnType} remove${domainObjectParameter.domainObjectAlias}ById(${domainObjectParameter.domainObjectIdType} id) {
    <#list removeDomainObjectById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if removeDomainObjectsByIds.activated>

    @Override
    public ${removeDomainObjectsByIds.methodReturnType} remove${domainObjectParameter.domainObjectsAlias}ByIds(List<${domainObjectParameter.domainObjectIdType}> ids) {
    <#list removeDomainObjectsByIds.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if getDomainObjectById.activated>

    @Override
    public ${getDomainObjectById.methodReturnType} get${domainObjectParameter.domainObjectAlias}ById(${domainObjectParameter.domainObjectIdType} id<#if aggregateRoot>, Boolean cascade</#if>) {
    <#list getDomainObjectById.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if getDomainObjectsByIds.activated>

    @Override
    public ${getDomainObjectsByIds.methodReturnType} get${domainObjectParameter.domainObjectsAlias}ByIds(List<${domainObjectParameter.domainObjectIdType}> ids<#if aggregateRoot>, Boolean cascade</#if>) {
    <#list getDomainObjectsByIds.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>
<#if getDomainObjectsByPage.activated>

    @Override
    public ${getDomainObjectsByPage.methodReturnType} get${domainObjectParameter.domainObjectsAlias}ByPage(${getDomainObjectsByPage.inputApiModelName} queryRequest<#if aggregateRoot>, Boolean cascade</#if>) {
    <#list getDomainObjectsByPage.methodBodyLines as methodBodyLine>
        ${methodBodyLine}
    </#list>
    }
</#if>

}