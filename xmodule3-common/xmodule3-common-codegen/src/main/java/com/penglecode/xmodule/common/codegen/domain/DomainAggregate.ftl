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
 * ${aggregateObjectTitle}Aggregate
 *
 * @author ${commentAuthor}
 * @version 1.0
 * @since ${generateTime}
 */
public class ${targetObjectName} extends ${aggregateMasterName} {

    private static final long serialVersionUID = 1L;

<#list domainFields as field>
    /** ${field["fieldComment"]} */
    <#list field["fieldAnnotations"] as fieldAnnotation>
    ${fieldAnnotation}
    </#list>
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
}
