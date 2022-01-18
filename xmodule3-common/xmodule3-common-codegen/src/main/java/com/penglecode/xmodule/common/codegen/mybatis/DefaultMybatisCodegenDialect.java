package com.penglecode.xmodule.common.codegen.mybatis;

import com.penglecode.xmodule.common.mybatis.RdbmsVendor;

/**
 * 默认的Mybatis模块代码生成方言
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/9/20 0:28
 */
public class DefaultMybatisCodegenDialect implements MybatisCodegenDialect {

    @Override
    public RdbmsVendor getDatabaseType() {
        return null; //返回空代表未知的
    }

}
