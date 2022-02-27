package com.penglecode.xmodule.common.codegen.mybatis;

import com.penglecode.xmodule.common.codegen.ModuleCodeGenerator;
import com.penglecode.xmodule.common.codegen.config.DomainEntityConfig;
import com.penglecode.xmodule.common.codegen.config.MybatisCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.MybatisJavaMapperConfig;
import com.penglecode.xmodule.common.codegen.config.MybatisXmlMapperConfig;
import com.penglecode.xmodule.common.codegen.support.CodegenContext;
import com.penglecode.xmodule.common.util.CollectionUtils;

import java.util.Map;

/**
 * Mybatis代码生成器
 * 专门用于生成指定bizModule模块下的Mybatis代码(如XxxMpper.java、XxxMapper.xml)
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/1/26 17:13
 */
public class MybatisCodeGenerator extends ModuleCodeGenerator<MybatisCodegenConfigProperties> {

    public MybatisCodeGenerator(String module) {
        super(module);
    }

    @Override
    protected String getCodeName() {
        return "Mybatis代码";
    }

    @Override
    protected void executeGenerate() throws Exception {
        MybatisCodegenConfigProperties codegenConfig = getCodegenConfig();
        Map<String,DomainEntityConfig> domainEntityConfigs = codegenConfig.getDomain().getDomainEntities();
        if(!CollectionUtils.isEmpty(domainEntityConfigs)) {
            for(Map.Entry<String,DomainEntityConfig> entry : domainEntityConfigs.entrySet()) {
                DomainEntityConfig domainEntityConfig = entry.getValue();
                //1、生成XxxMapper.java接口
                CodegenContext<MybatisCodegenConfigProperties,MybatisJavaMapperConfig,DomainEntityConfig> codegenContext1 = new CodegenContext<>(codegenConfig, codegenConfig.getMybatis().getJavaMapperConfig(), domainEntityConfig);
                generateTarget(codegenContext1, new MybatisJavaMapperCodegenParameterBuilder(codegenContext1).buildCodegenParameter());
                //2、生成XxxMapper.xml
                CodegenContext<MybatisCodegenConfigProperties,MybatisXmlMapperConfig,DomainEntityConfig> codegenContext2 = new CodegenContext<>(codegenConfig, codegenConfig.getMybatis().getXmlMapperConfig(), domainEntityConfig);
                generateTarget(codegenContext2, new MybatisXmlMapperCodegenParameterBuilder(codegenContext2).buildCodegenParameter());
            }
        }
    }

}
