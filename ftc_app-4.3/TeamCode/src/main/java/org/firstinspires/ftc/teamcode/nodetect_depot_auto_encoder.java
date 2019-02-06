package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous (name = "EncoderDepot")

public class nodetect_depot_auto_encoder extends LinearOpMode {
    public HardwareMappings robot = new HardwareMappings();


    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        robot.motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Methods_Climbing climber = new Methods_Climbing(robot, this);
        Methods_Intake intake = new Methods_Intake(robot, this);
        Methods_DriveTrain drivetrain = new Methods_DriveTrain(robot, this);
        Methods_Sensors sensor = new Methods_Sensors(robot, this);

        waitForStart();

        //hold marker
        robot.marker.setPosition(1);

        //relieve pressure off lock
        climber.MoveClimb(-1);
        while (opModeIsActive() && !sensor.isLiftDown()){
            telemetry.addData("touch", sensor.isLiftDown());
        }

        //unlock
        climber.Lock(.5);

        //drop from lander
        climber.liftUp();

        //drive straight, knock off the center mineral and continue to depot
        drivetrain.encoderDrive(3800, -3800, 3800, -3800, .5, 10);

        //deploy marker
        robot.marker.setPosition(.5);

    }
}