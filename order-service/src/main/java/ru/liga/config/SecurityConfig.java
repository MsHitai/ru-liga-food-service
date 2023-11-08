package ru.liga.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.liga.service.impl.UserDetailsImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class
SecurityConfig {

    private final UserDetailsImpl userService;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfig(UserDetailsImpl userService, JwtRequestFilter jwtRequestFilter) {
        this.userService = userService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeHttpRequests((authz) -> {
                    try {
                        authz
                                .mvcMatchers("/actuator/health/**").permitAll()
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
                                .mvcMatchers(HttpMethod.POST, "/login").permitAll()
                                .anyRequest().authenticated()
                                .and()
                                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .and()
                                .exceptionHandling()
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                                .and().addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
