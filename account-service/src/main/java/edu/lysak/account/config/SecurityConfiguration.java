package edu.lysak.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityConfiguration(RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(httpBasic -> {
                // Handle auth error
                httpBasic.authenticationEntryPoint(restAuthenticationEntryPoint);
            })
            // for Postman, the H2 console
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers -> {
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
            })
            // manage access to endpoints
            .authorizeHttpRequests(auth -> {
                // permit "/error/**" to return 400 instead of 401 for open endpoints
                // (endpoints redirecting to the /error/** in case of error and /error/ is secured by spring security)
                auth.requestMatchers("/actuator/shutdown", "/error/**").permitAll();
                auth.requestMatchers(toH2Console()).permitAll();
                auth.requestMatchers(HttpMethod.POST, "/api/acct/payments", "/api/auth/signup").permitAll();
                auth.requestMatchers(HttpMethod.PUT, "/api/acct/payments").permitAll();
                auth.anyRequest().authenticated();
            })
            .sessionManagement(sm -> {
                // no session
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            });

        return http.build();
    }
}
