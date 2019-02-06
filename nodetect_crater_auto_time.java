package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous (name = "Crater")

public class nodetect_crater_auto_time extends LinearOpMode {
    public HardwareMappings robot = new HardwareMappings();


    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        Methods_Climbing climber = new Methods_Climbing(robot, this);
        Methods_Intake intake = new Methods_Intake(robot, this);
        Methods_DriveTrain drivetrain = new Methods_DriveTrain(robot, this);
        Methods_Sensors sensor = new Methods_Sensors(robot, this);

        waitForStart();

        //hold marker
        robot.marker.setPosition(1);

        //relieve pressure off lock
        climber.MoveClimb(1);
        while (opModeIsActive() && !sensor.isLiftDown()){
            telemetry.addData("touch", sensor.isLiftDown());
        }

        //unlock
        climber.Lock(.5);

        //drop from lander
        climber.liftUp();
        sleep(100);

        //drive forward to knock of the center mineral
        drivetrain.timedDrive(1.5, 1, 0, 0, 1);

        intake.Intake(1);
        sleep(200);
        intake.Intake(0);
    }
}