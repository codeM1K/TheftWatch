package com.theftwatch.theftwatch.service;

import com.theftwatch.theftwatch.domain.Alert;
import com.theftwatch.theftwatch.domain.Camera;
import com.theftwatch.theftwatch.domain.Realm;
import com.theftwatch.theftwatch.domain.enums.AlertSeverity;
import com.theftwatch.theftwatch.domain.enums.AlertStatus;
import com.theftwatch.theftwatch.repository.AlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Transactional
    public Alert createAlert(String title, String description, AlertSeverity severity,
                             Camera camera, Realm realm) {
        Alert alert = new Alert();
        alert.setTitle(title);
        alert.setDescription(description);
        alert.setSeverity(severity);
        alert.setStatus(AlertStatus.OPEN);
        alert.setCamera(camera);
        alert.setRealm(realm);
        alert.setDetectedAt(LocalDateTime.now());
        return alertRepository.save(alert);
    }

    public List<Alert> findByRealm(String realmId) {
        return alertRepository.findByRealmIdOrderByDetectedAtDesc(realmId);
    }

    public List<Alert> findByCamera(String cameraId) {
        return alertRepository.findByCameraIdOrderByDetectedAtDesc(cameraId);
    }

    public List<Alert> findAll() {
        return alertRepository.findAll();
    }
}
