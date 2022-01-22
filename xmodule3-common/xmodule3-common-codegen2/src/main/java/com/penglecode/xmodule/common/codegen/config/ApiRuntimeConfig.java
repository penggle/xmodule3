package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.support.ApiMethod;

import java.util.Map;
import java.util.Set;

/**
 * API接口实现配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 15:15
 */
public class ApiRuntimeConfig extends GenerableTarget {

    /** API接口声明Map类型,[key=领域对象名称,value=接口方法名枚举] */
    private Map<String,Set<ApiMethod>> apiDeclarations;

    /** API接口继承父类(可以不配置) */
    private Class<?> apiExtendsClass;

    public Map<String, Set<ApiMethod>> getApiDeclarations() {
        return apiDeclarations;
    }

    public void setApiDeclarations(Map<String, Set<ApiMethod>> apiDeclarations) {
        this.apiDeclarations = apiDeclarations;
    }

    public Class<?> getApiExtendsClass() {
        return apiExtendsClass;
    }

    public void setApiExtendsClass(Class<?> apiExtendsClass) {
        this.apiExtendsClass = apiExtendsClass;
    }

}