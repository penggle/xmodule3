package com.penglecode.xmodule.common.codegen.support;

import com.penglecode.xmodule.common.codegen.config.DomainObjectConfig;
import com.penglecode.xmodule.common.codegen.config.GenerableTargetConfig;
import com.penglecode.xmodule.common.codegen.config.ModuleCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.exception.CodegenRuntimeException;
import com.penglecode.xmodule.common.util.ClassUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;

/**
 * 代码生成模板参数工厂
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 21:56
 */
public abstract class CodegenParameterFactory<C extends ModuleCodegenConfigProperties, T extends GenerableTargetConfig, D extends DomainObjectConfig, P extends CodegenParameter> {

    /** 代码生成配置 */
    private final C codegenConfig;

    /** 当前生成目标配置 */
    private final T targetConfig;

    /** 当前生成目标绑定的领域对象 */
    private final D domainObjectConfig;

    protected CodegenParameterFactory(CodegenContext<C,T,D> codegenContext) {
        this(codegenContext.getCodegenConfig(), codegenContext.getTargetConfig(), codegenContext.getDomainObjectConfig());
    }

    protected CodegenParameterFactory(C codegenConfig, T targetConfig, D domainObjectConfig) {
        this.codegenConfig = codegenConfig;
        this.targetConfig = targetConfig;
        this.domainObjectConfig = domainObjectConfig;
    }

    /**
     * 创建代码生成参数
     * @return
     */
    public P createCodegenParameter() {
        Class<P> parameterType = ClassUtils.getSuperClassGenericType(getClass(), CodegenParameterFactory.class, 4);
        try {
            P codegenParameter = parameterType.getConstructor(String.class).newInstance(getTargetTemplateName());
            return setCodegenParameterCustom(setCodegenParameterCommon(codegenParameter));
        } catch (Exception e) {
            throw new CodegenRuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 设置代码生成参数的公共部分
     * @param codegenParameter
     * @return
     */
    protected P setCodegenParameterCommon(P codegenParameter) {
        String domainObjectName = domainObjectConfig.getDomainObjectName();
        codegenParameter.setTargetFileName(targetConfig.getGeneratedTargetName(domainObjectName, false, true));
        codegenParameter.setTargetPackage(targetConfig.getTargetPackage());
        codegenParameter.setTargetClass(targetConfig.getGeneratedTargetName(domainObjectName, false, false));
        codegenParameter.setTargetAuthor(codegenConfig.getDomain().getDomainCommons().getCommentAuthor());
        codegenParameter.setTargetVersion("1.0.0");
        codegenParameter.setTargetCreated(DateTimeUtils.formatNow("yyyy'年'MM'月'dd'日' a HH:mm"));
        return codegenParameter;
    }

    /**
     * 设置代码生成参数的自定义部分
     * @param codegenParameter
     * @return
     */
    protected abstract P setCodegenParameterCustom(P codegenParameter);

    /**
     * 获取生成目标对象的模板名称
     * @return
     */
    protected abstract String getTargetTemplateName();

    protected C getCodegenConfig() {
        return codegenConfig;
    }

    protected T getTargetConfig() {
        return targetConfig;
    }

    protected D getDomainObjectConfig() {
        return domainObjectConfig;
    }

}
