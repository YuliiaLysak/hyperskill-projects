package edu.lysak.antifraud.domain.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "card_limit")
public class CardLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "max_allowed_amount")
    private long maxAllowedAmount;

    @Column(name = "max_manual_processing_amount")
    private long maxManualProcessingAmount;

}
