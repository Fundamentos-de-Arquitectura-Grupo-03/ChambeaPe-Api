package com.digitaldark.ChambeaPe_Api.email.service.impl;

import com.digitaldark.ChambeaPe_Api.email.util.OtpUtil;
import com.digitaldark.ChambeaPe_Api.shared.DateTimeEntity;
import com.digitaldark.ChambeaPe_Api.shared.exception.ValidationException;
import com.digitaldark.ChambeaPe_Api.user.dto.response.UserResponseDTO;
import com.digitaldark.ChambeaPe_Api.user.model.UsersEntity;
import com.digitaldark.ChambeaPe_Api.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

  @Mock
  private JavaMailSender mailSender;

  @Mock
  private OtpUtil otpUtil;

  @Mock
  private UserRepository userRepository;

  @Mock
  private DateTimeEntity dateTimeEntity;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private EmailServiceImpl emailService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void sendEmail() throws NoSuchFieldException, IllegalAccessException {
    // Arrange
    String[] toUser = {"test@test.com"};
    String subject = "Test Subject";
    String messageContent = "Test Message";

    // Usa reflexión para configurar el valor de emailAccount
    Field emailAccountField = EmailServiceImpl.class.getDeclaredField("emailAccount");
    emailAccountField.setAccessible(true);
    emailAccountField.set(emailService, "no-reply@chambeape.com");

    // Act
    emailService.sendEmail(toUser, subject, messageContent);

    // Assert
    verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
  }


  @Test
  void userRegistered() throws MessagingException, IOException, NoSuchFieldException, IllegalAccessException {
    // Arrange
    String toUser = "test@test.com";
    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

    Field emailAccountField = EmailServiceImpl.class.getDeclaredField("emailAccount");
    emailAccountField.setAccessible(true);
    emailAccountField.set(emailService, "no-reply@chambeape.com");

    // Act
    emailService.userRegistered(toUser);

    // Assert
    verify(mailSender, times(1)).send(mimeMessage);
  }


  @Test
  void userModified() throws MessagingException, NoSuchFieldException, IllegalAccessException {
    // Arrange
    String toUser = "test@test.com";
    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

    Field emailAccountField = EmailServiceImpl.class.getDeclaredField("emailAccount");
    emailAccountField.setAccessible(true);
    emailAccountField.set(emailService, "no-reply@chambeape.com");

    // Act
    emailService.userModified(toUser);

    // Assert
    verify(mailSender, times(1)).send(mimeMessage);
  }

  @Test
  void generateCodeOtp() throws MessagingException, NoSuchFieldException, IllegalAccessException {
    // Arrange
    String toUser = "test@test.com";
    String generatedOtp = "123456";
    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    when(otpUtil.generateOtp()).thenReturn(generatedOtp);
    UsersEntity user = new UsersEntity();
    when(userRepository.existsByEmail(toUser)).thenReturn(true);
    when(userRepository.findByEmail(toUser)).thenReturn(user);
    when(dateTimeEntity.currentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));

    Field emailAccountField = EmailServiceImpl.class.getDeclaredField("emailAccount");
    emailAccountField.setAccessible(true);
    emailAccountField.set(emailService, "no-reply@chambeape.com");

    // Act
    emailService.generateCodeOtp(toUser);

    // Assert
    assertEquals(generatedOtp, user.getOtp());
    verify(mailSender, times(1)).send(mimeMessage);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void validateOtpInChangePassword() {
    // Arrange
    String email = "test@test.com";
    String otp = "123456";
    UsersEntity user = new UsersEntity();
    user.setOtp(otp);
    user.setOtpGeneratedTime(Timestamp.valueOf("2024-09-25 13:50:00").toLocalDateTime());

    UserResponseDTO userResponse = new UserResponseDTO();
    userResponse.setId(1);
    userResponse.setFirstName("Test");
    userResponse.setLastName("User");
    userResponse.setEmail(email);
    userResponse.setPhoneNumber("123456789");

    // Mocks
    when(userRepository.findByEmail(email)).thenReturn(user);
    when(dateTimeEntity.currentTime()).thenReturn(Timestamp.valueOf("2024-09-25 13:53:00"));
    when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponse);

    // Act
    UserResponseDTO result = emailService.validateOtpInChangePassword(email, otp);

    // Assert
    assertNotNull(result);
    assertEquals(userResponse.getId(), result.getId());
    verify(modelMapper, times(1)).map(user, UserResponseDTO.class);
  }



  @Test
  void validateOtpInChangePassword_invalidOtp() {
    // Arrange
    String email = "test@test.com";
    String otp = "654321";
    UsersEntity user = new UsersEntity();
    user.setOtp("123456");
    when(userRepository.findByEmail(email)).thenReturn(user);

    // Act & Assert
    assertThrows(ValidationException.class, () -> emailService.validateOtpInChangePassword(email, otp));
  }

  @Test
  void validateOtpInChangePassword_expiredOtp() {
    // Arrange
    String email = "test@test.com";
    String otp = "123456";
    UsersEntity user = new UsersEntity();
    user.setOtp(otp);
    user.setOtpGeneratedTime(Timestamp.valueOf("2024-09-25 10:00:00").toLocalDateTime());
    when(userRepository.findByEmail(email)).thenReturn(user);
    when(dateTimeEntity.currentTime()).thenReturn(new Timestamp(System.currentTimeMillis() + 1000 * 60 * 5)); // OTP expired

    // Act & Assert
    assertThrows(ValidationException.class, () -> emailService.validateOtpInChangePassword(email, otp));
  }
}
