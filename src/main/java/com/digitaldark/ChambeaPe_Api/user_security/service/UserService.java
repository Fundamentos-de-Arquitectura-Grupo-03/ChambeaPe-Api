package com.digitaldark.ChambeaPe_Api.user_security.service;

import com.digitaldark.ChambeaPe_Api.security.model.dto.enums.EStatus;
import com.digitaldark.ChambeaPe_Api.security.model.dto.response.ApiResponse;
import com.digitaldark.ChambeaPe_Api.shared.exception.ResourceNotFoundException;
import com.digitaldark.ChambeaPe_Api.user_security.model.dto.UserResponseDto;
import com.digitaldark.ChambeaPe_Api.user_security.repository.IUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Servicies for users
 * @author Ray Del Carmen
 */

@Service
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(IUserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse<UserResponseDto> profile(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id " + userId));

        //It maps the user to a DTO
        var userDto = modelMapper.map(user, UserResponseDto.class);

        return new ApiResponse<>("Usuario encontrado", EStatus.SUCCESS, userDto);
    }

    @Override
    public ApiResponse<Object> deleteById(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con id " + userId));

        //Delete user
        userRepository.delete(user);

        return new ApiResponse<>("Usuario eliminado correctamente", EStatus.SUCCESS, null);
    }
}
