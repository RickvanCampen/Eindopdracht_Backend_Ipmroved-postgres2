package com.example.eindopdracht_backend_ipmroved.exceptions;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
    private final HttpStatus status;

    // Constructor om zowel het bericht als de status in te stellen
    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    // Constructor voor het doorgeven van zowel het bericht als de oorzaak
    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;  // Standaard status als er geen wordt meegegeven
    }

    // Methode om de HTTP-status op te halen
    public HttpStatus getStatus() {
        return status;
    }
}
