package com.example.eindopdracht_backend_ipmroved.service;

import com.example.eindopdracht_backend_ipmroved.models.Token;
import com.example.eindopdracht_backend_ipmroved.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public List<Token> getAllTokens() {
        return tokenRepository.findAll();
    }

    public Optional<Token> getTokenById(Long id) {
        return tokenRepository.findById(id);
    }

    public Optional<Token> getTokenByTokenValue(String tokenValue) {
        return tokenRepository.findByToken(tokenValue);
    }

    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    // Nieuwe method om alle tokens van een gebruiker te revoken en te markeren als expired
    public void revokeAllUserTokens(Integer userId) {
        List<Token> validTokens = tokenRepository.findAllValidTokenByUser(userId);
        for (Token token : validTokens) {
            token.setRevoked(true);
            token.setExpired(true);
            tokenRepository.save(token);
        }
    }

    // Nieuwe method om één token te revoken en te markeren als expired
    public void revokeToken(String tokenValue) {
        Optional<Token> tokenOpt = tokenRepository.findByToken(tokenValue);
        tokenOpt.ifPresent(token -> {
            token.setRevoked(true);
            token.setExpired(true);
            tokenRepository.save(token);
        });
    }
}
