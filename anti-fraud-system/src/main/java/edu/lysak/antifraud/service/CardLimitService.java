package edu.lysak.antifraud.service;

import edu.lysak.antifraud.domain.transaction.CardLimit;
import edu.lysak.antifraud.repository.CardLimitRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardLimitService {
    private static final long MAX_ALLOWED_AMOUNT = 200L;
    private static final long MAX_MANUAL_PROCESSING_AMOUNT = 1500L;

    private final CardLimitRepository cardLimitRepository;

    public CardLimitService(CardLimitRepository cardLimitRepository) {
        this.cardLimitRepository = cardLimitRepository;
    }

    public CardLimit getCardLimit(String cardNumber) {
        Optional<CardLimit> cardLimitFromDb = cardLimitRepository.findByCardNumber(cardNumber);
        if (cardLimitFromDb.isPresent()) {
            return cardLimitFromDb.get();
        }

        CardLimit newCardLimit = new CardLimit();
        newCardLimit.setCardNumber(cardNumber);
        newCardLimit.setMaxAllowedAmount(MAX_ALLOWED_AMOUNT);
        newCardLimit.setMaxManualProcessingAmount(MAX_MANUAL_PROCESSING_AMOUNT);
        cardLimitRepository.save(newCardLimit);
        return newCardLimit;
    }

    public long findMaxAllowedAmount(String cardNumber) {
        return cardLimitRepository.findByCardNumber(cardNumber)
                .map(CardLimit::getMaxAllowedAmount)
                .orElse(MAX_ALLOWED_AMOUNT);
    }

    public long findMaxManualProcessingAmount(String cardNumber) {
        return cardLimitRepository.findByCardNumber(cardNumber)
                .map(CardLimit::getMaxManualProcessingAmount)
                .orElse(MAX_MANUAL_PROCESSING_AMOUNT);
    }

    public void updateMaxAllowedAmount(String cardNumber, long newAmount) {
        cardLimitRepository.updateMaxAllowedAmount(cardNumber, newAmount);
    }

    public void updateMaxManualProcessingAmount(String cardNumber, long newAmount) {
        cardLimitRepository.updateMaxManualProcessingAmount(cardNumber, newAmount);
    }
}
