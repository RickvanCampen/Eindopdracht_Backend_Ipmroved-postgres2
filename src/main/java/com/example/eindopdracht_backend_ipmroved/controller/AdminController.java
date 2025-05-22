package com.example.eindopdracht_backend_ipmroved.controller;

import com.example.eindopdracht_backend_ipmroved.models.User;
import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateUserRoleRequest;
import com.example.eindopdracht_backend_ipmroved.dto.responses.AppointmentResponse;
import com.example.eindopdracht_backend_ipmroved.dto.responses.UserResponse;
import com.example.eindopdracht_backend_ipmroved.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    @GetMapping("/details/{username}")
    public ResponseEntity<UserResponse> getUserProfile(
            @PathVariable String username) {
        UserResponse response = service.getUserResponseByUsername(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/details/all")
    public List<UserResponse> getAllUsers() {
        return service.getAllUserResponses();
    }

    @PutMapping("/update_role/{username}")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable String username,
            @Valid @RequestBody UpdateUserRoleRequest request,
            Authentication authentication) {
        User updatedUser = service.updateUserRole(username, request, authentication);
        UserResponse response = UserResponse.from(updatedUser);
        return ResponseEntity.ok(response);
    }

    @Transactional
    @GetMapping("/appointments/all")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        List<AppointmentResponse> appointments = service.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }
}
