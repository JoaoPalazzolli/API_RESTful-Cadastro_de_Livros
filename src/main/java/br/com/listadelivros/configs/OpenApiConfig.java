package br.com.listadelivros.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    // http://localhost:8080/v3/api-docs
    // http://localhost:8080/swagger-ui/index.html
    
    @Bean
    OpenAPI customOpenApi(){
        return new OpenAPI().info(new Info()
                .title("Rest Full API with Java 17 and Spring Boot 3.0.6")
                .version("v1")
                .description("Some description about your API")
                .termsOfService("link")
                .license(new License()
                        .name("Apache 2.0")
                        .url("link")));
    }
}
