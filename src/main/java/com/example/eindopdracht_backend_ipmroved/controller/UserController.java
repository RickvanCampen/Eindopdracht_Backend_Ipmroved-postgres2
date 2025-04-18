package com.example.eindopdracht_backend_ipmroved.controller;

import com.example.eindopdracht_backend_ipmroved.models.User;
import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateUserRequest;
import com.example.eindopdracht_backend_ipmroved.dto.responses.UserResponse;
import com.example.eindopdracht_backend_ipmroved.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/details")
    public ResponseEntity<UserResponse> getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        UserResponse response = service.getUserProfile(username);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(
            @RequestBody UpdateUserRequest request,  // UpdateUserRequest ontvangen via @RequestBody
            Authentication authentication) {
        String username = authentication.getName();
        User updatedUser = service.updateUser(username, request, authentication);  // UserService update
        UserResponse response = UserResponse.from(updatedUser);  // Convert de User naar een response
        return ResponseEntity.ok(response);
    }
}
