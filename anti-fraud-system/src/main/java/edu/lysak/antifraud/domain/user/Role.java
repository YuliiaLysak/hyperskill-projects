package edu.lysak.antifraud.domain.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.Optional;

public enum Role implements GrantedAuthority {
    MERCHANT,
    ADMINISTRATOR,
    SUPPORT;

    @Override
    public String getAuthority() {
        return name();
    }

    public static Optional<Role> fromName(String name) {
        for (Role role : values()) {
            if (role.name().equals(name)) {
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }
}