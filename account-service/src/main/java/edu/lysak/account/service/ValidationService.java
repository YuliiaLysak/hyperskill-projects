package edu.lysak.account.service;

import edu.lysak.account.domain.Role;
import edu.lysak.account.domain.User;
import edu.lysak.account.exception.InvalidRequestException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ValidationService {
    private List<String> breachedPasswords = new ArrayList<>();
    private final String fileName;

    public ValidationService(
        @Value("${login.breached-passwords-file}") String fileName
    ) {
        this.fileName = fileName;
    }

    @PostConstruct
    public void init() throws URISyntaxException, IOException {
        URL dataFile = ClassLoader.getSystemResource(fileName);
        breachedPasswords = Files.readAllLines(Path.of(dataFile.toURI()));
    }

    public void validatePassword(String password) {
        if (password.length() < 12) {
            log.warn("Invalid password length");
            throw new InvalidRequestException("Password length must be 12 chars minimum!");
        } else if (breachedPasswords.contains(password)) {
            log.warn("Password is in the breached passwords");
            throw new InvalidRequestException("The password is in the hacker's database!");
        }
    }

    public void validateForAdminRole(Set<Role> userRoles, String errorMessage) {
        if (userRoles.contains(Role.ADMINISTRATOR)) {
            throw new InvalidRequestException(errorMessage);
        }
    }

    public void validateRoleForDeletion(User user, Role role, Set<Role> userRoles) {
        if (role == Role.ADMINISTRATOR) {
            log.warn("Can't remove [ADMINISTRATOR] role for user [{}]", user.getEmail());
            throw new InvalidRequestException("Can't remove ADMINISTRATOR role!");
        } else if (!userRoles.contains(role)) {
            log.warn("Role [{}] is not assigned to user [{}]", role, user.getEmail());
            throw new InvalidRequestException("The user does not have a role!");
        } else if (userRoles.size() == 1 && userRoles.contains(role)) {
            log.warn("Only existing role [{}] of a user [{}] can't be deleted", role, user.getEmail());
            throw new InvalidRequestException("The user must have at least one role!");
        }
    }

    public void validateForAdminAndBusinessRolesCombination(User user, Role role) {
        if (isAdminForBusiness(user, role) || isBusinessForAdmin(user, role)) {
            log.warn("The user cannot combine administrative and business roles!");
            throw new InvalidRequestException("The user cannot combine administrative and business roles!");
        }
    }

    private boolean isAdminForBusiness(User user, Role role) {
        boolean containsBusinessRole = user.getRoles().contains(Role.USER)
            || user.getRoles().contains(Role.ACCOUNTANT)
            || user.getRoles().contains(Role.AUDITOR);
        return role == Role.ADMINISTRATOR && containsBusinessRole;
    }

    private boolean isBusinessForAdmin(User user, Role role) {
        boolean isBusinessRole =
            role == Role.USER
                || role == Role.ACCOUNTANT
                || role == Role.AUDITOR;
        return user.getRoles().contains(Role.ADMINISTRATOR) && isBusinessRole;
    }
}
