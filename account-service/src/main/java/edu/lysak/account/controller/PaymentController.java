package edu.lysak.account.controller;

import edu.lysak.account.domain.User;
import edu.lysak.account.dto.PaymentRequest;
import edu.lysak.account.dto.PaymentResponse;
import edu.lysak.account.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/api/empl/payment")
    public ResponseEntity<?> getEmployeePayment(
        @AuthenticationPrincipal User user,
        @RequestParam(value = "period", required = false) String period
    ) {
        return ResponseEntity.ok(paymentService.getUserPayments(user, period));
    }

    @PostMapping("/api/acct/payments")
    public ResponseEntity<PaymentResponse> uploadPayments(
        @RequestBody List<@Valid PaymentRequest> payments
    ) {
        return ResponseEntity.ok(paymentService.uploadPayments(payments));
    }

    @PutMapping("/api/acct/payments")
    public ResponseEntity<PaymentResponse> updatePayment(
        @RequestBody @Valid PaymentRequest payment
    ) {
        return ResponseEntity.ok(paymentService.updatePayment(payment));
    }
}
