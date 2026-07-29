package com.theftwatch.theftwatch.streaming;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
            // Set appropriate content type based on file extension
            String contentType = getContentType(fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new FileSystemResource(file));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    private String getContentType(String fileName) {
        if (fileName.toLowerCase().endsWith(".ts")) {
            return "video/MP2T";
        } else if (fileName.toLowerCase().endsWith(".m3u8")) {
            return "application/vnd.apple.mpegurl";
        } else {
            return "application/octet-stream";
        }
    }
}