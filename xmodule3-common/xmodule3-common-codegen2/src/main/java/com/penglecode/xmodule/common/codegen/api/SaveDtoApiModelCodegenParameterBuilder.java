package com.penglecode.xmodule.common.codegen.api;

import com.penglecode.xmodule.common.codegen.config.ApiCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.ApiModelConfig;
import com.penglecode.xmodule.common.codegen.config.DomainEntityConfig;
import com.penglecode.xmodule.common.codegen.config.DomainEntityFieldConfig;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;

/**
 * (ApiModelType.SAVE_DTO)API接口数据模型代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/2/20 21:25
 */
public class SaveDtoApiModelCodegenParameterBuilder extends DtoApiModelCodegenParameterBuilder {

    public SaveDtoApiModelCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, ApiModelConfig, DomainEntityConfig> codegenContext) {
        super(codegenContext);
    }

    public SaveDtoApiModelCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, ApiModelConfig targetConfig, DomainEntityConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected boolean isGenerableField(DomainEntityFieldConfig domainEntityFieldConfig) {
        //不是辅助字段 && 不是create_time/update_time字段
        return !domainEntityFieldConfig.getFieldClass().isSupportField()
                && !domainEntityFieldConfig.getDomainEntityColumnConfig().getColumnName().equals(getCodegenConfig().getDomain().getDomainCommons().getDefaultCreateTimeColumn())
                && !domainEntityFieldConfig.getDomainEntityColumnConfig().getColumnName().equals(getCodegenConfig().getDomain().getDomainCommons().getDefaultUpdateTimeColumn());
    }

}
