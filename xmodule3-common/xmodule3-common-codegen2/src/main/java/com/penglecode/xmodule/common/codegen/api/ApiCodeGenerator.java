package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.ModuleCodeGenerator;
import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.ApiMethod;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * API接口代码生成器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/20 13:15
 */
public class ApiCodeGenerator extends ModuleCodeGenerator<ApiCodegenConfigProperties> {

    protected ApiCodeGenerator(String module) {
        super(module);
    }

    @Override
    protected String getCodeName() {
        return "Api代码";
    }

    @Override
    protected void executeGenerate() throws Exception {
        ApiCodegenConfigProperties codegenConfig = getCodegenConfig();
        Map<String,Set<ApiMethod>> clientApiDeclarations = codegenConfig.getApi().getClientConfig().getApiDeclarations();
        Map<String,Set<ApiMethod>> runtimeApiDeclarations = codegenConfig.getApi().getRuntimeConfig().getApiDeclarations();
        Map<String,DomainEntityConfig> domainEntityConfigs = codegenConfig.getDomain().getDomainEntities();
        if(!CollectionUtils.isEmpty(domainEntityConfigs)) {
            for (Map.Entry<String,DomainEntityConfig> entry : domainEntityConfigs.entrySet()) {
                DomainEntityConfig domainEntityConfig = entry.getValue();
                if(clientApiDeclarations.containsKey(domainEntityConfig.getDomainEntityName())) {
                    //1、生成指定领域实体的API接口(Client)
                    CodegenContext<ApiCodegenConfigProperties,ApiClientConfig,DomainEntityConfig> codegenContext1 = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getClientConfig(), domainEntityConfig);
                } else if(runtimeApiDeclarations.containsKey(domainEntityConfig.getDomainEntityName())) {
                    //2、生成指定领域实体的API接口(Controller/Client实现)
                    CodegenContext<ApiCodegenConfigProperties,ApiRuntimeConfig,DomainEntityConfig> codegenContext1 = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getRuntimeConfig(), domainEntityConfig);
                }
            }
        }
        Map<String,DomainAggregateConfig> domainAggregateConfigs = codegenConfig.getDomain().getDomainAggregates();
        if(!CollectionUtils.isEmpty(domainAggregateConfigs)) {
            for (Map.Entry<String, DomainAggregateConfig> entry : domainAggregateConfigs.entrySet()) {
                DomainAggregateConfig domainAggregateConfig = entry.getValue();
                if(clientApiDeclarations.containsKey(domainAggregateConfig.getDomainAggregateName())) {
                    //3、生成指定聚合根的API接口(Client)
                    CodegenContext<ApiCodegenConfigProperties,ApiClientConfig,DomainAggregateConfig> codegenContext1 = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getClientConfig(), domainAggregateConfig);
                } else if(runtimeApiDeclarations.containsKey(domainAggregateConfig.getDomainAggregateName())) {
                    //4、生成指定聚合根的API接口(Controller/Client实现)
                    CodegenContext<ApiCodegenConfigProperties,ApiRuntimeConfig,DomainAggregateConfig> codegenContext1 = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getRuntimeConfig(), domainAggregateConfig);
                }
            }
        }
    }

}
