package edu.lysak.antifraud.service;

import edu.lysak.antifraud.domain.transaction.RejectionReason;
import edu.lysak.antifraud.domain.transaction.Transaction;
import edu.lysak.antifraud.domain.transaction.TransactionResponse;
import edu.lysak.antifraud.domain.transaction.TransactionStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final StolenCardService stolenCardService;
    private final SuspiciousIpService suspiciousIpService;

    public TransactionService(
            StolenCardService stolenCardService,
            SuspiciousIpService suspiciousIpService
    ) {
        this.stolenCardService = stolenCardService;
        this.suspiciousIpService = suspiciousIpService;
    }

    public TransactionResponse processTransaction(Transaction transaction) {
        if (transaction.getAmount() <= 0
                || !suspiciousIpService.isValidIpFormat(transaction.getIp())
                || !stolenCardService.isValidCardNumber(transaction.getNumber())
        ) {
            throw new IllegalArgumentException();
        }

        TransactionResponse response = new TransactionResponse();
        List<String> rejectionReasons = new ArrayList<>();
        checkIpAndCardNumber(transaction.getIp(), transaction.getNumber(), rejectionReasons, response);
        checkTransactionAmount(transaction.getAmount(), rejectionReasons, response);

        String info = rejectionReasons.stream()
                .sorted()
                .collect(Collectors.joining(", "));
        response.setInfo(info);
        return response;
    }

    private void checkIpAndCardNumber(String ip, String cardNumber, List<String> rejectionReasons, TransactionResponse response) {
        boolean isSuspiciousIp = suspiciousIpService.existsByIp(ip);
        boolean isStolenCard = stolenCardService.existsByNumber(cardNumber);
        if (isSuspiciousIp) {
            rejectionReasons.add(RejectionReason.IP.getReason());
            response.setResult(TransactionStatus.PROHIBITED);
        }
        if (isStolenCard) {
            rejectionReasons.add(RejectionReason.CARD_NUMBER.getReason());
            response.setResult(TransactionStatus.PROHIBITED);
        }
    }

    private void checkTransactionAmount(long amount, List<String> rejectionReasons, TransactionResponse response) {
        if (amount <= 200 && rejectionReasons.isEmpty()) {
            rejectionReasons.add(RejectionReason.NONE.getReason());
            response.setResult(TransactionStatus.ALLOWED);
        } else if (amount <= 1500 && rejectionReasons.isEmpty()) {
            rejectionReasons.add(RejectionReason.AMOUNT.getReason());
            response.setResult(TransactionStatus.MANUAL_PROCESSING);
        } else if (amount > 1500) {
            rejectionReasons.add(RejectionReason.AMOUNT.getReason());
            response.setResult(TransactionStatus.PROHIBITED);
        }
    }
}
