package edu.lysak.account.controller;

import edu.lysak.account.dto.SecurityEventResponse;
import edu.lysak.account.service.SecurityEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuditController {
    private final SecurityEventService securityEventService;

    public AuditController(SecurityEventService securityEventService) {
        this.securityEventService = securityEventService;
    }

    @GetMapping("/api/security/events/")
    public ResponseEntity<List<SecurityEventResponse>> getSecurityEvents() {
        return ResponseEntity.ok(securityEventService.getAllSecurityEvents());
    }
}
