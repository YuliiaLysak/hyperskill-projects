package edu.lysak.antifraud.service;

import edu.lysak.antifraud.domain.transaction.CardLimit;
import edu.lysak.antifraud.domain.transaction.Region;
import edu.lysak.antifraud.domain.transaction.RejectionReason;
import edu.lysak.antifraud.domain.transaction.Transaction;
import edu.lysak.antifraud.domain.transaction.TransactionFeedbackRequest;
import edu.lysak.antifraud.domain.transaction.TransactionFeedbackResponse;
import edu.lysak.antifraud.domain.transaction.TransactionRequest;
import edu.lysak.antifraud.domain.transaction.TransactionResponse;
import edu.lysak.antifraud.domain.transaction.TransactionStatus;
import edu.lysak.antifraud.exception.TransactionFeedbackIsAlreadyAssignedException;
import edu.lysak.antifraud.exception.UnprocessableTransactionFeedback;
import edu.lysak.antifraud.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionService {
    private static final int ALLOWED_COUNT = 2;

    private final StolenCardService stolenCardService;
    private final SuspiciousIpService suspiciousIpService;
    private final TransactionRepository transactionRepository;
    private final CardLimitService cardLimitService;

    public TransactionService(
            StolenCardService stolenCardService,
            SuspiciousIpService suspiciousIpService,
            TransactionRepository transactionRepository,
            CardLimitService cardLimitService
    ) {
        this.stolenCardService = stolenCardService;
        this.suspiciousIpService = suspiciousIpService;
        this.transactionRepository = transactionRepository;
        this.cardLimitService = cardLimitService;
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
        checkTransactionAmount(request.getNumber(), request.getAmount(), rejectionReasons, response);

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
            String cardNumber,
            long amount,
            Set<RejectionReason> rejectionReasons,
            TransactionResponse response
    ) {
        CardLimit cardLimit = cardLimitService.getCardLimit(cardNumber);
        log.info("Current maxAllowedAmount = {}", cardLimit.getMaxAllowedAmount());
        log.info("Current maxManualProcessingAmount = {}", cardLimit.getMaxManualProcessingAmount());

        if (amount <= cardLimit.getMaxAllowedAmount() && rejectionReasons.isEmpty()) {
            rejectionReasons.add(RejectionReason.NONE);
            response.setResult(TransactionStatus.ALLOWED);
        } else if (amount <= cardLimit.getMaxManualProcessingAmount() && rejectionReasons.isEmpty()) {
            rejectionReasons.add(RejectionReason.AMOUNT);
            response.setResult(TransactionStatus.MANUAL_PROCESSING);
        } else if (amount > cardLimit.getMaxManualProcessingAmount()) {
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

    public List<TransactionFeedbackResponse> getAllTransactionHistory() {
        List<TransactionFeedbackResponse> transactions = new ArrayList<>();
        transactionRepository.findAll()
                .forEach(t -> transactions.add(mapToTransactionResponse(t)));
        transactions.sort(Comparator.naturalOrder());
        return transactions;

    }

    public Optional<List<TransactionFeedbackResponse>> getTransactionHistoryForCardNumber(String cardNumber) {
        if (!stolenCardService.isValidCardNumber(cardNumber)) {
            throw new IllegalArgumentException();
        }

        if (!transactionRepository.existsByNumber(cardNumber)) {
            return Optional.empty();
        }

        List<TransactionFeedbackResponse> transactions = new ArrayList<>();
        transactionRepository.findAllByNumber(cardNumber)
                .forEach(t -> transactions.add(mapToTransactionResponse(t)));
        transactions.sort(Comparator.naturalOrder());
        return Optional.of(transactions);

    }

    private TransactionFeedbackResponse mapToTransactionResponse(Transaction t) {
        return TransactionFeedbackResponse.builder()
                .transactionId(t.getId())
                .amount(t.getAmount())
                .ip(t.getIp())
                .number(t.getNumber())
                .region(t.getRegion())
                .date(t.getDate())
                .result(t.getResult())
                .feedback(t.getFeedback() == null ? "" : t.getFeedback().name())
                .build();
    }

    public Optional<TransactionFeedbackResponse> addTransactionFeedback(TransactionFeedbackRequest request) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(request.getTransactionId());
        if (optionalTransaction.isEmpty()) {
            return Optional.empty();
        }

        Transaction transactionFromDb = optionalTransaction.get();
        if (transactionFromDb.getFeedback() != null) {
            throw new TransactionFeedbackIsAlreadyAssignedException();
        }

        updateAmountLimit(
                transactionFromDb.getNumber(),
                request.getFeedback(),
                transactionFromDb.getResult(),
                transactionFromDb.getAmount()
        );

        transactionRepository.updateFeedback(request.getTransactionId(), request.getFeedback());

        return Optional.of(TransactionFeedbackResponse.builder()
                .transactionId(request.getTransactionId())
                .amount(transactionFromDb.getAmount())
                .ip(transactionFromDb.getIp())
                .number(transactionFromDb.getNumber())
                .region(transactionFromDb.getRegion())
                .date(transactionFromDb.getDate())
                .result(transactionFromDb.getResult())
                .feedback(request.getFeedback() == null ? "" : request.getFeedback().name())
                .build());
    }

    private void updateAmountLimit(
            String cardNumber,
            TransactionStatus feedback,
            TransactionStatus result,
            long transactionAmount
    ) {
        switch (feedback) {
            case ALLOWED -> updateAmountForAllowedFeedback(cardNumber, result, transactionAmount);
            case MANUAL_PROCESSING -> updateAmountForManualProcessingFeedback(cardNumber, result, transactionAmount);
            case PROHIBITED -> updateAmountForProhibitedFeedback(cardNumber, result, transactionAmount);
        }
    }

    private void updateAmountForAllowedFeedback(
            String cardNumber,
            TransactionStatus result,
            long transactionAmount
    ) {
        switch (result) {
            case ALLOWED -> throw new UnprocessableTransactionFeedback();
            case MANUAL_PROCESSING -> increaseMaxAllowedAmount(cardNumber, transactionAmount);
            case PROHIBITED -> {
                increaseMaxAllowedAmount(cardNumber, transactionAmount);
                increaseMaxManualProcessingAmount(cardNumber, transactionAmount);
            }
        }
    }

    private void updateAmountForManualProcessingFeedback(
            String cardNumber,
            TransactionStatus result,
            long transactionAmount
    ) {
        switch (result) {
            case ALLOWED -> decreaseMaxAllowedAmount(cardNumber, transactionAmount);
            case MANUAL_PROCESSING -> throw new UnprocessableTransactionFeedback();
            case PROHIBITED -> increaseMaxManualProcessingAmount(cardNumber, transactionAmount);
        }
    }

    private void updateAmountForProhibitedFeedback(
            String cardNumber,
            TransactionStatus result,
            long transactionAmount
    ) {
        switch (result) {
            case ALLOWED -> {
                decreaseMaxAllowedAmount(cardNumber, transactionAmount);
                decreaseMaxManualProcessingAmount(cardNumber, transactionAmount);
            }
            case MANUAL_PROCESSING -> decreaseMaxManualProcessingAmount(cardNumber, transactionAmount);
            case PROHIBITED -> throw new UnprocessableTransactionFeedback();
        }
    }

    private void increaseMaxAllowedAmount(String cardNumber, long transactionAmount) {
        long maxAllowedAmount = cardLimitService.findMaxAllowedAmount(cardNumber);
        cardLimitService.updateMaxAllowedAmount(
                cardNumber,
                increaseAmountLimit(maxAllowedAmount, transactionAmount)
        );
    }

    private void increaseMaxManualProcessingAmount(String cardNumber, long transactionAmount) {
        long maxManualProcessingAmount = cardLimitService.findMaxManualProcessingAmount(cardNumber);
        cardLimitService.updateMaxManualProcessingAmount(
                cardNumber,
                increaseAmountLimit(maxManualProcessingAmount, transactionAmount)
        );
    }

    private void decreaseMaxAllowedAmount(String cardNumber, long transactionAmount) {
        long maxAllowedAmount = cardLimitService.findMaxAllowedAmount(cardNumber);
        cardLimitService.updateMaxAllowedAmount(
                cardNumber,
                decreaseAmountLimit(maxAllowedAmount, transactionAmount)
        );
    }

    private void decreaseMaxManualProcessingAmount(String cardNumber, long transactionAmount) {
        long maxManualProcessingAmount = cardLimitService.findMaxManualProcessingAmount(cardNumber);
        cardLimitService.updateMaxManualProcessingAmount(
                cardNumber,
                decreaseAmountLimit(maxManualProcessingAmount, transactionAmount)
        );
    }

    private long increaseAmountLimit(long currentLimit, long transactionAmount) {
        return (long) Math.ceil(0.8 * currentLimit + 0.2 * transactionAmount);
    }

    private long decreaseAmountLimit(long currentLimit, long transactionAmount) {
        return (long) Math.ceil(0.8 * currentLimit - 0.2 * transactionAmount);
    }
}
