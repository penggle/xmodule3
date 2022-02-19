package com.penglecode.xmodule.common.codegen.mybatis;

import com.penglecode.xmodule.common.codegen.config.*;
import com.penglecode.xmodule.common.codegen.support.*;
import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.TemplateUtils;

import java.util.*;

/**
 * 领域实体的Mybatis Java-Mapper代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/6 22:03
 */
public class MybatisJavaMapperCodegenParameterBuilder extends CodegenParameterBuilder<MybatisCodegenConfigProperties, MybatisJavaMapperConfig, DomainEntityConfig, MybatisJavaMapperCodegenParameter> {

    public MybatisJavaMapperCodegenParameterBuilder(CodegenContext<MybatisCodegenConfigProperties, MybatisJavaMapperConfig, DomainEntityConfig> codegenContext) {
        super(codegenContext);
    }

    public MybatisJavaMapperCodegenParameterBuilder(MybatisCodegenConfigProperties codegenConfig, MybatisJavaMapperConfig targetConfig, DomainEntityConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected MybatisJavaMapperCodegenParameter setCustomCodegenParameter(MybatisJavaMapperCodegenParameter codegenParameter) {
        codegenParameter.setTargetComment(getDomainObjectConfig().getDomainEntityTitle() + "Mybatis-Mapper接口");
        codegenParameter.setTargetExtends(String.format("BaseMybatisMapper<%s>", getDomainObjectConfig().getDomainEntityName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(getDomainObjectConfig().getGeneratedTargetName(getDomainObjectConfig().getDomainEntityName(), true, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(BaseMybatisMapper.class.getName()));

        Set<String> mapperAnnotationSet = getCodegenConfig().getMybatis().getJavaMapperConfig().getMapperAnnotations();
        List<String> mapperAnnotations = new ArrayList<>();
        if(!CollectionUtils.isEmpty(mapperAnnotationSet)) {
            for(String mapperAnnotation : mapperAnnotationSet) {
                String[] mapperAnnotationArray = mapperAnnotation.split(":");
                mapperAnnotations.add(parseMapperAnnotations(mapperAnnotationArray[1]));
                codegenParameter.addTargetImportType(new FullyQualifiedJavaType(mapperAnnotationArray[0]));
            }
        }
        codegenParameter.setTargetAnnotations(mapperAnnotations);
        return codegenParameter;
    }

    protected String parseMapperAnnotations(String mapperAnnotations) {
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("runtimeDataSource", getDomainObjectConfig().getRuntimeDataSource());
        return TemplateUtils.parseTemplate(mapperAnnotations, parameter);
    }

    @Override
    protected String getTargetTemplateName() {
        return "MybatisJavaMapper.ftl";
    }

}
