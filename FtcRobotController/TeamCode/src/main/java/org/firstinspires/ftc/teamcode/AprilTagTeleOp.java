package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.List;

/**
 * TeleOp OpMode with AprilTag Detection
 * 
 * This OpMode allows you to control the robot while simultaneously detecting AprilTags.
 * Useful for testing and debugging computer vision during driver-controlled periods.
 */
@TeleOp(name = "AprilTag TeleOp", group = "TeleOp")
public class AprilTagTeleOp extends LinearOpMode {
    
    private AprilTagVision aprilTagVision;
    
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
        
        // Main TeleOp loop
        while (opModeIsActive() && !isStopRequested()) {
            // Update AprilTag detections
            aprilTagVision.update();
            
            // Get gamepad inputs (add your robot control here)
            double leftStickY = -gamepad1.left_stick_y;
            double leftStickX = gamepad1.left_stick_x;
            double rightStickX = gamepad1.right_stick_x;
            
            // Example: Your robot control code would go here
            // robotDrive.setMotorPower(leftStickY, leftStickX, rightStickX);
            
            // Display AprilTag information
            List<AprilTagDetection> detections = aprilTagVision.getDetections();
            
            telemetry.addData("Detections", detections.size());
            
            if (detections.size() > 0) {
                for (AprilTagDetection detection : detections) {
                    if (detection instanceof AprilTagSingleDetection) {
                        AprilTagSingleDetection singleDet = (AprilTagSingleDetection) detection;
                        telemetry.addData("Tag ID", singleDet.id);
                        telemetry.addData("Center X", "%.2f", singleDet.center.x);
                        telemetry.addData("Center Y", "%.2f", singleDet.center.y);
                        
                        if (detection.ftcPose != null) {
                            telemetry.addData("Distance", "%.2f", detection.ftcPose.z);
                            telemetry.addData("Yaw", "%.2f°", detection.ftcPose.yaw);
                        }
                    }
                }
                
                // Show closest tag
                AprilTagDetection closest = aprilTagVision.getClosestTag();
                if (closest != null) {
                    if (closest instanceof AprilTagSingleDetection) {
                        telemetry.addData("Closest Tag", ((AprilTagSingleDetection) closest).id);
                    }
                    telemetry.addData("Closest Dist", "%.2f", aprilTagVision.getDistance(closest));
                }
            } else {
                telemetry.addData("Status", "No tags detected");
            }
            
            // Show gamepad data
            telemetry.addData("Left Stick Y", "%.2f", leftStickY);
            telemetry.addData("Left Stick X", "%.2f", leftStickX);
            telemetry.addData("Right Stick X", "%.2f", rightStickX);
            
            telemetry.update();
            sleep(50); // Update at ~20Hz
        }
        
        // Cleanup
        aprilTagVision.stop();
    }
}
