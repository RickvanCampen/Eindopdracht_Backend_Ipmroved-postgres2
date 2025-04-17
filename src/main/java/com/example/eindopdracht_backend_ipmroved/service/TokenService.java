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

    public Optional<Token> getTokenById(Long id) { // Verander int naar Long
        return tokenRepository.findById(id);
    }

    public Optional<Token> getTokenByTokenValue(String tokenValue) {
        return tokenRepository.findByToken(tokenValue);
    }

    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }
}
