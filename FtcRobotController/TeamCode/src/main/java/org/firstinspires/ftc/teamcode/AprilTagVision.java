package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.ArrayList;
import java.util.List;

/**
 * AprilTag Vision Class for FTC
 * 
 * This class provides AprilTag detection using the FTC Vision SDK.
 * It can detect AprilTags from the camera and provide their IDs, positions, and orientations.
 */
public class AprilTagVision {
    
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTagProcessor;
    
    // Detection results
    private List<AprilTagDetection> detections = new ArrayList<>();
    private boolean initialized = false;
    
    /**
     * Initialize the camera and AprilTag processor
     * @param hardwareMap The hardware map from your OpMode
     * @param cameraMonitorViewId The ID of the camera monitor view (will show preview on RC app)
     */
    public void initialize(HardwareMap hardwareMap, int cameraMonitorViewId) {
        try {
            // Create AprilTag processor
            aprilTagProcessor = new AprilTagProcessor.Builder()
                    .setDrawAxes(true)
                    .setDrawCubeProjection(true)
                    .setDrawTagOutline(true)
                    .setDrawTagID(true)
                    .build();
            
            // Create VisionPortal with camera monitor view
            VisionPortal.Builder builder = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(aprilTagProcessor)
                    .setCameraResolution(new Size(640, 480));
            
            // Add camera monitor view if provided (shows preview on RC app)
            if (cameraMonitorViewId != 0) {
                builder.setLiveViewContainerId(cameraMonitorViewId);
            }
            builder.enableLiveView(true);
            
            visionPortal = builder.build();
            
            initialized = true;
            
        } catch (Exception e) {
            initialized = false;
            e.printStackTrace();
        }
    }
    
    /**
     * Initialize with internal camera (phone camera)
     * @param hardwareMap The hardware map from your OpMode
     * @param cameraMonitorViewId The ID of the camera monitor view (will show preview on RC app)
     */
    public void initializeInternalCamera(HardwareMap hardwareMap, int cameraMonitorViewId) {
        try {
            // Create AprilTag processor
            aprilTagProcessor = new AprilTagProcessor.Builder()
                    .setDrawAxes(true)
                    .setDrawCubeProjection(true)
                    .setDrawTagOutline(true)
                    .setDrawTagID(true)
                    .build();
            
            // Create VisionPortal with internal camera and camera monitor view
            VisionPortal.Builder builder = new VisionPortal.Builder()
                    .setCamera(BuiltinCameraDirection.BACK)
                    .addProcessor(aprilTagProcessor)
                    .setCameraResolution(new Size(640, 480));
            
            // Add camera monitor view if provided (shows preview on RC app)
            if (cameraMonitorViewId != 0) {
                builder.setLiveViewContainerId(cameraMonitorViewId);
            }
            builder.enableLiveView(true);
            
            visionPortal = builder.build();
            
            initialized = true;
            
        } catch (Exception e) {
            initialized = false;
            e.printStackTrace();
        }
    }
    
    /**
     * Update the detection results
     * Call this method periodically to get the latest detections
     */
    public void update() {
        if (initialized && aprilTagProcessor != null) {
            detections = aprilTagProcessor.getDetections();
        }
    }
    
    /**
     * Get all detected AprilTags
     * @return List of AprilTagDetection objects
     */
    public List<AprilTagDetection> getDetections() {
        return detections;
    }
    
    /**
     * Get a specific AprilTag by ID
     * @param id The AprilTag ID to search for
     * @return AprilTagDetection if found, null otherwise
     */
    public AprilTagDetection getDetectionById(int id) {
        for (AprilTagDetection detection : detections) {
            if (detection instanceof AprilTagSingleDetection) {
                AprilTagSingleDetection singleDet = (AprilTagSingleDetection) detection;
                if (singleDet.id == id) {
                    return detection;
                }
            }
        }
        return null;
    }
    
    /**
     * Check if a specific AprilTag ID is visible
     * @param id The AprilTag ID to check
     * @return true if the tag is visible, false otherwise
     */
    public boolean isTagVisible(int id) {
        return getDetectionById(id) != null;
    }
    
    /**
     * Get the number of detected tags
     * @return Number of AprilTags currently visible
     */
    public int getDetectionCount() {
        return detections.size();
    }
    
    /**
     * Get the closest AprilTag (by distance)
     * @return The closest AprilTagDetection, or null if no tags detected
     */
    public AprilTagDetection getClosestTag() {
        if (detections.isEmpty()) {
            return null;
        }
        
        AprilTagDetection closest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (AprilTagDetection detection : detections) {
            double distance = getDistance(detection);
            if (distance < minDistance) {
                minDistance = distance;
                closest = detection;
            }
        }
        
        return closest;
    }
    
    /**
     * Calculate distance to a tag
     * @param detection The AprilTag detection
     * @return Distance in inches (or Double.MAX_VALUE if invalid)
     */
    public double getDistance(AprilTagDetection detection) {
        if (detection == null || detection.ftcPose == null) {
            return Double.MAX_VALUE;
        }
        
        return detection.ftcPose.z;
    }
    
    /**
     * Get the horizontal angle to a tag
     * @param detection The AprilTag detection
     * @return Yaw angle in degrees (negative = left, positive = right)
     */
    public double getAngleToTag(AprilTagDetection detection) {
        if (detection == null || detection.ftcPose == null) {
            return 0;
        }
        
        return detection.ftcPose.yaw;
    }
    
    /**
     * Stop the camera and release resources
     */
    public void stop() {
        if (visionPortal != null) {
            visionPortal.close();
        }
        initialized = false;
    }
    
    /**
     * Check if the detection system is initialized
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Get VisionPortal reference for advanced usage
     * @return VisionPortal instance
     */
    public VisionPortal getVisionPortal() {
        return visionPortal;
    }
    
    /**
     * Get AprilTag processor for advanced usage
     * @return AprilTagProcessor instance
     */
    public AprilTagProcessor getProcessor() {
        return aprilTagProcessor;
    }
}
