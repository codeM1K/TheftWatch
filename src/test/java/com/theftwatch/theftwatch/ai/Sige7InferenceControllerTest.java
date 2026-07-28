package com.theftwatch.theftwatch.ai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@WebFluxTest(Sige7InferenceController.class)
class Sige7InferenceControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void healthEndpointShouldReturnOk() {
        webTestClient.get()
                .uri("/api/ai/inference/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .value(body -> assertTrue(body.containsKey("status")));
    }

    @Test
    void theftDetectionEndpointShouldReturnResult() {
        Map<String, Object> payload = Map.of(
                "image", "base64encodedimage",
                "camera_id", "camera-123"
        );

        webTestClient.post()
                .uri("/api/ai/inference/theft")
                .bodyValue(payload)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .value(body -> {
                    assertTrue(body.containsKey("theft_detected"));
                    assertTrue(body.containsKey("confidence"));
                    assertTrue(body.containsKey("timestamp"));
                });
    }
}
