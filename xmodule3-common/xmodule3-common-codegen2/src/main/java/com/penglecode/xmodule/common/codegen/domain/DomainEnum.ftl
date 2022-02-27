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
public enum ${targetClass} {

<#list enumValues as item>
    ${item.enumValue}(${item.codeValue}, ${item.nameValue})<#if (item_has_next)>,<#else>;</#if>
</#list>

    private final ${codeFieldType} ${codeFieldName};

    private final ${nameFieldType} ${nameFieldName};

    ${targetClass}(${codeFieldType} ${codeFieldName}, ${nameFieldType} ${nameFieldName}) {
        this.${codeFieldName} = ${codeFieldName};
        this.${nameFieldName} = ${nameFieldName};
    }

    public ${codeFieldType} ${codeGetterName}() {
        return ${codeFieldName};
    }

    public ${nameFieldType} ${nameGetterName}() {
        return ${nameFieldName};
    }

    public static ${targetClass} of(${codeFieldType} ${codeFieldName}) {
        if(${codeFieldName} != null) {
            for(${targetClass} em : values()) {
                if(em.${codeGetterName}().equals(${codeFieldName})) {
                    return em;
                }
            }
        }
        return null;
    }
}
