package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;


public class Methods_DriveTrain extends HardwareMappings {
    private LinearOpMode opModeObj = null;

    public HardwareMappings robot = null;

    public Methods_DriveTrain(HardwareMappings hardwaremap, LinearOpMode opMode) {
        robot = hardwaremap;
        opModeObj = opMode;
    }

    public void Drive(double ly, double lx, double rx, double power) {
        double FrontLeft = -ly - lx - rx;
        double FrontRight = ly - lx - rx;
        double BackRight = ly + lx - rx;
        double BackLeft = -ly + lx - rx;


        // clip the right/left values so that the values never exceed +/- 1
        FrontRight = Range.clip(FrontRight, -1, 1);
        FrontLeft = Range.clip(FrontLeft, -1, 1);
        BackLeft = Range.clip(BackLeft, -1, 1);
        BackRight = Range.clip(BackRight, -1, 1);

        // write the values to the motors
        robot.motorFrontRight.setPower(-FrontRight*power*.8);
        robot.motorFrontLeft.setPower(-FrontLeft*power);
        robot.motorBackLeft.setPower(-BackLeft*power*.8);
        robot.motorBackRight.setPower(-BackRight*power);
    }

    public void timedDrive(double time, double ly, double lx, double rx, double power){

        ElapsedTime runtime = new ElapsedTime();

        runtime.reset();
        double timeToDrive = runtime.seconds() + time;
        while (opModeObj.opModeIsActive() && runtime.seconds() < timeToDrive) {
            Drive(ly, lx, rx,power);
        }

        robot.motorFrontLeft.setPower(0);
        robot.motorFrontRight.setPower(0);
        robot.motorBackLeft.setPower(0);
        robot.motorBackRight.setPower(0);

    }

    public void encoderDrive(int Fl, int Fr, int Bl, int Br, double speed, double timeout) {

        int FLTarget;
        int FRTarget;
        int BLTarget;
        int BRTarget;
        ElapsedTime runtime = new ElapsedTime();

        final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
        final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
        final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
        final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                (WHEEL_DIAMETER_INCHES * 3.1415);

        // Ensure that the opmode is still active
        if (opModeObj.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            FLTarget = robot.motorFrontLeft.getCurrentPosition() + Fl;
            FRTarget = robot.motorFrontRight.getCurrentPosition() + Fr;
            BLTarget = robot.motorBackLeft.getCurrentPosition() + Bl;
            BRTarget = robot.motorBackRight.getCurrentPosition() + Br;
            robot.motorFrontLeft.setTargetPosition(FLTarget);
            robot.motorFrontRight.setTargetPosition(FRTarget);
            robot.motorBackLeft.setTargetPosition(BLTarget);
            robot.motorBackRight.setTargetPosition(BRTarget);

            // Turn On RUN_TO_POSITION
            robot.motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.motorFrontLeft.setPower(speed + 0.3);
            robot.motorFrontRight.setPower(speed);
            robot.motorBackLeft.setPower(speed);
            robot.motorBackRight.setPower(speed + 0.3);

            while (opModeObj.opModeIsActive() &&
                    (runtime.seconds() < timeout) &&
                    (robot.motorFrontLeft.isBusy() || robot.motorBackRight.isBusy())) {

            }

            // Stop all motion;
            robot.motorFrontLeft.setPower(0);
            robot.motorFrontRight.setPower(0);
            robot.motorBackLeft.setPower(0);
            robot.motorBackRight.setPower(0);

            robot.motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


            opModeObj.telemetry.addData("Finished driving:"," Done");
            opModeObj.telemetry.update();

            // Turn off RUN_TO_POSITION
            robot.motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void resetEncoders() {
        robot.motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
