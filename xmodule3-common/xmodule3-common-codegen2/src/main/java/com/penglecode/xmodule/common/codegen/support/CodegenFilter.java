package com.penglecode.xmodule.common.codegen.support;

import com.penglecode.xmodule.common.codegen.config.DomainObjectConfig;
import com.penglecode.xmodule.common.codegen.config.GenerableTargetConfig;
import com.penglecode.xmodule.common.codegen.config.ModuleCodegenConfigProperties;

/**
 * 代码自动生成过滤条件，用于动态过滤/跳过哪些代码生成
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/25 15:18
 */
@FunctionalInterface
public interface CodegenFilter {

    <C extends ModuleCodegenConfigProperties, T extends GenerableTargetConfig, D extends DomainObjectConfig> boolean filter(CodegenContext<C,T,D> codegenContext);

}
