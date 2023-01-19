package edu.lysak.antifraud.repository;

import edu.lysak.antifraud.domain.transaction.Region;
import edu.lysak.antifraud.domain.transaction.Transaction;
import edu.lysak.antifraud.domain.transaction.TransactionStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Query("SELECT DISTINCT transaction.region FROM Transaction transaction" +
            " WHERE transaction.number = :cardNumber" +
            " AND transaction.date >= :fromDate" +
            " AND transaction.date <= :toDate")
    Set<Region> getAllTransactionRegionsByCardNumber(
            @Param("cardNumber") String number,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    @Query("SELECT DISTINCT transaction.ip FROM Transaction transaction" +
            " WHERE transaction.number = :cardNumber" +
            " AND transaction.date >= :fromDate" +
            " AND transaction.date <= :toDate")
    Set<String> getAllTransactionIPsByCardNumber(
            @Param("cardNumber") String number,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    @Transactional
    @Modifying
    @Query("UPDATE Transaction transaction" +
            " SET transaction.feedback = :feedback" +
            " WHERE transaction.id = :transactionId")
    void updateFeedback(
            @Param("transactionId") long transactionId,
            @Param("feedback") TransactionStatus feedback
    );

    List<Transaction> findAllByNumber(String number);

    boolean existsByNumber(String number);

}
