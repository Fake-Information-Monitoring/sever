package com.fake.information.sever.demo.Config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
    private val SWAGGER_BASE_PACKAGE = "com.fake.information.sever.demo.Http.Api.Controller"
    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder().title("API接口文档")
            .description("用户信息管理")
            .version("1.0.0")
            .build();
    }
    @Bean
    public fun createRestApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors
                .basePackage(SWAGGER_BASE_PACKAGE)) //这里写的是API接口所在的包位置
            .paths(PathSelectors.any())
            .build();
    }
}