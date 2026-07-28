# TheftWatch - Advanced VMS with Augmented AI

Java 21 + Spring Boot 3 + Vaadin 24 video management system with AI-powered theft detection.

## Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL 17
- FFmpeg
- Sige 7 (Armsom) for AI inference (optional for initial setup)

## Quick Start

### Development (with Docker Compose)

```bash
# Start infrastructure
docker compose up -d postgres minio

# Run application
mvn spring-boot:run
```

### Production Build

```bash
mvn clean package -DskipTests
docker compose up --profile production
```

## Architecture

- **Frontend**: Vaadin 24 (Java-based UI)
- **Backend**: Spring Boot 3.3
- **Database**: PostgreSQL 17 with Flyway migrations
- **Storage**: MinIO (S3-compatible) for video/recordings
- **AI**: Sige 7 integration via REST API
- **Streaming**: FFmpeg RTSP → HLS transcoding

## User Hierarchy

1. **Super Admin**: Full system control, creates Special Admins and End Users
2. **Special Admin**: Manages End User realms/dominions, partial camera setup
3. **End User**: Business-level access to assigned realms/dominions

## Domain Model

- **Realm**: Top-level organizational unit (e.g., "Company A")
- **Dominion**: Location/branch within a realm (e.g., "Store #1", "Warehouse")
- **Camera**: RTSP-enabled camera assigned to realm/dominion
- **Alert**: AI-generated theft detection events

## Key Features

- Multi-camera RTSP streaming
- HLS video delivery for browser playback
- AI-powered theft detection via Sige 7
- Role-based access control (RBAC)
- Realm/Dominion hierarchy for multi-tenant deployments
- Alert management with severity levels
- Docker containerization

## Project Structure

```
src/main/java/com/theftwatch/theftwatch/
├── config/           - App configuration
├── domain/           - JPA entities
├── enums/            - Role, AlertSeverity, CameraStatus
├── repository/       - Spring Data JPA repos
├── service/          - Business logic
├── security/         - Auth & RBAC
├── ai/               - Sige 7 inference
├── streaming/        - FFmpeg RTSP/HLS
└── ui/               - Vaadin views
```

## Development

```bash
# Run tests
mvn test

# Package
mvn clean package

# Run
java -jar target/theftwatch-0.1.0.jar
```

## License

Proprietary
