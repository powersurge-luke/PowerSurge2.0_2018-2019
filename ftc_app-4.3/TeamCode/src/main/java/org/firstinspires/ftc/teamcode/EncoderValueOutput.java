package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "EncoderTest")
//@Disabled
    public class EncoderValueOutput extends LinearOpMode {
        public HardwareMappings robot = new HardwareMappings();


        @Override
        public void runOpMode() throws InterruptedException {
            robot.init(hardwareMap);

            Methods_DriveTrain drivetrain = new Methods_DriveTrain(robot, this);

            robot.motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


            waitForStart();

            while (opModeIsActive()) {

                telemetry.addData("FL", robot.motorFrontLeft.getCurrentPosition());
                telemetry.addData("FR", robot.motorFrontRight.getCurrentPosition());
                telemetry.addData("BL", robot.motorBackLeft.getCurrentPosition());
                telemetry.addData("BR", robot.motorBackRight.getCurrentPosition());
                telemetry.update();


            }
        }
    }
