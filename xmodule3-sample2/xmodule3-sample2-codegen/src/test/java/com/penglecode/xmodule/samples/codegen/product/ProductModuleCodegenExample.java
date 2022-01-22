package com.penglecode.xmodule.samples.codegen.product;

import com.penglecode.xmodule.samples.codegen.boot.SamplesCodegenApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/10/2 15:27
 */
@SpringBootTest(classes=SamplesCodegenApplication.class)
public class ProductModuleCodegenExample {

    /*@Test
    public void generateDomains() {
        DomainObjectCodeGenerator generator = new DomainObjectCodeGenerator("product");
        generator.override(config -> {
            config.getDomain().getDomainCommons().setTargetProject("../xmodule3-samples-domain/xmodule3-samples-domain-model/src/main/java");
            *//*config.getDomain().getDomainObjects().forEach((doName, doConfig) -> {
                doConfig.setTargetProject("../xmodule3-samples-domain/xmodule3-samples-common/src/main/java");
            });*//*
        }).filter((module, codegenConfig, targetConfig) -> {
            *//*
            if(targetConfig.getGeneratedTargetConfig() instanceof DomainObjectConfigProperties) {
                DomainObjectConfigProperties doConfig = (DomainObjectConfigProperties) targetConfig.getGeneratedTargetConfig();
                return doConfig.getDomainObjectName().equals("ProductInfo"); //只生成ProductInfo.java
            }
            return false;
            *//*
            return true;
        }).generate();
    }

    @Test
    public void generateMybatis() {
        MybatisCodeGenerator generator = new MybatisCodeGenerator("product");
        generator.override(config -> {
            config.getMybatis().getJavaMapperConfig().setTargetProject("../xmodule3-samples-dal/src/main/java");
            config.getMybatis().getXmlMapperConfig().setTargetProject("../xmodule3-samples-dal/src/main/java");
        }).filter((module, codegenConfig, targetConfig) -> {
            *//*
            if(targetConfig.getGeneratedTargetConfig() instanceof MybatisXmlMapperConfigProperties) {
                DomainObjectConfigProperties doConfig = targetConfig.getDomainObjectConfig();
                return doConfig.getDomainObjectName().equals("ProductSpec") || doConfig.getDomainObjectName().equals("ProductStock"); //只生成ProductSpecMapper.xml和ProductStockMapper.xml
            }
            return false;
            *//*
            return true;
        }).generate();
    }

    @Test
    public void generateService() {
        ServiceCodeGenerator generator = new ServiceCodeGenerator("product");
        generator.override(config -> {
            config.getService().getDomain().getInterfaceConfig().setTargetProject("../xmodule3-samples-domain/xmodule3-samples-domain-service-sdk/src/main/java");
            config.getService().getDomain().getImplementConfig().setTargetProject("../xmodule3-samples-domain/xmodule3-samples-domain-service-runtime/src/main/java");
        }).filter((module, codegenConfig, targetConfig) -> {
            //return targetConfig.getGeneratedTargetConfig() instanceof ServiceImplementConfigProperties; //只生成Service实现类
            return true;
        }).generate();
    }*/

}
