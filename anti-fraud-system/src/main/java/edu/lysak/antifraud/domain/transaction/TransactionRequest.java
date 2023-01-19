package edu.lysak.antifraud.domain.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TransactionRequest {

    @NotNull(message = "Amount should not be null")
    private long amount;

    @NotEmpty(message = "IP should not be empty")
    private String ip;

    @NotEmpty(message = "Number should not be empty")
    private String number;

    @NotNull(message = "Region should not be null")
    private Region region;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
}
