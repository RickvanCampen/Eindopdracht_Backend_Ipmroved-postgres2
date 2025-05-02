package com.example.eindopdracht_backend_ipmroved.service;

import com.example.eindopdracht_backend_ipmroved.dto.requests.CreateAppointmentRequest;
import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateAppointmentRequest;
import com.example.eindopdracht_backend_ipmroved.exceptions.AppException;
import com.example.eindopdracht_backend_ipmroved.models.Appointment;
import com.example.eindopdracht_backend_ipmroved.models.User;
import com.example.eindopdracht_backend_ipmroved.repository.AppointmentRepository;
import com.example.eindopdracht_backend_ipmroved.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private User user;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
        user.setUsername("User1");

        appointment = Appointment.builder()
                .id(1)
                .bicycle_name("Gazelle")
                .description("Test description")
                .date_time(LocalDateTime.now())
                .attachment("test.pdf")
                .user(user)
                .build();
    }

    @Test
    void getAppointmentsForUser_shouldReturnAppointments() {
        when(userRepository.findByUsername("User1")).thenReturn(Optional.of(user));
        when(appointmentRepository.findByUser(user)).thenReturn(Collections.singletonList(appointment));

        var responses = appointmentService.getAppointmentsForUser("User1");

        assertEquals(1, responses.size());
        assertEquals("Gazelle", responses.get(0).getBicycleName());
    }

    @Test
    void getAppointmentsForUser_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByUsername("invalid")).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () ->
                appointmentService.getAppointmentsForUser("invalid"));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void createAppointment_shouldReturnSavedAppointment() {
        CreateAppointmentRequest request = new CreateAppointmentRequest();
        request.setBicycleName("Gazelle");
        request.setDescription("New appointment");
        request.setDateTime(LocalDateTime.now());
        request.setAttachment("attachment.pdf");

        when(userRepository.findByUsername("User1")).thenReturn(Optional.of(user));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        var response = appointmentService.createAppointment("User1", request);

        assertEquals("Gazelle", response.getBicycleName());
    }

    @Test
    void createAppointment_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        CreateAppointmentRequest request = new CreateAppointmentRequest();

        AppException ex = assertThrows(AppException.class, () ->
                appointmentService.createAppointment("unknown", request));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void updateAppointment_shouldUpdateAndReturnAppointment() {
        UpdateAppointmentRequest request = new UpdateAppointmentRequest();
        request.setBicycleName("UpdatedBike");
        request.setDescription("Updated description");
        request.setDateTime(LocalDateTime.now());
        request.setAttachment("updated.pdf");

        when(userRepository.findByUsername("User1")).thenReturn(Optional.of(user));
        when(appointmentRepository.findByIdAndUser(1, user)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        var response = appointmentService.updateAppointment("User1", 1, request);

        assertEquals("UpdatedBike", response.getBicycleName());
    }

    @Test
    void updateAppointment_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByUsername("nouser")).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () ->
                appointmentService.updateAppointment("nouser", 1, new UpdateAppointmentRequest()));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void updateAppointment_shouldThrowException_whenAppointmentNotFound() {
        when(userRepository.findByUsername("User1")).thenReturn(Optional.of(user));
        when(appointmentRepository.findByIdAndUser(1, user)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () ->
                appointmentService.updateAppointment("User1", 1, new UpdateAppointmentRequest()));

        assertEquals("Appointment not found", ex.getMessage());
    }

    @Test
    void deleteAppointment_shouldDeleteSuccessfully() {
        when(userRepository.findByUsername("User1")).thenReturn(Optional.of(user));
        when(appointmentRepository.findByIdAndUser(1, user)).thenReturn(Optional.of(appointment));

        appointmentService.deleteAppointment("User1", 1);

        verify(appointmentRepository, times(1)).delete(appointment);
    }

    @Test
    void deleteAppointment_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () ->
                appointmentService.deleteAppointment("unknown", 1));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void deleteAppointment_shouldThrowException_whenAppointmentNotFound() {
        when(userRepository.findByUsername("User1")).thenReturn(Optional.of(user));
        when(appointmentRepository.findByIdAndUser(1, user)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () ->
                appointmentService.deleteAppointment("User1", 1));

        assertEquals("Appointment not found", ex.getMessage());
    }
}
