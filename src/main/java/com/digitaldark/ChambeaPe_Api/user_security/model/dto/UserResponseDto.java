package com.digitaldark.ChambeaPe_Api.user_security.model.dto;

import com.digitaldark.ChambeaPe_Api.user_security.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * DTO for user data response
 * @author Ray Del Carmen
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String email;
    private Set<Role> roles = new HashSet<>();
}

