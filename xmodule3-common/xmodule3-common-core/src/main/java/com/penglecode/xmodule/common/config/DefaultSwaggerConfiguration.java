package com.penglecode.xmodule.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 默认的swagger配置
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/4/13 23:01
 */
@Configuration
@EnableOpenApi
@EnableSwagger2
@EnableKnife4j
public class DefaultSwaggerConfiguration extends AbstractSpringConfiguration {

    private final SwaggerConfigProperties swaggerConfigProperties;

    public DefaultSwaggerConfiguration(SwaggerConfigProperties swaggerConfigProperties) {
        this.swaggerConfigProperties = swaggerConfigProperties;
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .enable(swaggerConfigProperties.isEnabled())
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerConfigProperties.getBasePackage())
                        .and(RequestHandlerSelectors.withMethodAnnotation(Operation.class)))
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description(String.format("%s接口测试文档", getEnvironment().getProperty("spring.application.name")))
                .contact(new Contact("Pengle", "https://github.com/penggle", "pengpeng.prox@gmail.com"))
                .version(getEnvironment().getProperty("spring.application.version", "1.0.0"))
                .title("API测试文档")
                .build();
    }

}
