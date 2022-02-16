package com.penglecode.xmodule.common.codegen.domain;

import com.penglecode.xmodule.common.codegen.support.CodegenParameter;

import java.util.List;

/**
 * 领域聚合根代码生成参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 21:48
 */
public class DomainAggregateCodegenParameter extends CodegenParameter {

    /** 实体自有字段 */
    private List<ObjectField> inherentFields;

    /** 实体辅助字段 */
    private List<ObjectField> supportFields;

    /** 实体所有字段(自有字段 + 辅助字段) */
    private List<ObjectField> allFields;

    public DomainAggregateCodegenParameter(String targetTemplateName) {
        super(targetTemplateName);
    }

    public List<ObjectField> getInherentFields() {
        return inherentFields;
    }

    public void setInherentFields(List<ObjectField> inherentFields) {
        this.inherentFields = inherentFields;
    }

    public List<ObjectField> getSupportFields() {
        return supportFields;
    }

    public void setSupportFields(List<ObjectField> supportFields) {
        this.supportFields = supportFields;
    }

    public List<ObjectField> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<ObjectField> allFields) {
        this.allFields = allFields;
    }

}
