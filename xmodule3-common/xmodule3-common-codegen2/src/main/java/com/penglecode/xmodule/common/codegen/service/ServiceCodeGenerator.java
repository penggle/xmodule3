package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.ModuleCodeGenerator;
import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.util.CollectionUtils;

import java.util.Map;

/**
 * Service代码生成器
 * 专门用于生成领域服务/应用服务接口及其实现类的代码生成器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/28 20:51
 */
public class ServiceCodeGenerator extends ModuleCodeGenerator<ServiceCodegenConfigProperties> {

    public ServiceCodeGenerator(String module) {
        super(module);
    }

    @Override
    protected String getCodeName() {
        return "Service代码";
    }

    @Override
    protected void executeGenerate() throws Exception {
        ServiceCodegenConfigProperties codegenConfig = getCodegenConfig();
        Map<String,DomainEntityConfig> domainEntityConfigs = codegenConfig.getDomain().getDomainEntities();
        if(!CollectionUtils.isEmpty(domainEntityConfigs)) {
            for(Map.Entry<String, DomainEntityConfig> entry : domainEntityConfigs.entrySet()) {
                DomainEntityConfig domainEntityConfig = entry.getValue();
                //1、生成领域服务XxxService.java接口
                CodegenContext<ServiceCodegenConfigProperties,ServiceInterfaceConfig,DomainEntityConfig> codegenContext1 = new CodegenContext<>(codegenConfig, codegenConfig.getService().getDomain().getInterfaceConfig(), domainEntityConfig);
                generateTarget(codegenContext1, new DomainServiceInterfaceCodegenParameterBuilder(codegenContext1).buildCodegenParameter());
                //2、生成领域服务XxxServiceImpl.java实现
                CodegenContext<ServiceCodegenConfigProperties,ServiceImplementConfig,DomainEntityConfig> codegenContext2 = new CodegenContext<>(codegenConfig, codegenConfig.getService().getDomain().getImplementConfig(), domainEntityConfig);
                generateTarget(codegenContext2, new DomainServiceImplementCodegenParameterBuilder(codegenContext2).buildCodegenParameter());
            }
        }
        Map<String,DomainAggregateConfig> domainAggregateConfigs = codegenConfig.getDomain().getDomainAggregates();
        if(!CollectionUtils.isEmpty(domainAggregateConfigs)) {
            for(Map.Entry<String,DomainAggregateConfig> entry : domainAggregateConfigs.entrySet()) {
                DomainAggregateConfig domainAggregateConfig = entry.getValue();
                //3、生成应用服务XxxAppService.java接口(如果需要的话)
                CodegenContext<ServiceCodegenConfigProperties,ServiceInterfaceConfig,DomainAggregateConfig> codegenContext1 = new CodegenContext<>(codegenConfig, codegenConfig.getService().getApplication().getInterfaceConfig(), domainAggregateConfig);
                generateTarget(codegenContext1, new ApplicationServiceInterfaceCodegenParameterBuilder(codegenContext1).buildCodegenParameter());
                //4、生成应用服务XxxAppServiceImpl.java实现(如果需要的话)
                CodegenContext<ServiceCodegenConfigProperties,ServiceImplementConfig,DomainAggregateConfig> codegenContext2 = new CodegenContext<>(codegenConfig, codegenConfig.getService().getApplication().getImplementConfig(), domainAggregateConfig);
                generateTarget(codegenContext2, new ApplicationServiceImplementCodegenParameterBuilder(codegenContext2).buildCodegenParameter());
            }
        }
    }

}
