package com.digitaldark.ChambeaPe_Api.security.service;

import com.digitaldark.ChambeaPe_Api.security.model.dto.request.LoginRequestDto;
import com.digitaldark.ChambeaPe_Api.security.model.dto.request.RegisterRequestDto;
import com.digitaldark.ChambeaPe_Api.security.model.dto.response.ApiResponse;
import com.digitaldark.ChambeaPe_Api.security.model.dto.response.RegisteredUserResponseDto;
import com.digitaldark.ChambeaPe_Api.security.model.dto.response.TokenResponseDto;

/**
 * Service for user authentication and registration
 * @author Ray Del Carmen
 */
public interface IAuthService {
    /**
     * Registra un usuario
     * @param request Datos para el registro
     * @return Usuario registrado
     */
    ApiResponse<RegisteredUserResponseDto> registerUser(RegisterRequestDto request, String role);

    /**
     * Realiza el login del usuario
     * @param request Credenciales
     * @return Token de acceso
     */
    ApiResponse<TokenResponseDto> login(LoginRequestDto request);
}
