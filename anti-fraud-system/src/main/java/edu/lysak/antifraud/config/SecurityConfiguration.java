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
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfiguration(
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            CustomAccessDeniedHandler customAccessDeniedHandler
    ) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(customAuthenticationEntryPoint) // Handles authentication error
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
                .requestMatchers(HttpMethod.POST, "/api/antifraud/suspicious-ip/**", "/api/antifraud/stolencard/**").hasAuthority("SUPPORT")
                .requestMatchers(HttpMethod.DELETE, "/api/antifraud/suspicious-ip/**", "/api/antifraud/stolencard/**").hasAuthority("SUPPORT")
                .requestMatchers(HttpMethod.GET, "/api/antifraud/suspicious-ip/**", "/api/antifraud/stolencard/**").hasAuthority("SUPPORT")
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler) // Handles authorization error
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
        return http.build();
    }
}
