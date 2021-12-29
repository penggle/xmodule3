package com.penglecode.xmodule.common.codegen.config;

/**
 * 领域对象服务Master-Slave配置
 * 用于动态生成领域服务：getSlaveByMasterId() removeSlaveByMasterId()等
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/24 11:14
 */
public class DomainMasterSlaveConfigProperties {

    private final DomainAggregateConfigProperties domainAggregateConfig;

    private final DomainAggregateSlaveConfigProperties domainAggregateSlaveConfig;

    private final DomainObjectConfigProperties masterDomainObjectConfig;

    private final DomainObjectConfigProperties slaveDomainObjectConfig;

    private final DomainObjectColumnConfigProperties masterDomainObjectId;

    public DomainMasterSlaveConfigProperties(DomainAggregateConfigProperties domainAggregateConfig, DomainAggregateSlaveConfigProperties domainAggregateSlaveConfig, DomainObjectConfigProperties masterDomainObjectConfig, DomainObjectConfigProperties slaveDomainObjectConfig) {
        this.domainAggregateConfig = domainAggregateConfig;
        this.domainAggregateSlaveConfig = domainAggregateSlaveConfig;
        this.masterDomainObjectConfig = masterDomainObjectConfig;
        this.slaveDomainObjectConfig = slaveDomainObjectConfig;
        this.masterDomainObjectId = masterDomainObjectConfig.getPkColumns().get(0);
    }

    public DomainAggregateConfigProperties getDomainAggregateConfig() {
        return domainAggregateConfig;
    }

    public DomainAggregateSlaveConfigProperties getDomainAggregateSlaveConfig() {
        return domainAggregateSlaveConfig;
    }

    public DomainObjectConfigProperties getMasterDomainObjectConfig() {
        return masterDomainObjectConfig;
    }

    public DomainObjectConfigProperties getSlaveDomainObjectConfig() {
        return slaveDomainObjectConfig;
    }

    public DomainObjectColumnConfigProperties getMasterDomainObjectId() {
        return masterDomainObjectId;
    }

}
