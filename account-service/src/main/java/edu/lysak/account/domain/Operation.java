package edu.lysak.account.domain;

import java.util.Optional;

public enum Operation {
    GRANT,
    REMOVE;

    public static Optional<Operation> fromName(String name) {
        for (Operation role : values()) {
            if (role.name().equals(name)) {
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }
}
