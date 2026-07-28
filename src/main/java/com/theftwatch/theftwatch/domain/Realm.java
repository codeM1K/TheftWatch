package com.theftwatch.theftwatch.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "realms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Realm {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "realm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dominion> dominions = new ArrayList<>();

    @OneToMany(mappedBy = "realm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Camera> cameras = new ArrayList<>();
}
