package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.consts.CodegenConstants;
import com.penglecode.xmodule.common.codegen.database.DatabaseIntrospector;
import com.penglecode.xmodule.common.codegen.database.IntrospectedColumn;
import com.penglecode.xmodule.common.codegen.database.IntrospectedTable;
import com.penglecode.xmodule.common.codegen.support.*;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.mybatis.DatabaseType;
import com.penglecode.xmodule.common.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 分模块的代码自动生成配置基类
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/22 16:28
 */
public abstract class ModuleCodegenConfigProperties implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleCodegenConfigProperties.class);

    /**
     * 业务/功能模块名,以此解决分工协作问题，
     * 需要在application.yaml中配置spring.codegen.config.{module}.*
     */
    private final String module;

    /** 领域对象代码生成配置 */
    private DomainConfig domain;

    protected ModuleCodegenConfigProperties(String module) {
        this.module = module;
    }

    public String getModule() {
        return module;
    }

    public DomainConfig getDomain() {
        return domain;
    }

    public void setDomain(DomainConfig domain) {
        this.domain = domain;
    }

    public String getCodegenConfigPrefix(String module) {
        return CodegenConstants.MODULE_CODEGEN_CONFIGURATION_PREFIX + module;
    }

    /**
     * 初始化代码生成配置
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("【{}】>>> 初始化代码生成配置开始...", module);
        //校验代码生成配置
        validateCodegenConfig();
        //初始化代码生成配置
        initCodegenConfig();
        LOGGER.info("【{}】<<< 初始化代码生成配置结束...", module);
    }

    /**
     * 校验代码生成配置
     * @throws Exception
     */
    protected void validateCodegenConfig() throws Exception {
        validateDomainCodegenConfig();
    }

    /**
     * 初始化代码生成配置
     * @throws Exception
     */
    protected void initCodegenConfig() throws Exception {
        initDomainCodegenConfig();
    }

    /**
     * 校验Domain代码生成配置
     */
    protected void validateDomainCodegenConfig() throws Exception {
        String codegenConfigPrefix = getCodegenConfigPrefix(module);
        //1、校验领域公共配置
        Assert.hasText(domain.getDomainCommons().getRuntimeDataSource(), String.format("Domain代码生成配置(%s.domain.domainCommons.runtimeDataSource)必须指定!", codegenConfigPrefix));
        Assert.hasText(domain.getDomainCommons().getIntrospectDataSource(), String.format("Domain代码生成配置(%s.domain.domainCommons.introspectDataSource)必须指定!", codegenConfigPrefix));
        Assert.hasText(domain.getDomainCommons().getTargetProject(), String.format("Domain代码生成配置(%s.domain.domainCommons.targetProject)必须指定!", codegenConfigPrefix));
        Assert.hasText(domain.getDomainCommons().getTargetPackage(), String.format("Domain代码生成配置(%s.domain.domainCommons.targetPackage)必须指定!", codegenConfigPrefix));

        Set<DomainEnumConfig> domainEnumConfigs = domain.getDomainCommons().getDomainEnums();
        if(!CollectionUtils.isEmpty(domainEnumConfigs)) {
            int index = 0;
            for(DomainEnumConfig domainEnumConfig : domainEnumConfigs) {
                Assert.hasText(domainEnumConfig.getDomainEnumName(), String.format("Domain代码生成配置(%s.domain.domainCommons.domainlEnums[%s].domainEnumName)必须指定!", codegenConfigPrefix, index));
                Assert.hasText(domainEnumConfig.getDomainEnumTitle(), String.format("Domain代码生成配置(%s.domain.domainCommons.domainlEnums[%s].domainEnumTitle)必须指定!", codegenConfigPrefix, index));
                Assert.notNull(domainEnumConfig.getDomainEnumCodeField(), String.format("Domain代码生成配置(%s.domain.domainCommons.domainlEnums[%s].domainEnumCodeField)必须指定!", codegenConfigPrefix, index));
                Assert.notNull(domainEnumConfig.getDomainEnumNameField(), String.format("Domain代码生成配置(%s.domain.domainCommons.domainlEnums[%s].domainEnumNameField)必须指定!", codegenConfigPrefix, index));
                if(domainEnumConfig.getDomainEnumClass() == null) {
                    Assert.notEmpty(domainEnumConfig.getDomainEnumValues(), String.format("Domain代码生成配置(%s.domain.domainCommons.domainlEnums[%s].domainEnumValues)必须指定!", codegenConfigPrefix, index));
                }
                index++;
            }
        }

        //2、校验领域实体配置
        Map<String,DomainEntityConfig> domainEntities = domain.getDomainEntities();
        Assert.notEmpty(domainEntities, String.format("Domain代码生成配置(%s.domain.domainEntities.*)必须指定!", codegenConfigPrefix));
        int index1 = 0;
        for(Map.Entry<String,DomainEntityConfig> entry1 : domainEntities.entrySet()) {
            DomainEntityConfig domainEntityConfig = entry1.getValue();
            Assert.hasText(domainEntityConfig.getDomainEntityName(), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityName)必须指定!", codegenConfigPrefix, index1));
            Assert.isTrue(CodegenUtils.isValidJavaTypeNaming(domainEntityConfig.getDomainEntityName()), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityName)命名不合法!", codegenConfigPrefix, index1));
            Assert.hasText(domainEntityConfig.getDomainEntityTable(), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityTable)必须指定!", codegenConfigPrefix, index1));
            Assert.hasText(domainEntityConfig.getDomainEntityTitle(), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityTitle)必须指定!", codegenConfigPrefix, index1));
            Assert.hasText(domainEntityConfig.getDomainEntityAlias(), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityAlias)必须指定!", codegenConfigPrefix, index1));
            Assert.isTrue(CodegenUtils.isValidJavaTypeNaming(domainEntityConfig.getDomainEntityAlias()), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityAlias)命名不合法!", codegenConfigPrefix, index1));
            Map<String,DomainEntityColumnConfig> domainEntityColumns = domainEntityConfig.getDomainEntityColumns();
            Assert.notEmpty(domainEntityColumns, String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityColumns.*)必须指定!", codegenConfigPrefix, index1));
            int index2 = 0;
            for(Map.Entry<String,DomainEntityColumnConfig> entry2 : domainEntityColumns.entrySet()) {
                DomainEntityColumnConfig domainEntityColumn = entry2.getValue();
                Assert.hasText(domainEntityColumn.getColumnName(), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityColumns[%s].columnName)必须指定!", codegenConfigPrefix, index1, index2));
                Assert.hasText(domainEntityColumn.getColumnTitle(), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityColumns[%s].columnTitle)必须指定!", codegenConfigPrefix, index1, index2));
                index2++;
            }
            index1++;
        }

        //3、校验领域聚合配置
        Map<String,DomainAggregateConfig> domainAggregates = domain.getDomainAggregates();
        if(!CollectionUtils.isEmpty(domainAggregates)) {
            index1 = 0;
            for(Map.Entry<String,DomainAggregateConfig> entry : domainAggregates.entrySet()) {
                DomainAggregateConfig domainAggregateConfig = entry.getValue();
                Assert.hasText(domainAggregateConfig.getDomainAggregateName(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].domainAggregateName)必须指定!", codegenConfigPrefix, index1));
                Assert.isTrue(CodegenUtils.isValidJavaTypeNaming(domainAggregateConfig.getDomainAggregateName()), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].domainAggregateName)命名不合法!", codegenConfigPrefix, index1));
                Assert.hasText(domainAggregateConfig.getDomainAggregateTitle(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].domainAggregateTitle)必须指定!", codegenConfigPrefix, index1));
                Assert.hasText(domainAggregateConfig.getDomainAggregateAlias(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].domainAggregateAlias)必须指定!", codegenConfigPrefix, index1));
                Assert.isTrue(CodegenUtils.isValidJavaTypeNaming(domainAggregateConfig.getDomainAggregateAlias()), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].domainAggregateAlias)命名不合法!", codegenConfigPrefix, index1));
                Assert.hasText(domainAggregateConfig.getAggregateMasterEntity(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateMasterEntity)必须指定!", codegenConfigPrefix, index1));
                DomainEntityConfig masterDomainEntity = domainEntities.get(domainAggregateConfig.getAggregateMasterEntity());
                Assert.notNull(masterDomainEntity, String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateMasterEntity)不存在!", codegenConfigPrefix, index1));
                Assert.isTrue(masterDomainEntity.getIdColumns().size() == 1, String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateMasterEntity)作为Master实体必须是单一主键!", codegenConfigPrefix, index1));
                Set<DomainAggregateSlaveConfig> domainAggregateSlaveConfigs = domainAggregateConfig.getAggregateSlaveEntities();
                Assert.notEmpty(domainAggregateSlaveConfigs, String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateSlaveEntities)必须指定!", codegenConfigPrefix, index1));
                int index2 = 0;
                for(DomainAggregateSlaveConfig domainAggregateSlaveConfig : domainAggregateSlaveConfigs) {
                    Assert.hasText(domainAggregateSlaveConfig.getAggregateSlaveEntity(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateSlaveEntities[%s].aggregateSlaveEntity)必须指定!", codegenConfigPrefix, index1, index2));
                    Assert.isTrue(domainEntities.containsKey(domainAggregateSlaveConfig.getAggregateSlaveEntity()), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateSlaveEntities[%s].aggregateSlaveEntity)不存在!", codegenConfigPrefix, index1, index2));
                    Assert.notNull(domainAggregateSlaveConfig.getMasterSlaveMapping(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateSlaveEntities[%s].masterSlaveMapping)必须指定!", codegenConfigPrefix, index1, index2));
                }
                index1++;
            }
        }
    }

    /**
     * 初始化Domain代码生成相关配置
     */
    protected void initDomainCodegenConfig() throws Exception {
        initDomainCommonsConfigs(getDomain().getDomainCommons());
        Map<String,DomainEntityConfig> domainEntityConfigs = getDomain().getDomainEntities();
        for(Map.Entry<String,DomainEntityConfig> entry : domainEntityConfigs.entrySet()) {
            DomainEntityConfig domainEntityConfig = entry.getValue();
            initDomainEntityConfig(domainEntityConfig);
            initDomainEntityColumnConfig(domainEntityConfig);
            initDomainEntityFieldConfig(domainEntityConfig);
        }
        Map<String,DomainAggregateConfig> domainAggregateConfigs = getDomain().getDomainAggregates();
        for(Map.Entry<String,DomainAggregateConfig> entry : domainAggregateConfigs.entrySet()) {
            initDomainAggregateConfig(entry.getValue());
        }
    }

    /**
     * 初始化领域对象公共配置
     * @param domainCommonsConfig
     */
    protected void initDomainCommonsConfigs(DomainCommonsConfig domainCommonsConfig) {
        domain.getDomainCommons().setCommentAuthor(StringUtils.defaultIfBlank(domain.getDomainCommons().getCommentAuthor(), CodegenConstants.DEFAULT_CODEGEN_COMMENT_AUTHOR));
        DomainIntrospectConfig domainIntrospectConfig = domain.getDomainCommons().getIntrospectConfig();
        domainIntrospectConfig.setIntrospectDataSource(domainCommonsConfig.getIntrospectDataSource());
        if(domainIntrospectConfig.getIntrospectDatabaseType() == null) { //推断数据库类型
            String inferJdbcUrl = SpringUtils.getEnvProperty(String.format("spring.datasource.%s.url", domain.getDomainCommons().getIntrospectDataSource()), String.class);
            domainIntrospectConfig.setIntrospectDatabaseType(DatabaseType.of(JdbcUtils.getDbType(inferJdbcUrl)));
        }
        Set<DomainEnumConfig> domainEnumConfigs = domain.getDomainCommons().getDomainEnums();
        if(!CollectionUtils.isEmpty(domainEnumConfigs)) {
            for(DomainEnumConfig domainEnumConfig : domainEnumConfigs) {
                domainEnumConfig.setTargetProject(StringUtils.defaultIfBlank(domainEnumConfig.getTargetProject(), domain.getDomainCommons().getTargetProject()));
                domainEnumConfig.setTargetPackage(StringUtils.defaultIfBlank(domainEnumConfig.getTargetPackage(), domain.getDomainCommons().defaultEnumsPackage()));
            }
        }
    }

    /**
     * 初始化领域实体配置信息
     * @param domainEntityConfig
     */
    protected void initDomainEntityConfig(DomainEntityConfig domainEntityConfig) throws SQLException {
        domainEntityConfig.setTargetProject(StringUtils.defaultIfBlank(domainEntityConfig.getTargetProject(), domain.getDomainCommons().getTargetProject()));
        domainEntityConfig.setTargetPackage(StringUtils.defaultIfBlank(domainEntityConfig.getTargetPackage(), domain.getDomainCommons().defaultModelPackage()));
        domainEntityConfig.setRuntimeDataSource(StringUtils.defaultIfBlank(domainEntityConfig.getRuntimeDataSource(), domain.getDomainCommons().getRuntimeDataSource()));
        domainEntityConfig.getIntrospectConfig().setIntrospectDataSource(StringUtils.defaultIfBlank(domainEntityConfig.getIntrospectConfig().getIntrospectDataSource(), domain.getDomainCommons().getIntrospectConfig().getIntrospectDataSource()));
        domainEntityConfig.getIntrospectConfig().setIntrospectDatabaseType(ObjectUtils.defaultIfNull(domainEntityConfig.getIntrospectConfig().getIntrospectDatabaseType(), domain.getDomainCommons().getIntrospectConfig().getIntrospectDatabaseType()));
        domainEntityConfig.getIntrospectConfig().setForceDateTimeAsString(ObjectUtils.defaultIfNull(domainEntityConfig.getIntrospectConfig().isForceDateTimeAsString(), domain.getDomainCommons().getIntrospectConfig().isForceDateTimeAsString()));
        domainEntityConfig.getIntrospectConfig().setForceNumber1AsBoolean(ObjectUtils.defaultIfNull(domainEntityConfig.getIntrospectConfig().isForceNumber1AsBoolean(), domain.getDomainCommons().getIntrospectConfig().isForceNumber1AsBoolean()));
        domainEntityConfig.getIntrospectConfig().setForceDecimalNumericAsDouble(ObjectUtils.defaultIfNull(domainEntityConfig.getIntrospectConfig().isForceDecimalNumericAsDouble(), domain.getDomainCommons().getIntrospectConfig().isForceDecimalNumericAsDouble()));
        DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(getDomain());
        //执行数据库内省
        domainEntityConfig.setIntrospectedTable(databaseIntrospector.introspectedTable(domainEntityConfig));
    }

    /**
     * 初始化领域实体的数据库列信息
     * @param domainEntityConfig
     */
    protected void initDomainEntityColumnConfig(DomainEntityConfig domainEntityConfig) {
        IntrospectedTable introspectedTable = domainEntityConfig.getIntrospectedTable();
        List<IntrospectedColumn> pkColumns = introspectedTable.getPkColumns();
        Map<String,IntrospectedColumn> allColumns = introspectedTable.getAllColumns().stream().collect(Collectors.toMap(IntrospectedColumn::getColumnName, Function.identity(), StreamUtils.preferOld()));
        Map<String,DomainEntityColumnConfig> domainEntityColumns = domainEntityConfig.getDomainEntityColumns();
        //1、domainEntityColumns中出现的列必须在表中真实存在,不存在的自动忽略
        processEntityColumnsPresent(domainEntityColumns, allColumns, pkColumns, introspectedTable);
        //2、domainEntityColumns未出现但在表真实存在的列也需要添加进去
        processEntityColumnsAbsent(domainEntityColumns, allColumns, pkColumns, introspectedTable);
        //3、domainEntityColumns最终处理
        processEntityColumnsFinally(domainEntityConfig);
    }

    /**
     * domainEntityColumns中出现的列必须在表中真实存在,不存在的自动忽略
     *
     * @param domainEntityColumns
     * @param allColumns
     * @param pkColumns
     * @param introspectedTable
     */
    protected void processEntityColumnsPresent(Map<String,DomainEntityColumnConfig> domainEntityColumns, Map<String,IntrospectedColumn> allColumns, List<IntrospectedColumn> pkColumns, IntrospectedTable introspectedTable) {
        for(Iterator<Map.Entry<String,DomainEntityColumnConfig>> iterator = domainEntityColumns.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String,DomainEntityColumnConfig> entry = iterator.next();
            String columnName = entry.getKey();
            DomainEntityColumnConfig domainEntityColumn = entry.getValue();
            IntrospectedColumn introspectedColumn = allColumns.get(columnName);
            if(introspectedColumn != null) {
                domainEntityColumn.setIntrospectedColumn(introspectedColumn);
                domainEntityColumn.setIdColumn(pkColumns.contains(introspectedColumn));
                domainEntityColumn.setColumnTitle(StringUtils.defaultIfBlank(domainEntityColumn.getColumnTitle(), columnName));
                if(!domainEntityColumn.isIdColumn() || pkColumns.size() != 1) {
                    domainEntityColumn.setIdGenerator(null);
                } else if(introspectedColumn.isAutoIncrement()) { //非复合主键且是AUTO_INCREMENT的则明确设置其主键生成策略
                    domainEntityColumn.setIdGenerator(IdGenStrategy.IDENTITY.buildGenerator(null));
                    domainEntityColumn.setColumnOnInsert(true);
                    domainEntityColumn.setValidateOnInsert(false);
                    domainEntityColumn.setColumnOnUpdate(false);
                    domainEntityColumn.setValidateOnUpdate(true);
                }
                //如果是客户端指定主键值或者是复合主键，则需要出现在insert语句列中并且添加javax.validation校验
                if(domainEntityColumn.isIdColumn() && (domainEntityColumn.getIdGenerator() == null || pkColumns.size() > 1)) {
                    domainEntityColumn.setColumnOnInsert(true);
                    domainEntityColumn.setColumnOnUpdate(false);
                    domainEntityColumn.setValidateOnUpsert(true);
                }
                //如果是基于SEQUENCE序列的主键生成方式，则其必须出现在insert列中
                if(domainEntityColumn.getIdGenerator() != null && IdGenStrategy.SEQUENCE.equals(domainEntityColumn.getIdGenerator().getStrategy())) {
                    domainEntityColumn.setColumnOnInsert(true);
                    domainEntityColumn.setValidateOnInsert(false);
                    domainEntityColumn.setColumnOnUpdate(false);
                    domainEntityColumn.setValidateOnUpdate(true);
                }
            } else { //出现在领域对象列配置中的字段但是数据库中不存在,则自动忽略
                iterator.remove();
                LOGGER.warn("【{}】>>> 代码自动生成配置列(columnName = {}, tableName = {})在数据库表中不存在,予以自动忽略...", module, columnName, introspectedTable.getTableName());
            }
        }
    }

    /**
     * domainEntityColumns未出现但在表真实存在的列也需要添加进去
     *
     * @param domainEntityColumns
     * @param allColumns
     * @param pkColumns
     * @param introspectedTable
     */
    protected void processEntityColumnsAbsent(Map<String,DomainEntityColumnConfig> domainEntityColumns, Map<String,IntrospectedColumn> allColumns, List<IntrospectedColumn> pkColumns, IntrospectedTable introspectedTable) {
        for(Map.Entry<String,IntrospectedColumn> entry : allColumns.entrySet()) {
            String columnName = entry.getKey();
            IntrospectedColumn introspectedColumn = entry.getValue();
            if(!domainEntityColumns.containsKey(columnName)) {
                DomainEntityColumnConfig domainEntityColumn = new DomainEntityColumnConfig();
                domainEntityColumn.setColumnName(columnName);
                domainEntityColumn.setIntrospectedColumn(introspectedColumn);
                domainEntityColumn.setIdColumn(pkColumns.contains(introspectedColumn));
                domainEntityColumn.setColumnTitle(columnName);
                //默认出现在insert/update列中
                domainEntityColumn.setColumnOnInsert(true);
                domainEntityColumn.setColumnOnUpdate(true);
                if(!introspectedColumn.isNullable()) {
                    domainEntityColumn.setValidateOnInsert(true);
                    domainEntityColumn.setValidateOnUpdate(true);
                }
                if(domainEntityColumn.isIdColumn() && pkColumns.size() == 1 && introspectedColumn.isAutoIncrement()){ //非复合主键且是AUTO_INCREMENT的则明确设置其主键生成策略
                    domainEntityColumn.setIdGenerator(IdGenStrategy.IDENTITY.buildGenerator(null));
                    domainEntityColumn.setColumnOnInsert(true);
                    domainEntityColumn.setValidateOnInsert(false);
                }
                //未出现在领域对象列配置中的字段代表不重要,不需要强制指定insert值,不需要出现在update列中
                domainEntityColumns.put(columnName, domainEntityColumn);
                LOGGER.warn("【{}】>>> 代码自动生成数据库列(columnName = {}, tableName = {})在配置列中不存在,予以自动添加...", module, columnName, introspectedTable.getTableName());
            }
        }
    }

    /**
     * domainEntityColumns最终处理
     * @param domainEntityConfig
     */
    protected void processEntityColumnsFinally(DomainEntityConfig domainEntityConfig) {
        Map<String,DomainEntityColumnConfig> domainEntityColumns = domainEntityConfig.getDomainEntityColumns();
        for(Map.Entry<String,DomainEntityColumnConfig> entry : domainEntityColumns.entrySet()) {
            DomainEntityColumnConfig domainEntityColumn = entry.getValue();
            domainEntityColumn.initValidateExpressions(getDomain());
        }
    }

    /**
     * 初始化领域聚合对象配置
     * @param domainAggregateConfig
     */
    protected void initDomainAggregateConfig(DomainAggregateConfig domainAggregateConfig) {
        domainAggregateConfig.setTargetProject(StringUtils.defaultIfBlank(domainAggregateConfig.getTargetProject(), domain.getDomainCommons().getTargetProject()));
        domainAggregateConfig.setTargetPackage(StringUtils.defaultIfBlank(domainAggregateConfig.getTargetPackage(), domain.getDomainCommons().defaultModelPackage()));
        for(DomainAggregateSlaveConfig domainAggregateSlaveConfig : domainAggregateConfig.getAggregateSlaveEntities()) {
            if(!domainAggregateSlaveConfig.isCascadingOnInsert()) {
                domainAggregateSlaveConfig.setValidateOnInsert(false);
            }
            if(!domainAggregateSlaveConfig.isCascadingOnUpdate()) {
                domainAggregateSlaveConfig.setValidateOnUpdate(false);
            }
            DomainEntityConfig slaveDomainEntityConfig = getDomain().getDomainEntities().get(domainAggregateSlaveConfig.getAggregateSlaveEntity());
            if(DomainMasterSlaveRelation.RELATION_11.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //添加1:1聚合关系下的字段
                String fieldName = StringUtils.lowerCaseFirstChar(slaveDomainEntityConfig.getDomainEntityAlias());
                FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(slaveDomainEntityConfig.getGeneratedTargetName(slaveDomainEntityConfig.getDomainEntityName(), true, false));
                String fieldTitle = slaveDomainEntityConfig.getDomainEntityTitle();
                DomainAggregateFieldConfig domainAggregateFieldConfig = new DomainAggregateFieldConfig(fieldName, fieldType, fieldTitle, fieldTitle, DomainObjectFieldClass.DOMAIN_AGGREGATE_FIELD, domainAggregateConfig, domainAggregateSlaveConfig);
                domainAggregateConfig.getDomainAggregateFields().put(slaveDomainEntityConfig.getDomainEntityName(), domainAggregateFieldConfig);
            } else if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //添加1:N聚合关系下的字段
                String fieldName = CodegenUtils.getPluralNameOfDomainObject(StringUtils.lowerCaseFirstChar(slaveDomainEntityConfig.getDomainEntityAlias()));
                FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(List.class.getName() + "<" + slaveDomainEntityConfig.getGeneratedTargetName(slaveDomainEntityConfig.getDomainEntityName(), true, false) + ">");
                String fieldTitle = slaveDomainEntityConfig.getDomainEntityTitle();
                DomainAggregateFieldConfig domainAggregateFieldConfig = new DomainAggregateFieldConfig(fieldName, fieldType, fieldTitle, fieldTitle, DomainObjectFieldClass.DOMAIN_AGGREGATE_FIELD, domainAggregateConfig, domainAggregateSlaveConfig);
                domainAggregateConfig.getDomainAggregateFields().put(slaveDomainEntityConfig.getDomainEntityName(), domainAggregateFieldConfig);
            }
            /*//初始化当前DomainAggregateSlaveConfig对应的DomainEntityConfig配置
            DomainEntityConfig masterDomainEntityConfig = getDomain().getDomainEntities().get(domainAggregateConfig.getAggregateMasterEntity());
            //relateFieldNameOfMaster在master领域对象中是唯一ID && relateFieldNameOfSlave是slave领域对象中的字段
            if(masterDomainEntityConfig.getIdColumns().size() == 1
                    && masterDomainEntityConfig.getIdColumns().get(0).getIntrospectedColumn().getJavaFieldName().equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfMaster())
                    && slaveDomainEntityConfig.getIntrospectedTable().getAllColumns().stream().anyMatch(column -> column.getJavaFieldName().equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfSlave()))) {
                slaveDomainEntityConfig.setDomainMasterSlaveConfig(new DomainMasterSlaveConfigProperties(domainAggregateConfig, domainAggregateSlaveConfig, masterDomainEntityConfig, slaveDomainEntityConfig));
            }*/
        }
    }

    /**
     * 初始化领域实体对象的所有字段
     * @param domainEntityConfig
     */
    protected void initDomainEntityFieldConfig(DomainEntityConfig domainEntityConfig) {
        Map<String, DomainEntityColumnConfig> domainEntityColumns = domainEntityConfig.getDomainEntityColumns();
        Map<String, DomainEntityFieldConfig> domainEntityFields = new LinkedHashMap<>();
        //1、初始化领域实体对象的持久化字段
        for(Map.Entry<String, DomainEntityColumnConfig> entry : domainEntityColumns.entrySet()) {
            DomainEntityColumnConfig domainEntityColumnConfig = entry.getValue();
            QueryConditionOperator queryConditionOperator = QueryConditionOperator.of(domainEntityColumnConfig.getOperatorOnQuery()); //此处仅为初步设置，在2部分的辅助字段生成逻辑中会对此进行修正
            DomainEntityFieldConfig objectFieldConfig = new DomainEntityFieldConfig(domainEntityColumnConfig.getIntrospectedColumn().getJavaFieldName(), domainEntityColumnConfig.getIntrospectedColumn().getJavaFieldType(), domainEntityColumnConfig.getColumnTitle(), domainEntityColumnConfig.getIntrospectedColumn().getColumnComment(), DomainObjectFieldClass.DOMAIN_ENTITY_FIELD, domainEntityColumnConfig, queryConditionOperator);
            domainEntityFields.put(objectFieldConfig.getFieldName(), objectFieldConfig);
        }
        //2、初始化领域实体对象的辅助字段
        Map<String,Set<QueryConditionOperator>> criteriaQueryColumnOperators = getCriteriaQueryColumnOperators(domainEntityConfig);
        if(!CollectionUtils.isEmpty(criteriaQueryColumnOperators)) {
            for(Map.Entry<String, DomainEntityColumnConfig> entry : domainEntityColumns.entrySet()) {
                DomainEntityColumnConfig domainEntityColumnConfig = entry.getValue();
                Set<QueryConditionOperator> columnQueryOperators = criteriaQueryColumnOperators.get(entry.getKey());
                String refJavaFieldName = domainEntityColumnConfig.getIntrospectedColumn().getJavaFieldName(); //需要设置辅助字段的持久化字段
                FullyQualifiedJavaType refJavaFieldType = domainEntityColumnConfig.getIntrospectedColumn().getJavaFieldType();
                if(!CollectionUtils.isEmpty(columnQueryOperators)) { //处理查询条件辅助字段
                    if(isRangeQuerySupportFieldRequired(columnQueryOperators)) { //需要辅助的范围查询条件?
                        String rangeMinSupportPropertyName = getRangeQuerySupportFieldName1(refJavaFieldName);
                        String fieldTitle = refJavaFieldName + "的范围查询条件辅助字段";
                        DomainEntityFieldConfig rangeMinFieldConfig = new DomainEntityFieldConfig(rangeMinSupportPropertyName, refJavaFieldType, fieldTitle, fieldTitle, DomainObjectFieldClass.DOMAIN_ENTITY_SUPPORTS_QUERY_INBOUND_FIELD, domainEntityColumnConfig, getGTQueryConditionOperator(columnQueryOperators));
                        domainEntityFields.put(rangeMinFieldConfig.getFieldName(), rangeMinFieldConfig);
                        String rangeMaxSupportPropertyName = getRangeQuerySupportFieldName2(refJavaFieldName);
                        DomainEntityFieldConfig rangeMaxFieldConfig = new DomainEntityFieldConfig(rangeMaxSupportPropertyName, refJavaFieldType, fieldTitle, fieldTitle, DomainObjectFieldClass.DOMAIN_ENTITY_SUPPORTS_QUERY_INBOUND_FIELD, domainEntityColumnConfig, getLTQueryConditionOperator(columnQueryOperators));
                        domainEntityFields.put(rangeMaxFieldConfig.getFieldName(), rangeMaxFieldConfig);
                        //辅助字段关联的持久化字段在此情况下可以存在EQ操作符的,此处修正1处的queryConditionOperator设置
                        domainEntityFields.get(refJavaFieldName).setQueryConditionOperator(QueryConditionOperator.EQ);
                    } else if(isInQuerySupportFieldRequired(columnQueryOperators)) { //需要辅助的in查询条件?
                        String inSupportPropertyName = getInQuerySupportFieldName(refJavaFieldName);
                        String fieldTitle = refJavaFieldName + "的IN查询条件辅助字段";
                        FullyQualifiedJavaType inSupportPropertyType = new FullyQualifiedJavaType(String.format("%s[]", refJavaFieldType)); //数组
                        DomainEntityFieldConfig supportFieldConfig = new DomainEntityFieldConfig(inSupportPropertyName, inSupportPropertyType, fieldTitle, fieldTitle, DomainObjectFieldClass.DOMAIN_ENTITY_SUPPORTS_QUERY_INBOUND_FIELD, domainEntityColumnConfig, QueryConditionOperator.IN);
                        domainEntityFields.put(supportFieldConfig.getFieldName(), supportFieldConfig);
                        //辅助字段关联的持久化字段在此情况下可以存在EQ操作符的,此处修正1处的queryConditionOperator设置
                        domainEntityFields.get(refJavaFieldName).setQueryConditionOperator(QueryConditionOperator.EQ);
                    }
                }
                if(StringUtils.isNotEmpty(domainEntityColumnConfig.getDecodeEnumType())) { //处理查询结果辅助字段
                    String codedSupportPropertyName = getCodedSupportFieldName(refJavaFieldName);
                    String fieldTitle = refJavaFieldName + "的查询结果辅助字段";
                    //为可枚举的字段值生成字段值的对应名称字段，例如为productType字段生成productTypeName字段
                    DomainEntityFieldConfig supportFieldConfig = new DomainEntityFieldConfig(codedSupportPropertyName,new FullyQualifiedJavaType("java.lang.String"), fieldTitle, fieldTitle, DomainObjectFieldClass.DOMAIN_ENTITY_SUPPORTS_QUERY_OUTBOUND_FIELD, domainEntityColumnConfig, null);
                    domainEntityFields.put(supportFieldConfig.getFieldName(), supportFieldConfig);
                }
            }
        }
        domainEntityConfig.setDomainEntityFields(domainEntityFields);
    }

    /**
     * 获取条件查询的字段及其操作符映射关系
     * @param domainEntityConfig
     * @return
     */
    protected Map<String,Set<QueryConditionOperator>> getCriteriaQueryColumnOperators(DomainEntityConfig domainEntityConfig) {
        return domainEntityConfig.getDomainEntityColumns().values().stream()
                .filter(column -> StringUtils.isNotEmpty(column.getOperatorOnQuery()))
                .collect(Collectors.toMap(DomainEntityColumnConfig::getColumnName, column -> {
                    String[] opa = StringUtils.strip(column.getOperatorOnQuery().trim(), ",").split(",");
                    return Stream.of(opa).map(QueryConditionOperator::of).filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));
                }));
    }

    /**
     * 是否需要范围查询辅助字段
     * @param columnQueryOperators
     * @return
     */
    protected boolean isRangeQuerySupportFieldRequired(Set<QueryConditionOperator> columnQueryOperators) {
        return columnQueryOperators.stream().anyMatch(operator -> operator.isGTRangeQueryCondition() || operator.isLTRangeQueryCondition());
    }

    /**
     * 是否需要in查询辅助字段
     * @param columnQueryOperators
     * @return
     */
    protected boolean isInQuerySupportFieldRequired(Set<QueryConditionOperator> columnQueryOperators) {
        return columnQueryOperators.stream().anyMatch(QueryConditionOperator::isInQueryCondition);
    }

    protected String getRangeQuerySupportFieldName1(String javaFieldName) {
        String prefix = isDateTimeField(javaFieldName) ? CodegenConstants.RANGE_START_JAVA_FIELD_PREFIX : CodegenConstants.RANGE_MIN_JAVA_FIELD_PREFIX;
        return prefix + Character.toUpperCase(javaFieldName.charAt(0)) + javaFieldName.substring(1);
    }

    protected String getRangeQuerySupportFieldName2(String javaFieldName) {
        String prefix = isDateTimeField(javaFieldName) ? CodegenConstants.RANGE_END_JAVA_FIELD_PREFIX : CodegenConstants.RANGE_MAX_JAVA_FIELD_PREFIX;
        return prefix + Character.toUpperCase(javaFieldName.charAt(0)) + javaFieldName.substring(1);
    }

    protected boolean isDateTimeField(String javaFieldName) {
        return javaFieldName.toLowerCase().contains("date") || javaFieldName.toLowerCase().contains("time");
    }

    protected String getInQuerySupportFieldName(String javaFieldName) {
        if(javaFieldName.endsWith("s")) {
            return javaFieldName + "es";
        } else {
            return javaFieldName + "s";
        }
    }

    protected String getCodedSupportFieldName(String javaFieldName) {
        return javaFieldName + "Name";
    }

    protected QueryConditionOperator getGTQueryConditionOperator(Set<QueryConditionOperator> columnQueryOperators) {
        QueryConditionOperator ltOperator = null;
        for(QueryConditionOperator operator : columnQueryOperators) {
            if(operator.isGTRangeQueryCondition()) {
                return operator;
            }
            if(operator.isLTRangeQueryCondition()) {
                ltOperator = operator;
            }
        }
        return QueryConditionOperator.LT.equals(ltOperator) ? QueryConditionOperator.GT : QueryConditionOperator.GTE;
    }

    protected QueryConditionOperator getLTQueryConditionOperator(Set<QueryConditionOperator> columnQueryOperators) {
        QueryConditionOperator gtOperator = null;
        for(QueryConditionOperator operator : columnQueryOperators) {
            if(operator.isLTRangeQueryCondition()) {
                return operator;
            }
            if(operator.isGTRangeQueryCondition()) {
                gtOperator = operator;
            }
        }
        return QueryConditionOperator.GT.equals(gtOperator) ? QueryConditionOperator.LT : QueryConditionOperator.LTE;
    }

    public DomainEntityColumnConfig getDefaultCreateTimeColumn(DomainEntityConfig domainEntityConfig) {
        DomainEntityColumnConfig createTimeColumn = domainEntityConfig.getDomainEntityColumns().get(getDomain().getDomainCommons().getDefaultCreateTimeColumn());
        if(createTimeColumn != null && FullyQualifiedJavaType.getStringInstance().equals(createTimeColumn.getIntrospectedColumn().getJavaFieldType())) {
            return createTimeColumn;
        }
        return null;
    }

    public DomainEntityColumnConfig getDefaultUpdateTimeColumn(DomainEntityConfig domainEntityConfig) {
        DomainEntityColumnConfig updateTimeColumn = domainEntityConfig.getDomainEntityColumns().get(getDomain().getDomainCommons().getDefaultUpdateTimeColumn());
        if(updateTimeColumn != null && FullyQualifiedJavaType.getStringInstance().equals(updateTimeColumn.getIntrospectedColumn().getJavaFieldType())) {
            return updateTimeColumn;
        }
        return null;
    }

}
