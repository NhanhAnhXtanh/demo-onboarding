package com.company.onboarding.security;

import io.jmix.core.JmixSecurityFilterChainOrder;
import io.jmix.security.util.JmixHttpSecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class CustomEndpointSecurityConfig {


    @Bean
    @Order(JmixSecurityFilterChainOrder.CUSTOM)
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.securityMatcher("/api/**")
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers("/api/public/**").permitAll()
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .authenticationManager(authenticationManager);
        return http.build();
    }

    @Bean
    @Order(JmixSecurityFilterChainOrder.CUSTOM)
    SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/public/**")
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().permitAll())
                .csrf((csrf -> csrf.disable()));
        JmixHttpSecurityUtils.configureAnonymous(http);
        return http.build();
    }
}
