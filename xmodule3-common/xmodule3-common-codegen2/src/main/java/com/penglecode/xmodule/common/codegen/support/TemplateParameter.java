package com.penglecode.xmodule.common.codegen.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Freemarker模板参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/24 15:50
 */
@SuppressWarnings("unchecked")
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

    /** 待生成目标对象的Freemarker模板文件名 */
    public String getTemplateFileName() {
        return (String) get("templateFileName");
    }

    public void setTargetFileName(String targetFileName) {
        put("targetFileName", targetFileName);
    }

    /** 待生成目标对象的文件名 */
    public String getTargetFileName() {
        return (String) get("targetFileName");
    }

    public void setTargetProjectImports(List<String> targetProjectImports) {
        put("targetProjectImports", targetProjectImports);
    }

    /** 待生成目标对象的项目内imports */
    public List<String> getTargetProjectImports() {
        return (List<String>) get("targetProjectImports");
    }

    public void setTargetThirdImports(List<String> targetThirdImports) {
        put("targetThirdImports", targetThirdImports);
    }

    /** 待生成目标对象的第三方imports */
    public List<String> getTargetThirdImports() {
        return (List<String>) get("targetThirdImports");
    }

    public void setTargetJdkImports(List<String> targetJdkImports) {
        put("targetJdkImports", targetJdkImports);
    }

    /** 待生成目标对象的JDK imports */
    public List<String> getTargetJdkImports() {
        return (List<String>) get("targetJdkImports");
    }

    public void setTargetClass(String targetClass) {
        put("targetClass", targetClass);
    }

    /** 待生成目标对象的类名 */
    public String getTargetClass() {
        return (String) get("targetClass");
    }

    public void setTargetExtends(String targetExtends) {
        put("targetExtends", targetExtends);
    }

    /** 待生成目标对象的extends父类名 */
    public String getTargetExtends() {
        return (String) get("targetExtends");
    }

    public void setTargetImplements(List<String> targetImplements) {
        put("targetImplements", targetImplements);
    }

    /** 待生成目标对象的实现接口名列表 */
    public List<String> getTargetImplements() {
        return (List<String>) get("targetImplements");
    }

    public void setTargetPackage(String targetPackage) {
        put("targetPackage", targetPackage);
    }

    /** 待生成目标对象的包名 */
    public String getTargetPackage() {
        return (String) get("targetPackage");
    }

    public void setTargetComment(String targetComment) {
        put("targetComment", targetComment);
    }

    /** 待生成目标对象的注释 */
    public String getTargetComment() {
        return (String) get("targetComment");
    }

    public void setTargetAuthor(String targetAuthor) {
        put("targetAuthor", targetAuthor);
    }

    /** 待生成目标对象的作者 */
    public String getTargetAuthor() {
        return (String) get("targetAuthor");
    }

    public void setTargetVersion(String targetVersion) {
        put("targetVersion", targetVersion);
    }

    /** 待生成目标对象的版本 */
    public String getTargetVersion() {
        return (String) get("targetVersion");
    }

    public void setTargetCreated(String targetCreated) {
        put("targetCreated", targetCreated);
    }

    /** 待生成目标对象的创建时间 */
    public String getTargetCreated() {
        return (String) get("targetCreated");
    }

    public void setTargetAnnotations(List<String> targetAnnotations) {
        put("targetAnnotations", targetAnnotations);
    }

    /** 待生成目标对象的SpringBean的名称 */
    public List<String> getTargetAnnotations() {
        return (List<String>) get("targetAnnotations");
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
