package com.example.eindopdracht_backend_ipmroved.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAppointmentRequest {

    @JsonProperty("bicycle_name")
    @NotBlank(message = "Fietsnaam mag niet leeg zijn.")
    private String bicycleName;

    @JsonProperty("description")
    @NotBlank(message = "Omschrijving mag niet leeg zijn.")
    private String description;

    @JsonProperty("date_time")
    @NotNull(message = "Datum/tijd mag niet leeg zijn.")
    @Future(message = "Afspraakdatum moet in de toekomst liggen.")
    private LocalDateTime dateTime;

    @JsonProperty("attachment")
    private String attachment;
}
