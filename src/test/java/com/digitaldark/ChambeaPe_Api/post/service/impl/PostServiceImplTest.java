package com.digitaldark.ChambeaPe_Api.post.service.impl;

import com.digitaldark.ChambeaPe_Api.post.dto.request.PostRequestDTO;
import com.digitaldark.ChambeaPe_Api.post.dto.response.PostResponseDTO;
import com.digitaldark.ChambeaPe_Api.post.model.PostsEntity;
import com.digitaldark.ChambeaPe_Api.post.repository.PostRepository;
import com.digitaldark.ChambeaPe_Api.shared.DateTimeEntity;
import com.digitaldark.ChambeaPe_Api.user.model.EmployerEntity;
import com.digitaldark.ChambeaPe_Api.user.repository.EmployerRepository;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceImplTest {

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private PostRepository postRepository;

  @Mock
  private EmployerRepository employerRepository;

  @InjectMocks
  private PostServiceImpl postService;

  @Mock
  private DateTimeEntity dateTimeEntity;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getPostById() {
    // Arrange
    int postId = 1;
    PostsEntity post = new PostsEntity();
    post.setId(postId);

    PostResponseDTO postResponse = new PostResponseDTO(postId, "Post Title", "Post Description",
        "Post Subtitle", "Post Image URL", 1);

    when(postRepository.existsById(postId)).thenReturn(true);
    when(postRepository.findById(postId)).thenReturn(post);
    when(modelMapper.map(post, PostResponseDTO.class)).thenReturn(postResponse);

    // Act
    PostResponseDTO result = postService.getPostById(postId);

    // Assert
    assertNotNull(result);
    assertEquals(postId, result.getId());
    verify(postRepository, times(1)).findById(postId);
  }


  @Test
  void getAllPosts() {
    // Arrange
    PostsEntity post1 = new PostsEntity();
    post1.setId(1);
    post1.setTitle("Post 1");

    PostsEntity post2 = new PostsEntity();
    post2.setId(2);
    post2.setTitle("Post 2");

    List<PostsEntity> posts = Arrays.asList(post1, post2);
    when(postRepository.findAll()).thenReturn(posts);

    // Act
    List<PostResponseDTO> result = postService.getAllPosts();

    // Assert
    assertEquals(2, result.size());
    verify(postRepository, times(1)).findAll();
  }


  @Test
  void getAllPostsByEmployerId() {
    // Arrange
    int employerId = 1;
    EmployerEntity employer = new EmployerEntity();
    employer.setId(employerId);

    PostsEntity post1 = new PostsEntity();
    post1.setId(1);

    PostsEntity post2 = new PostsEntity();
    post2.setId(2);

    List<PostsEntity> posts = Arrays.asList(post1, post2);
    when(employerRepository.findById(employerId)).thenReturn(employer);
    when(postRepository.findByEmployer(employer)).thenReturn(posts);

    // Act
    List<PostResponseDTO> result = postService.getAllPostsByEmployerId(employerId);

    // Assert
    assertEquals(2, result.size());
    verify(postRepository, times(1)).findByEmployer(employer);
  }


  @Test
  void createPost() {
    // Arrange
    int employerId = 1;
    PostRequestDTO postRequest = new PostRequestDTO("Trabajo de pintor", "Se necesita una persona para pintar una casa", "Pinturas", "Image URL");
    EmployerEntity employerEntity = new EmployerEntity();
    employerEntity.setId(employerId);

    PostsEntity postEntity = new PostsEntity();
    postEntity.setId(1);
    postEntity.setTitle(postRequest.getTitle());
    postEntity.setDescription(postRequest.getDescription());
    postEntity.setSubtitle(postRequest.getSubtitle());
    postEntity.setImgUrl(postRequest.getImgUrl());

    PostResponseDTO postResponseDTO = new PostResponseDTO(postEntity.getId(), postEntity.getTitle(), postEntity.getDescription(),
        postEntity.getSubtitle(), postEntity.getImgUrl(), employerId);

    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    when(employerRepository.existsById(employerId)).thenReturn(true);
    when(employerRepository.findById(employerId)).thenReturn(employerEntity);
    when(modelMapper.map(postRequest, PostsEntity.class)).thenReturn(postEntity);
    when(modelMapper.map(postEntity, PostResponseDTO.class)).thenReturn(postResponseDTO);
    when(dateTimeEntity.currentTime()).thenReturn(currentTime);

    // Act
    PostResponseDTO result = postService.createPost(postRequest, employerId);

    // Assert
    assertNotNull(result);
    assertEquals(postEntity.getId(), result.getId());
    assertEquals("Trabajo de pintor", result.getTitle());
    verify(postRepository, times(1)).save(postEntity);
    verify(modelMapper, times(1)).map(postRequest, PostsEntity.class);
    verify(modelMapper, times(1)).map(postEntity, PostResponseDTO.class);
  }

  @Test
  void updatePost() {
    // Arrange
    int postId = 1;
    PostRequestDTO postRequest = new PostRequestDTO("Nuevo Título", "Nueva descripción", "Nuevo subtítulo", "Nueva URL de imagen");
    PostsEntity postEntity = new PostsEntity();
    postEntity.setId(postId);
    postEntity.setTitle("Título antiguo");

    PostResponseDTO postResponseDTO = new PostResponseDTO(postId, postRequest.getTitle(), postRequest.getDescription(),
        postRequest.getSubtitle(), postRequest.getImgUrl(), postId);

    Timestamp currentTime = new Timestamp(System.currentTimeMillis());

    when(postRepository.existsById(postId)).thenReturn(true);
    when(postRepository.findById(postId)).thenReturn(postEntity);
    when(modelMapper.map(postEntity, PostResponseDTO.class)).thenReturn(postResponseDTO);
    when(dateTimeEntity.currentTime()).thenReturn(currentTime);

    // Act
    PostResponseDTO result = postService.updatePost(postId, postRequest);

    // Assert
    assertNotNull(result);
    assertEquals(postRequest.getTitle(), result.getTitle());
    assertEquals(postRequest.getDescription(), result.getDescription());
    verify(postRepository, times(1)).save(postEntity);
    verify(modelMapper, times(1)).map(postRequest, postEntity);
    verify(modelMapper, times(1)).map(postEntity, PostResponseDTO.class);
  }

  @Test
  void deletePost() {
    // Arrange
    int postId = 1;
    when(postRepository.existsById(postId)).thenReturn(true);
    // Act
    postService.deletePost(postId);
    // Assert
    verify(postRepository, times(1)).deleteById(postId);
  }

}