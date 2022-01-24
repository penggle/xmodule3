package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.support.ApiMethod;
import com.penglecode.xmodule.common.codegen.support.ApiProtocol;

import java.util.Map;
import java.util.Set;

/**
 * API接口Client配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/22 15:02
 */
public class ApiClientConfig extends GenerableTargetConfig {

    /** API接口协议,feign是必须的,dubbo是可选的 */
    private Set<ApiProtocol> apiProtocols;

    /** API接口声明Map类型,[key=领域对象名称,value=接口方法名枚举] */
    private Map<String,Set<ApiMethod>> apiDeclarations;

    /** API接口继承interface列表 */
    private Set<Class<?>> apiExtendsInterfaces;

    public Set<ApiProtocol> getApiProtocols() {
        return apiProtocols;
    }

    public void setApiProtocols(Set<ApiProtocol> apiProtocols) {
        this.apiProtocols = apiProtocols;
    }

    public Map<String, Set<ApiMethod>> getApiDeclarations() {
        return apiDeclarations;
    }

    public void setApiDeclarations(Map<String, Set<ApiMethod>> apiDeclarations) {
        this.apiDeclarations = apiDeclarations;
    }

    public Set<Class<?>> getApiExtendsInterfaces() {
        return apiExtendsInterfaces;
    }

    public void setApiExtendsInterfaces(Set<Class<?>> apiExtendsInterfaces) {
        this.apiExtendsInterfaces = apiExtendsInterfaces;
    }

    @Override
    public String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix) {
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + "ApiService" + (includeSuffix ? ".java" : "");
    }

}
