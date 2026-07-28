package com.theftwatch.theftwatch.domain;

import com.theftwatch.theftwatch.domain.enums.CameraStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cameras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Camera {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String rtspUrl;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String ipAddress;

    @Column
    private Integer port;

    @Column
    private String streamPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CameraStatus status = CameraStatus.OFFLINE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "realm_id")
    private Realm realm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dominion_id")
    private Dominion dominion;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime lastSeenAt;

    @Column
    private String streamUrl;

    @Column
    private Boolean aiEnabled = false;
}
