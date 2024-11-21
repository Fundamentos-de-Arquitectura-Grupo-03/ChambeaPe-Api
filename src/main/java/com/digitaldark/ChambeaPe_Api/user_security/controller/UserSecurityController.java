package com.digitaldark.ChambeaPe_Api.user_security.controller;

import com.digitaldark.ChambeaPe_Api.security.model.dto.response.ApiResponse;
import com.digitaldark.ChambeaPe_Api.user_security.model.dto.UserResponseDto;
import com.digitaldark.ChambeaPe_Api.user_security.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for users
 * This class is used to handle the requests for users
 * @version 1.0, 06/10/2021
 * @autor Ray Del Carmen
 */

@Tag(name = "User_Security")
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
@RestController
public class UserSecurityController {
    private final IUserService userService;

    public UserSecurityController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Obtiene el perfil de un usuario")
    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> profile(@PathVariable Long userId) {
        var res = userService.profile(userId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Elimina un usuario (ADMIN)")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse<Object>> deleteById(@PathVariable Long userId) {
        var res = userService.deleteById(userId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

