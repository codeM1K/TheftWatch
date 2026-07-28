package com.theftwatch.theftwatch.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/inference")
public class Sige7InferenceController {

    private static final Logger log = LoggerFactory.getLogger(Sige7InferenceController.class);

    @PostMapping("/theft")
    public Mono<ResponseEntity<Map<String, Object>>> detectTheft(@RequestBody Map<String, Object> payload) {
        String cameraId = (String) payload.get("camera_id");
        String imageBase64 = (String) payload.get("image");

        log.info("Received theft detection request for camera: {}", cameraId);

        boolean theftDetected = false;
        double confidence = 0.0;

        if (imageBase64 != null && !imageBase64.isBlank()) {
            try {
                java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
                byte[] imageBytes = decoder.decode(imageBase64);
                log.debug("Received image for camera {}: {} bytes", cameraId, imageBytes.length);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid base64 image for camera {}", cameraId);
            }
        }

        Map<String, Object> response = Map.of(
                "theft_detected", theftDetected,
                "confidence", confidence,
                "timestamp", LocalDateTime.now().toString(),
                "camera_id", cameraId
        );

        return Mono.just(ResponseEntity.ok(response));
    }

    @GetMapping("/health")
    public Mono<ResponseEntity<Map<String, Object>>> health() {
        return Mono.just(ResponseEntity.ok(Map.of(
                "status", "ok",
                "timestamp", LocalDateTime.now().toString()
        )));
    }
}
