package edu.lysak.antifraud.service;

import edu.lysak.antifraud.domain.card.DeletedCardResponse;
import edu.lysak.antifraud.domain.card.StolenCard;
import edu.lysak.antifraud.domain.card.StolenCardRequest;
import edu.lysak.antifraud.repository.StolenCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class StolenCardService {
    private final StolenCardRepository stolenCardRepository;

    public StolenCardService(StolenCardRepository stolenCardRepository) {
        this.stolenCardRepository = stolenCardRepository;
    }

    public Optional<StolenCard> addStolenCard(StolenCardRequest request) {
        if (!isValidCardNumber(request.getNumber())) {
            throw new IllegalArgumentException();
        }
        Optional<StolenCard> cardFromDb = stolenCardRepository.findByNumber(request.getNumber());
        if (cardFromDb.isPresent()) {
            return Optional.empty();
        }
        StolenCard stolenCard = new StolenCard();
        stolenCard.setNumber(request.getNumber());

        StolenCard savedStolenCard = stolenCardRepository.save(stolenCard);
        return Optional.of(savedStolenCard);
    }

    public boolean isValidCardNumber(String cardNumber) {
        return checkLuhnAlgorithm(cardNumber);
    }

    private boolean checkLuhnAlgorithm(String cardNumber) {
        String[] numbers = cardNumber.split("");
        int[] cardDigits = new int[16];
        for (int i = 0; i < numbers.length; i++) {
            int cardDigit = Integer.parseInt(numbers[i]);
            cardDigits[i] = cardDigit;
        }

        for (int i = 0; i < cardDigits.length - 1; i += 2) {
            cardDigits[i] *= 2;
            if (cardDigits[i] > 9) {
                cardDigits[i] -= 9;
            }
        }

        int sum = 0;
        for (int i = 0; i < cardDigits.length - 1; i++) {
            sum += cardDigits[i];
        }

        // checksum - 1 digit
        int checksum = 0;
        for (int i = 0; i < 10; i++) {
            if ((sum + i) % 10 == 0) {
                checksum = i;
            }
        }

        return cardDigits[15] == checksum;
    }

    public List<StolenCard> getStolenCards() {
        List<StolenCard> stolenCards = new ArrayList<>();
        stolenCardRepository.findAll().forEach(stolenCards::add);
        stolenCards.sort(Comparator.naturalOrder());
        return stolenCards;
    }

    @Transactional
    public Optional<DeletedCardResponse> deleteStolenCard(String cardNumber) {
        if (!isValidCardNumber(cardNumber)) {
            throw new IllegalArgumentException();
        }

        Optional<StolenCard> cardFromDb = stolenCardRepository.findByNumber(cardNumber);
        if (cardFromDb.isEmpty()) {
            return Optional.empty();
        }
        stolenCardRepository.delete(cardFromDb.get());
        DeletedCardResponse response = new DeletedCardResponse(String.format("Card %s successfully removed!", cardNumber));
        return Optional.of(response);
    }

    public boolean existsByNumber(String cardNumber) {
        return stolenCardRepository.existsByNumber(cardNumber);
    }
}
