package com.example.eindopdracht_backend_ipmroved;

import com.example.eindopdracht_backend_ipmroved.exceptions.AppException;
import com.example.eindopdracht_backend_ipmroved.repository.AppointmentRepository;
import com.example.eindopdracht_backend_ipmroved.repository.UserRepository;
import com.example.eindopdracht_backend_ipmroved.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AdminService adminService;

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> adminService.getUserResponseByUsername(username));
        assertThat(exception.getMessage()).isEqualTo("User not found");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnAllAppointmentsSuccessfully() {
        // Arrange
        when(appointmentRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<?> appointments = adminService.getAllAppointments();

        // Assert
        assertThat(appointments).isEmpty();
    }

    @Test
    void shouldReturnEmptyUserListWhenNoUsersExist() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<?> users = adminService.getAllUserResponses();

        // Assert
        assertThat(users).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenCurrentUserNotFoundWhileUpdatingRole() {
        // Arrange
        String username = "testUser";
        when(authentication.getName()).thenReturn("adminUser");
        when(userRepository.findByUsername("adminUser")).thenReturn(Optional.empty());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> adminService.updateUserRole(username, null, authentication));
        assertThat(exception.getMessage()).isEqualTo("User not found");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}