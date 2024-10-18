package com.digitaldark.ChambeaPe_Api.postulation.service.impl;

import com.digitaldark.ChambeaPe_Api.post.dto.response.PostResponseDTO;
import com.digitaldark.ChambeaPe_Api.post.model.PostsEntity;
import com.digitaldark.ChambeaPe_Api.post.repository.PostRepository;
import com.digitaldark.ChambeaPe_Api.postulation.dto.request.PostulationRequestDTO;
import com.digitaldark.ChambeaPe_Api.postulation.dto.response.PostulationResponseDTO;
import com.digitaldark.ChambeaPe_Api.postulation.dto.response.PostulationWorkerResponseDTO;
import com.digitaldark.ChambeaPe_Api.postulation.model.PostulationsEntity;
import com.digitaldark.ChambeaPe_Api.postulation.repository.PostulationRepository;
import com.digitaldark.ChambeaPe_Api.postulation.service.PostulationService;
import com.digitaldark.ChambeaPe_Api.shared.DateTimeEntity;
import com.digitaldark.ChambeaPe_Api.shared.exception.ResourceNotFoundException;
import com.digitaldark.ChambeaPe_Api.user.dto.WorkerDTO;
import com.digitaldark.ChambeaPe_Api.user.model.WorkerEntity;
import com.digitaldark.ChambeaPe_Api.user.repository.UserRepository;
import com.digitaldark.ChambeaPe_Api.user.repository.WorkerRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
public class PostulationServiceImpl implements PostulationService {
    @Autowired
    private PostulationRepository postulationRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DateTimeEntity dateTimeEntity;

    @Override
    public List<PostulationResponseDTO> getAllPostulationsByPost(int postId) {
        PostsEntity postEntity = postRepository.findById(postId);

        List<PostulationsEntity> postulationEntities = postulationRepository.findAllByPost(postEntity);

        return postulationEntities.stream()
                .map(postulationEntity -> {
                    PostulationResponseDTO postulationResponseDTO = modelMapper.map(postulationEntity, PostulationResponseDTO.class);
                    postulationResponseDTO.setWorker(getWorkerDTO(postulationEntity.getWorker()));
                    return postulationResponseDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PostulationResponseDTO createPostulation(int postId, int workerId, PostulationRequestDTO postulation) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found");
        }
        else if (!workerRepository.existsById(workerId)) {
            throw new ResourceNotFoundException("Worker not found");
        }

        if (postulationRepository.existsByPostAndWorker(postRepository.findById(postId), workerRepository.findById(workerId))) {
            throw new ResourceNotFoundException("Postulation already exists");
        }

        PostulationsEntity postulationEntity = modelMapper.map(postulation, PostulationsEntity.class);

        postulationEntity.setPost(postRepository.findById(postId));
        postulationEntity.setWorker(workerRepository.findById(workerId));
        postulationEntity.setIsActive( (byte) 1);
        postulationEntity.setIsAccepted( (byte) 0);
        postulationEntity.setDateCreated(dateTimeEntity.currentTime());
        postulationEntity.setDateUpdated(dateTimeEntity.currentTime());

        postulationRepository.save(postulationEntity);


        PostulationResponseDTO postulationResponseDTO = modelMapper.map(postulationEntity, PostulationResponseDTO.class);
        postulationResponseDTO.setWorker(getWorkerDTO(postulationEntity.getWorker()));

        return postulationResponseDTO;
    }

    @CrossOrigin(origins = "*")
    @Override
    public void deletePostulation(int postId, int workerId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found");
        }
        else if (!workerRepository.existsById(workerId)) {
            throw new ResourceNotFoundException("Worker not found");
        }
        PostulationsEntity postulationEntity = postulationRepository.findByPostAndWorker(postRepository.findById(postId), workerRepository.findById(workerId));
        if(postulationEntity == null){
            throw new ResourceNotFoundException("Postulation not found");
        }
        postulationRepository.deleteById(postulationEntity.getId());
    }


    @Override
    public void updatePostulation(int id) {
        if (!postulationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Postulation not found");
        }
        PostulationsEntity postulationEntity = postulationRepository.findById(id);

        postulationEntity.setIsAccepted( (byte) 1);
        postulationEntity.setDateUpdated(dateTimeEntity.currentTime());

        postulationRepository.save(postulationEntity);
    }

    @Override
    public List<PostulationWorkerResponseDTO> getAllPostulationsByWorker(int workerId) {
        if (!workerRepository.existsById(workerId)) {
            throw new ResourceNotFoundException("Worker not found");
        }

        List<PostulationsEntity> postulationEntities = postulationRepository.findAllByWorkerId(workerId);

        return postulationEntities.stream()
                .map(postulationEntity -> {
                    PostulationWorkerResponseDTO postulationWorkerResponseDTO = modelMapper.map(postulationEntity, PostulationWorkerResponseDTO.class);
                    postulationWorkerResponseDTO.setPost(getPostulationResponseDTO(postulationEntity));
                    return postulationWorkerResponseDTO;
                })
                .collect(Collectors.toList());
    }


    //Functions

    public WorkerDTO getWorkerDTO(WorkerEntity workerEntity){
        WorkerDTO workerDTO = modelMapper.map(workerEntity, WorkerDTO.class);
        modelMapper.map(workerEntity.getUser(), workerDTO);

        return workerDTO;
    }

    public PostResponseDTO getPostulationResponseDTO(PostulationsEntity postulationEntity){
        PostResponseDTO postResponseDTO = modelMapper.map(postulationEntity.getPost(), PostResponseDTO.class);
        postResponseDTO.setEmployerId(postulationEntity.getPost().getEmployer().getId());
        return postResponseDTO;
    }

}