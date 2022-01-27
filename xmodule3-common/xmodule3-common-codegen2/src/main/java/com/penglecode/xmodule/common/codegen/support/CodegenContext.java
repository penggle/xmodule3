package com.penglecode.xmodule.common.codegen.support;

import com.penglecode.xmodule.common.codegen.config.*;

/**
 * 代码生成上下文
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/25 14:22
 */
public class CodegenContext<C extends ModuleCodegenConfigProperties, T extends GenerableTargetConfig, D extends DomainObjectConfig> {

    private final CodegenModule codegenModule;

    /** 代码生成配置 */
    private final C codegenConfig;

    /** 当前生成目标配置 */
    private final T targetConfig;

    /** 当前生成目标绑定的领域对象 */
    private final D domainObjectConfig;

    public CodegenContext(CodegenModule codegenModule, C codegenConfig, T targetConfig, D domainObjectConfig) {
        this.codegenModule = codegenModule;
        this.codegenConfig = codegenConfig;
        this.targetConfig = targetConfig;
        this.domainObjectConfig = domainObjectConfig;
    }

    public CodegenModule getCodegenModule() {
        return codegenModule;
    }

    public C getCodegenConfig() {
        return codegenConfig;
    }

    public T getTargetConfig() {
        return targetConfig;
    }

    public D getDomainObjectConfig() {
        return domainObjectConfig;
    }

}