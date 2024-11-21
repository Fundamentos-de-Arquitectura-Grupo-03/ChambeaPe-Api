package com.digitaldark.ChambeaPe_Api.security.controller;


import com.digitaldark.ChambeaPe_Api.security.model.dto.request.LoginRequestDto;
import com.digitaldark.ChambeaPe_Api.security.model.dto.request.RegisterRequestDto;
import com.digitaldark.ChambeaPe_Api.security.model.dto.response.ApiResponse;
import com.digitaldark.ChambeaPe_Api.security.model.dto.response.RegisteredUserResponseDto;
import com.digitaldark.ChambeaPe_Api.security.model.dto.response.TokenResponseDto;
import com.digitaldark.ChambeaPe_Api.security.service.AuthService;
import com.digitaldark.ChambeaPe_Api.security.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for user authentication and registration
 * @author Ray Del Carmen
 */
@Tag(name = "Auth")
@SecurityRequirements //Desactive the security for this controller (swagger)
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@RestController
public class AuthController {
    private final IAuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    /**
     * Inicia sesión
     * @param request Datos para iniciar sesión
     * @return Token de acceso
     */
    @Operation(summary = "Inicia sesión")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDto>> login(@Valid @RequestBody LoginRequestDto request) {
        var res = service.login(request);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Registra un nuevo usuario
     * @param request Datos para el registro
     * @return Usuario registrado
     */
    @Operation(summary = "Registra un nuevo usuario")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisteredUserResponseDto>> registerUser(@Valid @RequestBody RegisterRequestDto request, String role) {
        var res = service.registerUser(request, role);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
