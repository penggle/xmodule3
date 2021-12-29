package ${targetPackage};

<#if (projectImports?size > 0 || thirdImports?size > 0)>
<#list projectImports as ipm>
import ${ipm};
</#list>
<#list thirdImports as ipm>
import ${ipm};
</#list>

</#if>
<#if (jdkImports?size > 0)>
<#list jdkImports as ipm>
import ${ipm};
</#list>

</#if>
/**
 * ${domainObjectTitle} 领域服务实现
 *
 * @author ${commentAuthor}
 * @version 1.0
 * @since ${generateTime}
 */
@Service("${serviceBeanName}")
public class ${targetObjectName} implements ${serviceInterfaceName} {

    @Resource(name="${mapperBeanName}")
    private ${mapperInterfaceName} ${mapperInstanceName};

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void create${domainObjectAlias}(${domainObjectName} ${domainObjectVarName}) {
    <#list createOperationCodes as createOperationCode>
        ${createOperationCode}
    </#list>
        ${mapperInstanceName}.insertModel(${domainObjectVarName});
    }

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void batchCreate${domainObjectAlias}(List<${domainObjectName}> ${domainObjectVarName}List) {
    <#list batchCreateOperationCodes as batchCreateOperationCode>
        ${batchCreateOperationCode}
    </#list>
        MybatisHelper.batchUpdateDomainObjects(${domainObjectVarName}List, this::create${domainObjectAlias}, ${mapperInstanceName});
    }

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void modify${domainObjectAlias}ById(${domainObjectName} ${domainObjectVarName}) {
    <#list modifyByIdOperationCodes as modifyByIdOperationCode>
        ${modifyByIdOperationCode}
    </#list>
        ${mapperInstanceName}.updateModelById(${domainObjectVarName}.identity(), updateColumns);
    }

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void batchModify${domainObjectAlias}ById(List<${domainObjectName}> ${domainObjectVarName}List) {
    <#list batchModifyOperationCodes as batchModifyOperationCode>
        ${batchModifyOperationCode}
    </#list>
        MybatisHelper.batchUpdateDomainObjects(${domainObjectVarName}List, this::modify${domainObjectAlias}ById, ${mapperInstanceName});
    }

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void remove${domainObjectAlias}ById(${domainObjectIdType} id) {
    <#list removeByIdOperationCodes as removeByIdOperationCode>
        ${removeByIdOperationCode}
    </#list>
        ${mapperInstanceName}.deleteModelById(id);
    }

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void remove${domainObjectAlias}ByIds(List<${domainObjectIdType}> ids) {
    <#list removeByIdsOperationCodes as removeByIdsOperationCode>
        ${removeByIdsOperationCode}
    </#list>
        MybatisHelper.batchDeleteDomainObjects(ids, ${mapperInstanceName});
    }

<#if removeSlaveByMasterIdParameter??>
    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void remove${removeSlaveByMasterIdParameter["slaveDomainObjectAlias"]}By${removeSlaveByMasterIdParameter["upperMasterDomainObjectId"]}(${removeSlaveByMasterIdParameter["masterDomainObjectIdType"]} ${removeSlaveByMasterIdParameter["masterDomainObjectId"]}) {
        BeanValidator.validateProperty(${getSlaveByMasterIdParameter["masterDomainObjectId"]}, ${removeSlaveByMasterIdParameter["getMasterDomainObjectIdRef"]});
        QueryCriteria<${domainObjectName}> criteria = LambdaQueryCriteria.ofEmpty(${domainObjectName}::new)
            .eq(${removeSlaveByMasterIdParameter["getMasterDomainObjectIdRef"]}, ${getSlaveByMasterIdParameter["masterDomainObjectId"]});
        ${mapperInstanceName}.${removeSlaveByMasterIdParameter["deleteSlaveMapperMethod"]}(criteria);
    }

</#if>
    @Override
    public ${domainObjectName} get${domainObjectAlias}ById(${domainObjectIdType} id) {
        return ObjectUtils.isEmpty(id) ? null : ${mapperInstanceName}.selectModelById(id);
    }

    @Override
    public List<${domainObjectName}> get${domainObjectAlias}ListByIds(List<${domainObjectIdType}> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : ${mapperInstanceName}.selectModelListByIds(ids);
    }

<#if getSlaveByMasterIdParameter??>
    @Override
    public ${getSlaveByMasterIdParameter["serviceMethodReturnType"]} get${getSlaveByMasterIdParameter["slaveDomainObjectAlias"]}By${getSlaveByMasterIdParameter["upperMasterDomainObjectId"]}(${getSlaveByMasterIdParameter["masterDomainObjectIdType"]} ${getSlaveByMasterIdParameter["masterDomainObjectId"]}) {
        if(!ObjectUtils.isEmpty(${getSlaveByMasterIdParameter["masterDomainObjectId"]})) {
            QueryCriteria<${domainObjectName}> criteria = LambdaQueryCriteria.ofEmpty(${domainObjectName}::new)
                .eq(${getSlaveByMasterIdParameter["getMasterDomainObjectIdRef"]}, ${getSlaveByMasterIdParameter["masterDomainObjectId"]});
            return ${mapperInstanceName}.${getSlaveByMasterIdParameter["selectSlaveMapperMethod"]}(criteria);
        }
        return ${getSlaveByMasterIdParameter["serviceMethodReturnEmpty"]};
    }

</#if>
<#if getSlaveByMasterIdsParameter??>
    @Override
    public ${getSlaveByMasterIdsParameter["serviceMethodReturnType"]} get${getSlaveByMasterIdsParameter["slaveDomainObjectsAlias"]}By${getSlaveByMasterIdsParameter["upperMasterDomainObjectIds"]}(${getSlaveByMasterIdsParameter["masterDomainObjectIdsType"]} ${getSlaveByMasterIdsParameter["masterDomainObjectIds"]}) {
        if(!CollectionUtils.isEmpty(${getSlaveByMasterIdsParameter["masterDomainObjectIds"]})) {
            QueryCriteria<${domainObjectName}> criteria = LambdaQueryCriteria.ofEmpty(${domainObjectName}::new)
                .in(${getSlaveByMasterIdsParameter["getMasterDomainObjectIdRef"]}, ${getSlaveByMasterIdsParameter["masterDomainObjectIds"]}.toArray());
            List<${domainObjectName}> ${getSlaveByMasterIdsParameter["lowerSlaveDomainObjectAlias"]}List = ${mapperInstanceName}.selectModelListByCriteria(criteria);
            if(!CollectionUtils.isEmpty(${getSlaveByMasterIdsParameter["lowerSlaveDomainObjectAlias"]}List)) {
                return ${getSlaveByMasterIdsParameter["lowerSlaveDomainObjectAlias"]}List.stream().collect(${getSlaveByMasterIdsParameter["slaveDomainObjectCollector"]});
            }
        }
        return Collections.emptyMap();
    }

</#if>
    @Override
    public List<${domainObjectName}> get${domainObjectAlias}ListByPage(${domainObjectName} condition, Page page) {
    <#list pageListOperationCodes as pageListOperationCode>
        ${pageListOperationCode}
    </#list>
        return MybatisHelper.selectDomainObjectListByPage(${mapperInstanceName}, criteria, page);
    }

    @Override
    @Transactional(transactionManager="${transactionManagerName}", readOnly=true)
    public void forEach${domainObjectAlias}(Consumer<${domainObjectName}> consumer) {
        ${mapperInstanceName}.selectAllModelList().forEach(consumer);
    }

    @Override
    @Transactional(transactionManager="${transactionManagerName}", readOnly=true)
    public void forEach${domainObjectAlias}(ObjIntConsumer<${domainObjectName}> consumer) {
        Cursor<${domainObjectName}> cursor = ${mapperInstanceName}.selectAllModelList();
        int index = 0;
        for (${domainObjectName} item : cursor) {
            consumer.accept(item, index++);
        }
    }

}