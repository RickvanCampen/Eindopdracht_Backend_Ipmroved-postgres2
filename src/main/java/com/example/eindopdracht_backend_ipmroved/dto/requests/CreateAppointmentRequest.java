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
public class CreateAppointmentRequest {
    @JsonProperty("bicycle_name")
    private String bicycleName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("date_time")
    private LocalDateTime dateTime;

    @JsonProperty("attachment")
    private String attachment;
}
