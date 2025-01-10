package com.algonet.algonetapi.services;

import com.algonet.algonetapi.exceptions.QueueInsertionException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
@Service
@AllArgsConstructor
@Transactional
public class QueueService {
    private final WebClient.Builder webClientBuilder;

    public void addSolutionToQueue(Integer solutionId) {
        WebClient webClient = webClientBuilder.baseUrl("http://localhost:9090").build();
        try {

            ResponseEntity<String> response = webClient.post()
                    .uri("/addToQueue")
                    .bodyValue("{\"id\": " + solutionId + "}")
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            if (response != null && response.getStatusCode().isError()) {
                throw new QueueInsertionException();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getClass());
            throw new QueueInsertionException();
        }
    }
}
