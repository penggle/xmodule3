package com.penglecode.xmodule.common.codegen.config;

/**
 * Mybatis代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/22 14:48
 */
public class MybatisConfig {

    /** XxxMapper.xml配置 */
    private MybatisXmlMapperConfig xmlMapperConfig;

    /** XxxMapper.java源码配置 */
    private MybatisJavaMapperConfig javaMapperConfig;

    public MybatisXmlMapperConfig getXmlMapperConfig() {
        return xmlMapperConfig;
    }

    public void setXmlMapperConfig(MybatisXmlMapperConfig xmlMapperConfig) {
        this.xmlMapperConfig = xmlMapperConfig;
    }

    public MybatisJavaMapperConfig getJavaMapperConfig() {
        return javaMapperConfig;
    }

    public void setJavaMapperConfig(MybatisJavaMapperConfig javaMapperConfig) {
        this.javaMapperConfig = javaMapperConfig;
    }

}