package edu.lysak.antifraud.service;

import edu.lysak.antifraud.domain.user.AccessOperation;
import edu.lysak.antifraud.domain.user.ChangeAccessRequest;
import edu.lysak.antifraud.domain.user.ChangeAccessResponse;
import edu.lysak.antifraud.domain.user.ChangeRoleRequest;
import edu.lysak.antifraud.domain.user.User;
import edu.lysak.antifraud.domain.user.Role;
import edu.lysak.antifraud.domain.user.UserRequest;
import edu.lysak.antifraud.domain.user.UserResponse;
import edu.lysak.antifraud.exception.AdministratorCannotBeBlockedException;
import edu.lysak.antifraud.exception.UserRoleIsAlreadyAssigned;
import edu.lysak.antifraud.exception.UserRoleIsNotSupportedException;
import edu.lysak.antifraud.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with provided username not found."));
    }

    public Optional<UserResponse> addNewUser(UserRequest userRequest) {
        Optional<User> userFromDb = userRepository.findByUsername(userRequest.getUsername());
        if (userFromDb.isPresent()) {
            return Optional.empty();
        }
        User user = new User();
        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        if (userRepository.count() == 0) {
            user.setRole(Role.ADMINISTRATOR);
            user.setAccountNonLocked(true);
        } else {
            user.setRole(Role.MERCHANT);
        }

        User savedUser = userRepository.save(user);
        return Optional.of(mapToUserResponse(savedUser));
    }

    public List<UserResponse> getAllUsersAscending() {
        List<UserResponse> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(mapToUserResponse(user)));
        users.sort(Comparator.naturalOrder());
        return users;
    }

    @Transactional
    public boolean deleteUser(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            return false;
        }
        userRepository.delete(byUsername.get());
        return true;
    }

    public Optional<UserResponse> changeUserRole(ChangeRoleRequest request) {
        Optional<Role> newRole = Role.fromName(request.getRole());
        if (newRole.isEmpty() || Role.ADMINISTRATOR.equals(newRole.get())) {
            throw new UserRoleIsNotSupportedException();
        }

        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User userFromDb = optionalUser.get();
        if (userFromDb.getRole().equals(newRole.get())) {
            throw new UserRoleIsAlreadyAssigned();
        }

        userFromDb.setRole(newRole.get());
        User updatedUser = userRepository.save(userFromDb);
        return Optional.of(mapToUserResponse(updatedUser));
    }

    public Optional<ChangeAccessResponse> changeAccess(ChangeAccessRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();
        if (user.getRole().equals(Role.ADMINISTRATOR)) {
            throw new AdministratorCannotBeBlockedException();
        }

        ChangeAccessResponse response = new ChangeAccessResponse();
        if (AccessOperation.LOCK.name().equals(request.getOperation())) {
            user.setAccountNonLocked(false);
            response.setStatus(String.format("User %s locked!", user.getUsername()));
        } else if (AccessOperation.UNLOCK.name().equals(request.getOperation())) {
            user.setAccountNonLocked(true);
            response.setStatus(String.format("User %s unlocked!", user.getUsername()));
        }

        userRepository.save(user);
        return Optional.of(response);
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getRole()
        );
    }
}
