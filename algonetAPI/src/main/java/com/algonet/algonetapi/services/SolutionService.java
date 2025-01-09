package com.algonet.algonetapi.services;

import com.algonet.algonetapi.exceptions.NotFoundException;
import com.algonet.algonetapi.models.dto.solutionDTOs.SolutionCreationDTO;
import com.algonet.algonetapi.models.entities.Problem;
import com.algonet.algonetapi.models.entities.Solution;
import com.algonet.algonetapi.models.entities.User;
import com.algonet.algonetapi.repositories.SolutionRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class SolutionService {

    private final SolutionRepository solutionRepository;
    private final EntityManager entityManager;
    private final WebClient.Builder webClientBuilder;

    public Solution create(User user, Integer problem_id, SolutionCreationDTO solutionCreationDTO) {

        Solution solution = new Solution();
        BeanUtils.copyProperties(solutionCreationDTO, solution);

        Problem problem = entityManager.find(Problem.class, problem_id);
        solution.setProblem(problem);

        solution.setUser(user);

        solution.setGrade(-1);
        solution.setCreatedAt(Instant.now());

        var savedSolution = solutionRepository.save(solution);

        WebClient webClient = webClientBuilder.baseUrl("http://localhost:9090").build();
        webClient.post()
                .uri("/addToQueue")
                .bodyValue("{\"id\": " + solution.getId() + "}")
                .retrieve()
                .toEntity(String.class)
                .doOnError(e -> System.err.println("Request failed: " + e.getMessage()))
                .subscribe(response -> {
                    if (response.getStatusCode() == HttpStatus.OK) {
                        System.out.println("Response: " + response.getBody());
                    }
                });

        return savedSolution;
    }

    public Solution get(Integer id) {
        return solutionRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Solution> getByUserAndProblemId(User user, Integer problemId) {

        return solutionRepository.findByUserAndProblemId(user, problemId)
                .orElseThrow(NotFoundException::new);
    }
}
