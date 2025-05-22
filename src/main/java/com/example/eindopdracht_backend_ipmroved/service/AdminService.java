package com.example.eindopdracht_backend_ipmroved.service;

import com.example.eindopdracht_backend_ipmroved.exceptions.AppException;
import com.example.eindopdracht_backend_ipmroved.models.Appointment;
import com.example.eindopdracht_backend_ipmroved.models.Role;
import com.example.eindopdracht_backend_ipmroved.models.User;
import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateUserRoleRequest;
import com.example.eindopdracht_backend_ipmroved.dto.responses.UserResponse;
import com.example.eindopdracht_backend_ipmroved.dto.responses.AppointmentResponse;
import com.example.eindopdracht_backend_ipmroved.repository.UserRepository;
import com.example.eindopdracht_backend_ipmroved.repository.AppointmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private final UserRepository repository;
    private final AppointmentRepository appointmentRepository;

    public AdminService(UserRepository repository, AppointmentRepository appointmentRepository) {
        this.repository = repository;
        this.appointmentRepository = appointmentRepository;
    }

    public UserResponse getUserProfile(String username) {
        var user = repository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        return UserResponse.from(user);
    }

    public List<UserResponse> getAllUserResponses() {
        var users = repository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            UserResponse userResponse = UserResponse.from(user);
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    public User updateUserRole(String username, UpdateUserRoleRequest updateUserRoleRequest, Authentication authentication) {
        String currentUsername = authentication.getName();
        User currentUser = repository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AppException("Only admins can update user roles", HttpStatus.FORBIDDEN);
        }
        User existingUser = repository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        if (existingUser.getRole() == Role.ADMIN) {
            throw new AppException("Cannot update an admin's role", HttpStatus.METHOD_NOT_ALLOWED);
        }
        existingUser.setRole(updateUserRoleRequest.getRole());
        return repository.save(existingUser);
    }

    public List<AppointmentResponse> getAllAppointments() {
        var appointments = appointmentRepository.findAll();
        List<AppointmentResponse> appointmentResponses = new ArrayList<>();
        for (Appointment appointment : appointments) {
            AppointmentResponse appointmentResponse = AppointmentResponse.from(appointment);
            appointmentResponses.add(appointmentResponse);
        }
        return appointmentResponses;
    }
}

