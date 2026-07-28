package com.theftwatch.theftwatch.repository;

import com.theftwatch.theftwatch.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, String> {
    List<Alert> findByRealmIdOrderByDetectedAtDesc(String realmId);
    List<Alert> findByCameraIdOrderByDetectedAtDesc(String cameraId);
}
