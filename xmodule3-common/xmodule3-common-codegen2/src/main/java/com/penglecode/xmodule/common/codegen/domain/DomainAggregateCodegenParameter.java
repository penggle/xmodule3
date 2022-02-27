package com.penglecode.xmodule.common.codegen.domain;

import com.penglecode.xmodule.common.codegen.support.CodegenParameter;
import com.penglecode.xmodule.common.codegen.support.ObjectFieldParameter;

import java.util.List;

/**
 * 领域聚合根代码生成参数
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/6 21:48
 */
public class DomainAggregateCodegenParameter extends CodegenParameter {

    /** 实体自有字段 */
    private List<ObjectFieldParameter> inherentFields;

    /** 实体辅助字段 */
    private List<ObjectFieldParameter> supportFields;

    /** 实体所有字段(自有字段 + 辅助字段) */
    private List<ObjectFieldParameter> allFields;

    public DomainAggregateCodegenParameter(String targetTemplateName) {
        super(targetTemplateName);
    }

    public List<ObjectFieldParameter> getInherentFields() {
        return inherentFields;
    }

    public void setInherentFields(List<ObjectFieldParameter> inherentFields) {
        this.inherentFields = inherentFields;
    }

    public List<ObjectFieldParameter> getSupportFields() {
        return supportFields;
    }

    public void setSupportFields(List<ObjectFieldParameter> supportFields) {
        this.supportFields = supportFields;
    }

    public List<ObjectFieldParameter> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<ObjectFieldParameter> allFields) {
        this.allFields = allFields;
    }

}
