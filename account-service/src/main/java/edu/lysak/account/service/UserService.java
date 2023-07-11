package edu.lysak.account.service;

import edu.lysak.account.domain.Role;
import edu.lysak.account.domain.SecurityEventType;
import edu.lysak.account.domain.User;
import edu.lysak.account.dto.ChangeRoleAccessRequest;
import edu.lysak.account.dto.PasswordResponse;
import edu.lysak.account.dto.UserRequest;
import edu.lysak.account.dto.UserResponse;
import edu.lysak.account.exception.InvalidRequestException;
import edu.lysak.account.exception.UserNotFoundException;
import edu.lysak.account.exception.UserRoleNotFoundException;
import edu.lysak.account.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final SecurityEventService securityEventService;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        ValidationService validationService,
        SecurityEventService securityEventService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validationService = validationService;
        this.securityEventService = securityEventService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username.toLowerCase(Locale.ROOT))
            .orElseThrow(() -> new UsernameNotFoundException("User with provided email not found."));
    }

    public User findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail.toLowerCase(Locale.ROOT))
            .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    public UserResponse signupUser(UserRequest userRequest, String path) {
        validationService.validatePassword(userRequest.getPassword());

        Optional<User> userFromDb = userRepository.findByEmail(
            userRequest.getEmail().toLowerCase(Locale.ROOT)
        );
        if (userFromDb.isPresent()) {
            log.warn("User with email [{}] already exists", userRequest.getEmail());
            throw new InvalidRequestException("User exist!");
        }

        User user = mapToUserEntity(userRequest);

        if (userRepository.count() == 0) {
            user.setRoles(Set.of(Role.ADMINISTRATOR));
        }

        User savedUser = userRepository.save(user);
        securityEventService.saveEvent(
            "Anonymous",
            savedUser.getEmail(),
            path,
            SecurityEventType.CREATE_USER
        );
        return mapToUserResponse(savedUser);
    }

    public PasswordResponse changePassword(User user, String newPassword, String path) {
        validatePassword(user, newPassword);
        userRepository.changeUserPassword(user.getUserId(), passwordEncoder.encode(newPassword));
        securityEventService.saveEvent(
            user.getEmail(),
            user.getEmail(),
            path,
            SecurityEventType.CHANGE_PASSWORD
        );
        return PasswordResponse.builder()
            .email(user.getEmail())
            .status("The password has been updated successfully")
            .build();
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
            .map(this::mapToUserResponse)
            .sorted(Comparator.comparing(UserResponse::getId))
            .toList();
    }

    public List<UserResponse> getAllUsersSorted() {
        Sort sort = Sort.by("userId").descending()
            .and(Sort.by(Sort.Direction.ASC, "name"));
        return userRepository.findAll(sort)
            .stream()
            .map(this::mapToUserResponse)
            .toList();
    }

    public List<UserResponse> getAllUsersPageable() {
        Pageable pageable = PageRequest.of(0, 1);
        Pageable pageableWithSort = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "userId"));
        return userRepository.findAll(pageable)
            .stream()
            .map(this::mapToUserResponse)
            .toList();
    }

    public UserResponse deleteUser(String userEmail, String currentUserEmail, String path) {
        User user = findByEmail(userEmail);
        validationService.validateForAdminRole(user.getRoles(), "Can't remove ADMINISTRATOR role!");

        userRepository.delete(user);
        securityEventService.saveEvent(
            currentUserEmail,
            user.getEmail(),
            path,
            SecurityEventType.DELETE_USER
        );
        return UserResponse.builder()
            .user(userEmail)
            .status("Deleted successfully!")
            .build();
    }

    public UserResponse changeUserRole(
        ChangeRoleAccessRequest request,
        String currentUserEmail,
        String path
    ) {
        User user = findByEmail(request.getUser());

        Role role = Role.fromName(request.getRole())
            .orElseThrow(() -> new UserRoleNotFoundException("Role not found!"));

        switch (request.getOperation()) {
            case GRANT -> addRole(user, role, currentUserEmail, path);
            case REMOVE -> removeRole(user, role, currentUserEmail, path);
        }
        return mapToUserResponse(user);
    }

    public UserResponse changeUserAccess(
        ChangeRoleAccessRequest request,
        String currentUserEmail,
        String path
    ) {
        User user = findByEmail(request.getUser());
        validationService.validateForAdminRole(user.getRoles(), "Can't lock the ADMINISTRATOR!");

        String statusMsg = "";
        String operationMsg = "";
        SecurityEventType eventType = null;
        switch (request.getOperation()) {
            case LOCK -> {
                lock(user);
                eventType = SecurityEventType.LOCK_USER;
                statusMsg = "locked";
                operationMsg = "Lock";
            }
            case UNLOCK -> {
                unlock(user);
                eventType = SecurityEventType.UNLOCK_USER;
                statusMsg = "unlocked";
                operationMsg = "Unlock";
            }
        }

        securityEventService.saveEvent(
            currentUserEmail,
            String.format("%s user %s", operationMsg, user.getEmail()),
            path,
            eventType
        );
        return UserResponse.builder()
            .status(String.format("User %s %s!", user.getEmail(), statusMsg))
            .build();
    }

    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
    }

    public void resetFailedAttempts(String email) {
        userRepository.updateFailedAttempts(0, email);
    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        userRepository.save(user);
    }

    public void unlock(User user) {
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
        userRepository.save(user);
    }

    private void addRole(User user, Role role, String currentUserEmail, String path) {
        validationService.validateForAdminAndBusinessRolesCombination(user, role);
        user.getRoles().add(role);
        userRepository.save(user);
        securityEventService.saveEvent(
            currentUserEmail,
            String.format("Grant role %s to %s", role, user.getEmail()),
            path,
            SecurityEventType.GRANT_ROLE
        );
    }

    private void removeRole(User user, Role role, String currentUserEmail, String path) {
        Set<Role> userRoles = user.getRoles();
        validationService.validateRoleForDeletion(user, role, userRoles);
        userRoles.remove(role);
        userRepository.save(user);
        securityEventService.saveEvent(
            currentUserEmail,
            String.format("Remove role %s from %s", role, user.getEmail()),
            path,
            SecurityEventType.REMOVE_ROLE
        );
    }

    private User mapToUserEntity(UserRequest userRequest) {
        return User.builder()
            .name(userRequest.getName())
            .lastname(userRequest.getLastname())
            .email(userRequest.getEmail().toLowerCase(Locale.ROOT))
            .password(passwordEncoder.encode(userRequest.getPassword()))
            .accountNonLocked(true)
            .roles(Set.of(Role.USER))
            .build();
    }

    private void validatePassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            log.warn("New password is the same as old one");
            throw new InvalidRequestException("The passwords must be different!");
        }
        validationService.validatePassword(newPassword);
    }

    private UserResponse mapToUserResponse(User user) {
        List<String> sortedRoles = user.getRoles().stream()
            .map(Role::getRoleName)
            .sorted()
            .toList();
        return UserResponse.builder()
            .id(user.getUserId())
            .name(user.getName())
            .lastname(user.getLastname())
            .email(user.getEmail())
            .roles(sortedRoles)
            .build();
    }
}
