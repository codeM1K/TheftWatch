package com.theftwatch.theftwatch.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class Sige7InferenceService {

    private static final Logger log = LoggerFactory.getLogger(Sige7InferenceService.class);

    @Value("${app.ai.sige7.base-url:http://localhost:8000}")
    private String baseUrl;

    @Value("${app.ai.sige7.inference-timeout:5s}")
    private String inferenceTimeout;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public Sige7InferenceService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
    }

    public InferenceResult detectTheft(String imageBase64, String cameraId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestBody = String.format("{\"image\": \"%s\", \"camera_id\": \"%s\"}",
                    imageBase64.replace("\"", "\\\""), cameraId);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            String response = restTemplate.postForObject(
                    baseUrl + "/infer/theft",
                    entity,
                    String.class
            );

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
