package com.example.eindopdracht_backend_ipmroved.dto.requests;

import com.example.eindopdracht_backend_ipmroved.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRoleRequest {
    private Role role;  // De rol als een Role type
}
