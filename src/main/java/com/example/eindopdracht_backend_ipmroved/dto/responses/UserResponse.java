package com.example.eindopdracht_backend_ipmroved.dto.responses;

import com.example.eindopdracht_backend_ipmroved.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Data
@AllArgsConstructor
@Builder
public class UserResponse {

    @JsonProperty("username")
    private String username;

    @JsonProperty("role")
    private String role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("createdAt")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("updatedAt")
    private Date updatedAt;


    public static UserResponse from(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}



