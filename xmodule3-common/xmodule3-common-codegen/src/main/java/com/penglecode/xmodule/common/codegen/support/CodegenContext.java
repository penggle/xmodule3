package com.penglecode.xmodule.common.codegen.support;

import com.penglecode.xmodule.common.codegen.config.DomainObjectColumnConfigProperties;
import com.penglecode.xmodule.common.codegen.config.DomainObjectConfigProperties;
import com.penglecode.xmodule.common.codegen.config.DomainObjectFieldConfigProperties;
import com.penglecode.xmodule.common.codegen.config.ModuleCodegenConfigProperties;

/**
 * 代码生成上下文
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/7/19 22:23
 */
public class CodegenContext {

    private final ModuleCodegenConfigProperties moduleCodegenConfig;

    private final DomainObjectConfigProperties domainObjectConfig;

    private final DomainObjectColumnConfigProperties domainObjectColumnConfig;

    private final DomainObjectFieldConfigProperties domainObjectFieldConfig;

    public CodegenContext(ModuleCodegenConfigProperties moduleCodegenConfig, DomainObjectConfigProperties domainObjectConfig, DomainObjectColumnConfigProperties domainObjectColumnConfig, DomainObjectFieldConfigProperties domainObjectFieldConfig) {
        this.moduleCodegenConfig = moduleCodegenConfig;
        this.domainObjectConfig = domainObjectConfig;
        this.domainObjectColumnConfig = domainObjectColumnConfig;
        this.domainObjectFieldConfig = domainObjectFieldConfig;
    }

    public ModuleCodegenConfigProperties getModuleCodegenConfig() {
        return moduleCodegenConfig;
    }

    public DomainObjectConfigProperties getDomainObjectConfig() {
        return domainObjectConfig;
    }

    public DomainObjectColumnConfigProperties getDomainObjectColumnConfig() {
        return domainObjectColumnConfig;
    }

    public DomainObjectFieldConfigProperties getDomainObjectFieldConfig() {
        return domainObjectFieldConfig;
    }

}
