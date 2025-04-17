package com.example.eindopdracht_backend_ipmroved.service;

import com.example.eindopdracht_backend_ipmroved.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository repository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        // If the token is not present or does not start with the Bearer prefix, the method returns without anything
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        // If the token is present, the method retrieves it from the tokenRepository
        // and sets its expired and revoked properties to true
        var storedToken = repository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            // The modified token is saved to the tokenRepository
            repository.save(storedToken);
            // The users security context is cleared using the SecurityContextHolder
            SecurityContextHolder.clearContext();
        }
    }
}