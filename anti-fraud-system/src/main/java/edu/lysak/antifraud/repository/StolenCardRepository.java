package edu.lysak.antifraud.repository;

import edu.lysak.antifraud.domain.card.StolenCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StolenCardRepository extends CrudRepository<StolenCard, Long> {

    Optional<StolenCard> findByNumber(String number);

    boolean existsByNumber(String number);
}
