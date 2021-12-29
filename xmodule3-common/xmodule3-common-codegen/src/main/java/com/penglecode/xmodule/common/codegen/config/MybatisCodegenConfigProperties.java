package com.penglecode.xmodule.common.codegen.config;

import org.springframework.util.Assert;

/**
 * Mybatis代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/9/5 0:24
 */
public class MybatisCodegenConfigProperties extends ModuleCodegenConfigProperties {

    /** Mybatis代码生成配置 */
    private MybatisConfigProperties mybatis;

    public MybatisCodegenConfigProperties(String bizModule) {
        super(bizModule);
    }

    public MybatisConfigProperties getMybatis() {
        return mybatis;
    }

    public void setMybatis(MybatisConfigProperties mybatis) {
        this.mybatis = mybatis;
    }

    @Override
    protected void validateCodegenConfig() throws Exception {
        super.validateCodegenConfig();
        validateMybatisCodegenConfig();
    }

    /**
     * 校验Mybatis代码生成配置
     * @throws Exception
     */
    protected void validateMybatisCodegenConfig() throws Exception {
        Assert.hasText(mybatis.getJavaMapperConfig().getTargetProject(), String.format("Mybatis代码生成配置(%s.mybatis.javaMapperConfig.targetProject')必须指定!", getModuleCodegenConfigPrefix(getBizModule())));
        Assert.hasText(mybatis.getJavaMapperConfig().getTargetPackage(), String.format("Mybatis代码生成配置(%s.mybatis.javaMapperConfig.targetPackage')必须指定!", getModuleCodegenConfigPrefix(getBizModule())));
        Assert.hasText(mybatis.getXmlMapperConfig().getTargetProject(), String.format("Mybatis代码生成配置(%s.mybatis.xmlMapperConfig.targetProject')必须指定!", getModuleCodegenConfigPrefix(getBizModule())));
        Assert.hasText(mybatis.getXmlMapperConfig().getTargetPackage(), String.format("Mybatis代码生成配置(%s.mybatis.xmlMapperConfig.targetPackage')必须指定!", getModuleCodegenConfigPrefix(getBizModule())));
    }

    @Override
    protected void initCodegenConfig() throws Exception {
        super.initCodegenConfig();
        initMybatisCodegenConfig();
    }

    /**
     * 初始化Mybatis代码生成相关配置
     */
    public void initMybatisCodegenConfig() {
        //1、初始化领域对象配置中的mapperAnnotations
        mybatis.getJavaMapperConfig().prepareMapperAnnotations(getDomain());
    }

}
