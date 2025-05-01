package com.example.eindopdracht_backend_ipmroved.service;

import com.example.eindopdracht_backend_ipmroved.exceptions.AppException;
import com.example.eindopdracht_backend_ipmroved.models.Role;
import com.example.eindopdracht_backend_ipmroved.models.Token;
import com.example.eindopdracht_backend_ipmroved.models.User;
import com.example.eindopdracht_backend_ipmroved.dto.requests.AuthRequest;
import com.example.eindopdracht_backend_ipmroved.dto.responses.AuthResponse;
import com.example.eindopdracht_backend_ipmroved.repository.UserRepository;
import com.example.eindopdracht_backend_ipmroved.repository.TokenRepository;
import com.example.eindopdracht_backend_ipmroved.dto.requests.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService service;
    private final TokenRepository tokenRepository;

    public AuthResponse registerUser(CreateUserRequest request) {
        var existingUser = repository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            throw new AppException("User already exists", HttpStatus.CONFLICT);
        }

        LocalDateTime now = LocalDateTime.now();
        Date createdAt = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date updatedAt = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(resolveRole(request.getRole()))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        repository.save(user);

        return AuthResponse.builder()
                .message("User registered successfully. Please log in.")
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        var jwtToken = service.generateToken(user);

        Token token = new Token();
        token.setToken(jwtToken);
        token.setExpired(false);
        token.setRevoked(false);
        token.setUser(user);

        tokenRepository.save(token);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    private Role resolveRole(String roleName) {
        if (roleName == null) return Role.USER;
        try {
            return Role.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Role.USER;
        }
    }
}
