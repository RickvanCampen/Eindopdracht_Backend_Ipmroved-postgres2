package com.example.eindopdracht_backend_ipmroved.dto.requests;

import com.example.eindopdracht_backend_ipmroved.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRoleRequest {

    @NotNull(message = "Role mag niet leeg zijn.")
    private Role role;
}
