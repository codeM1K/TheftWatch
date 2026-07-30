package com.theftwatch.theftwatch.streaming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

@Service
public class StreamIngestionService {

    private static final Logger log = LoggerFactory.getLogger(StreamIngestionService.class);

    @Value("${app.streaming.ffmpeg-path:ffmpeg}")
    private String ffmpegPath;

    @Value("${app.streaming.hls-output-dir:/tmp/theftwatch/hls}")
    private String hlsOutputDir;

    @Value("${app.storage.video-base-path:/data/videos}")
    private String videoBasePath;

    private Process ffmpegProcess;

    @Async
    public void startStream(String cameraId, String rtspUrl, String username, String password) {
        String outputDir = hlsOutputDir + "/" + cameraId;
        String outputPath = outputDir + "/stream.m3u8";

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    ffmpegPath,
                    "-rtsp_transport", "tcp",
                    "-i", buildRtspUrl(rtspUrl, username, password),
                    "-c:v", "copy",
                    "-c:a", "aac",
                    "-f", "hls",
                    "-hls_time", "4",
                    "-hls_list_size", "6",
                    "-hls_flags", "delete_segments",
                    "-hls_segment_filename", outputDir + "/segment_%03d.ts",
                    outputPath
            );

            pb.redirectErrorStream(true);
            ffmpegProcess = pb.start();

            log.info("Started FFmpeg stream for camera {}: {}", cameraId, rtspUrl);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(ffmpegProcess.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.debug("[FFmpeg {}] {}", cameraId, line);
                }
            }
        } catch (IOException e) {
            log.error("Failed to start stream for camera {}: {}", cameraId, e.getMessage());
        }
    }

    public void stopStream(String cameraId) {
        if (ffmpegProcess != null && ffmpegProcess.isAlive()) {
            ffmpegProcess.destroy();
            log.info("Stopped FFmpeg stream for camera {}", cameraId);
        }
    }

    private String buildRtspUrl(String baseUrl, String username, String password) {
        if (baseUrl.contains("@")) {
            return baseUrl;
        }
        if (username != null && !username.isEmpty()) {
            String protocol = baseUrl.startsWith("https") ? "https" : "http";
            if (baseUrl.startsWith("rtsp")) {
                String creds = username + ":" + password + "@";
                return baseUrl.replaceFirst("rtsp://", "rtsp://" + creds);
            }
        }
        return baseUrl;
    }
}
