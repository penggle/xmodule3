package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.ModuleCodeGenerator;
import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.ApiMethod;
import com.penglecode.xmodule.common.codegen.support.ApiModelType;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * API接口代码生成器
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/20 13:15
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
                if(clientApiDeclarations.containsKey(domainEntityConfig.getDomainEntityName())) { //1、生成指定领域实体的API接口(Client)
                    generateApiModel(codegenConfig, domainEntityConfig); //生成DTO
                    CodegenContext<ApiCodegenConfigProperties,ApiClientConfig,DomainEntityConfig> codegenContext = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getClientConfig(), domainEntityConfig);
                } else if(runtimeApiDeclarations.containsKey(domainEntityConfig.getDomainEntityName())) { //2、生成指定领域实体的API接口(Controller/Client实现)
                    generateApiModel(codegenConfig, domainEntityConfig); //生成DTO
                    CodegenContext<ApiCodegenConfigProperties,ApiRuntimeConfig,DomainEntityConfig> codegenContext = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getRuntimeConfig(), domainEntityConfig);
                }
            }
        }
        Map<String,DomainAggregateConfig> domainAggregateConfigs = codegenConfig.getDomain().getDomainAggregates();
        if(!CollectionUtils.isEmpty(domainAggregateConfigs)) {
            for (Map.Entry<String, DomainAggregateConfig> entry : domainAggregateConfigs.entrySet()) {
                DomainAggregateConfig domainAggregateConfig = entry.getValue();
                if(clientApiDeclarations.containsKey(domainAggregateConfig.getDomainAggregateName())) { //3、生成指定聚合根的API接口(Client)
                    generateApiModel(codegenConfig, domainAggregateConfig); //生成DTO
                    CodegenContext<ApiCodegenConfigProperties,ApiClientConfig,DomainAggregateConfig> codegenContext = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getClientConfig(), domainAggregateConfig);
                } else if(runtimeApiDeclarations.containsKey(domainAggregateConfig.getDomainAggregateName())) { //4、生成指定聚合根的API接口(Controller/Client实现)
                    generateApiModel(codegenConfig, domainAggregateConfig); //生成DTO
                    CodegenContext<ApiCodegenConfigProperties,ApiRuntimeConfig,DomainAggregateConfig> codegenContext = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getRuntimeConfig(), domainAggregateConfig);
                }
            }
        }
    }

    /**
     * 生成指定领域对象的Request/Response对象
     * @param codegenConfig
     * @param domainObjectConfig
     * @throws Exception
     */
    protected void generateApiModel(ApiCodegenConfigProperties codegenConfig, DomainObjectConfig domainObjectConfig) throws Exception {
        CodegenContext<ApiCodegenConfigProperties,ApiModelConfig,? extends DomainObjectConfig> codegenContext1 = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getModelConfig().withModelType(ApiModelType.SAVE_REQUEST), domainObjectConfig);
        generateTarget(codegenContext1, new SaveReqApiModelCodegenParameterBuilder<>(codegenContext1).buildCodegenParameter());

        CodegenContext<ApiCodegenConfigProperties,ApiModelConfig,? extends DomainObjectConfig> codegenContext2 = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getModelConfig().withModelType(ApiModelType.QUERY_REQUEST), domainObjectConfig);
        generateTarget(codegenContext2, new QueryReqApiModelCodegenParameterBuilder<>(codegenContext2).buildCodegenParameter());

        CodegenContext<ApiCodegenConfigProperties,ApiModelConfig,? extends DomainObjectConfig> codegenContext3 = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getModelConfig().withModelType(ApiModelType.QUERY_RESPONSE), domainObjectConfig);
        generateTarget(codegenContext3, new QueryResApiModelCodegenParameterBuilder<>(codegenContext3).buildCodegenParameter());
    }

    /**
     * 生成指定聚合根的Request/Response DTO对象
     * @param codegenConfig
     * @param domainAggregateConfig
     * @throws Exception
     */
    protected void generateApiModel(ApiCodegenConfigProperties codegenConfig, DomainAggregateConfig domainAggregateConfig) throws Exception {
        //生成Master的SAVE_DTO/QUERY_DTO
        generateDtoModel(codegenConfig, codegenConfig.getDomain().getDomainEntities().get(domainAggregateConfig.getAggregateMasterEntity()));
        for(DomainAggregateSlaveConfig domainAggregateSlaveConfig : domainAggregateConfig.getAggregateSlaveEntities()) {
            //生成Slave的SAVE_DTO/QUERY_DTO
            generateDtoModel(codegenConfig, codegenConfig.getDomain().getDomainEntities().get(domainAggregateSlaveConfig.getAggregateSlaveEntity()));
        }
        //生成聚合根的SAVE_REQUEST/QUERY_REQUEST/QUERY_RESPONSE对象
        generateApiModel(codegenConfig, domainAggregateConfig);
    }

    /**
     * 生成指定领域实体的DTO对象
     * @param codegenConfig
     * @param domainEntityConfig
     * @throws Exception
     */
    protected void generateDtoModel(ApiCodegenConfigProperties codegenConfig, DomainEntityConfig domainEntityConfig) throws Exception {
        CodegenContext<ApiCodegenConfigProperties,ApiModelConfig,DomainEntityConfig> codegenContext1 = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getModelConfig().withModelType(ApiModelType.SAVE_DTO), domainEntityConfig);
        generateTarget(codegenContext1, new SaveDtoApiModelCodegenParameterBuilder(codegenContext1).buildCodegenParameter());

        CodegenContext<ApiCodegenConfigProperties,ApiModelConfig,DomainEntityConfig> codegenContext2 = new CodegenContext<>(codegenConfig, codegenConfig.getApi().getModelConfig().withModelType(ApiModelType.QUERY_DTO), domainEntityConfig);
        generateTarget(codegenContext2, new QueryDtoApiModelCodegenParameterBuilder(codegenContext2).buildCodegenParameter());
    }

}
