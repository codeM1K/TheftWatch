package com.theftwatch.theftwatch.storage;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/storage")
public class StorageProxyController {

    private final MinioStorageService storageService;

    public StorageProxyController(MinioStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/videos/{*path}")
    public ResponseEntity<InputStreamResource> getVideo(@PathVariable String path) {
        try {
            java.io.InputStream is = storageService.downloadVideo(path);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .body(new InputStreamResource(is));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
