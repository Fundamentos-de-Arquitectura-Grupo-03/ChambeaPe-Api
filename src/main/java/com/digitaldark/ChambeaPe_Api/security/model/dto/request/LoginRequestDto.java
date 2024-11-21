package com.digitaldark.ChambeaPe_Api.security.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class that represents the API request body when a user logs in and the access token is returned to him/her
 * @author Ray Del Carmen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "El nombre de usuario o email es requerido")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 3, message = "La contraseña debe tener por lo menos 3 caracteres")
    private String password;
}
