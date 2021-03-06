package com.penglecode.xmodule.common.codegen.config;

/**
 * Mybatis- Mapper XML文件生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/22 14:38
 */
public class MybatisXmlMapperConfig extends GenerableTargetConfig {

    @Override
    public String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix) {
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + "Mapper" + (includeSuffix ? ".xml" : "");
    }

}
