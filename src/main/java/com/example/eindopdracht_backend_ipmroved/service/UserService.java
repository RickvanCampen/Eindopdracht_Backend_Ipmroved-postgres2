package com.example.eindopdracht_backend_ipmroved.service;

import com.example.eindopdracht_backend_ipmroved.exceptions.AppException;
import com.example.eindopdracht_backend_ipmroved.models.User;
import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateUserRequest;
import com.example.eindopdracht_backend_ipmroved.dto.responses.UserResponse;
import com.example.eindopdracht_backend_ipmroved.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse getUserProfile(String username) {
        var user = repository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        return UserResponse.from(user);
    }

    public User updateUser(String username, UpdateUserRequest updateUserRequest, Authentication authentication) {
        String currentUsername = authentication.getName();
        // Controleer of de huidige gebruiker probeert zichzelf bij te werken
        if (!currentUsername.equals(username)) {
            throw new AppException("Cannot update another user's account", HttpStatus.METHOD_NOT_ALLOWED);
        }
        User existingUser = repository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        // Als de gebruikersnaam is veranderd, controleer of die al bestaat
        String newUsername = updateUserRequest.getUsername();
        if (newUsername != null && !newUsername.equals(existingUser.getUsername())) {
            Optional<User> userWithSameUsername = repository.findByUsername(newUsername);
            if (userWithSameUsername.isPresent()) {
                throw new AppException("Username already exists", HttpStatus.CONFLICT);
            }
            existingUser.setUsername(newUsername);  // Update de gebruikersnaam
        }

        // Als er een nieuw wachtwoord is, update het wachtwoord
        String newPassword = updateUserRequest.getPassword();
        if (newPassword != null && !newPassword.isEmpty()) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encodedPassword);  // Update het wachtwoord
        }

        return repository.save(existingUser);  // Sla de bijgewerkte gebruiker op
    }
}
