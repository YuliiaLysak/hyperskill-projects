package edu.lysak.account.config;

import edu.lysak.account.domain.Role;
import edu.lysak.account.domain.SecurityEventType;
import edu.lysak.account.domain.User;
import edu.lysak.account.exception.UserNotFoundException;
import edu.lysak.account.service.SecurityEventService;
import edu.lysak.account.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final UserService userService;
    private final SecurityEventService securityEventService;
    private final int maxFailedAttempts;

    public CustomAuthenticationEntryPoint(
        UserService userService,
        SecurityEventService securityEventService,
        @Value("${login.max-failed-attempts}") int maxFailedAttempts
    ) {
        this.userService = userService;
        this.securityEventService = securityEventService;
        this.maxFailedAttempts = maxFailedAttempts;
    }

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {
        String email = getUsernameFromAuthHeader(request.getHeader(HttpHeaders.AUTHORIZATION));
        String requestURI = request.getRequestURI();
        String errorMessage = authException.getMessage();

        User user;
        boolean accountNonLocked = true;
        try {
            user = userService.findByEmail(email);
            accountNonLocked = user.isAccountNonLocked();
        } catch (UserNotFoundException exception) {
            user = null;
        }

        saveLoginFailedEvent(requestURI, email, accountNonLocked);

        // TODO: To pass test #95 stage 6/7 admin shouldn't be locked
        if (user == null || !accountNonLocked || user.getRoles().contains(Role.ADMINISTRATOR)) {
            sendError(response, errorMessage);
            return;
        }

        detectBruteForce(requestURI, email, user);
        sendError(response, errorMessage);
    }

    private String getUsernameFromAuthHeader(String authorizationHeader) {
        if (authorizationHeader == null) {
            return Role.ANONYMOUS.name();
        }
        String pair = new String(
            Base64.decodeBase64(authorizationHeader.substring("Basic ".length()))
        );
        return pair.split(":")[0];
    }

    private void saveLoginFailedEvent(String requestUri, String email, boolean accountNonLocked) {
        if (!Role.ANONYMOUS.name().equals(email) && accountNonLocked) {
            securityEventService.saveEvent(
                email,
                requestUri,
                requestUri,
                SecurityEventType.LOGIN_FAILED
            );
        }
    }

    private void detectBruteForce(String requestUri, String email, User user) {
        int failedAttempt = user.getFailedAttempt();
        if (failedAttempt < maxFailedAttempts - 1) {
            userService.increaseFailedAttempts(user);
        } else if (failedAttempt == maxFailedAttempts - 1) {
            userService.increaseFailedAttempts(user);
            securityEventService.saveEvent(
                email,
                requestUri,
                requestUri,
                SecurityEventType.BRUTE_FORCE
            );
            securityEventService.saveEvent(
                email,
                String.format("Lock user %s", email),
                requestUri,
                SecurityEventType.LOCK_USER
            );
            userService.lock(user);
        }
    }

    private void sendError(HttpServletResponse response, String errorMessage) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
    }
}
