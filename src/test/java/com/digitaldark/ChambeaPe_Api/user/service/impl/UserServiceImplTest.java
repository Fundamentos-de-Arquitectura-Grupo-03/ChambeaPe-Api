package com.digitaldark.ChambeaPe_Api.user.service.impl;

import com.digitaldark.ChambeaPe_Api.email.service.IEmailService;
import com.digitaldark.ChambeaPe_Api.user.dto.response.UserResponseDTO;
import com.digitaldark.ChambeaPe_Api.user.model.EmployerEntity;
import com.digitaldark.ChambeaPe_Api.user.model.UsersEntity;
import com.digitaldark.ChambeaPe_Api.user.repository.UserRepository;
import com.digitaldark.ChambeaPe_Api.user.service.EmployerService;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
  @Mock
  private UserRepository userRepository;

  @Mock
  private EmployerService employerService;

  @Mock
  private IEmailService emailService;

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private ModelMapper modelMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createUser() {
    // Arrange
    UsersEntity user = new UsersEntity();
    user.setId(1);
    user.setFirstName("Ruben");
    user.setLastName("Gonzales");
    user.setDateCreated(new Timestamp(System.currentTimeMillis()));
    user.setDateUpdated(new Timestamp(System.currentTimeMillis()));
    user.setEmail("ruben@test.com");
    user.setPhoneNumber("123456");
    user.setUserRole("E");
    user.setGender("M");

    EmployerEntity employer = new EmployerEntity();
    employer.setIsActive((byte) 1);
    employer.setDateCreated(new Timestamp(System.currentTimeMillis()));
    employer.setDateUpdated(new Timestamp(System.currentTimeMillis()));

    when(userRepository.existsById(user.getId())).thenReturn(false);
    when(userRepository.existsByEmailOrPhoneNumber(user.getEmail(), user.getPhoneNumber())).thenReturn(false);
    when(modelMapper.map(user, EmployerEntity.class)).thenReturn(employer);

    // Act
    try {
      userService.createUser(user);
    } catch (MessagingException | IOException e) {
      fail("Unexpected exception: " + e.getMessage());
    }

    // Assert
    verify(userRepository, times(1)).save(user);
    verify(employerService, times(1)).createEmployer(any(EmployerEntity.class));
    try {
      verify(emailService, times(1)).userRegistered(user.getEmail());
    } catch (MessagingException | IOException e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  void getUser() {
    // Arrange
    int userId = 1;
    UsersEntity user = new UsersEntity();
    user.setId(userId);
    user.setFirstName("Ruben");
    user.setLastName("Gonzales");
    user.setEmail("ruben@prueba.com");

    UserResponseDTO userResponseDTO = new UserResponseDTO(userId, user.getFirstName(), user.getLastName(), user.getEmail(),
        user.getPhoneNumber(), user.getBirthdate(), user.getGender(), user.getDni(), user.getProfilePic(), user.getDescription(), user.getUserRole(),
    user.getHasPremium());

    when(userRepository.existsById(userId)).thenReturn(true);
    when(userRepository.findById(userId)).thenReturn(user);
    when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);

    // Act
    UserResponseDTO result = userService.getUser(userId);

    // Assert
    assertEquals(userId, result.getId());
    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  void getAllUsers() {
    // Arrange
    UsersEntity user1 = new UsersEntity();
    user1.setId(1);
    user1.setFirstName("Ruben");
    user1.setLastName("Gonzales");
    user1.setEmail("ruben@test.com");

    UsersEntity user2 = new UsersEntity();
    user2.setId(2);
    user2.setFirstName("Maria");
    user2.setLastName("Perez");
    user2.setEmail("maria@test.com");

    List<UsersEntity> users = Arrays.asList(user1, user2);

    when(userRepository.findAll()).thenReturn(users);

    // Act
    List<UserResponseDTO> result = userService.getAllUsers();

    // Assert
    assertEquals(2, result.size());
    verify(userRepository, times(1)).findAll();
  }

  @Test
  void deleteUser() {
    // Arrange
    int userId = 1;

    // Act
    userService.deleteUser(userId);

    // Assert
    verify(userRepository, times(1)).deleteById(userId);
  }

  @Test
  void updateUser() {
    // Arrange
    int userId = 1;
    UsersEntity user = new UsersEntity();
    user.setId(userId);
    user.setFirstName("UpdatedName");
    user.setEmail("updated@test.com");

    when(userRepository.existsById(userId)).thenReturn(true);

    // Act
    userService.updateUser(userId, user);

    // Assert
    verify(userRepository, times(1)).save(user);
  }
}