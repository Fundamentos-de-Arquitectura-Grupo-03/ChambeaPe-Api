package com.digitaldark.ChambeaPe_Api.security.model.dto.response;

import com.digitaldark.ChambeaPe_Api.user_security.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Class that represents the API response when a user is registered
 * @author Ray Del Carmen
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredUserResponseDto {
    private Long userId;
    private String email;
    private Set<Role> roles = new HashSet<>();
}