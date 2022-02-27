package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codegen.util.CodegenUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import org.springframework.boot.autoconfigure.dal.NamedDatabase;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mybatis Mapper接口配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/8/21 13:39
 */
public class MybatisJavaMapperConfigProperties extends GeneratedTargetConfigProperties {

    /** 当前领域对象的Mapper接口上的数据库名称,默认单库即:'@NamedDatabase("${runtimeDataSource}")' */
    private Set<String> mapperAnnotations;

    public Set<String> getMapperAnnotations() {
        return mapperAnnotations;
    }

    public void setMapperAnnotations(Set<String> mapperAnnotations) {
        this.mapperAnnotations = mapperAnnotations;
    }

    public void prepareMapperAnnotations(DomainConfigProperties domainConfigProperties) {
        Set<String> finalMapperAnnotations = Optional.ofNullable(mapperAnnotations).orElseGet(LinkedHashSet::new);
        finalMapperAnnotations.add("@" + NamedDatabase.class.getSimpleName() + "(\"${runtimeDataSource}\")"); //增加默认的
        finalMapperAnnotations = finalMapperAnnotations
                .stream()
                .filter(StringUtils::isNotBlank)
                .map(expression -> CodegenUtils.parseAnnotationExpression(expression, domainConfigProperties))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        this.setMapperAnnotations(finalMapperAnnotations);
    }

    @Override
    public <T extends GeneratedTargetConfigProperties> String getGeneratedTargetName(DomainBoundedTargetConfigProperties<T> targetConfig, boolean includePackage, boolean includeSuffix) {
        return (includePackage ? getTargetPackage() + "." : "") + targetConfig.getDomainObjectConfig().getDomainObjectName() + "Mapper" + (includeSuffix ? ".java" : "");
    }

}
