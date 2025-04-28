package com.example.eindopdracht_backend_ipmroved.dto.responses;

import com.example.eindopdracht_backend_ipmroved.models.Appointment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AppointmentResponse {

    @JsonProperty("id")
    private int id;

    @JsonProperty("bicycle_name")
    private String bicycleName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("date_time")
    private LocalDateTime dateTime;

    @JsonProperty("username")
    private String username;

    @JsonProperty("attachment")
    private String attachment;

    public static AppointmentResponse from(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .bicycleName(appointment.getBicycle_name())
                .description(appointment.getDescription())
                .dateTime(appointment.getDate_time())
                .username(appointment.getUser().getUsername())
                .attachment(appointment.getAttachment())
                .build();
    }
}
