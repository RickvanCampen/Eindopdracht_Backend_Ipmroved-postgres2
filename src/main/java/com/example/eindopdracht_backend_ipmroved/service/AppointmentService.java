package com.example.eindopdracht_backend_ipmroved.service;

import com.example.eindopdracht_backend_ipmroved.exceptions.AppException;
import com.example.eindopdracht_backend_ipmroved.models.Appointment;
import com.example.eindopdracht_backend_ipmroved.models.User;
import com.example.eindopdracht_backend_ipmroved.dto.requests.CreateAppointmentRequest;
import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateAppointmentRequest;
import com.example.eindopdracht_backend_ipmroved.dto.responses.AppointmentResponse;
import com.example.eindopdracht_backend_ipmroved.repository.AppointmentRepository;
import com.example.eindopdracht_backend_ipmroved.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<AppointmentResponse> getAppointmentsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByUser(user);
        return appointments.stream()
                .map(AppointmentResponse::from) // Alleen de Appointment doorgeven
                .collect(Collectors.toList());
    }

    // Aanmaken van een afspraak
    @Transactional  // Zorgt ervoor dat de operatie binnen een transactie plaatsvindt
    public AppointmentResponse createAppointment(String username, CreateAppointmentRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        Appointment appointment = Appointment.builder()
                .bicycle_name(request.getBicycleName())
                .description(request.getDescription())
                .date_time(request.getDateTime())
                .user(user)
                .build();

        // Als er een bijlage is, decoderen en instellen
        if (request.getAttachment() != null && !request.getAttachment().isEmpty()) {
            appointment.setAttachment(Base64.getDecoder().decode(request.getAttachment()));
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return AppointmentResponse.from(savedAppointment); // Alleen de Appointment doorgeven
    }

    // Bijwerken van een afspraak
    @Transactional  // Zorgt ervoor dat de operatie binnen een transactie plaatsvindt
    public AppointmentResponse updateAppointment(String username, int id, UpdateAppointmentRequest request) {
        // Haal de gebruiker op
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        // Haal de afspraak op die bij de gebruiker hoort
        Appointment appointment = appointmentRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AppException("Appointment not found", HttpStatus.NOT_FOUND));

        // Werk de gegevens van de afspraak bij
        appointment.setBicycle_name(request.getBicycleName());
        appointment.setDescription(request.getDescription());
        appointment.setDate_time(request.getDateTime());

        // Als er een bijlage is, werk deze dan bij
        if (request.getAttachment() != null && !request.getAttachment().isEmpty()) {
            appointment.setAttachment(Base64.getDecoder().decode(request.getAttachment()));
        }

        // Sla de bijgewerkte afspraak op in de database
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return AppointmentResponse.from(updatedAppointment); // Alleen de Appointment doorgeven
    }

    // Verwijderen van een afspraak
    @Transactional  // Zorgt ervoor dat de operatie binnen een transactie plaatsvindt
    public void deleteAppointment(String username, int id) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        Appointment appointment = appointmentRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AppException("Appointment not found", HttpStatus.NOT_FOUND));
        appointmentRepository.delete(appointment);
    }
}
