package edu.lysak.antifraud.domain.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionFeedbackRequest {
    private long transactionId;
    private TransactionStatus feedback;
}
