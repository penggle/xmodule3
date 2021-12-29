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
 * ${domainObjectTitle} Mybatis-Mapper接口
 *
 * @author ${commentAuthor}
 * @version 1.0
 * @since ${generateTime}
 */
<#list mapperAnnotations as annotation>
${annotation}
</#list>
public interface ${targetObjectName} extends BaseMybatisMapper<${domainObjectName}> {

}