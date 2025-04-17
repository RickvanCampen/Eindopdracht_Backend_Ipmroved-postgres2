package com.example.eindopdracht_backend_ipmroved.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)  // Voorkomt lege velden in JSON response
public class AuthResponse {
    private String message;
    private String token;
}
