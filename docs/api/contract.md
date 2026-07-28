# TheftWatch API Contract

## Sige 7 Inference Service

The TheftWatch system exposes a REST API endpoint for the Sige 7 AI module to send inference results.

### POST /api/ai/inference/theft

Accepts inference requests from the Sige 7 device.

**Request Body:**
```json
{
  "camera_id": "string",
  "image": "base64_encoded_image",
  "timestamp": "ISO8601 datetime (optional)"
}
```

**Response:**
```json
{
  "theft_detected": boolean,
  "confidence": double,
  "timestamp": "ISO8601 datetime",
  "camera_id": "string"
}
```

### GET /api/ai/inference/health

Health check endpoint for the Sige 7 module.

**Response:**
```json
{
  "status": "ok",
  "timestamp": "ISO8601 datetime"
}
```

## Storage API

### GET /api/storage/videos/{path}

Proxies video file downloads from MinIO.

### Authentication

All API endpoints require Bearer token authentication except `/login/**` and `/api/ai/inference/**`.
