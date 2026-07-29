package com.theftwatch.theftwatch.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Sige7InferenceController.class)
class Sige7InferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void healthEndpointShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/ai/inference/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void theftDetectionEndpointShouldReturnResult() throws Exception {
        Map<String, Object> payload = Map.of(
                "image", "base64encodedimage",
                "camera_id", "camera-123"
        );

        mockMvc.perform(post("/api/ai/inference/theft")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.theft_detected").exists())
                .andExpect(jsonPath("$.confidence").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
