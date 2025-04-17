package com.example.eindopdracht_backend_ipmroved;

import com.example.eindopdracht_backend_ipmroved.models.User;
import com.example.eindopdracht_backend_ipmroved.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GetUserByUsernameIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnUserWhenUsernameExists() {
        // Stel dat de testgebruiker met username "user1" in de database bestaat
        String testUsername = "user1";

        User user = userRepository.findByUsername(testUsername).orElse(null);

        // Test dat de gebruiker bestaat en de juiste username heeft
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(testUsername);
    }
}
