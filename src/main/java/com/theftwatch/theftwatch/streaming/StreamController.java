package com.theftwatch.theftwatch.streaming;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Paths;

@RestController
@RequestMapping("/stream")
public class StreamController {

    @Value("${app.streaming.hls-output-dir:/data/hls}")
    private String hlsOutputDir;

    @GetMapping("/{cameraId}/{fileName:.+}")
    public ResponseEntity<FileSystemResource> getStream(@PathVariable String cameraId, @PathVariable String fileName) {
        String filePath = Paths.get(hlsOutputDir, cameraId, fileName).toString();
        File file = new File(filePath);
        
        if (file.exists() && file.isFile()) {
            return ResponseEntity.ok()
                    .body(new FileSystemResource(file));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}