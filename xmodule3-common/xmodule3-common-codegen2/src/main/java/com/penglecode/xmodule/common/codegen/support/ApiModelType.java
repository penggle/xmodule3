package com.penglecode.xmodule.common.codegen.support;

/**
 * API接口数据模型类型
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/20 22:20
 */
public enum ApiModelType {

    SAVE_DTO("dto", "${domainObjectName}SaveDTO", "${domainObjectTitle}保存(入站)DTO"),
    QUERY_DTO("dto", "${domainObjectName}QueryDTO", "${domainObjectTitle}查询(出站)DTO"),
    SAVE_REQUEST("request", "Save${domainObjectName}Request", "保存${domainObjectTitle}请求DTO"),
    QUERY_REQUEST("request", "Query${domainObjectName}Request", "查询${domainObjectTitle}请求DTO"),
    QUERY_RESPONSE("response", "Query${domainObjectName}Response", "查询${domainObjectTitle}响应DTO");

    private final String defaultSubPackage;

    private final String modelNameTemplate;

    private final String modelDescTemplate;

    ApiModelType(String defaultSubPackage, String modelNameTemplate, String modelDescTemplate) {
        this.defaultSubPackage = defaultSubPackage;
        this.modelNameTemplate = modelNameTemplate;
        this.modelDescTemplate = modelDescTemplate;
    }

    public String getDefaultSubPackage() {
        return defaultSubPackage;
    }

    public String getModelNameTemplate() {
        return modelNameTemplate;
    }

    public String getModelDescTemplate() {
        return modelDescTemplate;
    }
}
