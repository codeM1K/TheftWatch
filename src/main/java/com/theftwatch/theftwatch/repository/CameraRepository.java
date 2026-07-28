package com.theftwatch.theftwatch.repository;

import com.theftwatch.theftwatch.domain.Camera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CameraRepository extends JpaRepository<Camera, String> {
    List<Camera> findByRealmId(String realmId);
    List<Camera> findByDominionId(String dominionId);
}
