package com.theftwatch.theftwatch.service;

import com.theftwatch.theftwatch.domain.Realm;
import com.theftwatch.theftwatch.domain.User;
import com.theftwatch.theftwatch.repository.RealmRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RealmService {

    private final RealmRepository realmRepository;

    public RealmService(RealmRepository realmRepository) {
        this.realmRepository = realmRepository;
    }

    @Transactional
    public Realm createRealm(String name, String description, User createdBy) {
        Realm realm = new Realm();
        realm.setName(name);
        realm.setDescription(description);
        realm.setCreatedBy(createdBy);
        return realmRepository.save(realm);
    }

    @Transactional
    public Realm updateRealm(String id, String name, String description) {
        Realm realm = realmRepository.findById(id).orElse(null);
        if (realm == null) {
            throw new IllegalArgumentException("Realm not found");
        }
        realm.setName(name);
        realm.setDescription(description);
        return realmRepository.save(realm);
    }

    @Transactional
    public void deleteRealm(String id) {
        realmRepository.deleteById(id);
    }

    public List<Realm> findAll() {
        return realmRepository.findAll();
    }

    public Realm findById(String id) {
        return realmRepository.findById(id).orElse(null);
    }
}
