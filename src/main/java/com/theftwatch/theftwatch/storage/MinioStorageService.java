package com.theftwatch.theftwatch.storage;

import io.minio.MinioClient;
import io.minio.GetObjectResponse;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.RemoveObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class MinioStorageService {

    private static final Logger log = LoggerFactory.getLogger(MinioStorageService.class);

    private final MinioClient minioClient;
    private final String videoBucket;
    private final String snapshotBucket;

    public MinioStorageService(
            @Value("${minio.endpoint:http://localhost:9000}") String endpoint,
            @Value("${minio.access-key:theftwatch}") String accessKey,
            @Value("${minio.secret-key:theftwatch}") String secretKey,
            @Value("${minio.video-bucket:videos}") String videoBucket,
            @Value("${minio.snapshot-bucket:snapshots}") String snapshotBucket
    ) {
        this.videoBucket = videoBucket;
        this.snapshotBucket = snapshotBucket;

        try {
            this.minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();

            initializeBuckets();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize MinIO client", e);
        }
    }

    private void initializeBuckets() {
        try {
            if (!minioClient.bucketExists(io.minio.BucketExistsArgs.builder().bucket(videoBucket).build())) {
                minioClient.makeBucket(io.minio.MakeBucketArgs.builder().bucket(videoBucket).build());
            }
            if (!minioClient.bucketExists(io.minio.BucketExistsArgs.builder().bucket(snapshotBucket).build())) {
                minioClient.makeBucket(io.minio.MakeBucketArgs.builder().bucket(snapshotBucket).build());
            }
        } catch (Exception e) {
            log.warn("MinIO bucket initialization failed: {}", e.getMessage());
        }
    }

    public String uploadVideo(InputStream inputStream, String contentType, String cameraId) {
        String objectName = cameraId + "/" + UUID.randomUUID() + ".mp4";
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(videoBucket)
                    .object(objectName)
                    .stream(inputStream, -1L, -1)
                    .contentType(contentType)
                    .build());
            return objectName;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to upload video", e);
        }
    }

    public String uploadSnapshot(InputStream inputStream, String contentType, String cameraId) {
        String objectName = cameraId + "/" + UUID.randomUUID() + ".jpg";
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(snapshotBucket)
                    .object(objectName)
                    .stream(inputStream, -1L, -1)
                    .contentType(contentType)
                    .build());
            return objectName;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to upload snapshot", e);
        }
    }

    public InputStream downloadVideo(String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(videoBucket)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to download video", e);
        }
    }

    public void deleteVideo(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(videoBucket)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.warn("Failed to delete video {}: {}", objectName, e.getMessage());
        }
    }

    public String getVideoUrl(String objectName) {
        return String.format("/api/storage/videos/%s", objectName);
    }
}
