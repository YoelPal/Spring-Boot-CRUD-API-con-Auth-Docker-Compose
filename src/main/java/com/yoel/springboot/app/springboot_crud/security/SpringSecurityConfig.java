package com.yoel.springboot.app.springboot_crud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.yoel.springboot.app.springboot_crud.security.filter.JwtAuthenticationFilter;
import com.yoel.springboot.app.springboot_crud.security.filter.JwtValidationFilter;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SpringSecurityConfig {

    private static final String ROLE_ADMIN = "ADMIN";

    private AuthenticationConfiguration authenticationConfiguration;

    public SpringSecurityConfig(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }
    
    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authz -> authz
        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole(ROLE_ADMIN)
        .requestMatchers(HttpMethod.POST, "/api/users").hasRole(ROLE_ADMIN)
        .requestMatchers(HttpMethod.POST, "/api/products").hasRole(ROLE_ADMIN)
        .requestMatchers(HttpMethod.PUT, "/api/products/{id}").hasRole(ROLE_ADMIN)
        .requestMatchers(HttpMethod.DELETE, "/api/products/{id}").hasRole(ROLE_ADMIN)
        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole(ROLE_ADMIN)
        .anyRequest().authenticated())
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtValidationFilter(authenticationManager()))
        .csrf(config -> config.disable())
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
    }
}
