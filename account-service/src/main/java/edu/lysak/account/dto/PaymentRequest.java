package edu.lysak.account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.YearMonth;

@Setter
@Getter
public class PaymentRequest {

    private String employee;

    @JsonFormat(pattern = "MM-yyyy")
    private YearMonth period;

    @Positive(message = "'salary' must be non negative!")
    private long salary;
}
