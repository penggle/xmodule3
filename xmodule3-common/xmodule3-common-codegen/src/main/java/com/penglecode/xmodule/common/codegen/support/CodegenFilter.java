package com.penglecode.xmodule.common.codegen.support;

import com.penglecode.xmodule.common.codegen.config.DomainBoundedTargetConfigProperties;
import com.penglecode.xmodule.common.codegen.config.GeneratedTargetConfigProperties;
import com.penglecode.xmodule.common.codegen.config.ModuleCodegenConfigProperties;

/**
 * 代码自动生成过滤条件，用于动态过滤/跳过哪些代码生成
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/7/25 15:18
 */
@FunctionalInterface
public interface CodegenFilter<C extends ModuleCodegenConfigProperties, T extends GeneratedTargetConfigProperties> {

    boolean filter(CodegenModule module, C codegenConfig, DomainBoundedTargetConfigProperties<T> targetConfig);

}
