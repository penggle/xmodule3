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
 * ${aggregateObjectTitle} 领域服务实现
 *
 * @author ${commentAuthor}
 * @version 1.0
 * @since ${generateTime}
 */
@Service("${serviceBeanName}")
public class ${targetObjectName} implements ${serviceInterfaceName} {

<#list domainServices as domainService>
    @Resource(name="${domainService["domainServiceBeanName"]}")
    private ${domainService["domainServiceName"]} ${domainService["domainServiceInstanceName"]};

</#list>
    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void create${aggregateObjectAlias}(${aggregateObjectName} ${aggregateObjectVarName}) {
    <#list createOperationCodes as createOperationCode>
        ${createOperationCode}
    </#list>
    }

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void modify${aggregateObjectAlias}ById(${aggregateObjectName} ${aggregateObjectVarName}) {
    <#list modifyByIdOperationCodes as modifyByIdOperationCode>
        ${modifyByIdOperationCode}
    </#list>
    }

    @Override
    @Transactional(transactionManager="${transactionManagerName}", rollbackFor=Exception.class)
    public void remove${aggregateObjectAlias}ById(${aggregateObjectIdType} id) {
    <#list removeByIdOperationCodes as removeByIdOperationCode>
        ${removeByIdOperationCode}
    </#list>
    }

    @Override
    public ${aggregateObjectName} get${aggregateObjectAlias}ById(${aggregateObjectIdType} id, boolean cascade) {
    <#list getByIdOperationCodes as getByIdOperationCode>
        ${getByIdOperationCode}
    </#list>
    }

    @Override
    public List<${aggregateObjectName}> get${aggregateObjectAlias}ListByIds(List<${aggregateObjectIdType}> ids, boolean cascade) {
    <#list getByIdsOperationCodes as getByIdsOperationCode>
        ${getByIdsOperationCode}
    </#list>
    }

    @Override
    public List<${aggregateObjectName}> get${aggregateObjectAlias}ListByPage(${aggregateObjectName} condition, Page page, boolean cascade) {
    <#list pageListOperationCodes as pageListOperationCode>
        ${pageListOperationCode}
    </#list>
    }

    @Override
    @Transactional(transactionManager="${transactionManagerName}", readOnly=true)
    public void forEach${aggregateObjectAlias}(Consumer<${aggregateObjectName}> consumer) {
    <#list forEach1OperationCodes as forEach1OperationCode>
        ${forEach1OperationCode}
    </#list>
    }

    @Override
    @Transactional(transactionManager="${transactionManagerName}", readOnly=true)
    public void forEach${aggregateObjectAlias}(ObjIntConsumer<${aggregateObjectName}> consumer) {
    <#list forEach2OperationCodes as forEach2OperationCode>
        ${forEach2OperationCode}
    </#list>
    }

}