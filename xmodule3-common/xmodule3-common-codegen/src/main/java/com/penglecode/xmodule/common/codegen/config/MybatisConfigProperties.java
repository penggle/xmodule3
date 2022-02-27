package com.penglecode.xmodule.common.codegen.config;

/**
 * Mybatis代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/9/5 0:40
 */
public class MybatisConfigProperties {

    /**
     * XxxMapper.xml配置
     */
    private MybatisXmlMapperConfigProperties xmlMapperConfig;

    /**
     * XxxMapper.java源码配置
     */
    private MybatisJavaMapperConfigProperties javaMapperConfig;

    public MybatisXmlMapperConfigProperties getXmlMapperConfig() {
        return xmlMapperConfig;
    }

    public void setXmlMapperConfig(MybatisXmlMapperConfigProperties xmlMapperConfig) {
        this.xmlMapperConfig = xmlMapperConfig;
    }

    public MybatisJavaMapperConfigProperties getJavaMapperConfig() {
        return javaMapperConfig;
    }

    public void setJavaMapperConfig(MybatisJavaMapperConfigProperties javaMapperConfig) {
        this.javaMapperConfig = javaMapperConfig;
    }

}
