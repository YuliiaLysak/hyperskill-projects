package edu.lysak.antifraud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityConfiguration(RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
                // TODO: 17.01.2023 review how to use this authenticationEntryPoint
//                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handles auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeHttpRequests()// manage access
                .requestMatchers("/actuator/shutdown").permitAll() // needs to run test
                .requestMatchers(HttpMethod.POST, "/api/auth/user/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/auth/user/**").hasAuthority("ADMINISTRATOR")
                .requestMatchers(HttpMethod.GET, "/api/auth/list/**").hasAnyAuthority("ADMINISTRATOR", "SUPPORT")
                .requestMatchers(HttpMethod.POST, "/api/antifraud/transaction/**").hasAuthority("MERCHANT")
                .requestMatchers(HttpMethod.PUT, "/api/auth/access/**").hasAuthority("ADMINISTRATOR")
                .requestMatchers(HttpMethod.PUT, "/api/auth/role/**").hasAuthority("ADMINISTRATOR")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
        return http.build();
    }
}
