CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    last_login_at TIMESTAMP,
    created_by_id VARCHAR(36),
    CONSTRAINT fk_created_by FOREIGN KEY (created_by_id) REFERENCES users(id)
);

CREATE TABLE realms (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    created_by_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_realm_created_by FOREIGN KEY (created_by_id) REFERENCES users(id)
);

CREATE TABLE dominions (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(255) NOT NULL,
    realm_id VARCHAR(36) NOT NULL,
    created_by_id VARCHAR(36) NOT NULL,
    assigned_role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_dominion_realm FOREIGN KEY (realm_id) REFERENCES realms(id),
    CONSTRAINT fk_dominion_created_by FOREIGN KEY (created_by_id) REFERENCES users(id)
);

CREATE TABLE cameras (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    rtsp_url VARCHAR(1024) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    ip_address VARCHAR(45) NOT NULL,
    port INTEGER,
    stream_path VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'OFFLINE',
    realm_id VARCHAR(36),
    dominion_id VARCHAR(36),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    last_seen_at TIMESTAMP,
    stream_url VARCHAR(1024),
    ai_enabled BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_camera_realm FOREIGN KEY (realm_id) REFERENCES realms(id),
    CONSTRAINT fk_camera_dominion FOREIGN KEY (dominion_id) REFERENCES dominions(id)
);

CREATE TABLE alerts (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    severity VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    camera_id VARCHAR(36),
    realm_id VARCHAR(36),
    snapshot_path VARCHAR(1024),
    video_clip_path VARCHAR(1024),
    detected_at TIMESTAMP NOT NULL DEFAULT NOW(),
    acknowledged_at TIMESTAMP,
    resolved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    metadata JSONB,
    CONSTRAINT fk_alert_camera FOREIGN KEY (camera_id) REFERENCES cameras(id),
    CONSTRAINT fk_alert_realm FOREIGN KEY (realm_id) REFERENCES realms(id)
);

CREATE TABLE user_realms (
    user_id VARCHAR(36) NOT NULL,
    realm_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (user_id, realm_id),
    CONSTRAINT fk_user_realm_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_realm_realm FOREIGN KEY (realm_id) REFERENCES realms(id)
);

CREATE INDEX idx_cameras_realm ON cameras(realm_id);
CREATE INDEX idx_cameras_dominion ON cameras(dominion_id);
CREATE INDEX idx_alerts_realm ON alerts(realm_id);
CREATE INDEX idx_alerts_camera ON alerts(camera_id);
CREATE INDEX idx_alerts_detected_at ON alerts(detected_at DESC);
