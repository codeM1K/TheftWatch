package com.theftwatch.theftwatch.service;

import com.theftwatch.theftwatch.domain.Camera;
import com.theftwatch.theftwatch.domain.Dominion;
import com.theftwatch.theftwatch.domain.Realm;
import com.theftwatch.theftwatch.domain.enums.CameraStatus;
import com.theftwatch.theftwatch.repository.CameraRepository;
import com.theftwatch.theftwatch.streaming.StreamIngestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CameraService {

    private final CameraRepository cameraRepository;
    private final StreamIngestionService streamIngestionService;

    public CameraService(CameraRepository cameraRepository, StreamIngestionService streamIngestionService) {
        this.cameraRepository = cameraRepository;
        this.streamIngestionService = streamIngestionService;
    }

    @Transactional
    public Camera createCamera(String name, String model, String rtspUrl, String username,
                               String password, String ipAddress, Realm realm, Dominion dominion) {
        Camera camera = new Camera();
        camera.setName(name);
        camera.setModel(model);
        camera.setRtspUrl(rtspUrl);
        camera.setUsername(username);
        camera.setPassword(password);
        camera.setIpAddress(ipAddress);
        camera.setRealm(realm);
        camera.setDominion(dominion);
        camera.setStatus(CameraStatus.OFFLINE);
        return cameraRepository.save(camera);
    }

    public List<Camera> findAll() {
        return cameraRepository.findAll();
    }

    @Transactional
    public void startStream(String cameraId) {
        Camera camera = cameraRepository.findById(cameraId).orElse(null);
        if (camera != null) {
            streamIngestionService.startStream(cameraId, camera.getRtspUrl(), camera.getUsername(), camera.getPassword());
            camera.setStatus(CameraStatus.RECORDING);
            cameraRepository.save(camera);
        }
    }

    @Transactional
    public void stopStream(String cameraId) {
        Camera camera = cameraRepository.findById(cameraId).orElse(null);
        if (camera != null) {
            streamIngestionService.stopStream(cameraId);
            camera.setStatus(CameraStatus.OFFLINE);
            cameraRepository.save(camera);
        }
    }
}
