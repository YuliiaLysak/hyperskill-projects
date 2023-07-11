package edu.lysak.account.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.lysak.account.domain.SecurityEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecurityEventResponse {

    private Long id;
    private String date;
    private SecurityEventType action;
    private String subject;
    private String object;
    private String path;
}
