package com.penglecode.xmodule.common.codegen.mybatis;

import com.penglecode.xmodule.common.codegen.ModuleCodeGenerator;
import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.*;
import com.penglecode.xmodule.common.mybatis.DatabaseType;
import com.penglecode.xmodule.common.mybatis.MapperHelper;
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
 * @created 2021/8/16 19:15
 */
public class MybatisCodeGenerator extends ModuleCodeGenerator<MybatisCodegenConfigProperties> {

    private final MybatisCodegenDialect defaultMybatisCodegenDialect = new DefaultMybatisCodegenDialect();

    private volatile Map<DatabaseType,MybatisCodegenDialect> dbTypedMybatisCodegenDialects;

    public MybatisCodeGenerator(String bizModule) {
        super(bizModule);
    }

    @Override
    protected void generateCodes() throws Exception {
        MybatisCodegenConfigProperties codegenConfig = getModuleCodegenConfig();
        Map<String, DomainObjectConfigProperties> domainObjectConfigs = codegenConfig.getDomain().getDomainObjects();
        if(!CollectionUtils.isEmpty(domainObjectConfigs)) {
            for(Map.Entry<String,DomainObjectConfigProperties> entry : domainObjectConfigs.entrySet()) {
                DomainObjectConfigProperties domainObjectConfig = entry.getValue();
                //1、生成XxxMapper.java接口
                DomainBoundedTargetConfigProperties<MybatisJavaMapperConfigProperties> targetConfig1 = new DomainBoundedTargetConfigProperties<>(codegenConfig.getMybatis().getJavaMapperConfig(), domainObjectConfig, null);
                generateCode(codegenConfig, targetConfig1, createJavaMapperTemplateParameter(codegenConfig, targetConfig1));
                //2、生成XxxMapper.xml
                DomainBoundedTargetConfigProperties<MybatisXmlMapperConfigProperties> targetConfig2 = new DomainBoundedTargetConfigProperties<>(codegenConfig.getMybatis().getXmlMapperConfig(), domainObjectConfig, null);
                generateCode(codegenConfig, targetConfig2, createXmlMapperTemplateParameter(codegenConfig, targetConfig2));
            }
        }
    }

    @Override
    protected CodegenModule getCodeModule() {
        return CodegenModule.MYBATIS;
    }

    protected TemplateParameter createJavaMapperTemplateParameter(MybatisCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<MybatisJavaMapperConfigProperties> targetConfig) {
        TemplateParameter templateParameter = new TemplateParameter();
        Set<FullyQualifiedJavaType> allImportedTypes = new HashSet<>();
        addCommonTemplateParameter(templateParameter, codegenConfig, targetConfig, "MybatisJavaMapper.ftl");
        allImportedTypes.add(new FullyQualifiedJavaType(targetConfig.getDomainObjectConfig().getGeneratedTargetName(targetConfig, true, false)));
        allImportedTypes.add(new FullyQualifiedJavaType(BaseMybatisMapper.class.getName()));

        Set<String> mapperAnnotationSet = codegenConfig.getMybatis().getJavaMapperConfig().getMapperAnnotations();
        List<String> mapperAnnotations = new ArrayList<>();
        if(!CollectionUtils.isEmpty(mapperAnnotationSet)) {
            for(String mapperAnnotation : mapperAnnotationSet) {
                String[] mapperAnnotationArray = mapperAnnotation.split(":");
                mapperAnnotations.add(parseMapperAnnotations(mapperAnnotationArray[1], codegenConfig, targetConfig));
                allImportedTypes.add(new FullyQualifiedJavaType(mapperAnnotationArray[0]));
            }
        }
        templateParameter.put("mapperAnnotations", mapperAnnotations);
        calculateImportedTypes(allImportedTypes, templateParameter);
        return templateParameter;
    }

    protected TemplateParameter createXmlMapperTemplateParameter(MybatisCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<MybatisXmlMapperConfigProperties> targetConfig) {
        MybatisCodegenDialect mybatisCodegenDialect = getMybatisCodegenDialect(targetConfig.getDomainObjectConfig().getIntrospectConfig().getIntrospectDialect());
        TemplateParameter templateParameter = new TemplateParameter();
        addCommonTemplateParameter(templateParameter, codegenConfig, targetConfig, "MybatisXmlMapper.ftl");
        templateParameter.put("mapperNamespace", targetConfig.getGeneratedTargetConfig().getGeneratedTargetName(targetConfig, true, false));
        templateParameter.put("mapperHelperClass", MapperHelper.class.getName());
        templateParameter.put("domainTableName", targetConfig.getDomainObjectConfig().getDomainTableName());
        templateParameter.put("tableAliasName", QueryCriteria.TABLE_ALIAS_NAME);
        CodegenContext codegenContext = new CodegenContext(codegenConfig, targetConfig.getDomainObjectConfig(), null, null);
        templateParameter.put("deleteTableAliasName", mybatisCodegenDialect.getDeleteTableAliasName(codegenContext));
        templateParameter.put("cursorFetchSize", String.valueOf(mybatisCodegenDialect.getCursorFetchSize(codegenContext)));
        processInsertUpdateQueryColumns(templateParameter, codegenConfig, targetConfig, mybatisCodegenDialect);
        processPrimaryKeyColumns(templateParameter, codegenConfig, targetConfig);
        return templateParameter;
    }

    protected void processInsertUpdateQueryColumns(TemplateParameter templateParameter, MybatisCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<MybatisXmlMapperConfigProperties> targetConfig, MybatisCodegenDialect mybatisCodegenDialect) {
        List<Map<String,Object>> modelQueryColumns = new ArrayList<>();
        List<Map<String,Object>> modelUpdateColumns = new ArrayList<>();
        List<Map<String,Object>> modelInsertColumns = new ArrayList<>();
        for(Map.Entry<String,DomainObjectColumnConfigProperties> entry : targetConfig.getDomainObjectConfig().getDomainObjectColumns().entrySet()) {
            DomainObjectColumnConfigProperties columnConfig = entry.getValue();
            Map<String,Object> selectColumn = new HashMap<>();
            selectColumn.put("columnName", mybatisCodegenDialect.getSelectColumnClause(new CodegenContext(codegenConfig, targetConfig.getDomainObjectConfig(), columnConfig, targetConfig.getDomainObjectConfig().getDomainObjectFields().get(entry.getKey()))));
            selectColumn.put("fieldName", columnConfig.getIntrospectedColumn().getJavaFieldName());
            selectColumn.put("fieldType", columnConfig.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters());
            selectColumn.put("jdbcTypeName", columnConfig.getIntrospectedColumn().getJdbcTypeName());
            modelQueryColumns.add(selectColumn);
            if(columnConfig.isColumnOnUpdate() || columnConfig.isColumnOnInsert()) { //是否是插入列/更新列
                Map<String,Object> insertOrUpdateColumn = new HashMap<>();
                insertOrUpdateColumn.put("columnName", columnConfig.getColumnName());
                insertOrUpdateColumn.put("fieldName", columnConfig.getIntrospectedColumn().getJavaFieldName());
                insertOrUpdateColumn.put("fieldType", columnConfig.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters());
                insertOrUpdateColumn.put("jdbcTypeName", columnConfig.getIntrospectedColumn().getJdbcTypeName());
                if(columnConfig.isColumnOnUpdate()) {
                    modelUpdateColumns.add(insertOrUpdateColumn);
                }
                if(columnConfig.isColumnOnInsert()) {
                    modelInsertColumns.add(insertOrUpdateColumn);
                }
            }
        }
        templateParameter.put("modelQueryColumns", modelQueryColumns);
        templateParameter.put("modelUpdateColumns", modelUpdateColumns);
        templateParameter.put("modelInsertColumns", modelInsertColumns);
    }

    protected void processPrimaryKeyColumns(TemplateParameter templateParameter, MybatisCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<MybatisXmlMapperConfigProperties> targetConfig) {
        List<DomainObjectColumnConfigProperties> pkColumns = targetConfig.getDomainObjectConfig().getPkColumns();
        if(pkColumns.size() == 1) { //单一主键
            DomainObjectColumnConfigProperties singlePkColumn = pkColumns.get(0);
            String pkFieldType = singlePkColumn.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters();
            templateParameter.put("pkFieldType", pkFieldType);
            templateParameter.put("pkColumnName", singlePkColumn.getColumnName());
            templateParameter.put("pkFieldName", singlePkColumn.getIntrospectedColumn().getJavaFieldName());
            templateParameter.put("pkJdbcTypeName", singlePkColumn.getIntrospectedColumn().getJdbcTypeName());
            PrimaryKeyGenerator primaryKeyGenerator = singlePkColumn.getPrimaryKeyGenerator();
            if(primaryKeyGenerator != null) {
                templateParameter.put("pkGenStrategy", primaryKeyGenerator.getStrategy().toString());
                templateParameter.put("pkGenParameter", primaryKeyGenerator.getParameter());
            } else {
                templateParameter.put("pkGenStrategy", "NONE");
            }
        } else {
            templateParameter.put("pkFieldType", Map.class.getName());
            templateParameter.put("pkGenStrategy", "NONE");
        }
        List<Map<String,Object>> modelPrimaryKeyColumns = new ArrayList<>();
        for(DomainObjectColumnConfigProperties pkColumn : pkColumns) {
            Map<String,Object> primaryKey = new HashMap<>();
            primaryKey.put("pkFieldType", pkColumn.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters());
            primaryKey.put("pkColumnName", pkColumn.getColumnName());
            primaryKey.put("pkFieldName", pkColumn.getIntrospectedColumn().getJavaFieldName());
            primaryKey.put("pkJdbcTypeName", pkColumn.getIntrospectedColumn().getJdbcTypeName());
            modelPrimaryKeyColumns.add(primaryKey);
        }
        templateParameter.put("modelPrimaryKeyColumns", modelPrimaryKeyColumns);
    }

    protected MybatisCodegenDialect getMybatisCodegenDialect(DatabaseType dialectDbType) {
        Map<DatabaseType,MybatisCodegenDialect> mybatisCodegenDialects = dbTypedMybatisCodegenDialects;
        if(mybatisCodegenDialects == null) { //DCL Check
            mybatisCodegenDialects = getDbTypedMybatisCodegenDialects();
        }
        return mybatisCodegenDialects.getOrDefault(dialectDbType, defaultMybatisCodegenDialect);
    }

    protected synchronized Map<DatabaseType,MybatisCodegenDialect> getDbTypedMybatisCodegenDialects() {
        if(dbTypedMybatisCodegenDialects == null) {
            Map<String,MybatisCodegenDialect> allMybatisCodegenDialects = SpringUtils.getBeansOfType(MybatisCodegenDialect.class);
            dbTypedMybatisCodegenDialects = allMybatisCodegenDialects.values()
                    .stream().collect(Collectors.toMap(MybatisCodegenDialect::getDatabaseType, Function.identity(), BinaryOperator.maxBy(OrderComparator.INSTANCE)));
        }
        return dbTypedMybatisCodegenDialects;
    }

    protected String parseMapperAnnotations(String mapperAnnotations, MybatisCodegenConfigProperties codegenConfig, DomainBoundedTargetConfigProperties<MybatisJavaMapperConfigProperties> targetConfig) {
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("runtimeDataSource", targetConfig.getDomainObjectConfig().getRuntimeDataSource());
        return TemplateUtils.parseTemplate(mapperAnnotations, parameter);
    }

}
