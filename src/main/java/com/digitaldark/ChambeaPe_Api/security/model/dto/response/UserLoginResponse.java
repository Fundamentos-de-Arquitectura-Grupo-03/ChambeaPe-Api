package com.digitaldark.ChambeaPe_Api.security.model.dto.response;

import com.digitaldark.ChambeaPe_Api.user.dto.response.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {
    private UserResponseDTO user;
    private TokenResponseDto token;
}
