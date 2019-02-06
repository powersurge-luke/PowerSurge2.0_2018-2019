package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Methods_Intake {
    public static HardwareMappings robot = null;
    private LinearOpMode opModeObj = null;

    Methods_Sensors sensor = new Methods_Sensors(robot, opModeObj);


    public Methods_Intake(HardwareMappings hardwaremap, LinearOpMode opMode){
        robot = hardwaremap;
        opModeObj = opMode;
    }

    public void Intake (double power){
        robot.intake.setPower(power);
    }

    public void IntakeFolder (double power) {
        robot.intakeFolder.setPower(power);
        robot.intakeFolder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }


    public void HopperFolder (double position){
        robot.hopperFolderRight.setPosition(1 - position);
        robot.hopperFolderLeft.setPosition(position);
    }

    public void IntakeExtender (double speed) {
        robot.intakeExtender.setPower(speed);
    }

    public void PrepareForCollectingMinerals () {

        int IntakeFolderTarget = 2000;

        // Determine new target position, and pass to motor controller
        robot.intakeFolder.setTargetPosition(IntakeFolderTarget);

        // Turn On RUN_TO_POSITION
        robot.intakeFolder.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        ElapsedTime runtime = new ElapsedTime();

        robot.intakeFolder.setPower(1);

        runtime.reset();
        double timeout = runtime.seconds() + 2;

        // reset the timeout time and start motion.
        runtime.reset();
        robot.intakeFolder.setPower(1);


        while (robot.climbingMotor.isBusy() || robot.intakeExtender.isBusy() || robot.intakeFolder.isBusy()) {
            if(!sensor.isLiftDown()) {
                robot.climbingMotor.setPower(-1);
            }
            if(runtime.seconds() < timeout) {
                robot.intakeExtender.setPower(1);
            }
            if(robot.intakeFolder.getCurrentPosition() < IntakeFolderTarget && opModeObj.opModeIsActive()) {

                    // Stop all motion;
                    robot.intakeFolder.setPower(0);

                    // Turn off RUN_TO_POSITION
                    robot.intakeFolder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                    robot.intakeFolder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }


        }
    }
}