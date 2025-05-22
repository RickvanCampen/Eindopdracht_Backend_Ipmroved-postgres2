package com.example.eindopdracht_backend_ipmroved.controller;

import com.example.eindopdracht_backend_ipmroved.dto.requests.CreateAppointmentRequest;
import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateAppointmentRequest;
import com.example.eindopdracht_backend_ipmroved.dto.responses.AppointmentResponse;
import com.example.eindopdracht_backend_ipmroved.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments(Authentication authentication) {
        String username = authentication.getName();
        List<AppointmentResponse> responses = service.getAppointmentsForUser(username);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        AppointmentResponse response = service.createAppointment(username, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @PathVariable int id,
            @Valid @RequestBody UpdateAppointmentRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        AppointmentResponse response = service.updateAppointment(username, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable int id,
            Authentication authentication) {
        String username = authentication.getName();
        service.deleteAppointment(username, id);
        return ResponseEntity.ok().build();
    }
}
