package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.codegen.config.DomainEntityConfig;
import com.penglecode.xmodule.common.codegen.config.ServiceCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.ServiceInterfaceConfig;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;

/**
 * 领域实体的领域服务接口代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 22:03
 */
public class DomainServiceInterfaceCodegenParameterBuilder extends AbstractDomainServiceCodegenParameterBuilder<ServiceInterfaceConfig, DomainServiceInterfaceCodegenParameter> {

    public DomainServiceInterfaceCodegenParameterBuilder(CodegenContext<ServiceCodegenConfigProperties, ServiceInterfaceConfig, DomainEntityConfig> codegenContext) {
        super(codegenContext);
    }

    public DomainServiceInterfaceCodegenParameterBuilder(ServiceCodegenConfigProperties codegenConfig, ServiceInterfaceConfig targetConfig, DomainEntityConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected DomainServiceInterfaceCodegenParameter setCustomCodegenParameter(DomainServiceInterfaceCodegenParameter codegenParameter) {
        codegenParameter.setTargetComment(getDomainObjectConfig().getDomainObjectTitle() + "领域服务接口");
        return super.setCustomCodegenParameter(codegenParameter);
    }

    @Override
    protected String getTargetTemplateName() {
        return "DomainServiceInterface.ftl";
    }

}
