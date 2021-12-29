package org.springframework.boot.autoconfigure.dal;

import com.penglecode.xmodule.BasePackage;
import com.penglecode.xmodule.common.consts.ApplicationConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * DalMybatisComponentsBuilder抽象基类
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/7/16 14:11
 */
public abstract class AbstractDalMybatisComponentsBuilder implements DalMybatisComponentsBuilder, EnvironmentAware, ResourceLoaderAware, BeanFactoryAware {

    public static final String DEFAULT_BASE_PACKAGE = BasePackage.class.getPackage().getName();

    public static final String DEFAULT_CONFIG_LOCATION = "classpath:config/mybatis/default-mybatis-config.xml";

    public static final String DEFAULT_MAPPER_LOCATIONS = String.format("classpath*:%s/**/*Mapper.xml", DEFAULT_BASE_PACKAGE.replace(".", "/"));

    protected Environment environment;

    protected BeanFactory beanFactory;

    protected ResourceLoader resourceLoader;

    protected Resource resolveConfigLocation(String configLocation) {
        configLocation = StringUtils.defaultIfBlank(configLocation, DEFAULT_CONFIG_LOCATION);
        Resource resource = getDefaultResourcePatternResolver().getResource(configLocation);
        return resource.exists() ? resource : null;
    }

    protected Resource[] resolveMapperLocations(String[] mapperLocations) {
        mapperLocations = ArrayUtils.isNotEmpty(mapperLocations) ? mapperLocations : new String[]{ DEFAULT_MAPPER_LOCATIONS };
        return Stream.of(mapperLocations).flatMap(mapperLocation -> Stream.of(getResources(mapperLocation))).toArray(Resource[]::new);
    }

    protected Resource[] getResources(String mapperLocation) {
        try {
            return getDefaultResourcePatternResolver().getResources(mapperLocation);
        } catch (IOException e) {
            return new Resource[0];
        }
    }

    protected ResourcePatternResolver getDefaultResourcePatternResolver() {
        return ApplicationConstants.DEFAULT_RESOURCE_PATTERN_RESOLVER.get();
    }

    @Override
    public final void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public final void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public final void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
