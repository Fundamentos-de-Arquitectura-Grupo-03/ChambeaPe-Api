package com.digitaldark.ChambeaPe_Api.security.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class that represents the API response when a user logs in and the access token is returned to him/her
 * @author Ray Del Carmen
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {
    private String token;
}
