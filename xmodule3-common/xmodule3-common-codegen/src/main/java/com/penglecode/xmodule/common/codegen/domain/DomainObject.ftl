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
 * ${domainObjectTitle}Domain-Object
 *
 * @author ${commentAuthor}
 * @version 1.0
 * @since ${generateTime}
 */
public class ${targetObjectName} implements BaseDO {

    private static final long serialVersionUID = 1L;

<#list domainFields as field>
    /** ${field["fieldComment"]} */
    <#list field["fieldAnnotations"] as fieldAnnotation>
    ${fieldAnnotation}
    </#list>
    private ${field["fieldType"]} ${field["fieldName"]};

</#list>
<#if (supportFields?size > 0)>
    //以下属于辅助字段

</#if>
<#list supportFields as field>
    /** ${field["fieldComment"]} */
    private ${field["fieldType"]} ${field["fieldName"]};

</#list>
<#list allFields as field>
    public ${field["fieldType"]} ${field["fieldGetterName"]}() {
        return ${field["fieldName"]};
    }

    public void ${field["fieldSetterName"]}(${field["fieldType"]} ${field["fieldName"]}) {
        this.${field["fieldName"]} = ${field["fieldName"]};
    }

</#list>
    @Override
    public ${pkFieldType} identity() {
        return ${pkFieldName};
    }

<#if (overrideProcessMethod)>
    @Override
    public ${domainObjectName} processOutbound() {
    <#list decodeEnumFields as field>
        Optional.ofNullable(${field["refEnumTypeName"]}.of(${field["refFieldName"]})).ifPresent(em -> ${field["fieldSetterName"]}(em.${field["refEnumGetterName"]}()));
    </#list>
        return this;
    }
</#if>
}
