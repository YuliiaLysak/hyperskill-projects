package edu.lysak.account.service;

import edu.lysak.account.domain.SecurityEvent;
import edu.lysak.account.domain.SecurityEventType;
import edu.lysak.account.dto.SecurityEventResponse;
import edu.lysak.account.repository.SecurityEventRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class SecurityEventService {
    private final SecurityEventRepository securityEventRepository;

    public SecurityEventService(SecurityEventRepository securityEventRepository) {
        this.securityEventRepository = securityEventRepository;
    }

    public void saveEvent(String subject, String object, String path, SecurityEventType action) {
        SecurityEvent event = SecurityEvent.builder()
            .date(Instant.now().toString())
            .action(action)
            .subject(subject)
            .object(object)
            .path(path)
            .build();
        securityEventRepository.save(event);
    }

    public List<SecurityEventResponse> getAllSecurityEvents() {
        return securityEventRepository.findAll(Sort.by("eventId").ascending())
            .stream()
            .map(this::mapToEventResponse)
            .toList();
    }

    private SecurityEventResponse mapToEventResponse(SecurityEvent securityEvent) {
        return SecurityEventResponse.builder()
            .id(securityEvent.getEventId())
            .date(securityEvent.getDate())
            .action(securityEvent.getAction())
            .subject(securityEvent.getSubject())
            .object(securityEvent.getObject())
            .path(securityEvent.getPath())
            .build();
    }
}
