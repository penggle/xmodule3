package com.penglecode.xmodule.common.codegen.config;

/**
 * 绑定领域对象的目标生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/6/24 9:09
 */
public class DomainBoundedTargetConfigProperties<T extends GeneratedTargetConfigProperties> {

    /**
     * 代码生成目标对象配置
     */
    private final T generatedTargetConfig;

    /**
     * 绑定的领域对象配置
     */
    private final DomainObjectConfigProperties domainObjectConfig;

    /**
     * 绑定的领域聚合对象配置
     */
    private final DomainAggregateConfigProperties domainAggregateConfig;

    public DomainBoundedTargetConfigProperties(T generatedTargetConfig, DomainObjectConfigProperties domainObjectConfig, DomainAggregateConfigProperties domainAggregateConfig) {
        this.generatedTargetConfig = generatedTargetConfig;
        this.domainObjectConfig = domainObjectConfig;
        this.domainAggregateConfig = domainAggregateConfig;
    }

    public DomainObjectConfigProperties getDomainObjectConfig() {
        return domainObjectConfig;
    }

    public T getGeneratedTargetConfig() {
        return generatedTargetConfig;
    }

    public DomainAggregateConfigProperties getDomainAggregateConfig() {
        return domainAggregateConfig;
    }

}
