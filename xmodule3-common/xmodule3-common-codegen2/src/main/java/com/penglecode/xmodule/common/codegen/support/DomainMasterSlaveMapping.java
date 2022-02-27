package com.penglecode.xmodule.common.codegen.support;

import com.penglecode.xmodule.common.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主要领域对象与从属领域对象的映射关系
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/22 12:48
 */
public class DomainMasterSlaveMapping {

    private static final Pattern MAPPING_PATTERN = Pattern.compile("([a-zA-Z][a-zA-Z0-9_]+):([a-zA-Z][a-zA-Z0-9_]+)=(1:[1N])");

    /**
     * master领域对象的关联字段名
     */
    private final String relateFieldNameOfMaster;

    /**
     * slave领域对象的关联字段名
     */
    private final String relateFieldNameOfSlave;

    /**
     * master与slave的关联关系
     */
    private final DomainMasterSlaveRelation masterSlaveRelation;

    protected DomainMasterSlaveMapping(String relateFieldNameOfMaster, String relateFieldNameOfSlave, DomainMasterSlaveRelation masterSlaveRelation) {
        this.relateFieldNameOfMaster = StringUtils.snakeNamingToCamel(relateFieldNameOfMaster);
        this.relateFieldNameOfSlave = StringUtils.snakeNamingToCamel(relateFieldNameOfSlave);
        this.masterSlaveRelation = masterSlaveRelation;
    }

    public String getRelateFieldNameOfMaster() {
        return relateFieldNameOfMaster;
    }

    public String getRelateFieldNameOfSlave() {
        return relateFieldNameOfSlave;
    }

    public DomainMasterSlaveRelation getMasterSlaveRelation() {
        return masterSlaveRelation;
    }

    public static DomainMasterSlaveMapping parseMapping(String masterSlaveMapping) {
        masterSlaveMapping = masterSlaveMapping.replace(" ", "");
        Matcher matcher = MAPPING_PATTERN.matcher(masterSlaveMapping);
        if(matcher.matches()) {
            String relateFieldNameOfMaster = matcher.group(1);
            String relateFieldNameOfSlave = matcher.group(2);
            DomainMasterSlaveRelation masterSlaveRelation = DomainMasterSlaveRelation.of(matcher.group(3));
            if(StringUtils.isNotBlank(relateFieldNameOfMaster) && StringUtils.isNotBlank(relateFieldNameOfSlave) && masterSlaveRelation != null) {
                return new DomainMasterSlaveMapping(relateFieldNameOfMaster, relateFieldNameOfSlave, masterSlaveRelation);
            }
        }
        throw new IllegalArgumentException("Unresolved masterSlaveMapping: " + masterSlaveMapping);
    }

}
