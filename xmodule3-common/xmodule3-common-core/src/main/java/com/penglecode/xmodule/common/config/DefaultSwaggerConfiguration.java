package com.penglecode.xmodule.common.config;

import com.penglecode.xmodule.BasePackage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.providers.SpringWebProvider;
import org.springdoc.webmvc.core.SpringWebMvcProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 默认的swagger配置(基于springdoc + knife4j)
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/4/13 23:01
 */
@Configuration
@ConditionalOnClass({OpenAPI.class, GroupedOpenApi.class})
@ConditionalOnProperty(name=DefaultSwaggerConfiguration.CONFIGURATION_ENABLED, havingValue="true", matchIfMissing=true)
public class DefaultSwaggerConfiguration extends AbstractSpringConfiguration {

    public static final String CONFIGURATION_ENABLED = "springdoc.customized.enabled";

    @Bean
    public OpenAPI defaultNonGroupOpenApi(Info apiInfo, ExternalDocumentation apiExternalDocs, List<Server> apiServers) {
        return new OpenAPI()
                .info(apiInfo)
                .externalDocs(apiExternalDocs)
                .servers(apiServers);
    }

    /**
     * 分组的OpenAPI，将所有API放在一个默认大组中，解决knife4j-springdoc-ui:3.0.3前端JS报错的问题(bug)
     */
    @Bean
    @ConditionalOnMissingBean
    public GroupedOpenApi defaultGroupOpenApi() {
        return GroupedOpenApi.builder().group("default")
                //.addOperationCustomizer(new DefaultOperationCustomizer())
                .packagesToScan(BasePackage.class.getPackage().getName())
                .pathsToMatch("/api/**")
                .build();
    }

    /**
     * 解决需求：API文档中只显示具有@Operation注解的接口
     */
    @Bean
    @ConditionalOnProperty(name="springdoc.skip-unannotated-apis", matchIfMissing=true)
    public SpringWebProvider springWebProvider(){
        return new CustomSpringWebProvider();
    }

    /**
     * 各个应用可以予以覆盖
     */
    @Bean
    @ConditionalOnMissingBean
    public Info apiInfo() {
        return new Info()
                .title("API接口文档")
                .description(String.format("%s接口文档", getEnvironment().getProperty("spring.application.name")))
                .version(getEnvironment().getProperty("spring.application.version", "1.0.0"))
                .contact(new Contact().name("Pengle").url("https://github.com/penggle").email("pengpeng.prox@gmail.com"))
                .license(new License().name("Apache2.0").url("https://springdoc.org"));
    }

    /**
     * 各个应用可以予以覆盖
     */
    @Bean
    @ConditionalOnMissingBean
    public ExternalDocumentation apiExternalDocs() {
        return new ExternalDocumentation()
                .description("OpenAPI规范参考文档")
                .url("https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md");
    }

    /**
     * 各个应用可以予以覆盖
     */
    @Bean
    @ConditionalOnMissingBean
    public List<Server> apiServers() {
        Environment environment = getEnvironment();
        String serverUrl = String.format("http://127.0.0.1:%s%s", environment.getProperty("server.port", "8080"), environment.getProperty("server.servlet.context-path", ""));
        return Collections.singletonList(new Server().description(String.format("Server[%s]", environment.getProperty("spring.application.name"))).url(serverUrl));
    }

    /**
     * 解决需求：API文档中只显示具有@Operation注解的接口
     */
    public static class CustomSpringWebProvider extends SpringWebMvcProvider {

        @Override
        @SuppressWarnings("unchecked")
        public Map<RequestMappingInfo, HandlerMethod> getHandlerMethods() {
            Map<RequestMappingInfo, HandlerMethod> allHandlerMethods = super.getHandlerMethods();
            for(Iterator<Map.Entry<RequestMappingInfo, HandlerMethod>> iterator = allHandlerMethods.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry<RequestMappingInfo, HandlerMethod> entry = iterator.next();
                //考虑到递归查找,使用findAnnotation()而不是getAnnotation()
                Operation apiOperation = AnnotationUtils.findAnnotation(entry.getValue().getMethod(), Operation.class);
                if(apiOperation == null) {
                    iterator.remove();
                }
            }
            return allHandlerMethods;
        }

    }

}
