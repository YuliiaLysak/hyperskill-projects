package edu.lysak.account.service;

import edu.lysak.account.domain.Role;
import edu.lysak.account.domain.User;
import edu.lysak.account.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ValidationService {
    private static final List<String> BREACHED_PASSWORDS = List.of(
        "PasswordForJanuary",
        "PasswordForFebruary",
        "PasswordForMarch",
        "PasswordForApril",
        "PasswordForMay",
        "PasswordForJune",
        "PasswordForJuly",
        "PasswordForAugust",
        "PasswordForSeptember",
        "PasswordForOctober",
        "PasswordForNovember",
        "PasswordForDecember"
    );

    public void validatePassword(String password) {
        if (password.length() < 12) {
            log.warn("Invalid password length");
            throw new InvalidRequestException("Password length must be 12 chars minimum!");
        } else if (BREACHED_PASSWORDS.contains(password)) {
            log.warn("Password is in the breached passwords");
            throw new InvalidRequestException("The password is in the hacker's database!");
        }
    }

    public void validateForAdminRole(String userEmail, Set<Role> userRoles) {
        if (userRoles.contains(Role.ADMINISTRATOR)) {
            log.warn("User with email [{}] has ADMINISTRATOR role and can't be removed", userEmail);
            throw new InvalidRequestException("Can't remove ADMINISTRATOR role!");
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
        return role == Role.ADMINISTRATOR
            && (user.getRoles().contains(Role.USER) || user.getRoles().contains(Role.ACCOUNTANT));
    }

    private boolean isBusinessForAdmin(User user, Role role) {
        return user.getRoles().contains(Role.ADMINISTRATOR)
            && (role == Role.USER || role == Role.ACCOUNTANT);
    }
}
