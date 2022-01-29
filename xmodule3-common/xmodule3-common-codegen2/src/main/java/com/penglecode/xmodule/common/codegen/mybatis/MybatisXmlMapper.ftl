<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${mapperNamespace}">

    <!-- Auto-Generation Code Start -->
    <!--
        注意事项：
        1、代码生成器自动生成的代码尽量不要轻易修改!!! 如有修改则在重新生成时需要仔细比对合并，以防止代码覆盖问题发生!!!
        2、代码生成器自动生成的代码无法满足需求时，例如自定义的复杂统计查询，请严格写在最下的Customized Code区域，这样重新生成代码时也容易做合并!!!
    -->

    <sql id="SelectBaseColumnsClause">
        <trim suffixOverrides=",">
        <#list queryColumns as column>
            <if test="@${mapperHelperClass}@containsColumn(columns, '${column["fieldName"]}')">
                ${column["columnName"]}   ${column["fieldName"]},
            </if>
        </#list>
        </trim>
    </sql>

    <sql id="UpdateDynamicColumnsClause">
        <trim suffixOverrides=",">
        <#list updateColumns as column>
            <if test="@${mapperHelperClass}@containsColumn(columns, '${column["fieldName"]}')">
                ${tableAliasName}.${column["columnName"]} = <#noparse>#{</#noparse>columns.${column["fieldName"]}, jdbcType=${column["jdbcTypeName"]}<#noparse>}</#noparse>,
            </if>
        </#list>
        </trim>
    </sql>

    <insert id="insertModel" <#if idGenStrategy == "IDENTITY">keyProperty="${idFieldName}"</#if> parameterType="${domainObjectName}" statementType="PREPARED"<#if idGenStrategy??> useGeneratedKeys="true"</#if>>
    <#if idGenStrategy == "SEQUENCE">
        <selectKey resultType="${idFieldType}" order="BEFORE" keyProperty="${idFieldName}">
            SELECT ${idGenParameter}.NEXTVAL AS ${idFieldName} FROM dual
        </selectKey>
    </#if>
        INSERT INTO ${domainEntityTable}(
        <#list insertColumns as column>
            ${column["columnName"]}<#if column_has_next>,</#if>
        </#list>
        ) VALUES (
        <#list insertColumns as column>
            <#noparse>#{</#noparse>${column["fieldName"]}, jdbcType=${column["jdbcTypeName"]}<#noparse>}</#noparse><#if column_has_next>,</#if>
        </#list>
        )
    </insert>

    <update id="updateModelById" parameterType="java.util.Map" statementType="PREPARED">
        UPDATE ${domainEntityTable} ${tableAliasName}
           SET <include refid="UpdateDynamicColumnsClause"/>
         WHERE <#list idColumns as column>${tableAliasName}.${column["idColumnName"]} = <#noparse>#{</#noparse><#if (idColumns?size == 1)>id<#else>id.${column["idFieldName"]}</#if>, jdbcType=${column["idJdbcTypeName"]}<#noparse>}</#noparse><#if column_has_next> AND </#if></#list>
    </update>

    <update id="updateModelByCriteria" parameterType="java.util.Map" statementType="PREPARED">
        UPDATE ${domainEntityTable} ${tableAliasName}
           SET <include refid="UpdateDynamicColumnsClause"/>
        <include refid="CommonMybatisMapper.UpdateWhereCriteriaClause"/>
    </update>

    <delete id="deleteModelById" parameterType="java.util.Map" statementType="PREPARED">
        DELETE ${deleteTargetAliasName} FROM ${domainEntityTable} ${tableAliasName}
         WHERE <#list idColumns as column>${tableAliasName}.${column["idColumnName"]} = <#noparse>#{</#noparse><#if (idColumns?size == 1)>id<#else>id.${column["idFieldName"]}</#if>, jdbcType=${column["idJdbcTypeName"]}<#noparse>}</#noparse><#if column_has_next> AND </#if></#list>
    </delete>

    <delete id="deleteModelByIds" parameterType="java.util.Map" statementType="PREPARED">
    <#if (idColumns?size == 1)>
        DELETE ${deleteTargetAliasName} FROM ${domainEntityTable} ${tableAliasName}
         WHERE ${tableAliasName}.${idColumnName} in
        <foreach collection="ids" index="index" item="id" open="(" separator="," close=")">
            <#noparse>#{</#noparse>id, jdbcType=${idJdbcTypeName}<#noparse>}</#noparse>
        </foreach>
    <#else>
        DELETE ${deleteTargetAliasName} FROM ${domainEntityTable} ${tableAliasName}
         WHERE <foreach collection="ids" index="index" item="id" open="" separator=" OR " close="">(<#list idColumns as column>${tableAliasName}.${column["idColumnName"]} = <#noparse>#{</#noparse>id.${column["idFieldName"]}, jdbcType=${column["idJdbcTypeName"]}<#noparse>}</#noparse><#if column_has_next> AND </#if></#list>)</foreach>
    </#if>
    </delete>

    <delete id="deleteModelByCriteria" parameterType="java.util.Map" statementType="PREPARED">
        DELETE ${deleteTargetAliasName} FROM ${domainEntityTable} ${tableAliasName}
        <include refid="CommonMybatisMapper.DeleteWhereCriteriaClause"/>
    </delete>

    <select id="selectModelById" parameterType="java.util.Map" resultType="${domainObjectName}" statementType="PREPARED">
        SELECT <include refid="SelectBaseColumnsClause"/>
          FROM ${domainEntityTable} ${tableAliasName}
         WHERE <#list idColumns as column>${tableAliasName}.${column["idColumnName"]} = <#noparse>#{</#noparse><#if (idColumns?size == 1)>id<#else>id.${column["idFieldName"]}</#if>, jdbcType=${column["idJdbcTypeName"]}<#noparse>}</#noparse><#if column_has_next> AND </#if></#list>
    </select>

    <select id="selectModelByCriteria" parameterType="java.util.Map" resultType="${domainObjectName}" statementType="PREPARED">
        SELECT <include refid="SelectBaseColumnsClause"/>
          FROM ${domainEntityTable} ${tableAliasName}
        <include refid="CommonMybatisMapper.SelectWhereCriteriaClause"/>
    </select>

    <select id="selectModelCountByCriteria" parameterType="java.util.Map" resultType="java.lang.Integer" statementType="PREPARED">
        SELECT COUNT(*)
          FROM ${domainEntityTable} ${tableAliasName}
        <include refid="CommonMybatisMapper.SelectWhereCriteriaClause"/>
    </select>

    <select id="selectModelListByIds" parameterType="java.util.Map" resultType="${domainObjectName}" statementType="PREPARED">
    <#if (idColumns?size == 1)>
        SELECT <include refid="SelectBaseColumnsClause"/>
          FROM ${domainEntityTable} ${tableAliasName}
         WHERE ${tableAliasName}.${idColumnName} in
        <foreach collection="ids" index="index" item="id" open="(" separator="," close=")">
            <#noparse>#{</#noparse>id, jdbcType=${idJdbcTypeName}<#noparse>}</#noparse>
        </foreach>
    <#else>
        SELECT <include refid="SelectBaseColumnsClause"/>
          FROM ${domainEntityTable} ${tableAliasName}
         WHERE <foreach collection="ids" index="index" item="id" open="" separator=" OR " close="">(<#list idColumns as column>${tableAliasName}.${column["idColumnName"]} = <#noparse>#{</#noparse>id.${column["idFieldName"]}, jdbcType=${column["idJdbcTypeName"]}<#noparse>}</#noparse><#if column_has_next> AND </#if></#list>)</foreach>
    </#if>
    </select>

    <select id="selectAllModelList" parameterType="java.util.Map" resultType="${domainObjectName}" resultSetType="FORWARD_ONLY" fetchSize="${cursorFetchSize}" statementType="PREPARED">
        SELECT <include refid="SelectBaseColumnsClause"/>
          FROM ${domainEntityTable} ${tableAliasName}
    </select>

    <select id="selectAllModelCount" parameterType="java.util.Map" resultType="java.lang.Integer" statementType="PREPARED">
        SELECT COUNT(*) FROM ${domainEntityTable} ${tableAliasName}
    </select>

    <select id="selectModelListByCriteria" parameterType="java.util.Map" resultType="${domainObjectName}" statementType="PREPARED">
        SELECT <include refid="SelectBaseColumnsClause"/>
          FROM ${domainEntityTable} ${tableAliasName}
        <include refid="CommonMybatisMapper.SelectWhereCriteriaClause"/>
        <include refid="CommonMybatisMapper.SelectOrderByCriteriaClause"/>
    </select>

    <select id="selectModelPageListByCriteria" parameterType="java.util.Map" resultType="${domainObjectName}" statementType="PREPARED">
        SELECT <include refid="SelectBaseColumnsClause"/>
          FROM ${domainEntityTable} ${tableAliasName}
        <include refid="CommonMybatisMapper.SelectWhereCriteriaClause"/>
        <include refid="CommonMybatisMapper.SelectOrderByCriteriaClause"/>
    </select>

    <select id="selectModelPageCountByCriteria" parameterType="java.util.Map" resultType="java.lang.Integer" statementType="PREPARED">
        SELECT COUNT(*)
          FROM ${domainEntityTable} ${tableAliasName}
        <include refid="CommonMybatisMapper.SelectWhereCriteriaClause"/>
    </select>
    <!-- Auto-Generation Code End -->

    <!-- Customized Code Start -->
    <!-- 自定义的代码请严格写在该区域内!!! -->


    <!-- Customized Code End -->
</mapper>