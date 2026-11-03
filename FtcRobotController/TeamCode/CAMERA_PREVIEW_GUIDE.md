# Camera Preview Setup

## 📷 How to View Camera Feed

### Option 1: Use Camera Preview OpMode (Recommended for Testing)

1. **Connect your webcam** to Control Hub 3.0 USB port
2. **Deploy the updated code** to your robot
3. **Run "Camera Preview"** OpMode on the RC app
4. **Look at the RC app screen** - you will see the camera feed with AprilTag overlays

### Option 2: Use AprilTag Autonomous/TeleOp with Preview

Both **AprilTag Autonomous** and **AprilTag TeleOp** now have camera preview enabled:

1. **Connect webcam** to Control Hub USB port
2. **Deploy the code**
3. **Run either OpMode**
4. **Camera preview will appear** on the RC app automatically
5. **AprilTag detections** will be drawn on the preview (boxes, axes, IDs)

---

## 🔧 Configuration

### Webcam Name

The code assumes your webcam is named **"Webcam 1"** in the FTC Robot Controller app.

To check/change this:
1. Open FTC Robot Controller app on the robot controller phone
2. Go to **Menu → Configure Robot**
3. Check the webcam name under the Cameras section
4. If different, update the code:
```java
// In AprilTagVision.java or CameraPreview.java
.setCamera(hardwareMap.get(WebcamName.class, "YOUR_WEBCAM_NAME"))
```

### Camera Resolution

Current resolution: **640x480** (can be changed in code if needed)

---

## 📊 What You'll See

### Camera Preview OpMode
- Live camera feed from your webcam
- AprilTag detection overlays (green boxes, IDs, axes)
- Tag ID information in telemetry
- Resolution and camera status

### AprilTag Autonomous/TeleOp
- Same camera preview
- Tag detection information
- Distance and angle data
- Your robot control data (for TeleOp)

---

## 🎯 Usage Tips

### For Camera Alignment
1. Run "Camera Preview" OpMode
2. Adjust webcam position while watching the preview
3. Ensure AprilTags are clearly visible
4. Check lighting conditions

### For AprilTag Testing
1. Print some AprilTags
2. Place them in the camera's field of view
3. Run "Camera Preview" to verify detection
4. Check detection distance and accuracy

### For Live Robot Control
1. Run "AprilTag TeleOp" with your drive code
2. Watch camera preview while driving
3. Verify tag detection in real-time
4. Use tag data for navigation

---

## 📱 Camera Preview on RC App

The camera preview appears **on the Robot Controller phone screen**, not on your computer.

- The preview shows what the webcam sees
- AprilTag detections are drawn as colored boxes
- Tag IDs are displayed next to detections
- Pose information (axes) is shown on detected tags

---

## 🐛 Troubleshooting

### No Camera Preview
- Check that webcam is connected to USB port
- Verify webcam is recognized by Control Hub
- Check webcam name in FTC RC app configuration
- Ensure cameraMonitorViewId is being used (it is in the code)

### Black Screen
- Check webcam is powered on
- Try different USB port
- Check if another app is using the webcam
- Restart the robot controller

### Poor Detection
- Improve lighting
- Ensure AprilTags are flat and not tilted
- Check camera focus
- Verify AprilTags are properly printed
- Try moving camera closer/further

---

## 📁 Files Modified

- **CameraPreview.java** - New OpMode for camera preview
- **AprilTagVision.java** - Added camera preview support
- **AprilTagAutonomous.java** - Enabled webcam + preview
- **AprilTagTeleOp.java** - Enabled webcam + preview

---

## 🚀 Quick Start

```bash
# Build the project
cd C:\Users\Cezar\AndroidStudioProjects\2026-2027\FtcRobotController
./gradlew :TeamCode:assembleDebug

# Deploy to robot controller (via Android Studio or FTC app)
# Run "Camera Preview" OpMode
# Look at the RC app screen to see camera feed
```
