package com.penglecode.xmodule.common.codegen.mybatis;

import com.penglecode.xmodule.common.codegen.ModuleCodeGenerator;
import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.CodegenParameter;
import com.penglecode.xmodule.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.xmodule.common.codegen.support.IdGenerator;
import com.penglecode.xmodule.common.mybatis.MapperHelper;
import com.penglecode.xmodule.common.mybatis.SupportedDatabaseType;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.SpringUtils;
import com.penglecode.xmodule.common.util.TemplateUtils;
import org.springframework.core.OrderComparator;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mybatis代码生成器
 * 专门用于生成指定bizModule模块下的Mybatis代码(如XxxMpper.java、XxxMapper.xml)
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/26 17:13
 */
public class MybatisCodeGenerator extends ModuleCodeGenerator<MybatisCodegenConfigProperties> {

    private final MybatisCodegenDialect defaultMybatisCodegenDialect = new DefaultMybatisCodegenDialect();

    private volatile Map<SupportedDatabaseType,MybatisCodegenDialect> dbTypedMybatisCodegenDialects;

    public MybatisCodeGenerator(String module) {
        super(module);
    }

    @Override
    protected String getCodeName() {
        return "Mybatis代码";
    }

    @Override
    protected void executeGenerate() throws Exception {
        MybatisCodegenConfigProperties codegenConfig = getCodegenConfig();
        Map<String,DomainEntityConfig> domainEntityConfigs = codegenConfig.getDomain().getDomainEntities();
        if(!CollectionUtils.isEmpty(domainEntityConfigs)) {
            for(Map.Entry<String,DomainEntityConfig> entry : domainEntityConfigs.entrySet()) {
                DomainEntityConfig domainEntityConfig = entry.getValue();
                //1、生成XxxMapper.java接口
                CodegenContext<MybatisCodegenConfigProperties,MybatisJavaMapperConfig,DomainEntityConfig> codegenContext1 = new CodegenContext<>(codegenConfig, codegenConfig.getMybatis().getJavaMapperConfig(), domainEntityConfig);
                generateTarget(codegenContext1, createJavaMapperCodegenParameter(codegenContext1));
                //2、生成XxxMapper.xml
                CodegenContext<MybatisCodegenConfigProperties,MybatisXmlMapperConfig,DomainEntityConfig> codegenContext2 = new CodegenContext<>(codegenConfig, codegenConfig.getMybatis().getXmlMapperConfig(), domainEntityConfig);
                generateTarget(codegenContext2, createXmlMapperCodegenParameter(codegenContext2));
            }
        }
    }

    /**
     * 创建Mybatis Java-Mapper代码生成模板参数
     * @param codegenContext
     * @return
     */
    protected CodegenParameter createJavaMapperCodegenParameter(CodegenContext<MybatisCodegenConfigProperties, MybatisJavaMapperConfig, DomainEntityConfig> codegenContext) {
        CodegenParameter codegenParameter = super.createCodegenParameter(codegenContext, "MybatisJavaMapper.ftl");
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(codegenContext.getDomainObjectConfig().getGeneratedTargetName(codegenContext.getDomainObjectConfig().getDomainEntityName(), true, false)));
        codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(BaseMybatisMapper.class.getName()));

        Set<String> mapperAnnotationSet = codegenContext.getCodegenConfig().getMybatis().getJavaMapperConfig().getMapperAnnotations();
        List<String> mapperAnnotations = new ArrayList<>();
        if(!CollectionUtils.isEmpty(mapperAnnotationSet)) {
            for(String mapperAnnotation : mapperAnnotationSet) {
                String[] mapperAnnotationArray = mapperAnnotation.split(":");
                mapperAnnotations.add(parseMapperAnnotations(mapperAnnotationArray[1], codegenContext));
                codegenParameter.getTargetAllImportTypes().add(new FullyQualifiedJavaType(mapperAnnotationArray[0]));
            }
        }
        codegenParameter.put("mapperAnnotations", mapperAnnotations);
        return codegenParameter;
    }

    /**
     * 创建Mybatis Xml-Mapper代码生成模板参数
     * @param codegenContext
     * @return
     */
    protected CodegenParameter createXmlMapperCodegenParameter(CodegenContext<MybatisCodegenConfigProperties, MybatisXmlMapperConfig, DomainEntityConfig> codegenContext) {
        CodegenParameter codegenParameter = super.createCodegenParameter(codegenContext, "MybatisXmlMapper.ftl");
        MybatisCodegenDialect mybatisCodegenDialect = getMybatisCodegenDialect(codegenContext.getDomainObjectConfig().getIntrospectConfig().getIntrospectDatabaseType());
        codegenParameter.put("domainObjectName", codegenContext.getDomainObjectConfig().getDomainEntityName());
        codegenParameter.put("mapperNamespace", codegenContext.getTargetConfig().getGeneratedTargetName(codegenContext.getDomainObjectConfig().getDomainEntityName(), true, false));
        codegenParameter.put("mapperHelperClass", MapperHelper.class.getName());
        codegenParameter.put("domainEntityTable", codegenContext.getDomainObjectConfig().getDomainEntityTable());
        codegenParameter.put("tableAliasName", QueryCriteria.TABLE_ALIAS_NAME);
        codegenParameter.put("deleteTargetAliasName", mybatisCodegenDialect.getDeleteTargetAliasName(codegenContext));
        codegenParameter.put("cursorFetchSize", String.valueOf(mybatisCodegenDialect.getCursorFetchSize(codegenContext)));
        attachInsertUpdateQueryColumns(codegenContext, codegenParameter, mybatisCodegenDialect);
        attachIdColumns(codegenContext, codegenParameter);
        return codegenParameter;
    }

    /**
     * 附带上领域实体的insert/update/select字段参数
     * @param codegenContext
     * @param codegenParameter
     * @param mybatisCodegenDialect
     */
    protected void attachInsertUpdateQueryColumns(CodegenContext<MybatisCodegenConfigProperties, MybatisXmlMapperConfig, DomainEntityConfig> codegenContext, CodegenParameter codegenParameter, MybatisCodegenDialect mybatisCodegenDialect) {
        List<Map<String,Object>> queryColumns = new ArrayList<>();
        List<Map<String,Object>> updateColumns = new ArrayList<>();
        List<Map<String,Object>> insertColumns = new ArrayList<>();
        for(Map.Entry<String,DomainEntityColumnConfig> entry : codegenContext.getDomainObjectConfig().getDomainEntityColumns().entrySet()) {
            DomainEntityColumnConfig columnConfig = entry.getValue();
            Map<String,Object> selectColumn = new HashMap<>();
            selectColumn.put("columnName", mybatisCodegenDialect.getSelectColumnClause(columnConfig.getColumnName(), codegenContext));
            selectColumn.put("fieldName", columnConfig.getIntrospectedColumn().getJavaFieldName());
            selectColumn.put("fieldType", columnConfig.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters());
            selectColumn.put("jdbcTypeName", columnConfig.getIntrospectedColumn().getJdbcTypeName());
            queryColumns.add(selectColumn);
            if(columnConfig.isColumnOnUpdate() || columnConfig.isColumnOnInsert()) { //是否是插入列/更新列
                Map<String,Object> insertOrUpdateColumn = new HashMap<>();
                insertOrUpdateColumn.put("columnName", columnConfig.getColumnName());
                insertOrUpdateColumn.put("fieldName", columnConfig.getIntrospectedColumn().getJavaFieldName());
                insertOrUpdateColumn.put("fieldType", columnConfig.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters());
                insertOrUpdateColumn.put("jdbcTypeName", columnConfig.getIntrospectedColumn().getJdbcTypeName());
                if(columnConfig.isColumnOnUpdate()) {
                    updateColumns.add(insertOrUpdateColumn);
                }
                if(columnConfig.isColumnOnInsert()) {
                    insertColumns.add(insertOrUpdateColumn);
                }
            }
        }
        codegenParameter.put("queryColumns", queryColumns);
        codegenParameter.put("updateColumns", updateColumns);
        codegenParameter.put("insertColumns", insertColumns);
    }

    /**
     * 附带上领域实体的ID字段参数
     * @param codegenContext
     * @param codegenParameter
     */
    protected void attachIdColumns(CodegenContext<MybatisCodegenConfigProperties, MybatisXmlMapperConfig, DomainEntityConfig> codegenContext, CodegenParameter codegenParameter) {
        List<DomainEntityColumnConfig> idColumnConfigs = codegenContext.getDomainObjectConfig().getIdColumns();
        if(idColumnConfigs.size() == 1) { //单一主键
            DomainEntityColumnConfig singleIdColumn = idColumnConfigs.get(0);
            String idFieldType = singleIdColumn.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters();
            codegenParameter.put("idFieldType", idFieldType);
            codegenParameter.put("idColumnName", singleIdColumn.getColumnName());
            codegenParameter.put("idFieldName", singleIdColumn.getIntrospectedColumn().getJavaFieldName());
            codegenParameter.put("idJdbcTypeName", singleIdColumn.getIntrospectedColumn().getJdbcTypeName());
            IdGenerator idGenerator = singleIdColumn.getIdGenerator();
            if(idGenerator != null) {
                codegenParameter.put("idGenStrategy", idGenerator.getStrategy().toString());
                codegenParameter.put("idGenParameter", idGenerator.getParameter());
            } else {
                codegenParameter.put("idGenStrategy", "NONE");
            }
        } else {
            codegenParameter.put("idFieldType", Map.class.getName());
            codegenParameter.put("idGenStrategy", "NONE");
        }
        List<Map<String,Object>> idColumns = new ArrayList<>();
        for(DomainEntityColumnConfig idColumnConfig : idColumnConfigs) {
            Map<String,Object> idColumn = new HashMap<>();
            idColumn.put("idFieldType", idColumnConfig.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters());
            idColumn.put("idColumnName", idColumnConfig.getColumnName());
            idColumn.put("idFieldName", idColumnConfig.getIntrospectedColumn().getJavaFieldName());
            idColumn.put("idJdbcTypeName", idColumnConfig.getIntrospectedColumn().getJdbcTypeName());
            idColumns.add(idColumn);
        }
        codegenParameter.put("idColumns", idColumns);
    }

    protected MybatisCodegenDialect getMybatisCodegenDialect(SupportedDatabaseType dialectDbType) {
        Map<SupportedDatabaseType,MybatisCodegenDialect> mybatisCodegenDialects = dbTypedMybatisCodegenDialects;
        if(mybatisCodegenDialects == null) { //DCL Check
            mybatisCodegenDialects = getDbTypedMybatisCodegenDialects();
        }
        return mybatisCodegenDialects.getOrDefault(dialectDbType, defaultMybatisCodegenDialect);
    }

    protected synchronized Map<SupportedDatabaseType,MybatisCodegenDialect> getDbTypedMybatisCodegenDialects() {
        if(dbTypedMybatisCodegenDialects == null) {
            Map<String,MybatisCodegenDialect> allMybatisCodegenDialects = SpringUtils.getBeansOfType(MybatisCodegenDialect.class);
            dbTypedMybatisCodegenDialects = allMybatisCodegenDialects.values()
                    .stream().collect(Collectors.toMap(MybatisCodegenDialect::getDatabaseType, Function.identity(), BinaryOperator.maxBy(OrderComparator.INSTANCE)));
        }
        return dbTypedMybatisCodegenDialects;
    }

    protected String parseMapperAnnotations(String mapperAnnotations, CodegenContext<MybatisCodegenConfigProperties, MybatisJavaMapperConfig, DomainEntityConfig> codegenContext) {
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("runtimeDataSource", codegenContext.getDomainObjectConfig().getRuntimeDataSource());
        return TemplateUtils.parseTemplate(mapperAnnotations, parameter);
    }

}
