package edu.lysak.account.util;

import edu.lysak.account.exception.InsecurePasswordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SecurityUtil {
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

    public static void validatePassword(String password) {
        if (password.length() < 12) {
            log.warn("Invalid password length");
            throw new InsecurePasswordException("Password length must be 12 chars minimum!");
        } else if (BREACHED_PASSWORDS.contains(password)) {
            log.warn("Password is in the breached passwords");
            throw new InsecurePasswordException("The password is in the hacker's database!");
        }
    }
}
