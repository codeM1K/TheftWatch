# Streaming System Requirements

## Software Dependencies

1. **FFmpeg** - Required for generating HLS streams
   - Install FFmpeg on your system
   - Ensure FFmpeg is accessible via system PATH
   - Windows: Download from https://ffmpeg.org/download.html
   - Linux/Mac: `sudo apt install ffmpeg` or `brew install ffmpeg`

2. **Application Configuration** 
   The application looks for FFmpeg at:
   - Default: `ffmpeg` (uses system PATH)
   - Custom: Set `app.streaming.ffmpeg-path` in application.properties

## Current Implementation Status

### Working Components
1. UI Streaming View - Camera selection and controls
2. HTTP Stream Controller - Serves HLS segments from configured directory
3. Backend Integration - Connects to existing camera service

### Missing Components
1. FFmpeg Installation - Required for actual stream generation
2. Stream Serving Configuration - Proper HTTP endpoints to serve segments

## How to Test the Implementation

1. Install FFmpeg on your system
2. Run the application
3. Navigate to Streaming View
4. Select a camera that has RTSP URL configured
5. Click Start Stream
6. If successful, streaming should begin and be visible in the view

## Troubleshooting

### FFmpeg Not Found Errors
```
Cannot run program "ffmpeg": Exec failed, error: 2 (No such file or directory)
```

Solution: Install FFmpeg and ensure it's in PATH

### Stream Not Visible in Browser
Ensure:
1. FFmpeg is installed and working
2. The HLS output directory is properly configured
3. No firewall/security issues prevent access to streams