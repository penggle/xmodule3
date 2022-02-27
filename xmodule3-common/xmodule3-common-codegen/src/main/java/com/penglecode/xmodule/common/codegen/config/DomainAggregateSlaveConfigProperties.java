package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.support.DomainMasterSlaveMapping;

/**
 * 聚合对象的从属领域对象配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/10/17 17:28
 */
public class DomainAggregateSlaveConfigProperties {

    /**
     * 聚合对象的从属领域对象(指向下面domainObjects列表中的某一个)
     */
    private String aggregateSlaveName;

    /**
     * 主要领域对象与从属领域对象的映射关系(仅支持1:1/1:N)
     */
    private DomainMasterSlaveMapping masterSlaveMapping;

    /**
     * 新建主要领域对象时是否也级联新建从属领域对象
     */
    private boolean cascadingOnInsert = true;

    /**
     * 新建主要领域对象时是否校验从属领域对象不为空(仅在cascadingOnInsert=true时有效)
     */
    private boolean validateOnInsert = true;

    /**
     * 更新主要领域对象时是否也级联更新从属领域对象
     */
    private boolean cascadingOnUpdate = true;

    /**
     * 更新主要领域对象时是否校验从属领域对象不为空(仅在cascadingOnUpdate=true时有效)
     */
    private boolean validateOnUpdate = true;

    public String getAggregateSlaveName() {
        return aggregateSlaveName;
    }

    public void setAggregateSlaveName(String aggregateSlaveName) {
        this.aggregateSlaveName = aggregateSlaveName;
    }

    public DomainMasterSlaveMapping getMasterSlaveMapping() {
        return masterSlaveMapping;
    }

    public void setMasterSlaveMapping(String masterSlaveMapping) {
        this.masterSlaveMapping = DomainMasterSlaveMapping.of(masterSlaveMapping);
    }

    public boolean isCascadingOnInsert() {
        return cascadingOnInsert;
    }

    public void setCascadingOnInsert(boolean cascadingOnInsert) {
        this.cascadingOnInsert = cascadingOnInsert;
    }

    public boolean isCascadingOnUpdate() {
        return cascadingOnUpdate;
    }

    public void setCascadingOnUpdate(boolean cascadingOnUpdate) {
        this.cascadingOnUpdate = cascadingOnUpdate;
    }

    public boolean isValidateOnInsert() {
        return validateOnInsert;
    }

    public void setValidateOnInsert(boolean validateOnInsert) {
        this.validateOnInsert = validateOnInsert;
    }

    public boolean isValidateOnUpdate() {
        return validateOnUpdate;
    }

    public void setValidateOnUpdate(boolean validateOnUpdate) {
        this.validateOnUpdate = validateOnUpdate;
    }

    public void setCascadingOnUpsert(boolean cascadingOnUpsert) {
        setCascadingOnInsert(cascadingOnUpsert);
        setCascadingOnUpdate(cascadingOnUpsert);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainAggregateSlaveConfigProperties)) return false;

        DomainAggregateSlaveConfigProperties that = (DomainAggregateSlaveConfigProperties) o;

        return aggregateSlaveName.equals(that.aggregateSlaveName);
    }

    @Override
    public int hashCode() {
        return aggregateSlaveName.hashCode();
    }

}
