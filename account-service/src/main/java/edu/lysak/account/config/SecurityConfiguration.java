package edu.lysak.account.config;

import edu.lysak.account.domain.Role;
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
    private static final String USER_ROLE = Role.USER.name();
    private static final String ACCOUNTANT_ROLE = Role.ACCOUNTANT.name();
    private static final String ADMINISTRATOR_ROLE = Role.ADMINISTRATOR.name();

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
        http
            .httpBasic(httpBasic -> {
                // Handle auth error
                httpBasic.authenticationEntryPoint(customAuthenticationEntryPoint);
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
                auth.requestMatchers(HttpMethod.POST, "/api/auth/signup")
                    .permitAll();

                auth.requestMatchers(HttpMethod.POST, "/api/auth/changepass")
                    .hasAnyAuthority(USER_ROLE, ACCOUNTANT_ROLE, ADMINISTRATOR_ROLE);
                auth.requestMatchers(HttpMethod.GET, "/api/empl/payment")
                    .hasAnyAuthority(USER_ROLE, ACCOUNTANT_ROLE);
                auth.requestMatchers(HttpMethod.POST, "/api/acct/payments")
                    .hasAuthority(ACCOUNTANT_ROLE);
                auth.requestMatchers(HttpMethod.PUT, "/api/acct/payments")
                    .hasAuthority(ACCOUNTANT_ROLE);

                auth.requestMatchers(HttpMethod.GET, "/api/admin/user/**")
                    .hasAuthority(ADMINISTRATOR_ROLE);
                auth.requestMatchers(HttpMethod.PUT, "/api/admin/user/role")
                    .hasAuthority(ADMINISTRATOR_ROLE);
                auth.requestMatchers(HttpMethod.DELETE, "/api/admin/user/**")
                    .hasAuthority(ADMINISTRATOR_ROLE);

                auth.anyRequest().authenticated();
            })
            .exceptionHandling(eh -> eh.accessDeniedHandler(customAccessDeniedHandler))
            .sessionManagement(sm -> {
                // no session
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            });

        return http.build();
    }
}
