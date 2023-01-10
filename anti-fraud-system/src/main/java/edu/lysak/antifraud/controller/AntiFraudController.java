package edu.lysak.antifraud.controller;

import edu.lysak.antifraud.domain.Transaction;
import edu.lysak.antifraud.domain.TransactionResponse;
import edu.lysak.antifraud.domain.TransactionStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AntiFraudController {

    @PostMapping("/api/antifraud/transaction")
    public ResponseEntity<TransactionResponse> transaction(@RequestBody Transaction transaction) {
        long amount = transaction.getAmount();
        if (amount <= 0) {
            return ResponseEntity.badRequest().build();
        }

        TransactionResponse response = new TransactionResponse();
        if (amount <= 200) {
            response.setResult(TransactionStatus.ALLOWED);
        } else if (amount <= 1500) {
            response.setResult(TransactionStatus.MANUAL_PROCESSING);
        } else {
            response.setResult(TransactionStatus.PROHIBITED);
        }

        return ResponseEntity.ok(response);
    }
}
