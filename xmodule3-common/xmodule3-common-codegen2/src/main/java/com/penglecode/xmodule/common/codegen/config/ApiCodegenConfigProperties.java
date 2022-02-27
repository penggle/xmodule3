package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.support.ApiProtocol;
import com.penglecode.xmodule.common.web.servlet.support.ServletHttpApiSupport;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Api代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/24 16:46
 */
public class ApiCodegenConfigProperties extends ServiceCodegenConfigProperties {

    private ApiConfig api;

    public ApiCodegenConfigProperties(String module) {
        super(module);
    }

    public ApiConfig getApi() {
        return api;
    }

    public void setApi(ApiConfig api) {
        this.api = api;
    }

    @Override
    protected void validateCodegenConfig() throws Exception {
        super.validateCodegenConfig();
        validateApiCodegenConfig();
    }

    @Override
    protected void initCodegenConfig() throws Exception {
        super.initCodegenConfig();
        initApiCodegenConfig();
    }

    /**
     * 校验Api代码生成配置
     */
    protected void validateApiCodegenConfig() {
        String codegenConfigPrefix = getCodegenConfigPrefix(getModule());
        //忽略xxx.api.targetProject的公共配置
        Assert.hasText(api.getClientConfig().getTargetProject(), String.format("Api代码生成配置(%s.api.clientConfig.targetProject)必须指定!", codegenConfigPrefix));
        Assert.hasText(api.getRuntimeConfig().getTargetProject(), String.format("Api代码生成配置(%s.api.runtimeConfig.targetProject)必须指定!", codegenConfigPrefix));
        Assert.hasText(api.getTargetPackage(), String.format("Api代码生成配置(%s.api.targetPackage)必须指定!", codegenConfigPrefix));
        Assert.hasText(api.getTargetProject(), String.format("Api代码生成配置(%s.api.clientConfig.targetProject)必须指定!", codegenConfigPrefix));
        Assert.isTrue(!CollectionUtils.isEmpty(api.getClientConfig().getApiDeclarations()) || !CollectionUtils.isEmpty(api.getRuntimeConfig().getApiDeclarations()), String.format("Api代码生成配置(%s.api.clientConfig.apiDeclarations|%s.api.runtimeConfig.apiDeclarations)必须指定之一!", codegenConfigPrefix, codegenConfigPrefix));
    }

    /**
     * 初始化Api代码生成配置
     */
    protected void initApiCodegenConfig() {
        Set<ApiProtocol> apiProtocols = api.getClientConfig().getApiProtocols();
        if(apiProtocols == null) {
            apiProtocols = new HashSet<>();
        }
        apiProtocols.add(ApiProtocol.FEIGN);
        if(api.getRuntimeConfig().getApiExtendsClass() == null) {
            api.getRuntimeConfig().setApiExtendsClass(ServletHttpApiSupport.class);
        }
        if(api.getClientConfig().getApiDeclarations() == null) {
            api.getClientConfig().setApiDeclarations(new HashMap<>());
        }
        if(api.getRuntimeConfig().getApiDeclarations() == null) {
            api.getRuntimeConfig().setApiDeclarations(new HashMap<>());
        }
    }

}
