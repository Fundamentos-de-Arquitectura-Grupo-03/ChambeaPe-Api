package com.digitaldark.ChambeaPe_Api.user.service.impl;

import com.digitaldark.ChambeaPe_Api.email.service.IEmailService;
import com.digitaldark.ChambeaPe_Api.user.dto.EmployerDTO;
import com.digitaldark.ChambeaPe_Api.user.model.EmployerEntity;
import com.digitaldark.ChambeaPe_Api.user.repository.EmployerRepository;
import com.digitaldark.ChambeaPe_Api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import java.util.Arrays;
import java.util.List;
import org.modelmapper.ValidationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployerServiceImplTest {

  @Mock
  private EmployerRepository employerRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private IEmailService emailService;

  @InjectMocks
  private EmployerServiceImpl employerService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createEmployer() {
    EmployerEntity employer = new EmployerEntity();
    when(employerRepository.save(employer)).thenReturn(employer);

    EmployerEntity result = employerService.createEmployer(employer);

    assertEquals(employer, result);
    verify(employerRepository, times(1)).save(employer);
  }

  @Test
  void getAllEmployers_v1() {
    EmployerEntity employer1 = new EmployerEntity();
    EmployerEntity employer2 = new EmployerEntity();
    when(employerRepository.findAll()).thenReturn(Arrays.asList(employer1, employer2));

    List<EmployerEntity> result = employerService.getAllEmployers_v1();

    assertEquals(2, result.size());
    verify(employerRepository, times(1)).findAll();
  }

  @Test
  void getAllEmployers() {
    EmployerEntity employerEntity = new EmployerEntity();
    EmployerDTO employerDTO = new EmployerDTO();
    when(employerRepository.findAll()).thenReturn(Arrays.asList(employerEntity));
    when(modelMapper.map(any(EmployerEntity.class), eq(EmployerDTO.class))).thenReturn(employerDTO);

    List<EmployerDTO> result = employerService.getAllEmployers();

    assertEquals(1, result.size());
    verify(employerRepository, times(1)).findAll();
  }

  @Test
  void getEmployerById() {
    int employerId = 1;
    EmployerEntity employerEntity = new EmployerEntity();
    EmployerDTO employerDTO = new EmployerDTO();

    when(employerRepository.existsById(employerId)).thenReturn(true);
    when(employerRepository.findById(employerId)).thenReturn(employerEntity);
    when(modelMapper.map(employerEntity, EmployerDTO.class)).thenReturn(employerDTO);

    EmployerDTO result = employerService.getEmployerById(employerId);

    assertEquals(employerDTO, result);
    verify(employerRepository, times(1)).findById(employerId);
  }

  @Test
  void getEmployerByIdThrowsExceptionWhenNotFound() {
    int employerId = 1;

    when(employerRepository.existsById(employerId)).thenReturn(false);

    assertThrows(ValidationException.class, () -> employerService.getEmployerById(employerId));
    verify(employerRepository, never()).findById(employerId);
  }

  @Test
  void deleteEmployer() {
    int employerId = 1;
    when(employerRepository.existsById(employerId)).thenReturn(true);

    employerService.deleteEmployer(employerId);

    verify(employerRepository, times(1)).deleteById(employerId);
    verify(userRepository, times(1)).deleteById(employerId);
  }

  @Test
  void deleteEmployerThrowsExceptionWhenNotFound() {
    int employerId = 1;

    when(employerRepository.existsById(employerId)).thenReturn(false);

    assertThrows(ValidationException.class, () -> employerService.deleteEmployer(employerId));
    verify(employerRepository, never()).deleteById(employerId);
  }

}