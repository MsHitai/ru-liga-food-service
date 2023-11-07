package ru.liga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class CloudGatewayApp {

    public static void main(String[] args) {
        SpringApplication.run(CloudGatewayApp.class, args);
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        final CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:9000", "http://localhost:8080", "http://127.0.0.1:8080",
                "http://127.0.0.1:8082", "http://localhost:8082", "http://localhost:8081", "http://localhost:8083",
                "http://127.0.0.1:8081", "http://127.0.0.1:8083", "http://127.0.0.1:8084", "http://localhost:8084"));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "HEAD", "PUT"));
        corsConfig.addAllowedHeader("Access-Control-Allow-Origin");

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
