package com.penglecode.xmodule.common.codegen.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法代码生成参数
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/5 20:46
 */
@SuppressWarnings("unchecked")
public class CodegenMethodParameter extends HashMap<String,Object> {

    private static final long serialVersionUID = 1L;

    public CodegenMethodParameter() {
    }

    public CodegenMethodParameter(Map<? extends String, ?> m) {
        super(m);
    }

    public void setMethodActivated(boolean methodActivated) {
        put("methodActivated", methodActivated);
    }

    /** 方法是否激活 */
    public boolean getMethodActivated() {
        return (boolean) get("methodActivated");
    }

    public void setMethodDynamicReturnType(String methodDynamicReturnType) {
        put("methodDynamicReturnType", methodDynamicReturnType);
    }

    /** 方法动态返回类型 */
    public String getMethodDynamicReturnType() {
        return (String) get("methodDynamicReturnType");
    }

    public void setDomainObjectTitle(String domainObjectTitle) {
        put("domainObjectTitle", domainObjectTitle);
    }

    /** 领域对象中文名称 */
    public String getDomainObjectTitle() {
        return (String) get("domainObjectTitle");
    }

    public void setDomainObjectName(String domainObjectName) {
        put("domainObjectName", domainObjectName);
    }

    /** 领域对象英文名称 */
    public String getDomainObjectName() {
        return (String) get("domainObjectName");
    }

    public void setDomainObjectAlias(String domainObjectAlias) {
        put("domainObjectAlias", domainObjectAlias);
    }

    /** 领域对象英文别名(单数形式) */
    public String getDomainObjectAlias() {
        return (String) get("domainObjectAlias");
    }

    public void setDomainObjectAliases(String domainObjectAliases) {
        put("domainObjectAliases", domainObjectAliases);
    }

    /** 领域对象英文别名(复数形式) */
    public String getDomainObjectAliases() {
        return (String) get("domainObjectAliases");
    }

    public void setDomainObjectVariable(String domainObjectVariable) {
        put("domainObjectVariable", domainObjectVariable);
    }

    /** 领域对象变量名(单数形式) */
    public String getDomainObjectVariable() {
        return (String) get("domainObjectVariable");
    }

    public void setDomainObjectVariables(String domainObjectVariables) {
        put("domainObjectVariables", domainObjectVariables);
    }

    /** 领域对象变量名(复数形式) */
    public String getDomainObjectVariables() {
        return (String) get("domainObjectVariables");
    }

    public void setDomainObjectIdType(String domainObjectIdType) {
        put("domainObjectIdType", domainObjectIdType);
    }

    /** 领域对象ID字段类型*/
    public String getDomainObjectIdType() {
        return (String) get("domainObjectIdType");
    }

    public void setDomainObjectIdName(String domainObjectIdName) {
        put("domainObjectIdName", domainObjectIdName);
    }

    /** 领域对象ID字段名称(单数形式) */
    public String getDomainObjectIdName() {
        return (String) get("domainObjectIdName");
    }

    public void setDomainObjectIdsName(String domainObjectIdsName) {
        put("domainObjectIdsName", domainObjectIdsName);
    }

    /** 领域对象ID字段名称(复数形式) */
    public String getDomainObjectIdsName() {
        return (String) get("domainObjectIdsName");
    }

    public void setMethodCodeLines(List<String> methodCodeLines) {
        put("methodCodeLines", methodCodeLines);
    }

    /** 领域对象ID字段名称(复数形式) */
    public List<String> getMethodCodeLines() {
        return (List<String>) get("methodCodeLines");
    }

}
