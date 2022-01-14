package com.penglecode.xmodule.common.config;

import com.penglecode.xmodule.BasePackage;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * 默认的swagger配置(基于springdoc)
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/4/13 23:01
 */
@Configuration
public class DefaultSwaggerConfiguration extends AbstractSpringConfiguration {

    /**
     * 不分组的OpenAPI
     */
    /*@Bean
    public OpenAPI defaultNonGroupOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("测试API文档")
                        .description(String.format("%s接口测试文档", getEnvironment().getProperty("spring.application.name")))
                        .version(getEnvironment().getProperty("spring.application.version", "1.0.0"))
                        .contact(new Contact().name("Pengle").url("https://github.com/penggle").email("pengpeng.prox@gmail.com"))
                        .license(new License().name("Apache2.0").url("https://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("OpenAPI规范参考文档")
                        .url("https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md"));
    }*/

    /**
     * 使用分组的OpenAPI，将所有API放在一个默认大组中，解决knife4j-springdoc-ui:3.0.3报错的问题
     * PS: 如果不分组的话则申明{@link OpenAPI} bean
     */
    @Bean
    public GroupedOpenApi defaultGroupOpenApi() {
        return GroupedOpenApi.builder().group("default")
                .addOpenApiCustomiser(openApi -> openApi.info(info()).servers(servers()).setExternalDocs(externalDocs()))
                .packagesToScan(BasePackage.class.getPackage().getName())
                .pathsToMatch("/api/**")
                .build();
    }

    private Info info() {
        return new Info()
                .title("测试API文档")
                .description(String.format("%s接口测试文档", getEnvironment().getProperty("spring.application.name")))
                .version(getEnvironment().getProperty("spring.application.version", "1.0.0"))
                .contact(new Contact().name("Pengle").url("https://github.com/penggle").email("pengpeng.prox@gmail.com"))
                .license(new License().name("Apache2.0").url("https://springdoc.org"));
    }

    private ExternalDocumentation externalDocs() {
        return new ExternalDocumentation()
                .description("OpenAPI规范参考文档")
                .url("https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md");
    }

    private List<Server> servers() {
        return Collections.singletonList(new Server().url(String.format("http://127.0.0.1:%s", getEnvironment().getProperty("server.port", "8080"))).description("Embedded Tomcat9"));
    }

}
