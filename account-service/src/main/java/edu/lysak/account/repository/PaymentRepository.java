package edu.lysak.account.repository;

import edu.lysak.account.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Transactional
    @Modifying
    @Query("""
        UPDATE Payment payment
        SET payment.salary = :updatedSalary
        WHERE payment.userId = :userId
        AND payment.period = :period
        """)
    int updatePayment(
        @Param("userId") long userId,
        @Param("period") YearMonth period,
        @Param("updatedSalary") long updatedSalary
    );
}
