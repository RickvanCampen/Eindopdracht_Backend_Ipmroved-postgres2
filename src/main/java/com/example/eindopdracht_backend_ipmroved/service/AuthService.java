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
    private final TokenRepository tokenRepository;  // Voeg het TokenRepository toe

    // Methode voor registratie van gebruikers
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
                .role(Role.USER)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        repository.save(user);

        return AuthResponse.builder()
                .message("User registered successfully. Please log in.")
                .build();
    }

    // Methode voor authenticatie van gebruikers en het genereren van JWT-token
    public AuthResponse authenticate(AuthRequest request) {
        // Probeer de gebruiker te authenticeren met de gegeven username en password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Haal de gebruiker op uit de database
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        // Genereer het JWT-token voor de gebruiker
        var jwtToken = service.generateToken(user);

        // Maak een nieuw Token-object aan en sla deze op in de database
        Token token = new Token();
        token.setToken(jwtToken);         // Zet de gegenereerde token
        token.setExpired(false);           // Zet expired flag naar false
        token.setRevoked(false);           // Zet revoked flag naar false
        token.setUser(user);               // Koppel de token aan de gebruiker

        // Sla de token op in de database
        tokenRepository.save(token);

        // Return de AuthResponse met het token voor de client
        return AuthResponse.builder()
                .token(jwtToken)  // Geef de token terug aan de client
                .build();
    }
}
