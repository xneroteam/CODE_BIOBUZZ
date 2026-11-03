# AprilTag Detection for FTC Robot

This folder contains AprilTag detection code for your FTC robot using the FTC Vision SDK.

## Files

1. **AprilTagVision.java** - Core AprilTag detection class (corrected version)
2. **AprilTagAutonomous.java** - Example autonomous OpMode using AprilTags
3. **AprilTagTeleOp.java** - Example TeleOp OpMode with AprilTag detection

## Setup

### 1. Hardware Requirements
- Camera connected to Control Hub (internal phone camera or external webcam)
- AprilTags printed and placed on the field

### 2. Software Requirements
- FTC Vision SDK is already included in `build.dependencies.gradle`
- Android Studio with FTC SDK

### 3. Using the Code

#### Basic Usage in Your OpMode:

```java
import org.firstinspires.ftc.teamcode.AprilTagVision;

public class MyOpMode extends LinearOpMode {
    private AprilTagVision aprilTagVision;
    
    @Override
    public void runOpMode() {
        // Initialize
        aprilTagVision = new AprilTagVision();
        
        // Use internal camera (or external webcam)
        boolean useWebcam = false;
        
        if (useWebcam) {
            aprilTagVision.initialize(hardwareMap, cameraMonitorViewId);
        } else {
            aprilTagVision.initializeInternalCamera(hardwareMap, cameraMonitorViewId);
        }
        
        waitForStart();
        
        while (opModeIsActive()) {
            // Update detections
            aprilTagVision.update();
            
            // Get detections
            List<AprilTagDetection> detections = aprilTagVision.getDetections();
            
            // Check for specific tag
            if (aprilTagVision.isTagVisible(1)) {
                AprilTagDetection tag = aprilTagVision.getDetectionById(1);
                double distance = aprilTagVision.getDistance(tag);
                double angle = aprilTagVision.getAngleToTag(tag);
                
                // Use distance and angle for navigation
            }
            
            telemetry.update();
        }
        
        // Cleanup
        aprilTagVision.stop();
    }
}
```

## Key Methods

### AprilTagVision Class

- `initialize(hardwareMap, cameraMonitorViewId)` - Initialize with external webcam
- `initializeInternalCamera(hardwareMap, cameraMonitorViewId)` - Initialize with internal phone camera
- `update()` - Update detection results (call periodically)
- `getDetections()` - Get all detected AprilTags
- `getDetectionById(int id)` - Get specific tag by ID
- `isTagVisible(int id)` - Check if tag is visible
- `getClosestTag()` - Get the closest detected tag
- `getDistance(AprilTagDetection detection)` - Get distance to tag
- `getAngleToTag(AprilTagDetection detection)` - Get angle to tag
- `stop()` - Stop camera and release resources

## AprilTag IDs

Standard FTC AprilTag IDs:
- **Tag 1-6**: Common field tags
- Modify `TARGET_TAG_ID` in the example OpModes based on your field setup

## Camera Configuration

### Internal Phone Camera
```java
boolean useWebcam = false;
aprilTagVision.initializeInternalCamera(hardwareMap, cameraMonitorViewId);
```

### External Webcam
```java
boolean useWebcam = true;
aprilTagVision.initialize(hardwareMap, cameraMonitorViewId);
```

**Note:** For external webcam, ensure your webcam is named "Webcam 1" in the FTC Robot Controller app configuration.

## Navigation Example

```java
private void navigateToTag(AprilTagDetection tag) {
    double distance = aprilTagVision.getDistance(tag);
    double angle = aprilTagVision.getAngleToTag(tag);
    
    // Move to tag
    if (distance > 0.5) {
        robotDrive.forward(0.3);
    } else if (distance < 0.3) {
        robotDrive.backward(0.3);
    }
    
    // Turn to face tag
    if (angle > 5) {
        robotDrive.turnRight(0.2);
    } else if (angle < -5) {
        robotDrive.turnLeft(0.2);
    }
}
```

## Troubleshooting

### Camera not initializing
- Check camera permissions in AndroidManifest.xml
- Ensure camera is properly connected
- Try switching between internal/external camera
- Verify webcam name in FTC Robot Controller app

### No tags detected
- Ensure AprilTags are properly printed
- Check lighting conditions
- Verify camera is focused
- Make sure tags are within camera field of view
- Tags should be flat and not tilted

### Poor detection accuracy
- Improve lighting
- Use higher resolution AprilTags
- Ensure tags are flat and not tilted
- Check camera calibration
- Try different camera resolution

## Advanced Usage

### Custom Processor Configuration
```java
aprilTagProcessor = new AprilTagProcessor.Builder()
    .setDrawAxes(true)
    .setDrawCubeProjection(true)
    .setDrawTagOutline(true)
    .setDrawTagID(true)
    .build();
```

### Accessing VisionPortal
```java
VisionPortal portal = aprilTagVision.getVisionPortal();
// Advanced VisionPortal operations
```

## Resources

- [FTC Vision SDK Documentation](https://ftc-docs.firstinspires.org/)
- [AprilTag Library](https://github.com/FIRST-Tech-Challenge/FtcRobotController)
- [OpenCV for FTC](https://github.com/OpenFTC/EasyOpenCV)

## License

This code follows the same license as the FTC Robot Controller project.
