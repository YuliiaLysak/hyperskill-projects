package edu.lysak.antifraud.domain.ip;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuspiciousIpRequest {
    @NotEmpty(message = "IP should not be empty")
    private String ip;
}
