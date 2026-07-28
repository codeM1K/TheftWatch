package com.theftwatch.theftwatch.ai;

import org.springframework.stereotype.Service;

@Service
public class AlertService {

    private final Sige7InferenceService inferenceService;

    public AlertService(Sige7InferenceService inferenceService) {
        this.inferenceService = inferenceService;
    }

    public void processFrame(String cameraId, String frameBase64) {
        Sige7InferenceService.InferenceResult result = inferenceService.detectTheft(frameBase64, cameraId);

        if (result.theftDetected() && result.confidence() > 0.7) {
            // TODO: Create Alert entity and persist
            // TODO: Send notification to relevant users
            System.out.println("THEFT DETECTED on camera " + cameraId + " with confidence " + result.confidence());
        }
    }
}
