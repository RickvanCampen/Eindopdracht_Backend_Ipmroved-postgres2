package com.example.eindopdracht_backend_ipmroved.controller;

import com.example.eindopdracht_backend_ipmroved.dto.requests.CreateAppointmentRequest;
import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateAppointmentRequest;
import com.example.eindopdracht_backend_ipmroved.models.Appointment;
import com.example.eindopdracht_backend_ipmroved.models.Role;
import com.example.eindopdracht_backend_ipmroved.models.Token;
import com.example.eindopdracht_backend_ipmroved.models.User;
import com.example.eindopdracht_backend_ipmroved.repository.AppointmentRepository;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppointmentControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

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
        appointmentRepository.deleteAll();
        tokenRepository.deleteAll();
        userRepository.deleteAll();

        testUser = User.builder()
                .username("testuser")
                .password(passwordEncoder.encode("password"))
                .role(Role.USER)
                .build();

        userRepository.save(testUser);

        jwtToken = jwtService.generateToken(testUser);

        tokenRepository.save(
                Token.builder()
                        .token(jwtToken)
                        .expired(false)
                        .revoked(false)
                        .user(testUser)
                        .build()
        );
    }

    @Test
    void createAppointment_returnsCreatedAppointment() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        CreateAppointmentRequest request = CreateAppointmentRequest.builder()
                .bicycleName("Gazelle")
                .description("Onderhoud")
                .dateTime(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS))
                .attachment(null)
                .build();

        HttpEntity<CreateAppointmentRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURL("/api/v1/appointments"),
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Gazelle");
        assertThat(response.getBody()).contains("Onderhoud");

        List<Appointment> appointments = appointmentRepository.findAll();
        assertThat(appointments).hasSize(1);
        assertThat(appointments.get(0).getBicycle_name()).isEqualTo("Gazelle");
    }

    @Test
    void getAllAppointments_returnsListOfAppointments() {
        Appointment appointment = Appointment.builder()
                .bicycle_name("Gazelle")
                .description("Check-up")
                .date_time(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .user(testUser)
                .attachment(null)
                .build();
        appointmentRepository.save(appointment);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURL("/api/v1/appointments"),
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Gazelle");
        assertThat(response.getBody()).contains("Check-up");
    }

    @Test
    void updateAppointment_updatesAndReturnsUpdatedAppointment() {
        Appointment appointment = Appointment.builder()
                .bicycle_name("OldBike")
                .description("Old Description")
                .date_time(LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.SECONDS))
                .user(testUser)
                .attachment(null)
                .build();
        appointmentRepository.save(appointment);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        UpdateAppointmentRequest updateRequest = UpdateAppointmentRequest.builder()
                .bicycleName("NewBike")
                .description("Updated Description")
                .dateTime(LocalDateTime.now().plusDays(4).truncatedTo(ChronoUnit.SECONDS))
                .attachment(null)
                .build();

        HttpEntity<UpdateAppointmentRequest> requestEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURL("/api/v1/appointments/" + appointment.getId()),
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("NewBike");
        assertThat(response.getBody()).contains("Updated Description");

        var updatedAppointment = appointmentRepository.findById(appointment.getId());
        assertThat(updatedAppointment).isPresent();
        assertThat(updatedAppointment.get().getBicycle_name()).isEqualTo("NewBike");
    }

    @Test
    void deleteAppointment_removesAppointment() {
        Appointment appointment = Appointment.builder()
                .bicycle_name("ToDelete")
                .description("Description")
                .date_time(LocalDateTime.now().plusDays(5).truncatedTo(ChronoUnit.SECONDS))
                .user(testUser)
                .attachment(null)
                .build();
        appointmentRepository.save(appointment);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                createURL("/api/v1/appointments/" + appointment.getId()),
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(appointmentRepository.findById(appointment.getId())).isNotPresent();
    }

    private String createURL(String uri) {
        return "http://localhost:" + port + uri;
    }
}
