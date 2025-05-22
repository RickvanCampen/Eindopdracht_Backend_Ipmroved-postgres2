package com.example.eindopdracht_backend_ipmroved.controller;

import com.example.eindopdracht_backend_ipmroved.models.Token;
import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateUserRequest;
import com.example.eindopdracht_backend_ipmroved.models.Role;
import com.example.eindopdracht_backend_ipmroved.models.User;
import com.example.eindopdracht_backend_ipmroved.repository.TokenRepository;
import com.example.eindopdracht_backend_ipmroved.repository.UserRepository;
import com.example.eindopdracht_backend_ipmroved.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TestRestTemplate restTemplate;

    private User testUser;
    private String jwtToken;

    @BeforeEach
    void setup() {
        tokenRepository.deleteAll();
        userRepository.deleteAll();

        testUser = User.builder()
                .username("testuser")
                .password(passwordEncoder.encode("password")) // gehashed wachtwoord
                .role(Role.USER)
                .build();

        userRepository.save(testUser);

        jwtToken = jwtService.generateToken(testUser);

        Token token = new Token();
        token.setToken(jwtToken);
        token.setUser(testUser);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

    @Test
    void getUserProfile_returnsUserResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURL("/api/v1/user"),
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("testuser");
    }

    @Test
    void updateUser_updatesUserAndReturnsUpdatedResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setUsername("newuser");
        updateRequest.setPassword("newpassword");

        HttpEntity<UpdateUserRequest> requestEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURL("/api/v1/user"),
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("newuser");

        var updatedUser = userRepository.findByUsername("newuser");
        assertThat(updatedUser).isPresent();
    }

    private String createURL(String uri) {
        return "http://localhost:" + port + uri;
    }
}
