package edu.lysak.antifraud.repository;

import edu.lysak.antifraud.domain.transaction.CardLimit;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CardLimitRepository extends CrudRepository<CardLimit, Long> {

    Optional<CardLimit> findByCardNumber(String cardNumber);

    @Transactional
    @Modifying
    @Query("UPDATE CardLimit cardLimit" +
            " SET cardLimit.maxAllowedAmount = :newAmount" +
            " WHERE cardLimit.cardNumber = :cardNumber")
    void updateMaxAllowedAmount(
            @Param("cardNumber") String cardNumber,
            @Param("newAmount") long newAmount
    );

    @Transactional
    @Modifying
    @Query("UPDATE CardLimit cardLimit" +
            " SET cardLimit.maxManualProcessingAmount = :newAmount" +
            " WHERE cardLimit.cardNumber = :cardNumber")
    void updateMaxManualProcessingAmount(
            @Param("cardNumber") String cardNumber,
            @Param("newAmount") long newAmount
    );
}
