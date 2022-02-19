package com.penglecode.xmodule.common.codegen.service;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务方法
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 23:56
 */
public class ServiceMethod {

    /** 是否生成当前方法,默认true */
    private boolean activated;

    /** 对应的领域对象参数 */
    private DomainObjectParameter domainObjectParameter;

    /** 方法返回类型 */
    private String methodReturnType;

    /** 方法名 */
    private String methodName;

    /** 方法体代码行 */
    private List<String> methodBodyLines = new ArrayList<>();

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public DomainObjectParameter getDomainObjectParameter() {
        return domainObjectParameter;
    }

    public void setDomainObjectParameter(DomainObjectParameter domainObjectParameter) {
        this.domainObjectParameter = domainObjectParameter;
    }

    public String getMethodReturnType() {
        return methodReturnType;
    }

    public void setMethodReturnType(String methodReturnType) {
        this.methodReturnType = methodReturnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getMethodBodyLines() {
        return methodBodyLines;
    }

    public void setMethodBodyLines(List<String> methodBodyLines) {
        this.methodBodyLines = methodBodyLines;
    }

}
