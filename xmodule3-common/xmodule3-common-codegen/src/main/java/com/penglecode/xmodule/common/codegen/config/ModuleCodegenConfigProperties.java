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
import org.springframework.util.Assert;

import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 分模块的代码自动生成配置基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/7 22:24
 */
public abstract class ModuleCodegenConfigProperties {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModuleCodegenConfigProperties.class);

	private static final Pattern JAVA_TYPE_NAMING_PATTERN = Pattern.compile("[A-Z][a-zA-Z0-9]+");

	/**
	 * 业务模块名或者开发者名字,以此解决分工协作问题，
	 * 需要在application.yaml中配置spring.codegen.config.{module}.*
	 */
	private final String bizModule;

	/** 领域对象代码生成配置 */
	private DomainConfigProperties domain;

	protected ModuleCodegenConfigProperties(String bizModule) {
		this.bizModule = bizModule;
	}

	public String getBizModule() {
		return bizModule;
	}

	public DomainConfigProperties getDomain() {
		return domain;
	}

	public void setDomain(DomainConfigProperties domain) {
		this.domain = domain;
	}

	/**
	 * 初始化代码生成配置
	 * @throws Exception
	 */
	public void initConfig() throws Exception {
		LOGGER.info("【{}】>>> 初始化代码生成配置开始...", bizModule);
		//校验代码生成配置
		validateCodegenConfig();
		//初始化代码生成配置
		initCodegenConfig();
		LOGGER.info("【{}】<<< 初始化代码生成配置结束...", bizModule);
	}

	public static String getModuleCodegenConfigPrefix(String module) {
		return CodegenConstants.MODULE_CODEGEN_CONFIGURATION_PREFIX + module;
	}

	/**
	 * 校验代码生成配置
	 * @throws Exception
	 */
	protected void validateCodegenConfig() throws Exception {
		validateDomainCodegenConfig();
	}

	/**
	 * 校验Domain代码生成配置
	 */
	protected void validateDomainCodegenConfig() {
		Assert.hasText(domain.getDomainCommons().getRuntimeDataSource(), String.format("Domain代码生成配置(%s.domain.domainCommons.runtimeDataSource')必须指定!", getModuleCodegenConfigPrefix(bizModule)));
		Assert.hasText(domain.getDomainCommons().getIntrospectDataSource(), String.format("Domain代码生成配置(%s.domain.domainCommons.introspectDataSource')必须指定!", getModuleCodegenConfigPrefix(bizModule)));
		Assert.hasText(domain.getDomainCommons().getTargetProject(), String.format("Domain代码生成配置(%s.domain.domainCommons.targetProject')必须指定!", getModuleCodegenConfigPrefix(bizModule)));
		Assert.hasText(domain.getDomainCommons().getTargetPackage(), String.format("Domain代码生成配置(%s.domain.domainCommons.targetPackage')必须指定!", getModuleCodegenConfigPrefix(bizModule)));

		Set<DomainEnumConfigProperties> domainEnumConfigs = domain.getDomainCommons().getDomainEnums();
		if(!CollectionUtils.isEmpty(domainEnumConfigs)) {
			int index = 0;
			for(DomainEnumConfigProperties domainEnumConfig : domainEnumConfigs) {
				Assert.hasText(domainEnumConfig.getEnumName(), String.format("Domain代码生成配置(%s.domain.domainCommons.domainlEnums[%s].enumName')必须指定!", getModuleCodegenConfigPrefix(bizModule), index));
				Assert.hasText(domainEnumConfig.getEnumTitle(), String.format("Domain代码生成配置(%s.domain.domainCommons.domainlEnums[%s].enumTitle')必须指定!", getModuleCodegenConfigPrefix(bizModule), index));
				Assert.isTrue(domainEnumConfig.getCodeField() != null && domainEnumConfig.getNameField() != null, String.format("Domain代码生成配置(%s.domain.domainCommons.domainlEnums[%s].enumFields')必须指定!", getModuleCodegenConfigPrefix(bizModule), index));
				Assert.notEmpty(domainEnumConfig.getEnumValues(), String.format("Domain代码生成配置(%s.domain.domainCommons.domainlEnums[%s].enumValues')必须指定!", getModuleCodegenConfigPrefix(bizModule), index));
				index++;
			}
		}
		Map<String,DomainAggregateConfigProperties> domainAggregates = domain.getDomainAggregates();
		if(!CollectionUtils.isEmpty(domainAggregates)) {
			int index1 = 0;
			for(Map.Entry<String,DomainAggregateConfigProperties> entry : domainAggregates.entrySet()) {
				DomainAggregateConfigProperties domainAggregateConfig = entry.getValue();
				Assert.hasText(domainAggregateConfig.getAggregateObjectName(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateObjectName')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1));
				Assert.isTrue(JAVA_TYPE_NAMING_PATTERN.matcher(domainAggregateConfig.getAggregateObjectName()).matches(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateObjectName')不合法!", getModuleCodegenConfigPrefix(bizModule), index1));
				Assert.hasText(domainAggregateConfig.getAggregateObjectTitle(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateObjectTitle')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1));
				Assert.hasText(domainAggregateConfig.getAggregateObjectAlias(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateObjectAlias')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1));
				Assert.isTrue(JAVA_TYPE_NAMING_PATTERN.matcher(domainAggregateConfig.getAggregateObjectAlias()).matches(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateObjectAlias')不合法!", getModuleCodegenConfigPrefix(bizModule), index1));
				Assert.hasText(domainAggregateConfig.getAggregateMasterName(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateMasterName')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1));
				Set<DomainAggregateSlaveConfigProperties> domainAggregateSlaveConfigs = domainAggregateConfig.getAggregateObjectSlaves();
				Assert.notEmpty(domainAggregateSlaveConfigs, String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateObjectSlaves')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1));
				int index2 = 0;
				for(DomainAggregateSlaveConfigProperties domainAggregateSlaveConfig : domainAggregateSlaveConfigs) {
					Assert.hasText(domainAggregateSlaveConfig.getAggregateSlaveName(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateObjectSlaves[%s].aggregateSlaveName')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1, index2));
					Assert.notNull(domainAggregateSlaveConfig.getMasterSlaveMapping(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateObjectSlaves[%s].masterSlaveMapping')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1, index2));
				}
				index1++;
			}
		}
		Map<String,DomainObjectConfigProperties> domainObjects = domain.getDomainObjects();
		Assert.notEmpty(domainObjects, String.format("Domain代码生成配置(%s.domain.domainObjects.*')必须指定!", getModuleCodegenConfigPrefix(bizModule)));
		int index1 = 0;
		for(Map.Entry<String,DomainObjectConfigProperties> entry1 : domainObjects.entrySet()) {
			DomainObjectConfigProperties domainObjectConfig = entry1.getValue();
			Assert.hasText(domainObjectConfig.getDomainObjectName(), String.format("Domain代码生成配置(%s.domain.domainObjects[%s].domainObjectName')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1));
			Assert.isTrue(JAVA_TYPE_NAMING_PATTERN.matcher(domainObjectConfig.getDomainObjectName()).matches(), String.format("Domain代码生成配置(%s.domain.domainObjects[%s].domainObjectName')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1));
			Assert.hasText(domainObjectConfig.getDomainTableName(), String.format("Domain代码生成配置(%s.domain.domainObjects[%s].domainTableName')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1));
			Assert.hasText(domainObjectConfig.getDomainObjectTitle(), String.format("Domain代码生成配置(%s.domain.domainObjects[%s].domainObjectTitle')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1));
			Assert.hasText(domainObjectConfig.getDomainObjectAlias(), String.format("Domain代码生成配置(%s.domain.domainObjects[%s].domainObjectAlias')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1));
			Assert.isTrue(JAVA_TYPE_NAMING_PATTERN.matcher(domainObjectConfig.getDomainObjectAlias()).matches(), String.format("Domain代码生成配置(%s.domain.domainObjects[%s].domainObjectAlias')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1));
			Map<String, DomainObjectColumnConfigProperties> domainObjectColumns = domainObjectConfig.getDomainObjectColumns();
			Assert.notEmpty(domainObjectColumns, String.format("Domain代码生成配置(%s.domain.domainObjects[%s].domainObjectColumns.*')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1));
			int index2 = 0;
			for(Map.Entry<String,DomainObjectColumnConfigProperties> entry2 : domainObjectColumns.entrySet()) {
				DomainObjectColumnConfigProperties domainObjectColumn = entry2.getValue();
				Assert.hasText(domainObjectColumn.getColumnName(), String.format("Domain代码生成配置(%s.domain.domainObjects[%s].domainObjectColumns[%s].columnName')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1, index2));
				Assert.hasText(domainObjectColumn.getColumnTitle(), String.format("Domain代码生成配置(%s.domain.domainObjects[%s].domainObjectColumns[%s].columnTitle')必须指定!", getModuleCodegenConfigPrefix(bizModule), index1, index2));
				index2++;
			}
			index1++;
		}
	}

	/**
	 * 初始化代码生成配置
	 * @throws Exception
	 */
	protected void initCodegenConfig() throws Exception {
		initDomainCodegenConfig();
	}

	/**
	 * 初始化Domain代码生成相关配置
	 */
	protected void initDomainCodegenConfig() throws Exception {
		initDomainCommonsConfigs(getDomain().getDomainCommons());
		Map<String,DomainObjectConfigProperties> domainObjectConfigs = getDomain().getDomainObjects();
		for(Map.Entry<String,DomainObjectConfigProperties> entry : domainObjectConfigs.entrySet()) {
			DomainObjectConfigProperties domainObjectConfig = entry.getValue();
			initDomainObjectConfigs(domainObjectConfig);
			initDomainColumnConfigs(domainObjectConfig);
			initDomainFieldConfigs(domainObjectConfig);
		}
		Map<String,DomainAggregateConfigProperties> domainAggregateConfigs = getDomain().getDomainAggregates();
		for(Map.Entry<String,DomainAggregateConfigProperties> entry : domainAggregateConfigs.entrySet()) {
			initDomainAggregateConfig(entry.getValue());
		}
	}

	/**
	 * 初始化领域对象公共配置
	 * @param domainCommonsConfig
	 */
	protected void initDomainCommonsConfigs(DomainCommonsConfigProperties domainCommonsConfig) {
		domain.getDomainCommons().setCommentAuthor(StringUtils.defaultIfBlank(domain.getDomainCommons().getCommentAuthor(), CodegenConstants.DEFAULT_CODEGEN_COMMENT_AUTHOR));
		if(domain.getDomainCommons().getIntrospectConfig().getIntrospectDialect() == null) { //补充数据库方言
			String inferJdbcUrl = SpringUtils.getEnvProperty(String.format("spring.datasource.%s.url", domain.getDomainCommons().getIntrospectDataSource()), String.class);
			domain.getDomainCommons().getIntrospectConfig().setIntrospectDialect(DatabaseType.of(JdbcUtils.getDbType(inferJdbcUrl)));
		}
		domain.getDomainCommons().getIntrospectConfig().setIntrospectDataSource(domain.getDomainCommons().getIntrospectDataSource());
		Set<DomainEnumConfigProperties> domainEnumConfigs = domain.getDomainCommons().getDomainEnums();
		if(!CollectionUtils.isEmpty(domainEnumConfigs)) {
			for(DomainEnumConfigProperties domainEnumConfig : domainEnumConfigs) {
				domainEnumConfig.setTargetPackage(StringUtils.defaultIfBlank(domainEnumConfig.getTargetPackage(), domain.getDomainCommons().defaultEnumsPackage()));
			}
		}
	}

	/**
	 * 初始化领域聚合对象配置
	 * @param domainAggregateConfig
	 */
	protected void initDomainAggregateConfig(DomainAggregateConfigProperties domainAggregateConfig) {
		domainAggregateConfig.setTargetPackage(StringUtils.defaultIfBlank(domainAggregateConfig.getTargetPackage(), domain.getDomainCommons().defaultModelPackage()));
		for(DomainAggregateSlaveConfigProperties domainAggregateSlaveConfig : domainAggregateConfig.getAggregateObjectSlaves()) {
			if(!domainAggregateSlaveConfig.isCascadingOnInsert()) {
				domainAggregateSlaveConfig.setValidateOnInsert(false);
			}
			if(!domainAggregateSlaveConfig.isCascadingOnUpdate()) {
				domainAggregateSlaveConfig.setValidateOnUpdate(false);
			}
			DomainObjectConfigProperties slaveDomainObjectConfig = getDomain().getDomainObjects().get(domainAggregateSlaveConfig.getAggregateSlaveName());
			if(DomainMasterSlaveRelation.RELATION_11.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //1:1关系?
				String fieldName = CodegenUtils.lowerCaseFirstChar(slaveDomainObjectConfig.getDomainObjectAlias());
				FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(slaveDomainObjectConfig.getGeneratedTargetName(null, true, false));
				String fieldTitle = slaveDomainObjectConfig.getDomainObjectTitle();
				DomainAggregateFieldConfigProperties aggregateObjectFieldConfig = new DomainAggregateFieldConfigProperties(fieldName, fieldType, fieldTitle, fieldTitle, domainAggregateConfig, domainAggregateSlaveConfig);
				domainAggregateConfig.getAggregateObjectFields().put(slaveDomainObjectConfig.getDomainObjectName(), aggregateObjectFieldConfig);
			} else if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getMasterSlaveRelation())) { //1:N关系?
				String fieldName = CodegenUtils.lowerCaseFirstChar(slaveDomainObjectConfig.getDomainObjectAlias()) + "List";
				FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(List.class.getName() + "<" + slaveDomainObjectConfig.getGeneratedTargetName(null, true, false) + ">");
				String fieldTitle = slaveDomainObjectConfig.getDomainObjectTitle();
				DomainAggregateFieldConfigProperties aggregateObjectFieldConfig = new DomainAggregateFieldConfigProperties(fieldName, fieldType, fieldTitle, fieldTitle, domainAggregateConfig, domainAggregateSlaveConfig);
				domainAggregateConfig.getAggregateObjectFields().put(slaveDomainObjectConfig.getDomainObjectName(), aggregateObjectFieldConfig);
			}
			//初始化当前DomainAggregateSlaveConfigProperties对应的DomainObjectConfigProperties配置
			DomainObjectConfigProperties masterDomainObjectConfig = getDomain().getDomainObjects().get(domainAggregateConfig.getAggregateMasterName());
			//relateFieldNameOfMaster在master领域对象中是唯一ID && relateFieldNameOfSlave是slave领域对象中的字段
			if(masterDomainObjectConfig.getPkColumns().size() == 1
					&& masterDomainObjectConfig.getPkColumns().get(0).getIntrospectedColumn().getJavaFieldName().equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfMaster())
					&& slaveDomainObjectConfig.getIntrospectedTable().getAllColumns().stream().anyMatch(column -> column.getJavaFieldName().equals(domainAggregateSlaveConfig.getMasterSlaveMapping().getRelateFieldNameOfSlave()))) {
				slaveDomainObjectConfig.setDomainMasterSlaveConfig(new DomainMasterSlaveConfigProperties(domainAggregateConfig, domainAggregateSlaveConfig, masterDomainObjectConfig, slaveDomainObjectConfig));
			}
		}
	}

	/**
	 * 初始化领域对象配置信息
	 * @param domainObjectConfig
	 */
	protected void initDomainObjectConfigs(DomainObjectConfigProperties domainObjectConfig) throws SQLException {
		domainObjectConfig.setTargetPackage(StringUtils.defaultIfBlank(domainObjectConfig.getTargetPackage(), domain.getDomainCommons().defaultModelPackage()));
		domainObjectConfig.setRuntimeDataSource(StringUtils.defaultIfBlank(domainObjectConfig.getRuntimeDataSource(), domain.getDomainCommons().getRuntimeDataSource()));
		domainObjectConfig.getIntrospectConfig().setIntrospectDataSource(StringUtils.defaultIfBlank(domainObjectConfig.getIntrospectConfig().getIntrospectDataSource(), domain.getDomainCommons().getIntrospectConfig().getIntrospectDataSource()));
		domainObjectConfig.getIntrospectConfig().setIntrospectDialect(ObjectUtils.defaultIfNull(domainObjectConfig.getIntrospectConfig().getIntrospectDialect(), domain.getDomainCommons().getIntrospectConfig().getIntrospectDialect()));
		domainObjectConfig.getIntrospectConfig().setForceDateTimeAsString(ObjectUtils.defaultIfNull(domainObjectConfig.getIntrospectConfig().isForceDateTimeAsString(), domain.getDomainCommons().getIntrospectConfig().isForceDateTimeAsString()));
		domainObjectConfig.getIntrospectConfig().setForceNumber1AsBoolean(ObjectUtils.defaultIfNull(domainObjectConfig.getIntrospectConfig().isForceNumber1AsBoolean(), domain.getDomainCommons().getIntrospectConfig().isForceNumber1AsBoolean()));
		domainObjectConfig.getIntrospectConfig().setForceDecimalNumericAsDouble(ObjectUtils.defaultIfNull(domainObjectConfig.getIntrospectConfig().isForceDecimalNumericAsDouble(), domain.getDomainCommons().getIntrospectConfig().isForceDecimalNumericAsDouble()));
		DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(getDomain());
		domainObjectConfig.setIntrospectedTable(databaseIntrospector.introspectedTable(domainObjectConfig));
	}

	/**
	 * 初始化领域对象的数据库列信息
	 * @param domainObjectConfig
	 */
	protected void initDomainColumnConfigs(DomainObjectConfigProperties domainObjectConfig) {
		IntrospectedTable introspectedTable = domainObjectConfig.getIntrospectedTable();
		List<IntrospectedColumn> pkColumns = introspectedTable.getPkColumns();
		Map<String,IntrospectedColumn> allColumns = introspectedTable.getAllColumns().stream().collect(Collectors.toMap(IntrospectedColumn::getColumnName, Function.identity(), StreamUtils.preferOld()));
		Map<String,DomainObjectColumnConfigProperties> domainObjectColumns = domainObjectConfig.getDomainObjectColumns();
		//1、domainObjectColumns中出现的列必须在表中真实存在,不存在的自动忽略
		processDomainColumnsPresent(domainObjectColumns, allColumns, pkColumns, introspectedTable);
		//2、domainObjectColumns未出现但在表真实存在的列也需要添加进去
		processDomainColumnsAbsent(domainObjectColumns, allColumns, pkColumns, introspectedTable);
		//3、domainObjectColumns最终处理
		processDomainColumnsFinal(domainObjectConfig);
	}

	/**
	 * domainObjectColumns中出现的列必须在表中真实存在,不存在的自动忽略
	 *
	 * @param domainObjectColumns
	 * @param allColumns
	 * @param pkColumns
	 * @param introspectedTable
	 */
	protected void processDomainColumnsPresent(Map<String,DomainObjectColumnConfigProperties> domainObjectColumns, Map<String,IntrospectedColumn> allColumns, List<IntrospectedColumn> pkColumns, IntrospectedTable introspectedTable) {
		for(Iterator<Map.Entry<String,DomainObjectColumnConfigProperties>> iterator = domainObjectColumns.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String,DomainObjectColumnConfigProperties> entry = iterator.next();
			String columnName = entry.getKey();
			DomainObjectColumnConfigProperties domainObjectColumn = entry.getValue();
			IntrospectedColumn introspectedColumn = allColumns.get(columnName);
			if(introspectedColumn != null) {
				domainObjectColumn.setIntrospectedColumn(introspectedColumn);
				domainObjectColumn.setPrimaryKey(pkColumns.contains(introspectedColumn));
				domainObjectColumn.setColumnTitle(StringUtils.defaultIfBlank(domainObjectColumn.getColumnTitle(), columnName));
				if(!domainObjectColumn.isPrimaryKey() || pkColumns.size() != 1) {
					domainObjectColumn.setPrimaryKeyGenerator(null);
				} else if(introspectedColumn.isAutoIncrement()) { //非复合主键且是AUTO_INCREMENT的则明确设置其主键生成策略
					domainObjectColumn.setPrimaryKeyGenerator(PrimaryKeyGenStrategy.IDENTITY.buildGenerator(""));
					domainObjectColumn.setColumnOnInsert(true);
					domainObjectColumn.setValidateOnInsert(false);
					domainObjectColumn.setColumnOnUpdate(false);
					domainObjectColumn.setValidateOnUpdate(true);
				}
				//如果是客户端指定主键值或者是复合主键，则需要出现在insert语句列中并且添加javax.validation校验
				if(domainObjectColumn.isPrimaryKey() && (domainObjectColumn.getPrimaryKeyGenerator() == null || pkColumns.size() > 1)) {
					domainObjectColumn.setColumnOnInsert(true);
					domainObjectColumn.setColumnOnUpdate(false);
					domainObjectColumn.setValidateOnUpsert(true);
				}
				//如果是基于SEQUENCE序列的主键生成方式，则其必须出现在insert列中
				if(domainObjectColumn.getPrimaryKeyGenerator() != null && PrimaryKeyGenStrategy.SEQUENCE.equals(domainObjectColumn.getPrimaryKeyGenerator().getStrategy())) {
					domainObjectColumn.setColumnOnInsert(true);
					domainObjectColumn.setValidateOnInsert(false);
					domainObjectColumn.setColumnOnUpdate(false);
					domainObjectColumn.setValidateOnUpdate(true);
				}
			} else { //出现在领域对象列配置中的字段但是数据库中不存在,则自动忽略
				iterator.remove();
				LOGGER.warn("【{}】>>> 代码自动生成配置列(columnName = {}, tableName = {})在数据库表中不存在,予以自动忽略...", bizModule, columnName, introspectedTable.getTableName());
			}
		}
	}

	/**
	 * domainObjectColumns未出现但在表真实存在的列也需要添加进去
	 *
	 * @param domainObjectColumns
	 * @param allColumns
	 * @param pkColumns
	 * @param introspectedTable
	 */
	protected void processDomainColumnsAbsent(Map<String,DomainObjectColumnConfigProperties> domainObjectColumns, Map<String,IntrospectedColumn> allColumns, List<IntrospectedColumn> pkColumns, IntrospectedTable introspectedTable) {
		for(Map.Entry<String,IntrospectedColumn> entry : allColumns.entrySet()) {
			String columnName = entry.getKey();
			IntrospectedColumn introspectedColumn = entry.getValue();
			if(!domainObjectColumns.containsKey(columnName)) {
				DomainObjectColumnConfigProperties domainObjectColumn = new DomainObjectColumnConfigProperties();
				domainObjectColumn.setColumnName(columnName);
				domainObjectColumn.setIntrospectedColumn(introspectedColumn);
				domainObjectColumn.setPrimaryKey(pkColumns.contains(introspectedColumn));
				domainObjectColumn.setColumnTitle(columnName);
				//默认出现在insert/update列中
				domainObjectColumn.setColumnOnInsert(true);
				domainObjectColumn.setColumnOnUpdate(true);
				if(!introspectedColumn.isNullable()) {
					domainObjectColumn.setValidateOnInsert(true);
					domainObjectColumn.setValidateOnUpdate(true);
				}
				if(domainObjectColumn.isPrimaryKey() && pkColumns.size() == 1 && introspectedColumn.isAutoIncrement()){ //非复合主键且是AUTO_INCREMENT的则明确设置其主键生成策略
					domainObjectColumn.setPrimaryKeyGenerator(PrimaryKeyGenStrategy.IDENTITY.buildGenerator(""));
					domainObjectColumn.setColumnOnInsert(true);
					domainObjectColumn.setValidateOnInsert(false);
				}
				//未出现在领域对象列配置中的字段代表不重要,不需要强制指定insert值,不需要出现在update列中
				domainObjectColumns.put(columnName, domainObjectColumn);
				LOGGER.warn("【{}】>>> 代码自动生成数据库列(columnName = {}, tableName = {})在配置列中不存在,予以自动添加...", bizModule, columnName, introspectedTable.getTableName());
			}
		}
	}
	
	protected void processDomainColumnsFinal(DomainObjectConfigProperties domainObjectConfig) {
		Map<String,DomainObjectColumnConfigProperties> domainObjectColumns = domainObjectConfig.getDomainObjectColumns();
		for(Map.Entry<String,DomainObjectColumnConfigProperties> entry : domainObjectColumns.entrySet()) {
			DomainObjectColumnConfigProperties domainObjectColumn = entry.getValue();
			domainObjectColumn.prepareValidateExpressions(getDomain());
		}
	}

	/**
	 * 初始化领域对象实体Model的所有字段
	 * @param domainObjectConfig
	 */
	protected void initDomainFieldConfigs(DomainObjectConfigProperties domainObjectConfig) {
		Map<String, DomainObjectColumnConfigProperties> domainObjectColumns = domainObjectConfig.getDomainObjectColumns();
		Map<String, DomainObjectFieldConfigProperties> domainObjectFields = new LinkedHashMap<>();
		//1、初始化领域对象实体Model的持久化字段
		for(Map.Entry<String, DomainObjectColumnConfigProperties> entry : domainObjectColumns.entrySet()) {
			DomainObjectColumnConfigProperties domainColumnConfig = entry.getValue();
			QueryConditionOperator queryConditionOperator = QueryConditionOperator.of(domainColumnConfig.getOperatorOnQuery()); //此处仅为初步设置，在2部分的辅助字段生成逻辑中会对此进行修正
			DomainObjectFieldConfigProperties objectFieldConfig = new DomainObjectFieldConfigProperties(domainColumnConfig.getIntrospectedColumn().getJavaFieldName(), domainColumnConfig.getIntrospectedColumn().getJavaFieldType(), domainColumnConfig.getColumnTitle(), domainColumnConfig.getIntrospectedColumn().getColumnComment(), domainColumnConfig.isPrimaryKey(), null, queryConditionOperator, domainColumnConfig);
			domainObjectFields.put(objectFieldConfig.getFieldName(), objectFieldConfig);
		}
		//2、初始化领域对象实体Model的辅助字段
		Map<String,Set<QueryConditionOperator>> criteriaQueryColumnOperators = getCriteriaQueryColumnOperators(domainObjectConfig);
		if(!CollectionUtils.isEmpty(criteriaQueryColumnOperators)) {
			for(Map.Entry<String, DomainObjectColumnConfigProperties> entry : domainObjectColumns.entrySet()) {
				DomainObjectColumnConfigProperties domainColumnConfig = entry.getValue();
				Set<QueryConditionOperator> columnQueryOperators = criteriaQueryColumnOperators.get(entry.getKey());
				String refJavaFieldName = domainColumnConfig.getIntrospectedColumn().getJavaFieldName(); //需要设置辅助字段的持久化字段
				FullyQualifiedJavaType refJavaFieldType = domainColumnConfig.getIntrospectedColumn().getJavaFieldType();
				if(!CollectionUtils.isEmpty(columnQueryOperators)) { //处理查询条件辅助字段
					if(isRangeQuerySupportFieldRequired(columnQueryOperators)) { //需要辅助的范围查询条件?
						String rangeMinSupportPropertyName = getRangeQuerySupportFieldName1(refJavaFieldName);
						String fieldTitle = refJavaFieldName + "的范围查询条件辅助字段";
						DomainObjectFieldConfigProperties rangeMinFieldConfig = new DomainObjectFieldConfigProperties(rangeMinSupportPropertyName, refJavaFieldType, fieldTitle, fieldTitle, false, ObjectSupportFieldType.QUERY_INPUT, getGTQueryConditionOperator(columnQueryOperators), domainColumnConfig);
						domainObjectFields.put(rangeMinFieldConfig.getFieldName(), rangeMinFieldConfig);
						String rangeMaxSupportPropertyName = getRangeQuerySupportFieldName2(refJavaFieldName);
						DomainObjectFieldConfigProperties rangeMaxFieldConfig = new DomainObjectFieldConfigProperties(rangeMaxSupportPropertyName, refJavaFieldType, fieldTitle, fieldTitle, false, ObjectSupportFieldType.QUERY_INPUT, getLTQueryConditionOperator(columnQueryOperators),domainColumnConfig);
						domainObjectFields.put(rangeMaxFieldConfig.getFieldName(), rangeMaxFieldConfig);
						//辅助字段关联的持久化字段在此情况下可以存在EQ操作符的,此处修正1处的queryConditionOperator设置
						domainObjectFields.get(refJavaFieldName).setQueryConditionOperator(QueryConditionOperator.EQ);
					} else if(isInQuerySupportFieldRequired(columnQueryOperators)) { //需要辅助的in查询条件?
						String inSupportPropertyName = getInQuerySupportFieldName(refJavaFieldName);
						String fieldTitle = refJavaFieldName + "的IN查询条件辅助字段";
						FullyQualifiedJavaType inSupportPropertyType = new FullyQualifiedJavaType(String.format("%s[]", refJavaFieldType)); //数组
						DomainObjectFieldConfigProperties supportFieldConfig = new DomainObjectFieldConfigProperties(inSupportPropertyName, inSupportPropertyType, fieldTitle, fieldTitle, false, ObjectSupportFieldType.QUERY_INPUT, QueryConditionOperator.IN, domainColumnConfig);
						domainObjectFields.put(supportFieldConfig.getFieldName(), supportFieldConfig);
						//辅助字段关联的持久化字段在此情况下可以存在EQ操作符的,此处修正1处的queryConditionOperator设置
						domainObjectFields.get(refJavaFieldName).setQueryConditionOperator(QueryConditionOperator.EQ);
					}
				}
				if(StringUtils.isNotEmpty(domainColumnConfig.getDecodeEnumType())) { //处理查询结果辅助字段
					String codedSupportPropertyName = getCodedSupportFieldName(refJavaFieldName);
					String fieldTitle = refJavaFieldName + "的查询结果辅助字段";
					//为可枚举的字段值生成字段值的对应名称字段，例如为productType字段生成productTypeName字段
					DomainObjectFieldConfigProperties supportFieldConfig = new DomainObjectFieldConfigProperties(codedSupportPropertyName,new FullyQualifiedJavaType("java.lang.String"), fieldTitle, fieldTitle, false, ObjectSupportFieldType.QUERY_OUTPUT, null, domainColumnConfig);
					domainObjectFields.put(supportFieldConfig.getFieldName(), supportFieldConfig);
				}
			}
		}
		domainObjectConfig.setDomainObjectFields(domainObjectFields);
	}

	/**
	 * 获取条件查询的字段及其操作符映射关系
	 * @param domainObjectConfig
	 * @return
	 */
	protected Map<String,Set<QueryConditionOperator>> getCriteriaQueryColumnOperators(DomainObjectConfigProperties domainObjectConfig) {
		return domainObjectConfig.getDomainObjectColumns().values().stream()
				.filter(column -> StringUtils.isNotEmpty(column.getOperatorOnQuery()))
				.collect(Collectors.toMap(DomainObjectColumnConfigProperties::getColumnName, column -> {
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

}
