package com.example.eindopdracht_backend_ipmroved.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    @JsonProperty("username")
    @NotBlank(message = "Gebruikersnaam mag niet leeg zijn.")
    @Size(min = 3, max = 50, message = "Gebruikersnaam moet tussen 3 en 50 tekens zijn.")
    private String username;

    @JsonProperty("password")
    @NotBlank(message = "Wachtwoord mag niet leeg zijn.")
    @Size(min = 6, message = "Wachtwoord moet minimaal 6 tekens bevatten.")
    private String password;

    @JsonProperty("role")
    @NotBlank(message = "Rol mag niet leeg zijn.")
    @Pattern(regexp = "USER|ADMIN", message = "Rol moet USER of ADMIN zijn.")
    private String role;
}
