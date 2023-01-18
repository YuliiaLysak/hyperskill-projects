package edu.lysak.antifraud.domain.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Transaction {
    private long amount;
    private String ip;
    private String number;
}
