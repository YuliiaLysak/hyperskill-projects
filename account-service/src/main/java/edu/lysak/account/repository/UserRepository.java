package edu.lysak.account.repository;

import edu.lysak.account.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("""
        UPDATE User user
        SET user.password = :newPassword
        WHERE user.userId = :userId
        """)
    void changeUserPassword(
        @Param("userId") long userId,
        @Param("newPassword") String newPassword
    );

    @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.payments payments
        WHERE u.userId = :userId
        """)
    Optional<User> findUserWithPaymentsByUserId(
        @Param("userId") Long userId
    );

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.failedAttempt = :failAttempts WHERE u.email = :email")
    void updateFailedAttempts(@Param("failAttempts") int failAttempts, @Param("email") String email);
}
