package edu.lysak.account.service;

import edu.lysak.account.dto.UserRequest;
import edu.lysak.account.dto.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserService {

    public Optional<UserResponse> signupUser(UserRequest userRequest) {
        if (!isValidRequest(userRequest)) {
            return Optional.empty();
        }
        return Optional.of(mapToUserResponse(userRequest));
    }

    private boolean isValidRequest(UserRequest userRequest) {
        return StringUtils.hasLength(userRequest.getName())
            && StringUtils.hasLength(userRequest.getLastname())
            && StringUtils.hasLength(userRequest.getPassword())
            && userRequest.getEmail().endsWith("@acme.com");
    }

    private UserResponse mapToUserResponse(UserRequest userRequest) {
        return UserResponse.builder()
            .name(userRequest.getName())
            .lastname(userRequest.getLastname())
            .email(userRequest.getEmail())
            .build();
    }
}
