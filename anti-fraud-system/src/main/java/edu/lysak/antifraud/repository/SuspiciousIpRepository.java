package edu.lysak.antifraud.repository;

import edu.lysak.antifraud.domain.ip.SuspiciousIp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuspiciousIpRepository extends CrudRepository<SuspiciousIp, Long> {

    Optional<SuspiciousIp> findByIp(String ip);

    boolean existsByIp(String ip);
}
