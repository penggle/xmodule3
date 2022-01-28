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
public enum ${targetClass} {

<#list enumValues as item>
    ${item["enumValue"]}(${item["enumCode"]}, ${item["enumName"]})<#if (item_has_next)>,<#else>;</#if>
</#list>

    private final ${enumCodeFieldType} ${enumCodeFieldName};

    private final ${enumNameFieldType} ${enumNameFieldName};

    ${enumName}(${enumCodeFieldType} ${enumCodeFieldName}, ${enumNameFieldType} ${enumNameFieldName}) {
        this.${enumCodeFieldName} = ${enumCodeFieldName};
        this.${enumNameFieldName} = ${enumNameFieldName};
    }

    public ${enumCodeFieldType} ${enumCodeGetterName}() {
        return ${enumCodeFieldName};
    }

    public ${enumNameFieldType} ${enumNameGetterName}() {
        return ${enumNameFieldName};
    }

    public static ${targetClass} of(${enumCodeFieldType} ${enumCodeFieldName}) {
        if(${enumCodeFieldName} != null) {
            for(${targetClass} em : values()) {
                if(em.${enumCodeGetterName}().equals(${enumCodeFieldName})) {
                    return em;
                }
            }
        }
        return null;
    }
}
