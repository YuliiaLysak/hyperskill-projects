package edu.lysak.antifraud.domain.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionResponse {
    private TransactionStatus result;
    private String info;
}
