package com.example.eindopdracht_backend_ipmroved.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @JsonProperty
    @Size(min = 3, max = 50, message = "Gebruikersnaam moet tussen 3 en 50 tekens zijn.")
    private String username;

    @JsonProperty
    @Size(min = 6, message = "Wachtwoord moet minimaal 6 tekens bevatten.")
    private String password;
}
