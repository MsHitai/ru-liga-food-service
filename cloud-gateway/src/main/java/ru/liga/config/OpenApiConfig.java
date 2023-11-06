package ru.liga.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OpenApiConfig {

    @Bean
    public OpenAPI gateWayOpenApi() {
        return new OpenAPI().info(new Info().title("Liga Food Service APIs ")
                .description("Documentation for all the Microservices in Liga Food Service Application")
                .version("v1.0.0")
                .contact(new Contact()
                        .name("Liga Food Service Application Development Team")
                        .email("liga-food-service@test.com")));
    }
}
