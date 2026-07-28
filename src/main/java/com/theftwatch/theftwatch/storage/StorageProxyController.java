package com.theftwatch.theftwatch.storage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/storage")
public class StorageProxyController {

    private final MinioStorageService storageService;

    public StorageProxyController(MinioStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/videos/{*path}")
    public Mono<ResponseEntity<org.springframework.core.io.InputStreamResource>> getVideo(@PathVariable String path) {
        try {
            java.io.InputStream is = storageService.downloadVideo(path);
            return Mono.just(ResponseEntity.ok()
                    .header("Content-Type", "video/mp4")
                    .body(new org.springframework.core.io.InputStreamResource(is)));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.notFound().build());
        }
    }
}
