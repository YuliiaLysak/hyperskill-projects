package edu.lysak.account.service;

import edu.lysak.account.domain.Role;
import edu.lysak.account.domain.User;
import edu.lysak.account.dto.UserRequest;
import edu.lysak.account.dto.UserResponse;
import edu.lysak.account.exception.UserAlreadyExistsException;
import edu.lysak.account.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username.toLowerCase())
            .orElseThrow(() -> new UsernameNotFoundException("User with provided email not found."));
    }

    public Optional<UserResponse> signupUser(UserRequest userRequest) {
        userRequest.setEmail(userRequest.getEmail().toLowerCase(Locale.ROOT));
        if (!isValidRequest(userRequest)) {
            log.warn("Invalid request");
            return Optional.empty();
        }

        Optional<User> userFromDb = userRepository.findByEmail(userRequest.getEmail());
        if (userFromDb.isPresent()) {
            log.warn("User with email [{}] already exists", userRequest.getEmail());
            throw new UserAlreadyExistsException();
        }

        User user = mapToUserEntity(userRequest);
        User savedUser = userRepository.save(user);
        return Optional.of(mapToUserResponse(savedUser));
    }

    public UserResponse findUser(User user) {
        return mapToUserResponse(user);
    }

    private User mapToUserEntity(UserRequest userRequest) {
        return User.builder()
            .name(userRequest.getName())
            .lastname(userRequest.getLastname())
            .email(userRequest.getEmail())
            .password(passwordEncoder.encode(userRequest.getPassword()))
            .role(Role.USER)
            .build();
    }

    private boolean isValidRequest(UserRequest userRequest) {
        return StringUtils.hasLength(userRequest.getName())
            && StringUtils.hasLength(userRequest.getLastname())
            && StringUtils.hasLength(userRequest.getPassword())
            && userRequest.getEmail().endsWith("@acme.com");
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .lastname(user.getLastname())
            .email(user.getEmail())
            .build();
    }
}
