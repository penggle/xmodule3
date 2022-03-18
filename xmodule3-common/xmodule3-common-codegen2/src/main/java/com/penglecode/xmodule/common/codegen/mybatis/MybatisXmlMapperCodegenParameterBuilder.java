package com.penglecode.xmodule.common.codegen.mybatis;

import com.penglecode.xmodule.common.codegen.config.DomainEntityColumnConfig;
import com.penglecode.xmodule.common.codegen.config.DomainEntityConfig;
import com.penglecode.xmodule.common.codegen.config.MybatisCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.MybatisXmlMapperConfig;
import com.penglecode.xmodule.common.codegen.mybatis.MybatisXmlMapperCodegenParameter.*;
import com.penglecode.xmodule.common.codegen.support.*;
import com.penglecode.xmodule.common.mybatis.MapperHelper;
import com.penglecode.xmodule.common.mybatis.DatabaseType;
import com.penglecode.xmodule.common.mybatis.dsl.QueryCriteria;
import com.penglecode.xmodule.common.util.SpringUtils;
import org.springframework.core.OrderComparator;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 领域实体的Mybatis XML-Mapper代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/6 22:03
 */
public class MybatisXmlMapperCodegenParameterBuilder extends CodegenParameterBuilder<MybatisCodegenConfigProperties, MybatisXmlMapperConfig, DomainEntityConfig, MybatisXmlMapperCodegenParameter> {

    private final MybatisCodegenDialect defaultMybatisCodegenDialect = new DefaultMybatisCodegenDialect();

    private volatile Map<DatabaseType,MybatisCodegenDialect> dbTypedMybatisCodegenDialects;

    public MybatisXmlMapperCodegenParameterBuilder(CodegenContext<MybatisCodegenConfigProperties, MybatisXmlMapperConfig, DomainEntityConfig> codegenContext) {
        super(codegenContext);
    }

    public MybatisXmlMapperCodegenParameterBuilder(MybatisCodegenConfigProperties codegenConfig, MybatisXmlMapperConfig targetConfig, DomainEntityConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected MybatisXmlMapperCodegenParameter setCustomCodegenParameter(MybatisXmlMapperCodegenParameter codegenParameter) {
        MybatisCodegenDialect mybatisCodegenDialect = getMybatisCodegenDialect(getDomainObjectConfig().getIntrospectConfig().getIntrospectDatabaseType());
        codegenParameter.setDomainObjectName(getDomainObjectConfig().getDomainEntityName());
        codegenParameter.setMapperNamespace(getTargetConfig().getGeneratedTargetName(getDomainObjectConfig().getDomainEntityName(), true, false));
        codegenParameter.setMapperHelperClass(MapperHelper.class.getName());
        codegenParameter.setDomainEntityTable(getDomainObjectConfig().getDomainEntityTable());
        codegenParameter.setTableAliasName(QueryCriteria.TABLE_ALIAS_NAME);
        CodegenContext<MybatisCodegenConfigProperties, MybatisXmlMapperConfig, DomainEntityConfig> codegenContext = new CodegenContext<>(getCodegenConfig(), getTargetConfig(), getDomainObjectConfig());
        codegenParameter.setDeleteTargetAliasName(mybatisCodegenDialect.getDeleteTargetAliasName(codegenContext));
        codegenParameter.setCursorFetchSize(mybatisCodegenDialect.getCursorFetchSize(codegenContext));
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
    protected void attachInsertUpdateQueryColumns(CodegenContext<MybatisCodegenConfigProperties, MybatisXmlMapperConfig, DomainEntityConfig> codegenContext, MybatisXmlMapperCodegenParameter codegenParameter, MybatisCodegenDialect mybatisCodegenDialect) {
        List<QueryColumn> queryColumns = new ArrayList<>();
        List<QueryColumn> updateColumns = new ArrayList<>();
        List<QueryColumn> insertColumns = new ArrayList<>();
        for(Map.Entry<String,DomainEntityColumnConfig> entry : codegenContext.getDomainObjectConfig().getDomainEntityColumns().entrySet()) {
            DomainEntityColumnConfig columnConfig = entry.getValue();
            QueryColumn selectColumn = new QueryColumn();
            selectColumn.setColumnName(mybatisCodegenDialect.getSelectColumnClause(columnConfig.getColumnName(), codegenContext));
            selectColumn.setFieldName(columnConfig.getIntrospectedColumn().getJavaFieldName());
            selectColumn.setFieldType(columnConfig.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters());
            selectColumn.setJdbcTypeName(columnConfig.getIntrospectedColumn().getJdbcTypeName());
            queryColumns.add(selectColumn);
            if(columnConfig.isColumnOnUpdate() || columnConfig.isColumnOnInsert()) { //是否是插入列/更新列
                QueryColumn insertOrUpdateColumn = new QueryColumn();
                insertOrUpdateColumn.setColumnName(columnConfig.getColumnName());
                insertOrUpdateColumn.setFieldName(columnConfig.getIntrospectedColumn().getJavaFieldName());
                insertOrUpdateColumn.setFieldType(columnConfig.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters());
                insertOrUpdateColumn.setJdbcTypeName(columnConfig.getIntrospectedColumn().getJdbcTypeName());
                if(columnConfig.isColumnOnUpdate()) {
                    updateColumns.add(insertOrUpdateColumn);
                }
                if(columnConfig.isColumnOnInsert()) {
                    insertColumns.add(insertOrUpdateColumn);
                }
            }
        }
        codegenParameter.setQueryColumns(queryColumns);
        codegenParameter.setUpdateColumns(updateColumns);
        codegenParameter.setInsertColumns(insertColumns);
    }

    /**
     * 附带上领域实体的ID字段参数
     * @param codegenContext
     * @param codegenParameter
     */
    protected void attachIdColumns(CodegenContext<MybatisCodegenConfigProperties, MybatisXmlMapperConfig, DomainEntityConfig> codegenContext, MybatisXmlMapperCodegenParameter codegenParameter) {
        List<DomainEntityColumnConfig> idColumnConfigs = codegenContext.getDomainObjectConfig().getIdColumns();
        if(idColumnConfigs.size() == 1) { //单一主键
            DomainEntityColumnConfig singleIdColumn = idColumnConfigs.get(0);
            String idFieldType = singleIdColumn.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters();
            codegenParameter.setIdFieldType(idFieldType);
            codegenParameter.setIdColumnName(singleIdColumn.getColumnName());
            codegenParameter.setIdFieldName(singleIdColumn.getIntrospectedColumn().getJavaFieldName());
            codegenParameter.setIdJdbcTypeName(singleIdColumn.getIntrospectedColumn().getJdbcTypeName());
            IdGenerator idGenerator = singleIdColumn.getIdGenerator();
            if(idGenerator != null) {
                codegenParameter.setIdGenStrategy(idGenerator.getStrategy().toString());
                codegenParameter.setIdGenParameter(idGenerator.getParameter());
            } else {
                codegenParameter.setIdGenStrategy("NONE");
            }
        } else {
            codegenParameter.setIdFieldType(Map.class.getName());
            codegenParameter.setIdGenStrategy("NONE");
        }
        List<IdColumn> idColumns = new ArrayList<>();
        for(DomainEntityColumnConfig idColumnConfig : idColumnConfigs) {
            IdColumn idColumn = new IdColumn();
            idColumn.setIdFieldType(idColumnConfig.getIntrospectedColumn().getJavaFieldType().getFullyQualifiedNameWithoutTypeParameters());
            idColumn.setIdColumnName(idColumnConfig.getColumnName());
            idColumn.setIdFieldName(idColumnConfig.getIntrospectedColumn().getJavaFieldName());
            idColumn.setIdJdbcTypeName(idColumnConfig.getIntrospectedColumn().getJdbcTypeName());
            idColumns.add(idColumn);
        }
        codegenParameter.setIdColumns(idColumns);
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

    @Override
    protected String getTargetTemplateName() {
        return "MybatisXmlMapper.ftl";
    }

}
