package edu.lysak.account.controller;

import edu.lysak.account.domain.User;
import edu.lysak.account.dto.UserResponse;
import edu.lysak.account.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    private final UserService userService;

    public PaymentController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/empl/payment")
    public UserResponse getEmployeePayment(@AuthenticationPrincipal User user) {
        return userService.findUser(user);
    }

//    @PostMapping("/api/acct/payments")
//    public void uploadPayment() {
//
//    }
//
//    @PutMapping("/api/acct/payments")
//    public void updatePayment() {
//
//    }
}
