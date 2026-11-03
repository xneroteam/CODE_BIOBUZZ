package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@TeleOp(name = "Mecanum TeleOp", group = "Robot")
public class TeleOP extends LinearOpMode {

    private DcMotorEx frontLeftDrive;
    private DcMotorEx frontRightDrive;
    private DcMotorEx rearLeftDrive;
    private DcMotorEx rearRightDrive;

    static final double DEADBAND = 0.05;
    // GoBILDA 5203 20:1: 446.28 ticks per revolution of the output shaft
    static final double TICKS_PER_REV = 446.28;

    @Override
    public void runOpMode() {
        // Hardware config names: 0=FrontRight, 1=BackRight, 2=FrontLeft, 3=BackLeft
        frontRightDrive = hardwareMap.get(DcMotorEx.class, "FrontLeft  ");
        rearRightDrive  = hardwareMap.get(DcMotorEx.class, "BackLeft");
        frontLeftDrive  = hardwareMap.get(DcMotorEx.class, "FrontRight");
        rearLeftDrive   = hardwareMap.get(DcMotorEx.class, "BackRight");

        // Mecanum wheels on the left side must be reversed so positive power = forward
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        rearLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        rearRightDrive.setDirection(DcMotor.Direction.FORWARD);

        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Mecanum drive ready. Press PLAY");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double drive  = deadband(gamepad1.left_stick_y);    // forward / back
            double strafe = deadband(-gamepad1.left_stick_x);   // left / right
            double turn   = deadband(gamepad1.right_stick_x);   // rotate in place

            // Mecanum kinematics
            double frontLeft  = drive + strafe + turn;
            double frontRight = drive - strafe - turn;
            double rearLeft   = drive - strafe + turn;
            double rearRight  = drive + strafe - turn;

            // Normalize so no value exceeds 1.0
            double max = Math.max(1.0, Math.max(Math.abs(frontLeft),
                    Math.max(Math.abs(frontRight),
                    Math.max(Math.abs(rearLeft), Math.abs(rearRight)))));

            frontLeftDrive.setPower(frontLeft / max);
            frontRightDrive.setPower(frontRight / max);
            rearLeftDrive.setPower(rearLeft / max);
            rearRightDrive.setPower(rearRight / max);

            // Telemetry from the motor controllers
            double flRpm = rpm(frontLeftDrive);
            double frRpm = rpm(frontRightDrive);
            double rlRpm = rpm(rearLeftDrive);
            double rrRpm = rpm(rearRightDrive);
            double avgRpm = (flRpm + frRpm + rlRpm + rrRpm) / 4.0;

            telemetry.addLine("=== Drive ===");
            telemetry.addData("drive / strafe / turn", "%.2f / %.2f / %.2f", drive, strafe, turn);
            telemetry.addData("robot speed", "%.0f RPM avg", avgRpm);
            telemetry.addLine("=== Motors (RPM | amps | ticks) ===");
            telemetry.addData("frontLeft",  "%6.0f | %.2f | %d", flRpm, current(frontLeftDrive),  frontLeftDrive.getCurrentPosition());
            telemetry.addData("frontRight", "%6.0f | %.2f | %d", frRpm, current(frontRightDrive), frontRightDrive.getCurrentPosition());
            telemetry.addData("rearLeft",   "%6.0f | %.2f | %d", rlRpm, current(rearLeftDrive),   rearLeftDrive.getCurrentPosition());
            telemetry.addData("rearRight",  "%6.0f | %.2f | %d", rrRpm, current(rearRightDrive),  rearRightDrive.getCurrentPosition());
            telemetry.update();
        }

        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        rearLeftDrive.setPower(0);
        rearRightDrive.setPower(0);
    }

    double deadband(double v) {
        return Math.abs(v) < DEADBAND ? 0.0 : v;
    }

    double rpm(DcMotorEx m) {
        return m.getVelocity() * 60.0 / TICKS_PER_REV;
    }

    double current(DcMotorEx m) {
        try {
            return m.getCurrent(CurrentUnit.AMPS);
        } catch (UnsupportedOperationException e) {
            return Double.NaN; // controller doesn't report current
        }
    }
}
