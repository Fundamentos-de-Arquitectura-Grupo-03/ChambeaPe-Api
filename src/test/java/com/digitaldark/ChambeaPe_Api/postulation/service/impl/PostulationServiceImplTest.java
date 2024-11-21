package com.digitaldark.ChambeaPe_Api.postulation.service.impl;

import com.digitaldark.ChambeaPe_Api.post.dto.response.PostResponseDTO;
import com.digitaldark.ChambeaPe_Api.post.model.PostsEntity;
import com.digitaldark.ChambeaPe_Api.post.repository.PostRepository;
import com.digitaldark.ChambeaPe_Api.postulation.dto.request.PostulationRequestDTO;
import com.digitaldark.ChambeaPe_Api.postulation.dto.response.PostulationResponseDTO;
import com.digitaldark.ChambeaPe_Api.postulation.dto.response.PostulationWorkerResponseDTO;
import com.digitaldark.ChambeaPe_Api.postulation.model.PostulationsEntity;
import com.digitaldark.ChambeaPe_Api.postulation.repository.PostulationRepository;
import com.digitaldark.ChambeaPe_Api.shared.DateTimeEntity;
import com.digitaldark.ChambeaPe_Api.user.model.EmployerEntity;
import com.digitaldark.ChambeaPe_Api.user.model.WorkerEntity;
import com.digitaldark.ChambeaPe_Api.user.repository.WorkerRepository;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostulationServiceImplTest {

  @InjectMocks
  private PostulationServiceImpl postulationService;

  @Mock
  private PostulationRepository postulationRepository;

  @Mock
  private PostRepository postRepository;

  @Mock
  private WorkerRepository workerRepository;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private DateTimeEntity dateTimeEntity;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllPostulationsByPost() {
    // Arrange
    int postId = 1;
    PostsEntity postEntity = new PostsEntity();
    postEntity.setId(postId);

    WorkerEntity workerEntity = new WorkerEntity();
    workerEntity.setId(1);

    List<PostulationsEntity> postulationEntities = new ArrayList<>();
    PostulationsEntity postulationEntity = new PostulationsEntity();
    postulationEntity.setPost(postEntity);
    postulationEntity.setWorker(workerEntity);
    postulationEntities.add(postulationEntity);

    PostulationResponseDTO postulationResponseDTO = new PostulationResponseDTO();

    when(postRepository.existsById(postId)).thenReturn(true);
    when(postRepository.findById(postId)).thenReturn(postEntity);
    when(postulationRepository.findAllByPost(postEntity)).thenReturn(postulationEntities);
    when(modelMapper.map(any(PostulationsEntity.class), eq(PostulationResponseDTO.class)))
        .thenReturn(postulationResponseDTO);

    // Act
    List<PostulationResponseDTO> result = postulationService.getAllPostulationsByPost(postId);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(postRepository, times(1)).findById(postId);
    verify(postulationRepository, times(1)).findAllByPost(postEntity);
  }


  @Test
  void createPostulation() {
    // Arrange
    int postId = 1;
    int workerId = 1;

    PostsEntity postEntity = new PostsEntity();
    postEntity.setId(postId);

    WorkerEntity workerEntity = new WorkerEntity();
    workerEntity.setId(workerId);

    PostulationRequestDTO postulationRequest = new PostulationRequestDTO();
    PostulationsEntity postulationEntity = new PostulationsEntity();

    PostulationResponseDTO postulationResponseDTO = new PostulationResponseDTO();

    when(postRepository.existsById(postId)).thenReturn(true);
    when(workerRepository.existsById(workerId)).thenReturn(true);
    when(postRepository.findById(postId)).thenReturn(postEntity);
    when(workerRepository.findById(workerId)).thenReturn(workerEntity);
    when(postulationRepository.existsByPostAndWorker(postEntity, workerEntity)).thenReturn(false);
    when(modelMapper.map(any(PostulationRequestDTO.class), eq(PostulationsEntity.class)))
        .thenReturn(postulationEntity);
    when(modelMapper.map(any(PostulationsEntity.class), eq(PostulationResponseDTO.class)))
        .thenReturn(postulationResponseDTO);

    // Act
    PostulationResponseDTO result = postulationService.createPostulation(postId, workerId, postulationRequest);

    // Assert
    assertNotNull(result);
    verify(postulationRepository, times(1)).save(any(PostulationsEntity.class));
  }


  @Test
  void deletePostulation() {
    // Arrange
    int postId = 1;
    int workerId = 1;

    PostsEntity postEntity = new PostsEntity();
    postEntity.setId(postId);

    WorkerEntity workerEntity = new WorkerEntity();
    workerEntity.setId(workerId);

    PostulationsEntity postulationEntity = new PostulationsEntity();
    postulationEntity.setId(1);
    postulationEntity.setPost(postEntity);
    postulationEntity.setWorker(workerEntity);

    when(postRepository.existsById(postId)).thenReturn(true);
    when(workerRepository.existsById(workerId)).thenReturn(true);
    when(postRepository.findById(postId)).thenReturn(postEntity);
    when(workerRepository.findById(workerId)).thenReturn(workerEntity);
    when(postulationRepository.findByPostAndWorker(postEntity, workerEntity)).thenReturn(postulationEntity);

    // Act
    postulationService.deletePostulation(postId, workerId);

    // Assert
    verify(postulationRepository, times(1)).deleteById(postulationEntity.getId());
  }


  @Test
  void updatePostulation() {
    // Arrange
    int postulationId = 1;
    PostulationsEntity postulationEntity = new PostulationsEntity();

    when(postulationRepository.existsById(postulationId)).thenReturn(true);
    when(postulationRepository.findById(postulationId)).thenReturn(postulationEntity);
    when(dateTimeEntity.currentTime()).thenReturn(new java.sql.Timestamp(System.currentTimeMillis()));

    // Act
    postulationService.updatePostulation(postulationId);

    // Assert
    verify(postulationRepository, times(1)).save(postulationEntity);
  }

  @Test
  void getAllPostulationsByWorker() {
    // Arrange
    int workerId = 1;
    WorkerEntity workerEntity = new WorkerEntity();
    workerEntity.setId(workerId);

    EmployerEntity employerEntity = new EmployerEntity();
    employerEntity.setId(1);

    PostsEntity postEntity = new PostsEntity();
    postEntity.setId(1);
    postEntity.setEmployer(employerEntity);

    PostulationsEntity postulationEntity = new PostulationsEntity();
    postulationEntity.setWorker(workerEntity);
    postulationEntity.setPost(postEntity);

    List<PostulationsEntity> postulations = Arrays.asList(postulationEntity);

    PostResponseDTO postResponseDTO = new PostResponseDTO();
    postResponseDTO.setEmployerId(1);

    when(workerRepository.existsById(workerId)).thenReturn(true);
    when(postulationRepository.findAllByWorkerId(workerId)).thenReturn(postulations);
    when(modelMapper.map(any(PostulationsEntity.class), eq(PostulationWorkerResponseDTO.class)))
        .thenReturn(new PostulationWorkerResponseDTO());
    when(modelMapper.map(any(PostsEntity.class), eq(PostResponseDTO.class))).thenReturn(postResponseDTO);

    // Act
    List<PostulationWorkerResponseDTO> result = postulationService.getAllPostulationsByWorker(workerId);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(workerRepository, times(1)).existsById(workerId);
    verify(postulationRepository, times(1)).findAllByWorkerId(workerId);
  }

}
