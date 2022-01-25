package com.penglecode.xmodule.common.codegen.support;

import java.util.HashMap;
import java.util.Map;

/**
 * Freemarker模板参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/24 15:50
 */
public class TemplateParameter extends HashMap<String,Object> {

    private static final long serialVersionUID = 1L;

    public TemplateParameter() {
        super();
    }

    public TemplateParameter(Map<? extends String, ?> m) {
        super(m);
    }

    public void setTemplateFileName(String templateFileName) {
        put("templateFileName", templateFileName);
    }

    public String getTemplateFileName() {
        return (String) get("templateFileName");
    }

    public void setTargetFileName(String targetFileName) {
        put("targetFileName", targetFileName);
    }

    public String getTargetFileName() {
        return (String) get("targetFileName");
    }

    public void setTargetObjectName(String targetObjectName) {
        put("targetObjectName", targetObjectName);
    }

    public String getTargetObjectName() {
        return (String) get("targetObjectName");
    }

    public void setTargetPackage(String targetPackage) {
        put("targetPackage", targetPackage);
    }

    public String getTargetPackage() {
        return (String) get("targetPackage");
    }

    public void setCommentAuthor(String commentAuthor) {
        put("commentAuthor", commentAuthor);
    }

    public String getCommentAuthor() {
        return (String) get("commentAuthor");
    }

    public void setGenerateTime(String generateTime) {
        put("generateTime", generateTime);
    }

    public String getGenerateTime() {
        return (String) get("generateTime");
    }

    public void setDomainObjectTitle(String domainObjectTitle) {
        put("domainObjectTitle", domainObjectTitle);
    }

    public String getDomainObjectTitle() {
        return (String) get("domainObjectTitle");
    }

    public void setDomainObjectName(String domainObjectName) {
        put("domainObjectName", domainObjectName);
    }

    public String getDomainObjectName() {
        return (String) get("domainObjectName");
    }

    public void setDomainObjectAlias(String domainObjectAlias) {
        put("domainObjectAlias", domainObjectAlias);
    }

    public String getDomainObjectAlias() {
        return (String) get("domainObjectAlias");
    }

    public void setDomainObjectVarName(String domainObjectVarName) {
        put("domainObjectVarName", domainObjectVarName);
    }

    public String getDomainObjectVarName() {
        return (String) get("domainObjectVarName");
    }

    public void setAggregateObjectTitle(String aggregateObjectTitle) {
        put("aggregateObjectTitle", aggregateObjectTitle);
    }

    public String getAggregateObjectTitle() {
        return (String) get("aggregateObjectTitle");
    }

    public void setAggregateObjectName(String aggregateObjectName) {
        put("aggregateObjectName", aggregateObjectName);
    }

    public String getAggregateObjectName() {
        return (String) get("aggregateObjectName");
    }

    public void setAggregateObjectAlias(String aggregateObjectAlias) {
        put("aggregateObjectAlias", aggregateObjectAlias);
    }

    public String getAggregateObjectAlias() {
        return (String) get("aggregateObjectAlias");
    }

    public void setAggregateObjectVarName(String aggregateObjectVarName) {
        put("aggregateObjectVarName", aggregateObjectVarName);
    }

    public String getAggregateObjectVarName() {
        return (String) get("aggregateObjectVarName");
    }

}
