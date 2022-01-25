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

    private final C codegenConfig;

    private final T targetConfig;

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
