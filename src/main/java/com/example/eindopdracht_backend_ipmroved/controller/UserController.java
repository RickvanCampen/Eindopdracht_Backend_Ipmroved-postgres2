package com.example.eindopdracht_backend_ipmroved.controller;

import com.example.eindopdracht_backend_ipmroved.models.User;
import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateUserRequest;
import com.example.eindopdracht_backend_ipmroved.dto.responses.UserResponse;
import com.example.eindopdracht_backend_ipmroved.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<UserResponse> getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        UserResponse response = service.getUserProfile(username);
        return ResponseEntity.ok(response);
    }

    @PutMapping("")
    public ResponseEntity<UserResponse> updateUser(
            @RequestBody UpdateUserRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        User updatedUser = service.updateUser(username, request, authentication);
        UserResponse response = UserResponse.from(updatedUser);
        return ResponseEntity.ok(response);
    }
}
