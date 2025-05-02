package com.example.eindopdracht_backend_ipmroved.service;

import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateUserRequest;
import com.example.eindopdracht_backend_ipmroved.dto.responses.UserResponse;
import com.example.eindopdracht_backend_ipmroved.exceptions.AppException;
import com.example.eindopdracht_backend_ipmroved.models.Role;
import com.example.eindopdracht_backend_ipmroved.models.User;
import com.example.eindopdracht_backend_ipmroved.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    private final User testUser = new User();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        testUser.setId(1);
        testUser.setRole(Role.USER);
        testUser.setUsername("User1");
        testUser.setPassword("Password1");
    }

    @Test
    void getUserProfile_shouldReturnUserResponse_whenUserExists() {
        when(repository.findByUsername("User1")).thenReturn(Optional.of(testUser));

        UserResponse response = userService.getUserProfile("User1");

        assertNotNull(response);
        assertEquals("User1", response.getUsername());
        verify(repository).findByUsername("User1");
    }

    @Test
    void getUserProfile_shouldThrowException_whenUserNotFound() {
        when(repository.findByUsername("unknown")).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () ->
                userService.getUserProfile("unknown"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void updateUser_shouldUpdateUsernameAndPassword_whenValid() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("newname");
        request.setPassword("newpassword");

        when(authentication.getName()).thenReturn("User1");
        when(repository.findByUsername("User1")).thenReturn(Optional.of(testUser));
        when(repository.findByUsername("newname")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedPass");
        when(repository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User updated = userService.updateUser("User1", request, authentication);

        assertEquals("newname", updated.getUsername());
        assertEquals("encodedPass", updated.getPassword());
    }

    @Test
    void updateUser_shouldThrowException_whenUsernamesMismatch() {
        when(authentication.getName()).thenReturn("anotheruser");

        AppException ex = assertThrows(AppException.class, () ->
                userService.updateUser("User1", new UpdateUserRequest(), authentication));

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, ex.getStatus());
    }

    @Test
    void updateUser_shouldThrowException_whenUserNotFound() {
        when(authentication.getName()).thenReturn("User1");
        when(repository.findByUsername("User1")).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () ->
                userService.updateUser("User1", new UpdateUserRequest(), authentication));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void updateUser_shouldThrowException_whenUsernameExists() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("existinguser");

        User otherUser = new User();
        otherUser.setUsername("existinguser");

        when(authentication.getName()).thenReturn("User1");
        when(repository.findByUsername("User1")).thenReturn(Optional.of(testUser));
        when(repository.findByUsername("existinguser")).thenReturn(Optional.of(otherUser));

        AppException ex = assertThrows(AppException.class, () ->
                userService.updateUser("User1", request, authentication));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    void updateUser_shouldSkipPasswordUpdate_whenPasswordIsEmpty() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("User1");
        request.setPassword("");

        when(authentication.getName()).thenReturn("User1");
        when(repository.findByUsername("User1")).thenReturn(Optional.of(testUser));
        when(repository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User updated = userService.updateUser("User1", request, authentication);

        assertEquals("User1", updated.getUsername());
        assertEquals("Password1", updated.getPassword());
    }
}
