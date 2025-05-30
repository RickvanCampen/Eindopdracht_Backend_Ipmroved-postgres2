package com.example.eindopdracht_backend_ipmroved.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {
    @JsonProperty("status")
    private final int status;
    @JsonProperty("message")
    private final String message;
    @JsonProperty("timestamp")
    private final LocalDateTime timestamp;

    public ErrorResponse(int status, String message, Date timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}


