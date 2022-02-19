package com.penglecode.xmodule.common.codegen.domain;

import com.penglecode.xmodule.common.codegen.config.DomainEnumConfig;
import com.penglecode.xmodule.common.codegen.config.DomainObjectCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.codegen.support.CodegenParameterBuilder;
import com.penglecode.xmodule.common.codegen.util.CodegenUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 领域枚举代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 22:03
 */
public class DomainEnumCodegenParameterBuilder extends CodegenParameterBuilder<DomainObjectCodegenConfigProperties, DomainEnumConfig, DomainEnumConfig, DomainEnumCodegenParameter> {

    public DomainEnumCodegenParameterBuilder(CodegenContext<DomainObjectCodegenConfigProperties, DomainEnumConfig, DomainEnumConfig> codegenContext) {
        super(codegenContext);
    }

    public DomainEnumCodegenParameterBuilder(DomainObjectCodegenConfigProperties codegenConfig, DomainEnumConfig targetConfig, DomainEnumConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected DomainEnumCodegenParameter setCustomCodegenParameter(DomainEnumCodegenParameter codegenParameter) {
        codegenParameter.setTargetComment(getDomainObjectConfig().getDomainEnumTitle());
        List<DomainEnumCodegenParameter.EnumValue> enumValues = getTargetConfig().getDomainEnumValues().entrySet().stream().map(entry -> {
            DomainEnumCodegenParameter.EnumValue enumValue = new DomainEnumCodegenParameter.EnumValue();
            enumValue.setEnumValue(entry.getKey());
            enumValue.setCodeValue((String)CodegenUtils.quotingValueIfNecessary(entry.getValue()[0]));
            enumValue.setNameValue((String)CodegenUtils.quotingValueIfNecessary(entry.getValue()[1]));
            return enumValue;
        }).collect(Collectors.toList());
        codegenParameter.setEnumValues(enumValues);
        codegenParameter.setCodeFieldType(getTargetConfig().getDomainEnumCodeField().getFieldType().getShortName());
        codegenParameter.setCodeFieldName(getTargetConfig().getDomainEnumCodeField().getFieldName());
        codegenParameter.setCodeGetterName(CodegenUtils.getGetterMethodName(getTargetConfig().getDomainEnumCodeField().getFieldName(), getTargetConfig().getDomainEnumCodeField().getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.addTargetImportType(getTargetConfig().getDomainEnumCodeField().getFieldType());
        codegenParameter.setNameFieldType(getTargetConfig().getDomainEnumNameField().getFieldType().getShortName());
        codegenParameter.setNameFieldName(getTargetConfig().getDomainEnumNameField().getFieldName());
        codegenParameter.setNameGetterName(CodegenUtils.getGetterMethodName(getTargetConfig().getDomainEnumNameField().getFieldName(), getTargetConfig().getDomainEnumNameField().getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.addTargetImportType(getTargetConfig().getDomainEnumNameField().getFieldType());
        return codegenParameter;
    }

    @Override
    protected String getTargetTemplateName() {
        return "DomainEnum.ftl";
    }

}
