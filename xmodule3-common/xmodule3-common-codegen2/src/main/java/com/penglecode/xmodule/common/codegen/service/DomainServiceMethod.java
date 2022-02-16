package com.penglecode.xmodule.common.codegen.service;

import java.util.ArrayList;
import java.util.List;

/**
 * 领域服务方法
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 23:56
 */
public class DomainServiceMethod {

    /** 是否生成当前方法,默认true */
    private boolean activated;

    /** 领域对象名称 */
    private String domainObjectName;

    /** 领域对象的Title */
    private String domainObjectTitle;

    /** 领域对象的别名(单数形式) */
    private String domainObjectAliasName;

    /** 领域对象的别名(复数形式) */
    private String domainObjectAliasesName;

    /** 领域对象变量名(单数形式) */
    private String domainObjectVariableName;

    /** 领域对象变量名(复数形式) */
    private String domainObjectVariablesName;

    /** 领域对象ID字段类型 */
    private String domainObjectIdType;

    /** 领域对象ID字段名称(单数形式) */
    private String domainObjectIdName;

    /** 领域对象ID字段名称(复数形式) */
    private String domainObjectIdsName;

    /** 方法动态返回类型 */
    private String methodDynamicReturnType;

    /** 方法体代码行 */
    private final List<String> methodBodyLines = new ArrayList<>();

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getDomainObjectName() {
        return domainObjectName;
    }

    public void setDomainObjectName(String domainObjectName) {
        this.domainObjectName = domainObjectName;
    }

    public String getDomainObjectTitle() {
        return domainObjectTitle;
    }

    public void setDomainObjectTitle(String domainObjectTitle) {
        this.domainObjectTitle = domainObjectTitle;
    }

    public String getDomainObjectAliasName() {
        return domainObjectAliasName;
    }

    public void setDomainObjectAliasName(String domainObjectAliasName) {
        this.domainObjectAliasName = domainObjectAliasName;
    }

    public String getDomainObjectAliasesName() {
        return domainObjectAliasesName;
    }

    public void setDomainObjectAliasesName(String domainObjectAliasesName) {
        this.domainObjectAliasesName = domainObjectAliasesName;
    }

    public String getDomainObjectVariableName() {
        return domainObjectVariableName;
    }

    public void setDomainObjectVariableName(String domainObjectVariableName) {
        this.domainObjectVariableName = domainObjectVariableName;
    }

    public String getDomainObjectVariablesName() {
        return domainObjectVariablesName;
    }

    public void setDomainObjectVariablesName(String domainObjectVariablesName) {
        this.domainObjectVariablesName = domainObjectVariablesName;
    }

    public String getDomainObjectIdType() {
        return domainObjectIdType;
    }

    public void setDomainObjectIdType(String domainObjectIdType) {
        this.domainObjectIdType = domainObjectIdType;
    }

    public String getDomainObjectIdName() {
        return domainObjectIdName;
    }

    public void setDomainObjectIdName(String domainObjectIdName) {
        this.domainObjectIdName = domainObjectIdName;
    }

    public String getDomainObjectIdsName() {
        return domainObjectIdsName;
    }

    public void setDomainObjectIdsName(String domainObjectIdsName) {
        this.domainObjectIdsName = domainObjectIdsName;
    }

    public String getMethodDynamicReturnType() {
        return methodDynamicReturnType;
    }

    public void setMethodDynamicReturnType(String methodDynamicReturnType) {
        this.methodDynamicReturnType = methodDynamicReturnType;
    }

    protected List<String> getMethodBodyLines() {
        return methodBodyLines;
    }

    public void addMethodBodyLine(String methodBodyLine) {
        methodBodyLines.add(methodBodyLine);
    }

}
