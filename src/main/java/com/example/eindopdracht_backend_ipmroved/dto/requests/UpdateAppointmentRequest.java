package com.example.eindopdracht_backend_ipmroved.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAppointmentRequest {
    @JsonProperty
    private String bicycleName;

    @JsonProperty
    private String description;

    @JsonProperty
    private LocalDateTime dateTime;

    @JsonProperty
    private String attachment;
}
