package com.digitaldark.ChambeaPe_Api.user_security.service;

import com.digitaldark.ChambeaPe_Api.security.model.dto.response.ApiResponse;
import com.digitaldark.ChambeaPe_Api.user_security.model.dto.UserResponseDto;

/**
 * Servicio para operaciones con usuarios
 * @author Ray Del Carmen
 */

public interface IUserService {
    /**
     * Obtiene los datos de un usuario por su id
     * @param userId id del usuario
     * @return Datos del usuario
     */
    ApiResponse<UserResponseDto> profile(Long userId);

    /**
     * Elimina un usuario por su id
     * @param userId id del usuario
     * @return Respuesta de la operación
     */
    ApiResponse<Object> deleteById(Long userId);
}
