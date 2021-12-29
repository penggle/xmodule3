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
 * ${enumTitle}
 *
 * @author ${commentAuthor}
 * @version 1.0
 * @since ${generateTime}
 */
public enum ${targetObjectName} {

<#list enumValues as item>
    ${item["value"]}(${item["code"]}, ${item["name"]})<#if (item_has_next)>,<#else>;</#if>
</#list>

    private final ${codeFieldType} ${codeFieldName};

    private final ${nameFieldType} ${nameFieldName};

    ${enumName}(${codeFieldType} ${codeFieldName}, ${nameFieldType} ${nameFieldName}) {
        this.${codeFieldName} = ${codeFieldName};
        this.${nameFieldName} = ${nameFieldName};
    }

    public ${codeFieldType} ${codeGetterName}() {
        return ${codeFieldName};
    }

    public ${nameFieldType} ${nameGetterName}() {
        return ${nameFieldName};
    }

    public static ${enumName} of(${codeFieldType} ${codeFieldName}) {
        if(${codeFieldName} != null) {
            for(${enumName} em : values()) {
                if(em.${codeGetterName}().equals(${codeFieldName})) {
                    return em;
                }
            }
        }
        return null;
    }
}
