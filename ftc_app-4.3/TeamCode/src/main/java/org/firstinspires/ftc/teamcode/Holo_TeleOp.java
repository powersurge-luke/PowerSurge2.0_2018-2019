package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


/*
   Robot wheel mapping:
          X FRONT X
        X           X
      X  FL       FR  X
              X
             XXX
              X
      X  BL       BR  X
        X           X
          X       X
*/

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Holonomic")
//@Disabled
public class Holo_TeleOp extends LinearOpMode {
    public HardwareMappings robot = new HardwareMappings();



    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        Methods_Climbing climber = new Methods_Climbing(robot, this);
        Methods_Intake intake = new Methods_Intake(robot, this);
        Methods_DriveTrain drivetrain = new Methods_DriveTrain(robot, this);
        Methods_Sensors sensor = new Methods_Sensors(robot, this);


        waitForStart();

        while (opModeIsActive()) {

            //drivetrain
            if(gamepad1.right_bumper) {
                robot.power = 1;
            }
            else if(gamepad1.left_bumper) {
                robot.power = .8;
            }

            drivetrain.Drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, robot.power);

        //intakeExtender
            if (sensor.isSlideIn()){
                if(gamepad2.left_stick_y > 0) {
                    intake.IntakeExtender(gamepad2.left_stick_y * .7);
                }
                else {
                    intake.IntakeExtender(0);
                }
            }
            else{
                intake.IntakeExtender(gamepad2.left_stick_y * .7);
            }


            if (gamepad2.y){
                intake.IntakeFolder(.6);
            }
            else if (gamepad2.a){
                intake.IntakeFolder(-.6);
            }
            else {
                intake.IntakeFolder(0);
            }


        //hopperFolder
        if (gamepad2.right_bumper && (!sensor.isLiftDown())) {
            intake.HopperFolder(1);
        }
        else if (gamepad2.left_bumper){
            intake.HopperFolder(.1);
        }

        //intake
        if (gamepad2.b) {
            intake.Intake(1);
        }
        else if (gamepad2.x){
            intake.Intake(-.7);
        }
        else {
            intake.Intake(0);
        }

        //touch sensor outputs
        telemetry.addData("lift", sensor.isLiftDown());
        telemetry.addData("slides", sensor.isSlideIn());

        //climber
        if (sensor.isLiftDown()){
        if(gamepad2.dpad_up) {
            climber.Lock(.5);
            climber.MoveClimb(1);
        }
        else {
            climber.MoveClimb(0);
        }
        }
        else if (sensor.isLiftUp()) {
            if (gamepad2.dpad_down) {
                climber.MoveClimb(-1);
            } else {
                climber.MoveClimb(0);
            }
        }
        else {
            if (gamepad2.dpad_up) {
                climber.MoveClimb(1);
            } else if (gamepad2.dpad_down) {
                climber.MoveClimb(-1);
            } else {
                climber.MoveClimb(0);
            }
        }

        //lock
        if (gamepad1.x) {
            climber.Lock(0.5);
        } else if (gamepad1.b) {
            climber.Lock(0);
        }


        if (gamepad1.y) {
            robot.marker.setPosition(.5);
        }
        else if (gamepad1.y) {
            robot.marker.setPosition(1);
        }

        telemetry.addData("lx", gamepad1.left_stick_x);
        telemetry.addData("ly", gamepad1.left_stick_y);
        telemetry.addData("rx", gamepad1.right_stick_x);
        telemetry.update();

        }
    }
}
