package edu.lysak.antifraud.domain.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Amount should not be null")
    @Column(name = "amount")
    private long amount;

    @NotEmpty(message = "IP should not be empty")
    @Column(name = "ip")
    private String ip;

    @NotEmpty(message = "Number should not be empty")
    @Column(name = "number")
    private String number;

    @NotNull(message = "Region should not be null")
    @Column(name = "region")
    private Region region;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "result")
    private TransactionStatus result;

    @Column(name = "info")
    private String info;

    @Column(name = "feedback")
    private TransactionStatus feedback;
}
