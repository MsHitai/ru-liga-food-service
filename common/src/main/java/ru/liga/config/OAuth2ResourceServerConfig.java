package ru.liga.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class OAuth2ResourceServerConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().mvcMatchers("/actuator/health/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/webjars/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                .mvcMatchers(HttpMethod.GET, "/orders/swagger-ui.html").permitAll()
                .mvcMatchers(HttpMethod.GET, "/kitchen/swagger-ui.html").permitAll()
                .mvcMatchers(HttpMethod.GET, "/deliveries/swagger-ui.html").permitAll()
                .mvcMatchers(HttpMethod.GET, "/swagger-resources/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/orders/v3/api-docs/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/kitchen/v3/api-docs/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/deliveries/v3/api-docs/**").permitAll()
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
}
