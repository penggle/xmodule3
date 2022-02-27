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
public class ${targetClass} implements EntityObject {

    private static final long serialVersionUID = 1L;

<#list inherentFields as field>
    /** ${field.fieldComment} */
    <#list field.fieldAnnotations as fieldAnnotation>
    ${fieldAnnotation}
    </#list>
    private ${field.fieldType} ${field.fieldName};

</#list>
<#if (supportFields?size > 0)>
    //以下属于辅助字段

</#if>
<#list supportFields as field>
    /** ${field.fieldComment} */
    private ${field.fieldType} ${field.fieldName};

</#list>
<#list allFields as field>
    public ${field.fieldType} ${field.fieldGetterName}() {
        return ${field.fieldName};
    }

    public void ${field.fieldSetterName}(${field.fieldType} ${field.fieldName}) {
        this.${field.fieldName} = ${field.fieldName};
    }

</#list>
    @Override
    public ${idFieldType} identity() {
        return ${idFieldName};
    }

<#if (enumFieldDecodes?size > 0)>
    @Override
    public ${targetClass} beforeOutbound() {
    <#list enumFieldDecodes as decode>
        Optional.ofNullable(${decode.refEnumTypeName}.of(${decode.entityFieldName})).ifPresent(em -> ${decode.entityFieldSetterName}(em.${decode.refEnumNameFieldGetterName}()));
    </#list>
        return this;
    }
</#if>
}
