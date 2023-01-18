package edu.lysak.antifraud.controller;

import edu.lysak.antifraud.domain.card.DeletedCardResponse;
import edu.lysak.antifraud.domain.card.StolenCard;
import edu.lysak.antifraud.domain.card.StolenCardRequest;
import edu.lysak.antifraud.domain.ip.DeletedIpResponse;
import edu.lysak.antifraud.domain.ip.SuspiciousIp;
import edu.lysak.antifraud.domain.ip.SuspiciousIpRequest;
import edu.lysak.antifraud.domain.transaction.Transaction;
import edu.lysak.antifraud.domain.transaction.TransactionResponse;
import edu.lysak.antifraud.service.StolenCardService;
import edu.lysak.antifraud.service.SuspiciousIpService;
import edu.lysak.antifraud.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AntiFraudController {
    private final SuspiciousIpService suspiciousIpService;
    private final StolenCardService stolenCardService;
    private final TransactionService transactionService;

    public AntiFraudController(
            SuspiciousIpService suspiciousIpService,
            StolenCardService stolenCardService,
            TransactionService transactionService
    ) {
        this.suspiciousIpService = suspiciousIpService;
        this.stolenCardService = stolenCardService;
        this.transactionService = transactionService;
    }

    @PostMapping("/api/antifraud/transaction")
    public ResponseEntity<TransactionResponse> transaction(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.processTransaction(transaction));
    }

    @PostMapping("/api/antifraud/suspicious-ip")
    public ResponseEntity<SuspiciousIp> addSuspiciousIp(@RequestBody @Valid SuspiciousIpRequest suspiciousIp) {
        return suspiciousIpService.addSuspiciousIp(suspiciousIp)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping("/api/antifraud/suspicious-ip")
    public ResponseEntity<List<SuspiciousIp>> getSuspiciousIpList() {
        return ResponseEntity.ok(suspiciousIpService.getSuspiciousIpList());
    }

    @DeleteMapping("/api/antifraud/suspicious-ip/{ip}")
    public ResponseEntity<DeletedIpResponse> deleteSuspiciousIp(@PathVariable("ip") String ip) {
        return suspiciousIpService.deleteSuspiciousIp(ip)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/api/antifraud/stolencard")
    public ResponseEntity<StolenCard> addStolenCard(@RequestBody @Valid StolenCardRequest request) {
        return stolenCardService.addStolenCard(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping("/api/antifraud/stolencard")
    public ResponseEntity<List<StolenCard>> getStolenCards() {
        return ResponseEntity.ok(stolenCardService.getStolenCards());
    }

    @DeleteMapping("/api/antifraud/stolencard/{number}")
    public ResponseEntity<DeletedCardResponse> deleteStolenCard(@PathVariable("number") String cardNumber) {
        return stolenCardService.deleteStolenCard(cardNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
