package edu.lysak.account.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Optional;

@Getter
public enum Role implements GrantedAuthority {
    ANONYMOUS("ROLE_ANONYMOUS"),
    USER("ROLE_USER"),
    ACCOUNTANT("ROLE_ACCOUNTANT"),
    ADMINISTRATOR("ROLE_ADMINISTRATOR"),
    AUDITOR("ROLE_AUDITOR");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

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
