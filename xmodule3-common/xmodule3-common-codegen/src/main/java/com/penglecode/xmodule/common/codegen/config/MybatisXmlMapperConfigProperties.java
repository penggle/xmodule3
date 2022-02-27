package com.penglecode.xmodule.common.codegen.config;

/**
 * Mybatis- Mapper XML文件生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/9/5 13:40
 */
public class MybatisXmlMapperConfigProperties extends GeneratedTargetConfigProperties {

    @Override
    public <T extends GeneratedTargetConfigProperties> String getGeneratedTargetName(DomainBoundedTargetConfigProperties<T> targetConfig, boolean includePackage, boolean includeSuffix) {
        return (includePackage ? getTargetPackage() + "." : "") + targetConfig.getDomainObjectConfig().getDomainObjectName() + "Mapper" + (includeSuffix ? ".java" : "");
    }

}
