package edu.lysak.account.config;

import edu.lysak.account.domain.Role;
import edu.lysak.account.domain.SecurityEventType;
import edu.lysak.account.service.SecurityEventService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final SecurityEventService securityEventService;

    public CustomAccessDeniedHandler(SecurityEventService securityEventService) {
        this.securityEventService = securityEventService;
    }

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        securityEventService.saveEvent(
            auth == null ? Role.ANONYMOUS.name() : auth.getName(),
            request.getRequestURI(),
            request.getRequestURI(),
            SecurityEventType.ACCESS_DENIED
        );
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied!");
    }
}
