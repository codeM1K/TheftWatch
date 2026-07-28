package com.theftwatch.theftwatch.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Sige7InferenceService {

    private static final Logger log = LoggerFactory.getLogger(Sige7InferenceService.class);

    @Value("${app.ai.sige7.base-url:http://localhost:8000}")
    private String baseUrl;

    @Value("${app.ai.sige7.inference-timeout:5s}")
    private String inferenceTimeout;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public Sige7InferenceService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public InferenceResult detectTheft(String imageBase64, String cameraId) {
        try {
            String response = webClient.post()
                    .uri("/infer/theft")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"image\": \"" + imageBase64 + "\", \"camera_id\": \"" + cameraId + "\"}")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode json = objectMapper.readTree(response);
            boolean theftDetected = json.path("theft_detected").asBoolean(false);
            double confidence = json.path("confidence").asDouble(0.0);

            return new InferenceResult(theftDetected, confidence, LocalDateTime.now());
        } catch (Exception e) {
            log.error("Sige 7 inference failed for camera {}: {}", cameraId, e.getMessage());
            return new InferenceResult(false, 0.0, LocalDateTime.now());
        }
    }

    public record InferenceResult(boolean theftDetected, double confidence, LocalDateTime timestamp) {
    }
}
