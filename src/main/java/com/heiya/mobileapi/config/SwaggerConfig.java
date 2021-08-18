/**
 * 
 */
package com.heiya.mobileapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Dian Krisnanjaya
 * Note: to open swagger, enter http://<hostname>:<port>/swagger-ui.html
 *
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig {

	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder().build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Heiya Customer App API")
                .description("This API is provided to enable Customer Mobile App to perform payment order transaction from their Smartphones")
                .license("Heiya License")
                .version("1.0.0")
                .build();
    }
    
}