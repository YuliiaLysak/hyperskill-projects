package edu.lysak.antifraud.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFeedbackResponse implements Comparable<TransactionFeedbackResponse> {
    private Long transactionId;
    private long amount;
    private String ip;
    private String number;
    private Region region;
    private LocalDateTime date;
    private TransactionStatus result;
    private String feedback;

    @Override
    public int compareTo(TransactionFeedbackResponse other) {
        return Long.compare(this.transactionId, other.transactionId);
    }
}
