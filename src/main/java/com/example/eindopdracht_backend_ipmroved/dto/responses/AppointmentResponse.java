package com.example.eindopdracht_backend_ipmroved.dto.responses;

import com.example.eindopdracht_backend_ipmroved.models.Appointment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Base64;

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
    private String attachment;  // Dit blijft een String (base64-gecodeerde string)

    // Pas de from-methode aan zodat deze enkel de Appointment accepteert
    public static AppointmentResponse from(Appointment appointment) {
        // Converteer byte[] naar base64 String (indien attachment niet null is)
        String attachmentBase64 = appointment.getAttachment() != null ?
                Base64.getEncoder().encodeToString(appointment.getAttachment()) : null;

        return AppointmentResponse.builder()
                .id(appointment.getId()) // Zorgt ervoor dat het ID wordt meegenomen
                .bicycleName(appointment.getBicycle_name())
                .description(appointment.getDescription())
                .dateTime(appointment.getDate_time())
                .username(appointment.getUser().getUsername()) // Haalt de username uit de User in Appointment
                .attachment(attachmentBase64) // Converteer naar base64 en voeg toe
                .build();
    }
}
