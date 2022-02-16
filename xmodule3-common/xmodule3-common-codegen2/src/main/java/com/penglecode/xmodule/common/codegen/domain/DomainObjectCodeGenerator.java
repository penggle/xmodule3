package com.penglecode.xmodule.common.codegen.domain;

import com.penglecode.xmodule.common.codegen.ModuleCodeGenerator;
import com.penglecode.xmodule.common.codegen.config.DomainAggregateConfig;
import com.penglecode.xmodule.common.codegen.config.DomainEntityConfig;
import com.penglecode.xmodule.common.codegen.config.DomainEnumConfig;
import com.penglecode.xmodule.common.codegen.config.DomainObjectCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * 领域对象(枚举、实体、聚合根)代码生成器
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/28 21:01
 */
public class DomainObjectCodeGenerator extends ModuleCodeGenerator<DomainObjectCodegenConfigProperties> {

    public DomainObjectCodeGenerator(String module) {
        super(module);
    }

    @Override
    protected String getCodeName() {
        return "Domain领域对象代码";
    }

    @Override
    protected void executeGenerate() throws Exception {
        DomainObjectCodegenConfigProperties codegenConfig = getCodegenConfig();
        Set<DomainEnumConfig> domainEnumConfigs = codegenConfig.getDomain().getDomainCommons().getDomainEnums();
        if(!CollectionUtils.isEmpty(domainEnumConfigs)) { //1、生成领域枚举对象
            for(DomainEnumConfig domainEnumConfig : domainEnumConfigs) {
                if(domainEnumConfig.getDomainEnumClass() == null) { //需要生成领域枚举对象?
                    CodegenContext<DomainObjectCodegenConfigProperties,DomainEnumConfig,DomainEnumConfig> codegenContext = new CodegenContext<>(codegenConfig, domainEnumConfig, domainEnumConfig);
                    generateTarget(codegenContext, new DomainEnumCodegenParameterFactory(codegenContext).createCodegenParameter());
                }
            }
        }
        Map<String,DomainEntityConfig> domainEntityConfigs = codegenConfig.getDomain().getDomainEntities();
        if(!CollectionUtils.isEmpty(domainEntityConfigs)) { //2、生成领域实体对象
            for(Map.Entry<String,DomainEntityConfig> entry : domainEntityConfigs.entrySet()) {
                DomainEntityConfig domainEntityConfig = entry.getValue();
                CodegenContext<DomainObjectCodegenConfigProperties,DomainEntityConfig,DomainEntityConfig> codegenContext = new CodegenContext<>(codegenConfig, domainEntityConfig, domainEntityConfig);
                generateTarget(codegenContext, new DomainEntityCodegenParameterFactory(codegenContext).createCodegenParameter());
            }
        }
        Map<String,DomainAggregateConfig> domainAggregateConfigs = codegenConfig.getDomain().getDomainAggregates();
        if(!CollectionUtils.isEmpty(domainAggregateConfigs)) { //3、生成聚合对象
            for(Map.Entry<String,DomainAggregateConfig> entry : domainAggregateConfigs.entrySet()) {
                DomainAggregateConfig domainAggregateConfig = entry.getValue();
                CodegenContext<DomainObjectCodegenConfigProperties,DomainAggregateConfig,DomainAggregateConfig> codegenContext = new CodegenContext<>(codegenConfig, domainAggregateConfig, domainAggregateConfig);
                generateTarget(codegenContext, new DomainAggregateCodegenParameterFactory(codegenContext).createCodegenParameter());
            }
        }
    }

}