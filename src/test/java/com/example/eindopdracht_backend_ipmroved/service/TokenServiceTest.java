package com.example.eindopdracht_backend_ipmroved.service;

import com.example.eindopdracht_backend_ipmroved.models.Token;
import com.example.eindopdracht_backend_ipmroved.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    private Token token1;
    private Token token2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        token1 = Token.builder()
                .id(1)
                .token("token1")
                .expired(false)
                .revoked(false)
                .build();

        token2 = Token.builder()
                .id(2)
                .token("token2")
                .expired(false)
                .revoked(false)
                .build();
    }

    @Test
    void getAllTokens_returnsList() {
        when(tokenRepository.findAll()).thenReturn(Arrays.asList(token1, token2));

        List<Token> tokens = tokenService.getAllTokens();

        assertEquals(2, tokens.size());
        verify(tokenRepository, times(1)).findAll();
    }

    @Test
    void getTokenById_returnsToken() {
        when(tokenRepository.findById(1L)).thenReturn(Optional.of(token1));

        Optional<Token> found = tokenService.getTokenById(1L);

        assertTrue(found.isPresent());
        assertEquals("token1", found.get().getToken());
        verify(tokenRepository).findById(1L);
    }

    @Test
    void getTokenByTokenValue_returnsToken() {
        when(tokenRepository.findByToken("token1")).thenReturn(Optional.of(token1));

        Optional<Token> found = tokenService.getTokenByTokenValue("token1");

        assertTrue(found.isPresent());
        assertEquals(1, found.get().getId());
        verify(tokenRepository).findByToken("token1");
    }

    @Test
    void saveToken_savesAndReturnsToken() {
        when(tokenRepository.save(token1)).thenReturn(token1);

        Token saved = tokenService.saveToken(token1);

        assertNotNull(saved);
        assertEquals("token1", saved.getToken());
        verify(tokenRepository).save(token1);
    }

    @Test
    void revokeAllUserTokens_revokesTokens() {
        token1.setUser(null); // user is needed, but we mock repo anyway
        token2.setUser(null);
        when(tokenRepository.findAllValidTokenByUser(1)).thenReturn(Arrays.asList(token1, token2));

        tokenService.revokeAllUserTokens(1);

        assertTrue(token1.isRevoked());
        assertTrue(token1.isExpired());
        assertTrue(token2.isRevoked());
        assertTrue(token2.isExpired());

        verify(tokenRepository, times(2)).save(any(Token.class));
    }

    @Test
    void revokeToken_revokesSingleToken() {
        when(tokenRepository.findByToken("token1")).thenReturn(Optional.of(token1));

        tokenService.revokeToken("token1");

        assertTrue(token1.isRevoked());
        assertTrue(token1.isExpired());
        verify(tokenRepository).save(token1);
    }

    @Test
    void revokeToken_doesNothingIfTokenNotFound() {
        when(tokenRepository.findByToken("unknown")).thenReturn(Optional.empty());

        tokenService.revokeToken("unknown");

        verify(tokenRepository, never()).save(any(Token.class));
    }
}
