package com.example.eindopdracht_backend_ipmroved;

import com.example.eindopdracht_backend_ipmroved.exceptions.AppException;
import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateUserRequest;
import com.example.eindopdracht_backend_ipmroved.repository.UserRepository;
import com.example.eindopdracht_backend_ipmroved.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> userService.getUserProfile(username));
        assertThat(exception.getMessage()).isEqualTo("User not found");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldThrowExceptionWhenUserTriesToUpdateAnotherUserProfile() {
        // Arrange
        String username = "user1";
        String otherUser = "user2";
        UpdateUserRequest updateRequest = new UpdateUserRequest("newUsername", "newPassword123");
        when(authentication.getName()).thenReturn(username);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> userService.updateUser(otherUser, updateRequest, authentication));
        assertThat(exception.getMessage()).isEqualTo("Cannot update another user's account");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }
}