package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.mybatis.SupportedDatabaseType;

/**
 * 领域对象内省配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/22 11:28
 */
public class DomainIntrospectConfig {

    /** 自省数据源名称 */
    private String introspectDataSource;

    /** 自省方言数据库 */
    private SupportedDatabaseType introspectDatabaseType;

    /** 强制将数据库中的日期时间字段映射为String类型? 默认为Java8日期/时间对象 */
    private boolean forceDateTimeAsString = true;

    /** 强制将数据库中的INT(1)/TINYINT(1)/NUMBER(1)等字段映射为Boolean类型? */
    private boolean forceNumber1AsBoolean = true;

    /** 强制将数据库中的浮点型字段映射为Double类型? 默认为BigDecimal */
    private boolean forceDecimalNumericAsDouble = true;

    public String getIntrospectDataSource() {
        return introspectDataSource;
    }

    public void setIntrospectDataSource(String introspectDataSource) {
        this.introspectDataSource = introspectDataSource;
    }

    public SupportedDatabaseType getIntrospectDatabaseType() {
        return introspectDatabaseType;
    }

    public void setIntrospectDatabaseType(SupportedDatabaseType introspectDatabaseType) {
        this.introspectDatabaseType = introspectDatabaseType;
    }

    public boolean isForceDateTimeAsString() {
        return forceDateTimeAsString;
    }

    public void setForceDateTimeAsString(boolean forceDateTimeAsString) {
        this.forceDateTimeAsString = forceDateTimeAsString;
    }

    public boolean isForceNumber1AsBoolean() {
        return forceNumber1AsBoolean;
    }

    public void setForceNumber1AsBoolean(boolean forceNumber1AsBoolean) {
        this.forceNumber1AsBoolean = forceNumber1AsBoolean;
    }

    public boolean isForceDecimalNumericAsDouble() {
        return forceDecimalNumericAsDouble;
    }

    public void setForceDecimalNumericAsDouble(boolean forceDecimalNumericAsDouble) {
        this.forceDecimalNumericAsDouble = forceDecimalNumericAsDouble;
    }

}
