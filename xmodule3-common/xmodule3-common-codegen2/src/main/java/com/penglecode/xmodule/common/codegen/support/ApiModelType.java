package com.penglecode.xmodule.common.codegen.support;

/**
 * API接口数据模型类型
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/20 22:20
 */
public enum ApiModelType {

    //
    INBOUND_DTO("dto", "${domainObjectName}IDTO", "${domainObjectTitle}入站DTO"),
    OUTBOUND_DTO("dto", "${domainObjectName}ODTO", "${domainObjectTitle}出站DTO"),
    CREATE_REQUEST("request", "Create${domainObjectName}Request", "创建${domainObjectTitle}请求DTO"),
    MODIFY_REQUEST("request", "Modify${domainObjectName}Request", "修改${domainObjectTitle}请求DTO"),
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
