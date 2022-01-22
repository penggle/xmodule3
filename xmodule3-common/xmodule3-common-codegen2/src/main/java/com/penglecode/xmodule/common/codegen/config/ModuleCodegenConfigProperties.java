package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.consts.CodegenConstants;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Set;

/**
 * 分模块的代码自动生成配置基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 16:28
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

    public static String getModuleCodegenConfigPrefix(String module) {
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
        String codegenConfigPrefix = getModuleCodegenConfigPrefix(module);
        //1、校验领域公共配置
        Assert.hasText(domain.getDomainCommons().getRuntimeDataSource(), String.format("Domain代码生成配置(%s.domain.domainCommons.runtimeDataSource')必须指定!", codegenConfigPrefix));
        Assert.hasText(domain.getDomainCommons().getIntrospectDataSource(), String.format("Domain代码生成配置(%s.domain.domainCommons.introspectDataSource')必须指定!", codegenConfigPrefix));
        Assert.hasText(domain.getDomainCommons().getTargetProject(), String.format("Domain代码生成配置(%s.domain.domainCommons.targetProject')必须指定!", codegenConfigPrefix));
        Assert.hasText(domain.getDomainCommons().getTargetPackage(), String.format("Domain代码生成配置(%s.domain.domainCommons.targetPackage')必须指定!", codegenConfigPrefix));

        Set<DomainEnumConfig> domainEnumConfigs = domain.getDomainCommons().getDomainEnums();
        if(!CollectionUtils.isEmpty(domainEnumConfigs)) {
            int index = 0;
            for(DomainEnumConfig domainEnumConfig : domainEnumConfigs) {
                Assert.hasText(domainEnumConfig.getDomainEnumName(), String.format("Domain代码生成配置(%s.domain.domainCommons.domainlEnums[%s].domainEnumName')必须指定!", codegenConfigPrefix, index));
                Assert.hasText(domainEnumConfig.getDomainEnumTitle(), String.format("Domain代码生成配置(%s.domain.domainCommons.domainlEnums[%s].domainEnumTitle')必须指定!", codegenConfigPrefix, index));
                Assert.notNull(domainEnumConfig.getDomainEnumCodeField(), String.format("Domain代码生成配置(%s.domain.domainCommons.domainlEnums[%s].domainEnumCodeField')必须指定!", codegenConfigPrefix, index));
                Assert.notNull(domainEnumConfig.getDomainEnumNameField(), String.format("Domain代码生成配置(%s.domain.domainCommons.domainlEnums[%s].domainEnumNameField')必须指定!", codegenConfigPrefix, index));
                if(domainEnumConfig.getDomainEnumClass() == null) {
                    Assert.notEmpty(domainEnumConfig.getDomainEnumValues(), String.format("Domain代码生成配置(%s.domain.domainCommons.domainlEnums[%s].domainEnumValues')必须指定!", codegenConfigPrefix, index));
                }
                index++;
            }
        }

        //2、校验领域实体配置
        Map<String,DomainEntityConfig> domainEntities = domain.getDomainEntities();
        Assert.notEmpty(domainEntities, String.format("Domain代码生成配置(%s.domain.domainEntities.*')必须指定!", codegenConfigPrefix));
        int index1 = 0;
        for(Map.Entry<String,DomainEntityConfig> entry1 : domainEntities.entrySet()) {
            DomainEntityConfig domainEntityConfig = entry1.getValue();
            Assert.hasText(domainEntityConfig.getDomainEntityName(), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityName')必须指定!", codegenConfigPrefix, index1));
            Assert.isTrue(CodegenUtils.isValidJavaTypeNaming(domainEntityConfig.getDomainEntityName()), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityName')命名不合法!", codegenConfigPrefix, index1));
            Assert.hasText(domainEntityConfig.getDomainEntityTable(), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityTable')必须指定!", codegenConfigPrefix, index1));
            Assert.hasText(domainEntityConfig.getDomainEntityTitle(), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityTitle')必须指定!", codegenConfigPrefix, index1));
            Assert.hasText(domainEntityConfig.getDomainEntityAlias(), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityAlias')必须指定!", codegenConfigPrefix, index1));
            Assert.isTrue(CodegenUtils.isValidJavaTypeNaming(domainEntityConfig.getDomainEntityAlias()), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityAlias')命名不合法!", codegenConfigPrefix, index1));
            Map<String,DomainEntityColumnConfig> domainEntityColumns = domainEntityConfig.getDomainEntityColumns();
            Assert.notEmpty(domainEntityColumns, String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityColumns.*')必须指定!", codegenConfigPrefix, index1));
            int index2 = 0;
            for(Map.Entry<String,DomainEntityColumnConfig> entry2 : domainEntityColumns.entrySet()) {
                DomainEntityColumnConfig domainEntityColumn = entry2.getValue();
                Assert.hasText(domainEntityColumn.getColumnName(), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityColumns[%s].columnName')必须指定!", codegenConfigPrefix, index1, index2));
                Assert.hasText(domainEntityColumn.getColumnTitle(), String.format("Domain代码生成配置(%s.domain.domainEntities[%s].domainEntityColumns[%s].columnTitle')必须指定!", codegenConfigPrefix, index1, index2));
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
                Assert.hasText(domainAggregateConfig.getDomainAggregateName(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].domainAggregateName')必须指定!", codegenConfigPrefix, index1));
                Assert.isTrue(CodegenUtils.isValidJavaTypeNaming(domainAggregateConfig.getDomainAggregateName()), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].domainAggregateName')命名不合法!", codegenConfigPrefix, index1));
                Assert.hasText(domainAggregateConfig.getDomainAggregateTitle(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].domainAggregateTitle')必须指定!", codegenConfigPrefix, index1));
                Assert.hasText(domainAggregateConfig.getDomainAggregateAlias(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].domainAggregateAlias')必须指定!", codegenConfigPrefix, index1));
                Assert.isTrue(CodegenUtils.isValidJavaTypeNaming(domainAggregateConfig.getDomainAggregateAlias()), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].domainAggregateAlias')命名不合法!", codegenConfigPrefix, index1));
                Assert.hasText(domainAggregateConfig.getAggregateMasterEntity(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateMasterEntity')必须指定!", codegenConfigPrefix, index1));
                Assert.isTrue(domainEntities.containsKey(domainAggregateConfig.getAggregateMasterEntity()), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateMasterEntity')不存在!", codegenConfigPrefix, index1));
                Set<DomainAggregateSlaveConfig> domainAggregateSlaveConfigs = domainAggregateConfig.getAggregateSlaveEntities();
                Assert.notEmpty(domainAggregateSlaveConfigs, String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateSlaveEntities')必须指定!", codegenConfigPrefix, index1));
                int index2 = 0;
                for(DomainAggregateSlaveConfig domainAggregateSlaveConfig : domainAggregateSlaveConfigs) {
                    Assert.hasText(domainAggregateSlaveConfig.getAggregateSlaveEntity(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateSlaveEntities[%s].aggregateSlaveEntity')必须指定!", codegenConfigPrefix, index1, index2));
                    Assert.isTrue(domainEntities.containsKey(domainAggregateSlaveConfig.getAggregateSlaveEntity()), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateSlaveEntities[%s].aggregateSlaveEntity')不存在!", codegenConfigPrefix, index1, index2));
                    Assert.notNull(domainAggregateSlaveConfig.getMasterSlaveMapping(), String.format("Domain代码生成配置(%s.domain.domainAggregates[%s].aggregateSlaveEntities[%s].masterSlaveMapping')必须指定!", codegenConfigPrefix, index1, index2));
                }
                index1++;
            }
        }
    }

    /**
     * 初始化Domain代码生成相关配置
     */
    protected void initDomainCodegenConfig() throws Exception {

    }

}
