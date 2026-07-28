package com.theftwatch.theftwatch.repository;

import com.theftwatch.theftwatch.domain.Dominion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DominionRepository extends JpaRepository<Dominion, String> {
    List<Dominion> findByRealmId(String realmId);
    List<Dominion> findByCreatedById(String createdById);
}
