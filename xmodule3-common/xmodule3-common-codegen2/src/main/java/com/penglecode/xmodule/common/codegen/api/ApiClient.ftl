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
public interface ${targetClass} {
<#if createDomainObject.activated>

    /**
     * 创建${domainObjectParameter.domainObjectTitle}
     *
     * @param createRequest     - 请求参数
     */
    @Operation(summary="创建${domainObjectParameter.domainObjectTitle}")
    @PostMapping(value="/create", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    ${createDomainObject.methodReturnType} create${domainObjectParameter.domainObjectAlias}(@RequestBody ${createDomainObject.inputApiModelName} createRequest);
</#if>
<#if modifyDomainObjectById.activated>

    /**
     * 根据ID修改${domainObjectParameter.domainObjectTitle}
     *
     * @param modifyRequest     - 请求参数
     */
    @Operation(summary="根据ID修改${domainObjectParameter.domainObjectTitle}")
    @PostMapping(value="/modify", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    ${modifyDomainObjectById.methodReturnType} modify${domainObjectParameter.domainObjectAlias}ById(@RequestBody ${modifyDomainObjectById.inputApiModelName} modifyRequest);
</#if>
<#if removeDomainObjectById.activated>

    /**
     * 根据ID删除${domainObjectParameter.domainObjectTitle}
     *
     * @param id    - ID主键
     */
    @Operation(summary="根据ID删除${domainObjectParameter.domainObjectTitle}")
    @PostMapping(value="/remove/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    ${removeDomainObjectById.methodReturnType} remove${domainObjectParameter.domainObjectAlias}ById(@PathVariable("id") ${domainObjectParameter.domainObjectIdType} id);
</#if>
<#if removeDomainObjectsByIds.activated>

    /**
     * 根据多个ID删除${domainObjectParameter.domainObjectTitle}
     *
     * @param ids    - ID主键列表
     */
    @Operation(summary="根据多个ID删除${domainObjectParameter.domainObjectTitle}")
    @PostMapping(value="/remove/ids", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    ${removeDomainObjectsByIds.methodReturnType} remove${domainObjectParameter.domainObjectsAlias}ByIds(@RequestBody List<${domainObjectParameter.domainObjectIdType}> ids);
</#if>
<#if getDomainObjectById.activated>

    /**
     * 根据ID获取${domainObjectParameter.domainObjectTitle}
     *
     * @param id        - ID主键
    <#if aggregateRoot>
     * @param cascade   - 是否级联带出其他信息
    </#if>
     * @return 返回完整的${domainObjectParameter.domainObjectTitle}
     */
    @Operation(summary="根据ID获取${domainObjectParameter.domainObjectTitle}")
    @GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    ${getDomainObjectById.methodReturnType} get${domainObjectParameter.domainObjectAlias}ById(@PathVariable("id") ${domainObjectParameter.domainObjectIdType} id<#if aggregateRoot>, @RequestParam(name="cascade", defaultValue="false") Boolean cascade</#if>);
</#if>
<#if getDomainObjectsByIds.activated>

    /**
     * 根据多个ID获取${domainObjectParameter.domainObjectTitle}
     *
     * @param ids       - ID主键列表
    <#if aggregateRoot>
     * @param cascade   - 是否级联带出其他信息
    </#if>
     * @return 返回完整的${domainObjectParameter.domainObjectTitle}
     */
    @Operation(summary="根据多个ID获取${domainObjectParameter.domainObjectTitle}")
    @PostMapping(value="/ids", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    ${getDomainObjectsByIds.methodReturnType} get${domainObjectParameter.domainObjectsAlias}ByIds(@RequestBody List<${domainObjectParameter.domainObjectIdType}> ids<#if aggregateRoot>, @RequestParam(name="cascade", defaultValue="false") Boolean cascade</#if>);
</#if>
<#if getDomainObjectsByPage.activated>

    /**
     * 根据条件查询${domainObjectParameter.domainObjectTitle}列表(排序、分页)
     *
     * @param queryRequest		- 查询请求
    <#if aggregateRoot>
     * @param cascade            - 是否级联带出其他信息
    </#if>
     * @return 返回完整的${domainObjectParameter.domainObjectTitle}
     */
    @Operation(summary="根据条件查询${domainObjectParameter.domainObjectTitle}列表(排序、分页)")
    @PostMapping(value="/list", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    ${getDomainObjectsByPage.methodReturnType} get${domainObjectParameter.domainObjectsAlias}ByPage(@RequestBody ${getDomainObjectsByPage.inputApiModelName} queryRequest<#if aggregateRoot>, @RequestParam(name="cascade", defaultValue="false") Boolean cascade</#if>);
</#if>

}