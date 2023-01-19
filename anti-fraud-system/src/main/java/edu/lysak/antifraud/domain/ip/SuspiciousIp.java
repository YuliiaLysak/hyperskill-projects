package edu.lysak.antifraud.domain.ip;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "suspicious_ip")
public class SuspiciousIp implements Comparable<SuspiciousIp> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "IP should not be empty")
    @Column(name = "ip")
    private String ip;

    // TODO: 17.01.2023 is it okay fot entity to be comparable?
    @Override
    public int compareTo(SuspiciousIp other) {
        return Long.compare(this.id, other.id);
    }
}
