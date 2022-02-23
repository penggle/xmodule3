package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.support.CodegenParameter;
import com.penglecode.xmodule.common.codegen.support.ObjectFieldParameter;

import java.util.List;

/**
 * API接口数据模型代码生成参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/20 21:21
 */
public class ApiModelCodegenParameter extends CodegenParameter {

    /** 实体所有字段 */
    private List<ObjectFieldParameter> inherentFields;

    public ApiModelCodegenParameter(String targetTemplateName) {
        super(targetTemplateName);
    }

    public List<ObjectFieldParameter> getInherentFields() {
        return inherentFields;
    }

    public void setInherentFields(List<ObjectFieldParameter> inherentFields) {
        this.inherentFields = inherentFields;
    }

}
