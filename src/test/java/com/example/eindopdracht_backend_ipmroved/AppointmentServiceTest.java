package com.example.eindopdracht_backend_ipmroved;

import com.example.eindopdracht_backend_ipmroved.dto.requests.CreateAppointmentRequest;
import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateAppointmentRequest;
import com.example.eindopdracht_backend_ipmroved.exceptions.AppException;
import com.example.eindopdracht_backend_ipmroved.models.Appointment;
import com.example.eindopdracht_backend_ipmroved.models.User;
import com.example.eindopdracht_backend_ipmroved.repository.AppointmentRepository;
import com.example.eindopdracht_backend_ipmroved.repository.UserRepository;
import com.example.eindopdracht_backend_ipmroved.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void shouldThrowExceptionWhenUserNotFoundForAppointments() {
        // Arrange
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> appointmentService.getAppointmentsForUser(username));
        assertThat(exception.getMessage()).isEqualTo("User not found");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldCreateAppointmentSuccessfully() {
        // Arrange
        String username = "user1";
        User user = new User();
        user.setUsername(username);
        CreateAppointmentRequest request = new CreateAppointmentRequest("Bike1", "Fix tire", LocalDateTime.now(), "");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var response = appointmentService.createAppointment(username, request);

        // Assert
        assertThat(response.getBicycleName()).isEqualTo("Bike1");
        assertThat(response.getDescription()).isEqualTo("Fix tire");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentAppointment() {
        // Arrange
        String username = "user1";
        int appointmentId = 1;
        UpdateAppointmentRequest request = new UpdateAppointmentRequest("Bike2", "Change brakes", LocalDateTime.now(), "");
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(appointmentRepository.findByIdAndUser(appointmentId, user)).thenReturn(Optional.empty());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> appointmentService.updateAppointment(username, appointmentId, request));
        assertThat(exception.getMessage()).isEqualTo("Appointment not found");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldDeleteAppointmentSuccessfully() {
        // Arrange
        String username = "user1";
        int appointmentId = 1;
        User user = new User();
        user.setUsername(username);
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setUser(user);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(appointmentRepository.findByIdAndUser(appointmentId, user)).thenReturn(Optional.of(appointment));
        doNothing().when(appointmentRepository).delete(appointment);

        // Act
        appointmentService.deleteAppointment(username, appointmentId);

        // Assert
        verify(appointmentRepository, times(1)).delete(appointment);
    }
}
