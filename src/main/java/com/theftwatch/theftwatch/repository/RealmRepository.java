package com.theftwatch.theftwatch.repository;

import com.theftwatch.theftwatch.domain.Realm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RealmRepository extends JpaRepository<Realm, String> {
    List<Realm> findByCreatedById(String createdById);
}
