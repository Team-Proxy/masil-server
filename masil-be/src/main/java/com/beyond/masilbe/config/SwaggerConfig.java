package com.beyond.masilbe.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Masil API")
                .description("Masil Backend API Documentation")
                .version("v0.0.1")
                .contact(new Contact().name("Masil Backend Team").url("https://github.com/Team-Proxy/masil-server"))
                .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT"));
    }
}
