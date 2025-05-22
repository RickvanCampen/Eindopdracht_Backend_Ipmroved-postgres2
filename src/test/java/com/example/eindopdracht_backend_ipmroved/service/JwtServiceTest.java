package com.example.eindopdracht_backend_ipmroved.service;

import com.example.eindopdracht_backend_ipmroved.TestUtil.TestUtils;
import com.example.eindopdracht_backend_ipmroved.models.Role;
import com.example.eindopdracht_backend_ipmroved.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private static final String SECRET = "mytestsecretmytestsecretmytestsecret!"; // min 32 chars

    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET);
        TestUtils.setField(jwtService, "expirationTime", 3600000L); // 1 uur

        user = User.builder()
                .username("testuser")
                .role(Role.USER)
                .build();
    }

    // Helper om token te maken met custom expiry millis vanaf nu (negatief = in verleden)
    private String generateTokenWithCustomExpiry(User user) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis + (long) -60000);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ROLE_" + user.getRole().name());

        return io.jsonwebtoken.Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    void generateToken_shouldGenerateValidToken() {
        String token = jwtService.generateToken(user);
        assertNotNull(token);
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        String token = jwtService.generateToken(user);
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void generateToken_shouldIncludeRoleClaim() {
        String token = jwtService.generateToken(user);
        String role = jwtService.extractClaim(token, claims -> (String) claims.get("role"));
        assertEquals("ROLE_USER", role);
    }

    @Test
    void generateToken_withExtraClaims_shouldIncludeClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customKey", "customValue");

        String token = jwtService.generateToken(extraClaims, user);
        String customClaim = jwtService.extractClaim(token, claims -> (String) claims.get("customKey"));
        assertEquals("customValue", customClaim);
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(user);
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void isTokenValid_shouldReturnFalseIfUsernameDoesNotMatch() {
        User otherUser = User.builder()
                .username("otheruser")
                .role(Role.USER)
                .build();
        String token = jwtService.generateToken(user);
        assertFalse(jwtService.isTokenValid(token, otherUser));
    }

    @Test
    void extractClaim_shouldThrowExceptionForInvalidToken() {
        String invalidToken = "invalid.token";
        assertThrows(Exception.class, () -> jwtService.extractClaim(invalidToken, Claims::getSubject));
    }

    @Test
    void extractExpiration_shouldReturnDateAfterNow() {
        String token = jwtService.generateToken(user);
        var expiration = TestUtils.invokePrivateMethod(jwtService, "extractExpiration", token);

        assertNotNull(expiration);
        assertTrue(expiration instanceof Date);
        assertTrue(((Date) expiration).after(new Date()));
    }
}
