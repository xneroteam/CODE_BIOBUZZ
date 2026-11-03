package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.List;

/**
 * Camera Preview OpMode
 * 
 * This OpMode displays the camera feed from your connected webcam.
 * Useful for verifying camera alignment and AprilTag detection.
 * Connect your webcam to Control Hub 3.0 USB port before starting.
 */
@TeleOp(name = "Camera Preview", group = "Utility")
public class CameraPreview extends LinearOpMode {
    
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTagProcessor;
    
    @Override
    public void runOpMode() throws InterruptedException {
        // Create AprilTag processor (optional - for visualization)
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(true)
                .setDrawTagID(true)
                .build();
        
        // Get camera monitor view ID for displaying the camera feed on RC app
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        
        // Initialize VisionPortal with webcam and camera preview enabled
        VisionPortal.Builder builder = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTagProcessor)
                .setCameraResolution(new Size(640, 480));
        
        // Enable camera preview (shows on RC app)
        if (cameraMonitorViewId != 0) {
            builder.setLiveViewContainerId(cameraMonitorViewId);
        }
        builder.enableLiveView(true);
        
        visionPortal = builder.build();
        
        telemetry.addData("Status", "Camera initialized");
        telemetry.addData("Camera", "Webcam 1 (USB)");
        telemetry.addData("Resolution", "640x480");
        telemetry.addData("Camera Preview", "Enabled on RC app");
        telemetry.addData("AprilTag Processing", "Enabled");
        telemetry.addData("Instructions", "Look at the RC app to see camera preview");
        telemetry.update();
        
        waitForStart();
        
        if (isStopRequested()) {
            visionPortal.close();
            return;
        }
        
        telemetry.addData("Status", "Camera preview running");
        telemetry.update();
        
        // Main loop - just keep the camera running
        while (opModeIsActive() && !isStopRequested()) {
            // Show detection info if any tags are visible
            List<AprilTagDetection> detections = aprilTagProcessor.getDetections();
            telemetry.addData("Tags Detected", detections.size());
            
            if (detections.size() > 0) {
                for (AprilTagDetection detection : detections) {
                    if (detection instanceof AprilTagSingleDetection) {
                        AprilTagSingleDetection singleDet = (AprilTagSingleDetection) detection;
                        telemetry.addData("Tag ID", singleDet.id);
                    }
                }
            }
            
            telemetry.update();
            sleep(100);
        }
        
        // Cleanup
        visionPortal.close();
    }
}
