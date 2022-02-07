package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * 领域对象(实体、聚合根)配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/1/25 14:29
 */
public abstract class DomainObjectConfig extends GenerableTargetConfig {

    /**
     * 返回当前领域对象的名称(英文)
     *
     * @return
     */
    public abstract String getDomainObjectName();

    /**
     * 返回当前领域对象的名称(中文)
     *
     * @return
     */
    public abstract String getDomainObjectTitle();

    /**
     * 返回当前领域对象的别名(英文)
     *
     * @return
     */
    public abstract String getDomainObjectAlias();

}
