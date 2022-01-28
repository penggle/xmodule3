package ${targetPackage};

<#if (targetProjectImports?size > 0 || targetThirdImports?size > 0)>
    <#list targetProjectImports as ipm>
        import ${ipm};
    </#list>
    <#list targetThirdImports as ipm>
        import ${ipm};
    </#list>

</#if>
<#if (targetJdkImports?size > 0)>
    <#list targetJdkImports as ipm>
        import ${ipm};
    </#list>

</#if>
/**
 * ${targetComment}
 *
 * @author ${targetAuthor}
 * @version ${targetVersion}
 * @since ${targetCreated}
 */
public class ${targetClass} implements EntityObject {

    private static final long serialVersionUID = 1L;

<#list entityInherentFields as field>
    /** ${field["entityFieldComment"]} */
    <#list field["entityFieldAnnotations"] as fieldAnnotation>
    ${fieldAnnotation}
    </#list>
    private ${field["entityFieldType"]} ${field["entityFieldName"]};

</#list>
<#if (entitySupportFields?size > 0)>
    //以下属于辅助字段

</#if>
<#list entitySupportFields as field>
    /** ${field["entityFieldComment"]} */
    private ${field["entityFieldType"]} ${field["entityFieldName"]};

</#list>
<#list allEntityFields as field>
    public ${field["entityFieldType"]} ${field["entityFieldGetterName"]}() {
        return ${field["entityFieldName"]};
    }

    public void ${field["entityFieldSetterName"]}(${field["entityFieldType"]} ${field["entityFieldName"]}) {
        this.${field["entityFieldName"]} = ${field["entityFieldName"]};
    }

</#list>
    @Override
    public ${entityIdFieldType} identity() {
        return ${entityIdFieldName};
    }

<#if (entityDecodeEnumFields?size > 0)>
    @Override
    public ${targetClass} beforeOutbound() {
    <#list entityDecodeEnumFields as field>
        Optional.ofNullable(${field["refEnumTypeName"]}.of(${field["entityFieldName"]})).ifPresent(em -> ${field["entityFieldSetterName"]}(em.${field["refEnumNameFieldGetterName"]}()));
    </#list>
        return this;
    }
</#if>
}
