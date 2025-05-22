package com.example.eindopdracht_backend_ipmroved.mapper;

import com.example.eindopdracht_backend_ipmroved.dto.requests.CreateAppointmentRequest;
import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateAppointmentRequest;
import com.example.eindopdracht_backend_ipmroved.dto.responses.AppointmentResponse;
import com.example.eindopdracht_backend_ipmroved.models.Appointment;
import com.example.eindopdracht_backend_ipmroved.models.User;

public class AppointmentMapper {

    public static Appointment toEntity(CreateAppointmentRequest request, User user) {
        return Appointment.builder()
                .bicycle_name(request.getBicycleName())
                .description(request.getDescription())
                .date_time(request.getDateTime())
                .attachment(request.getAttachment())
                .user(user)
                .build();
    }

    public static void updateEntity(Appointment appointment, UpdateAppointmentRequest request) {
        appointment.setBicycle_name(request.getBicycleName());
        appointment.setDescription(request.getDescription());
        appointment.setDate_time(request.getDateTime());
        appointment.setAttachment(request.getAttachment());
    }

    public static AppointmentResponse toResponse(Appointment appointment) {
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
