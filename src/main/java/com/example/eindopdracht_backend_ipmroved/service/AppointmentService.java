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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final UserRepository userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    // Ophalen van afspraken voor een specifieke gebruiker
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        List<Appointment> appointments = appointmentRepository.findByUser(user);
        return appointments.stream()
                .map(AppointmentResponse::from)
                .collect(Collectors.toList());
    }

    // Aanmaken van een nieuwe afspraak
    @Transactional
    public AppointmentResponse createAppointment(String username, CreateAppointmentRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        Appointment appointment = Appointment.builder()
                .bicycle_name(request.getBicycleName())
                .description(request.getDescription())
                .date_time(request.getDateTime())
                .attachment(request.getAttachment()) // <-- Attachment zetten!
                .user(user)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return AppointmentResponse.from(savedAppointment);
    }

    // Bijwerken van een bestaande afspraak
    @Transactional
    public AppointmentResponse updateAppointment(String username, int id, UpdateAppointmentRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        Appointment appointment = appointmentRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AppException("Appointment not found", HttpStatus.NOT_FOUND));

        appointment.setBicycle_name(request.getBicycleName());
        appointment.setDescription(request.getDescription());
        appointment.setDate_time(request.getDateTime());
        appointment.setAttachment(request.getAttachment()); // <-- Attachment bijwerken!

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return AppointmentResponse.from(updatedAppointment);
    }

    // Verwijderen van een afspraak
    @Transactional
    public void deleteAppointment(String username, int id) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        Appointment appointment = appointmentRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AppException("Appointment not found", HttpStatus.NOT_FOUND));

        appointmentRepository.delete(appointment);
    }
}
