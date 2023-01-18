package edu.lysak.antifraud.service;

import edu.lysak.antifraud.domain.transaction.Region;
import edu.lysak.antifraud.domain.transaction.RejectionReason;
import edu.lysak.antifraud.domain.transaction.Transaction;
import edu.lysak.antifraud.domain.transaction.TransactionRequest;
import edu.lysak.antifraud.domain.transaction.TransactionResponse;
import edu.lysak.antifraud.domain.transaction.TransactionStatus;
import edu.lysak.antifraud.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionService {
    private static final int ALLOWED_COUNT = 2;

    private final StolenCardService stolenCardService;
    private final SuspiciousIpService suspiciousIpService;
    private final TransactionRepository transactionRepository;

    public TransactionService(
            StolenCardService stolenCardService,
            SuspiciousIpService suspiciousIpService,
            TransactionRepository transactionRepository
    ) {
        this.stolenCardService = stolenCardService;
        this.suspiciousIpService = suspiciousIpService;
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse processTransaction(TransactionRequest request) {
        if (request.getAmount() <= 0
                || !suspiciousIpService.isValidIpFormat(request.getIp())
                || !stolenCardService.isValidCardNumber(request.getNumber())
        ) {
            throw new IllegalArgumentException();
        }
        TransactionResponse response = new TransactionResponse();
        Set<RejectionReason> rejectionReasons = new HashSet<>();

        checkForRegionCorrelation(request, response, rejectionReasons);
        checkForIpCorrelation(request, response, rejectionReasons);
        checkForSuspiciousIp(request.getIp(), rejectionReasons, response);
        checkForStolenCard(request.getNumber(), rejectionReasons, response);
        checkTransactionAmount(request.getAmount(), rejectionReasons, response);

        String info = rejectionReasons.stream()
                .map(RejectionReason::getReason)
                .sorted()
                .collect(Collectors.joining(", "));
        response.setInfo(info);

        Transaction transaction = mapToTransaction(request, response);
        transactionRepository.save(transaction);
        return response;
    }

    private void checkForRegionCorrelation(
            TransactionRequest request,
            TransactionResponse response,
            Set<RejectionReason> rejectionReasons
    ) {
        Set<Region> transactionRegions = transactionRepository.getAllTransactionRegionsByCardNumber(
                request.getNumber(),
                request.getDate().minusHours(1),
                request.getDate()
        );
        transactionRegions.remove(request.getRegion());
        log.info("transactionRegions: {}", transactionRegions);

        if (transactionRegions.size() == ALLOWED_COUNT) {
            response.setResult(TransactionStatus.MANUAL_PROCESSING);
            rejectionReasons.add(RejectionReason.REGION_CORRELATION);
        } else if (transactionRegions.size() > ALLOWED_COUNT) {
            response.setResult(TransactionStatus.PROHIBITED);
            rejectionReasons.add(RejectionReason.REGION_CORRELATION);
        }
    }

    private void checkForIpCorrelation(
            TransactionRequest request,
            TransactionResponse response,
            Set<RejectionReason> rejectionReasons
    ) {
        Set<String> transactionIPs = transactionRepository.getAllTransactionIPsByCardNumber(
                request.getNumber(),
                request.getDate().minusHours(1),
                request.getDate()
        );
        transactionIPs.remove(request.getIp());
        log.info("transactionIPs: {}", transactionIPs);

        if (transactionIPs.size() == ALLOWED_COUNT) {
            response.setResult(TransactionStatus.MANUAL_PROCESSING);
            rejectionReasons.add(RejectionReason.IP_CORRELATION);
        } else if (transactionIPs.size() > ALLOWED_COUNT) {
            response.setResult(TransactionStatus.PROHIBITED);
            rejectionReasons.add(RejectionReason.IP_CORRELATION);
        }
    }

    private void checkForSuspiciousIp(
            String ip,
            Set<RejectionReason> rejectionReasons,
            TransactionResponse response
    ) {
        if (suspiciousIpService.existsByIp(ip)) {
            rejectionReasons.add(RejectionReason.IP);
            response.setResult(TransactionStatus.PROHIBITED);
        }
    }

    private void checkForStolenCard(
            String cardNumber,
            Set<RejectionReason> rejectionReasons,
            TransactionResponse response
    ) {
        if (stolenCardService.existsByNumber(cardNumber)) {
            rejectionReasons.add(RejectionReason.CARD_NUMBER);
            response.setResult(TransactionStatus.PROHIBITED);
        }
    }

    private void checkTransactionAmount(
            long amount,
            Set<RejectionReason> rejectionReasons,
            TransactionResponse response
    ) {
        if (amount <= 200 && rejectionReasons.isEmpty()) {
            rejectionReasons.add(RejectionReason.NONE);
            response.setResult(TransactionStatus.ALLOWED);
        } else if (amount <= 1500 && rejectionReasons.isEmpty()) {
            rejectionReasons.add(RejectionReason.AMOUNT);
            response.setResult(TransactionStatus.MANUAL_PROCESSING);
        } else if (amount > 1500) {
            rejectionReasons.add(RejectionReason.AMOUNT);
            response.setResult(TransactionStatus.PROHIBITED);
        }
    }

    private Transaction mapToTransaction(TransactionRequest request, TransactionResponse response) {
        return Transaction.builder()
                .amount(request.getAmount())
                .ip(request.getIp())
                .number(request.getNumber())
                .region(request.getRegion())
                .date(request.getDate())
                .result(response.getResult())
                .info(response.getInfo())
                .build();
    }
}
