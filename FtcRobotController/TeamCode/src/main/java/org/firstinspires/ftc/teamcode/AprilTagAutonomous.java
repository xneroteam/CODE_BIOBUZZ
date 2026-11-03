package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.List;

/**
 * Example Autonomous OpMode using AprilTag Detection
 * 
 * This OpMode demonstrates how to use AprilTag detection for autonomous navigation.
 * The robot will detect AprilTags and display their information on telemetry.
 */
@Autonomous(name = "AprilTag Autonomous", group = "Autonomous")
public class AprilTagAutonomous extends LinearOpMode {
    
    private AprilTagVision aprilTagVision;
    
    // Tag IDs you want to detect (modify these based on your field setup)
    private static final int TARGET_TAG_ID = 1;
    private static final int[] ALL_TAG_IDS = {1, 2, 3, 4, 5, 6};
    
    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize AprilTag vision
        aprilTagVision = new AprilTagVision();
        
        // Use external webcam (connected to Control Hub USB port)
        boolean useWebcam = true;
        
        // Get camera monitor view ID (this will show the camera preview on the RC app)
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        
        if (useWebcam) {
            // Initialize with external webcam
            aprilTagVision.initialize(hardwareMap, cameraMonitorViewId);
        } else {
            // Initialize with internal phone camera
            aprilTagVision.initializeInternalCamera(hardwareMap, cameraMonitorViewId);
        }
        
        telemetry.addData("Status", "Initializing camera...");
        telemetry.update();
        
        // Wait for camera to initialize
        while (!isStarted() && !isStopRequested()) {
            if (aprilTagVision.isInitialized()) {
                telemetry.addData("Status", "Camera initialized");
                telemetry.update();
                break;
            }
            sleep(100);
        }
        
        waitForStart();
        
        if (isStopRequested()) {
            aprilTagVision.stop();
            return;
        }
        
        // Main autonomous loop
        while (opModeIsActive() && !isStopRequested()) {
            // Update detections
            aprilTagVision.update();
            
            // Get all detections
            List<AprilTagDetection> detections = aprilTagVision.getDetections();
            
            // Display detection information
            telemetry.addData("Number of Tags", detections.size());
            
            if (detections.size() > 0) {
                // Show info for each detected tag
                for (AprilTagDetection detection : detections) {
                    if (detection instanceof AprilTagSingleDetection) {
                        AprilTagSingleDetection singleDet = (AprilTagSingleDetection) detection;
                        telemetry.addData("Tag ID", singleDet.id);
                        telemetry.addData("Tag X", singleDet.center.x);
                        telemetry.addData("Tag Y", singleDet.center.y);
                        
                        if (detection.ftcPose != null) {
                            telemetry.addData("Distance", detection.ftcPose.z);
                            telemetry.addData("Yaw (deg)", detection.ftcPose.yaw);
                            telemetry.addData("Pitch (deg)", detection.ftcPose.pitch);
                            telemetry.addData("Roll (deg)", detection.ftcPose.roll);
                        }
                    }
                }
                
                // Check for specific target tag
                AprilTagDetection targetTag = aprilTagVision.getDetectionById(TARGET_TAG_ID);
                if (targetTag != null) {
                    telemetry.addData("Target Tag Found", "YES");
                    telemetry.addData("Target Distance", aprilTagVision.getDistance(targetTag));
                    telemetry.addData("Target Angle", aprilTagVision.getAngleToTag(targetTag));
                    
                    // Add your autonomous logic here based on tag position
                    // Example: navigate to tag
                    navigateToTag(targetTag);
                } else {
                    telemetry.addData("Target Tag Found", "NO");
                }
                
                // Show closest tag
                AprilTagDetection closest = aprilTagVision.getClosestTag();
                if (closest != null) {
                    if (closest instanceof AprilTagSingleDetection) {
                        telemetry.addData("Closest Tag ID", ((AprilTagSingleDetection) closest).id);
                    }
                    telemetry.addData("Closest Distance", aprilTagVision.getDistance(closest));
                }
            } else {
                telemetry.addData("Status", "No tags detected");
            }
            
            telemetry.update();
            sleep(50); // Update at ~20Hz
        }
        
        // Cleanup
        aprilTagVision.stop();
    }
    
    /**
     * Example navigation logic based on tag detection
     * Replace this with your actual robot movement code
     */
    private void navigateToTag(AprilTagDetection tag) {
        double distance = aprilTagVision.getDistance(tag);
        double angle = aprilTagVision.getAngleToTag(tag);
        
        // Example navigation logic (pseudo-code)
        // You would replace this with your actual motor control code
        
        /*
        if (distance > 0.5) {
            // Move forward if too far
            robotDrive.forward(0.3);
        } else if (distance < 0.3) {
            // Move backward if too close
            robotDrive.backward(0.3);
        } else {
            // Stop if at correct distance
            robotDrive.stop();
        }
        
        if (angle > 5) {
            // Turn right if tag is to the right
            robotDrive.turnRight(0.2);
        } else if (angle < -5) {
            // Turn left if tag is to the left
            robotDrive.turnLeft(0.2);
        }
        */
        
        telemetry.addData("Navigation", "Dist: %.2f, Angle: %.2f", distance, angle);
    }
}
