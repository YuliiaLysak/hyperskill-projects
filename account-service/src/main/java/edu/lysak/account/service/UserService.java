package edu.lysak.account.service;

import edu.lysak.account.domain.Role;
import edu.lysak.account.domain.User;
import edu.lysak.account.dto.ChangeUserRoleRequest;
import edu.lysak.account.dto.PasswordResponse;
import edu.lysak.account.dto.UserRequest;
import edu.lysak.account.dto.UserResponse;
import edu.lysak.account.exception.InvalidRequestException;
import edu.lysak.account.exception.UserNotFoundException;
import edu.lysak.account.exception.UserRoleNotFoundException;
import edu.lysak.account.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        ValidationService validationService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validationService = validationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username.toLowerCase(Locale.ROOT))
            .orElseThrow(() -> new UsernameNotFoundException("User with provided email not found."));
    }

    public UserResponse signupUser(UserRequest userRequest) {
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
        return mapToUserResponse(savedUser);
    }

    public PasswordResponse changePassword(User user, String newPassword) {
        validatePassword(user, newPassword);
        userRepository.changeUserPassword(user.getUserId(), passwordEncoder.encode(newPassword));
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

    public UserResponse deleteUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail.toLowerCase(Locale.ROOT))
            .orElseThrow(() -> new UserNotFoundException("User not found!"));

        validationService.validateForAdminRole(userEmail, user.getRoles());

        userRepository.delete(user);
        return UserResponse.builder()
            .user(userEmail)
            .status("Deleted successfully!")
            .build();
    }

    public UserResponse changeUserRole(ChangeUserRoleRequest request) {
        User user = userRepository.findByEmail(request.getUser().toLowerCase(Locale.ROOT))
            .orElseThrow(() -> new UserNotFoundException("User not found!"));

        Role role = Role.fromName(request.getRole())
            .orElseThrow(() -> new UserRoleNotFoundException("Role not found!"));

        switch (request.getOperation()) {
            case GRANT -> addRole(user, role);
            case REMOVE -> deleteRole(user, role);
        }
        return mapToUserResponse(user);
    }

    private void addRole(User user, Role role) {
        validationService.validateForAdminAndBusinessRolesCombination(user, role);
        user.getRoles().add(role);
        userRepository.save(user);
    }

    private void deleteRole(User user, Role role) {
        Set<Role> userRoles = user.getRoles();
        validationService.validateRoleForDeletion(user, role, userRoles);
        userRoles.remove(role);
        userRepository.save(user);
    }

    private User mapToUserEntity(UserRequest userRequest) {
        return User.builder()
            .name(userRequest.getName())
            .lastname(userRequest.getLastname())
            .email(userRequest.getEmail().toLowerCase(Locale.ROOT))
            .password(passwordEncoder.encode(userRequest.getPassword()))
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
