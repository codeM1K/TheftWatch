package com.theftwatch.theftwatch.domain;

import com.theftwatch.theftwatch.domain.enums.AlertSeverity;
import com.theftwatch.theftwatch.domain.enums.AlertStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertStatus status = AlertStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camera_id")
    private Camera camera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "realm_id")
    private Realm realm;

    @Column
    private String snapshotPath;

    @Column
    private String videoClipPath;

    @Column(nullable = false)
    private LocalDateTime detectedAt = LocalDateTime.now();

    private LocalDateTime acknowledgedAt;

    private LocalDateTime resolvedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(columnDefinition = "JSONB")
    private String metadata;
}
