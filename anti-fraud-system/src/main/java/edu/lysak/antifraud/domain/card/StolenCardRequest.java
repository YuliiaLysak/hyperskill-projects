package edu.lysak.antifraud.domain.card;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StolenCardRequest {
    @NotEmpty(message = "Number should not be empty")
    private String number;
}
