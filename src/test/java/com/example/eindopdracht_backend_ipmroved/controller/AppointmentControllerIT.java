package com.example.eindopdracht_backend_ipmroved.controller;

import com.example.eindopdracht_backend_ipmroved.dto.requests.CreateAppointmentRequest;
import com.example.eindopdracht_backend_ipmroved.dto.requests.UpdateAppointmentRequest;
import com.example.eindopdracht_backend_ipmroved.models.Appointment;
import com.example.eindopdracht_backend_ipmroved.models.User;
import com.example.eindopdracht_backend_ipmroved.repository.AppointmentRepository;
import com.example.eindopdracht_backend_ipmroved.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
class AppointmentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setup() {
        appointmentRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("User1");
        testUser.setPassword("password");
        userRepository.save(testUser);
    }

    @Test
    @WithMockUser(username = "User1")
    void createAndGetAppointment() throws Exception {
        CreateAppointmentRequest request = new CreateAppointmentRequest();
        request.setBicycleName("Gazelle");
        request.setDescription("Test Description");
        request.setDateTime(LocalDateTime.now().plusDays(1));
        request.setAttachment("test.pdf");

        // POST /api/v1/appointments
        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bicycle_name").value("Gazelle"));

        // GET /api/v1/appointments
        mockMvc.perform(get("/api/v1/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "User1")
    void updateAppointment() throws Exception {
        Appointment appointment = Appointment.builder()
                .bicycle_name("OldBike")
                .description("Old desc")
                .date_time(LocalDateTime.now().plusDays(1))
                .attachment("old.pdf")
                .user(testUser)
                .build();
        appointment = appointmentRepository.save(appointment);

        UpdateAppointmentRequest request = new UpdateAppointmentRequest();
        request.setBicycleName("NewBike");
        request.setDescription("Updated");
        request.setDateTime(LocalDateTime.now().plusDays(2));
        request.setAttachment("new.pdf");

        // PUT /api/v1/appointments/{id}
        mockMvc.perform(put("/api/v1/appointments/" + appointment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bicycle_name").value("NewBike"));
    }

    @Test
    @WithMockUser(username = "User1")
    void deleteAppointment() throws Exception {
        Appointment appointment = Appointment.builder()
                .bicycle_name("ToDelete")
                .description("Desc")
                .date_time(LocalDateTime.now().plusDays(1))
                .attachment("del.pdf")
                .user(testUser)
                .build();
        appointment = appointmentRepository.save(appointment);

        // DELETE /api/v1/appointments/{id}
        mockMvc.perform(delete("/api/v1/appointments/" + appointment.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
