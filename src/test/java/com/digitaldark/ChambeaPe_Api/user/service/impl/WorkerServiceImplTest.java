package com.digitaldark.ChambeaPe_Api.user.service.impl;

import com.digitaldark.ChambeaPe_Api.email.service.IEmailService;
import com.digitaldark.ChambeaPe_Api.shared.exception.ResourceNotFoundException;
import com.digitaldark.ChambeaPe_Api.shared.exception.ValidationException;
import com.digitaldark.ChambeaPe_Api.user.dto.WorkerDTO;
import com.digitaldark.ChambeaPe_Api.user.model.WorkerEntity;
import com.digitaldark.ChambeaPe_Api.user.repository.UserRepository;
import com.digitaldark.ChambeaPe_Api.user.repository.WorkerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkerServiceImplTest {

  @Mock
  private WorkerRepository workerRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private IEmailService emailService;

  @InjectMocks
  private WorkerServiceImpl workerService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createWorker() {
    // Arrange
    WorkerEntity worker = new WorkerEntity();
    when(workerRepository.save(worker)).thenReturn(worker);

    // Act
    WorkerEntity result = workerService.createWorker(worker);

    // Assert
    assertEquals(worker, result);
    verify(workerRepository, times(1)).save(worker);
  }

  @Test
  void getAllWorkers_v1() {
    // Arrange
    WorkerEntity worker1 = new WorkerEntity();
    WorkerEntity worker2 = new WorkerEntity();
    when(workerRepository.findAll()).thenReturn(Arrays.asList(worker1, worker2));

    // Act
    List<WorkerEntity> result = workerService.getAllWorkers_v1();

    // Assert
    assertEquals(2, result.size());
    verify(workerRepository, times(1)).findAll();
  }

  @Test
  void getAllWorkers() {
    // Arrange
    WorkerEntity workerEntity = new WorkerEntity();
    WorkerDTO workerDTO = new WorkerDTO();
    when(workerRepository.findAll()).thenReturn(Arrays.asList(workerEntity));
    when(modelMapper.map(workerEntity, WorkerDTO.class)).thenReturn(workerDTO);

    // Act
    List<WorkerDTO> result = workerService.getAllWorkers();

    // Assert
    assertEquals(1, result.size());
    verify(workerRepository, times(1)).findAll();
  }

  @Test
  void getWorkerById() {
    // Arrange
    int workerId = 1;
    WorkerEntity workerEntity = new WorkerEntity();
    WorkerDTO workerDTO = new WorkerDTO();

    when(workerRepository.existsById(workerId)).thenReturn(true);
    when(workerRepository.findById(workerId)).thenReturn(workerEntity);
    when(modelMapper.map(workerEntity, WorkerDTO.class)).thenReturn(workerDTO);

    // Act
    WorkerDTO result = workerService.getWorkerById(workerId);

    // Assert
    assertEquals(workerDTO, result);
    verify(workerRepository, times(1)).findById(workerId);
  }

  @Test
  void getWorkerByIdThrowsExceptionWhenNotFound() {
    // Arrange
    int workerId = 1;
    when(workerRepository.existsById(workerId)).thenReturn(false);

    // Act & Assert
    assertThrows(ResourceNotFoundException.class, () -> workerService.getWorkerById(workerId));
    verify(workerRepository, never()).findById(workerId);
  }

  @Test
  void deleteWorker() {
    // Arrange
    int workerId = 1;
    when(workerRepository.existsById(workerId)).thenReturn(true);

    // Act
    workerService.deleteWorker(workerId);

    // Assert
    verify(workerRepository, times(1)).deleteById(workerId);
    verify(userRepository, times(1)).deleteById(workerId);
  }

  @Test
  void deleteWorkerThrowsExceptionWhenNotFound() {
    // Arrange
    int workerId = 1;
    when(workerRepository.existsById(workerId)).thenReturn(false);

    // Act & Assert
    assertThrows(ValidationException.class, () -> workerService.deleteWorker(workerId));
    verify(workerRepository, never()).deleteById(workerId);
  }

  @Test
  void validarWorkerDTOThrowsExceptionWhenInvalid() {
    // Arrange
    WorkerDTO workerDTO = new WorkerDTO(); // DTO vacío para provocar fallo en la validación

    // Act & Assert
    assertThrows(ValidationException.class, () -> workerService.validarWorkerDTO(workerDTO));
  }
}
