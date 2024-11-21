package com.digitaldark.ChambeaPe_Api.contract.service.impl;
import org.modelmapper.config.Configuration;

import com.digitaldark.ChambeaPe_Api.contract.dto.request.ContractRequestDTO;
import com.digitaldark.ChambeaPe_Api.contract.dto.response.ContractResponseDTO;
import com.digitaldark.ChambeaPe_Api.contract.model.ContractEntity;
import com.digitaldark.ChambeaPe_Api.contract.repository.ContractRepository;
import com.digitaldark.ChambeaPe_Api.post.model.PostsEntity;
import com.digitaldark.ChambeaPe_Api.post.repository.PostRepository;
import com.digitaldark.ChambeaPe_Api.shared.DateTimeEntity;
import com.digitaldark.ChambeaPe_Api.user.repository.EmployerRepository;
import com.digitaldark.ChambeaPe_Api.user.repository.WorkerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractServiceImplTest {

  @Mock
  private ContractRepository contractRepository;

  @Mock
  private PostRepository postRepository;

  @Mock
  private WorkerRepository workerRepository;

  @Mock
  private EmployerRepository employerRepository;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private DateTimeEntity dateTimeEntity;

  @InjectMocks
  private ContractServiceImpl contractService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createContract() {
    // Arrange
    ContractRequestDTO contractRequest = new ContractRequestDTO();
    contractRequest.setPostId(1);
    contractRequest.setState("PENDING");
    contractRequest.setWorkerId(1);
    contractRequest.setEmployerId(1);
    contractRequest.setSalary(1000.0);
    contractRequest.setStartDay(Timestamp.valueOf("2024-09-25 10:00:00"));
    contractRequest.setEndDay(Timestamp.valueOf("2024-09-30 18:00:00"));

    PostsEntity postEntity = new PostsEntity();
    postEntity.setId(1);

    // Mocking ModelMapper and its Configuration
    Configuration mockConfiguration = mock(Configuration.class);
    when(modelMapper.getConfiguration()).thenReturn(mockConfiguration);
    when(mockConfiguration.setAmbiguityIgnored(true)).thenReturn(mockConfiguration);

    // Simulaciones para repositorios y DateTimeEntity
    when(postRepository.existsById(1)).thenReturn(true);
    when(postRepository.findById(1)).thenReturn(postEntity);
    when(contractRepository.existsByPost(postEntity)).thenReturn(false);
    when(workerRepository.existsById(contractRequest.getWorkerId())).thenReturn(true);
    when(employerRepository.existsById(contractRequest.getEmployerId())).thenReturn(true);
    when(dateTimeEntity.currentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));

    // Simulación del mapeo de objetos
    ContractEntity contractEntity = new ContractEntity();
    when(modelMapper.map(contractRequest, ContractEntity.class)).thenReturn(contractEntity);
    when(modelMapper.map(contractEntity, ContractResponseDTO.class)).thenReturn(new ContractResponseDTO());

    // Act
    ContractResponseDTO result = contractService.createContract(contractRequest);

    // Assert
    assertNotNull(result);
    verify(contractRepository, times(1)).save(any(ContractEntity.class));
  }


  @Test
  void deleteContract() {
    // Arrange
    int contractId = 1;
    when(contractRepository.existsById(contractId)).thenReturn(true);

    // Act
    contractService.deleteContract(contractId);

    // Assert
    verify(contractRepository, times(1)).deleteById(contractId);
  }

  @Test
  void updateContract() {
    // Arrange
    int contractId = 1;
    ContractRequestDTO contractRequest = new ContractRequestDTO();
    contractRequest.setState("PENDING");
    contractRequest.setWorkerId(1);
    contractRequest.setEmployerId(1);
    contractRequest.setPostId(1);
    contractRequest.setSalary(1000.0);
    contractRequest.setStartDay(Timestamp.valueOf("2024-09-25 10:00:00"));
    contractRequest.setEndDay(Timestamp.valueOf("2024-09-30 18:00:00"));

    ContractEntity contractEntity = new ContractEntity();

    when(contractRepository.existsById(contractId)).thenReturn(true);
    when(contractRepository.findById(contractId)).thenReturn(contractEntity);
    when(workerRepository.existsById(contractRequest.getWorkerId())).thenReturn(true);
    when(employerRepository.existsById(contractRequest.getEmployerId())).thenReturn(true);
    when(postRepository.existsById(contractRequest.getPostId())).thenReturn(true);
    when(dateTimeEntity.currentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));

    when(modelMapper.getConfiguration()).thenReturn(new ModelMapper().getConfiguration());

    // Act
    ContractResponseDTO result = contractService.updateContract(contractId, contractRequest);

    // Assert
    assertEquals(result, modelMapper.map(contractEntity, ContractResponseDTO.class));
    verify(contractRepository, times(1)).save(contractEntity);
  }



  @Test
  void getContractByWorkerIdAndEmployerId() {
    // Arrange
    int workerId = 1;
    int employerId = 1;

    List<ContractEntity> contractEntities = new ArrayList<>();
    contractEntities.add(new ContractEntity());

    when(workerRepository.existsById(workerId)).thenReturn(true);
    when(employerRepository.existsById(employerId)).thenReturn(true);
    when(contractRepository.findByWorkerIdAndEmployerId(workerId, employerId)).thenReturn(contractEntities);

    // Act
    List<ContractResponseDTO> result = contractService.getContractByWorkerIdAndEmployerId(workerId, employerId);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void getAllContractsByUserId() {
    // Arrange
    int userId = 1;
    List<ContractResponseDTO> contractResponseDTOList = new ArrayList<>();

    when(workerRepository.existsById(userId)).thenReturn(true);
    when(contractRepository.findByAllByUserId(userId)).thenReturn(contractResponseDTOList);

    // Act
    List<ContractResponseDTO> result = contractService.getAllContractsByUserId(userId);

    // Assert
    assertNotNull(result);
  }

  @Test
  void validateContractRequest() {
    // Arrange
    ContractRequestDTO contractRequest = new ContractRequestDTO();
    contractRequest.setWorkerId(1);
    contractRequest.setEmployerId(1);
    contractRequest.setPostId(1);
    contractRequest.setSalary(1000.0);
    contractRequest.setState("PENDING");

    contractRequest.setStartDay(Timestamp.valueOf("2024-09-25 10:00:00"));
    contractRequest.setEndDay(Timestamp.valueOf("2024-09-30 18:00:00"));

    when(workerRepository.existsById(contractRequest.getWorkerId())).thenReturn(true);
    when(employerRepository.existsById(contractRequest.getEmployerId())).thenReturn(true);
    when(postRepository.existsById(contractRequest.getPostId())).thenReturn(true);

    // Act & Assert
    assertDoesNotThrow(() -> contractService.validateContractRequest(contractRequest));
  }

}
