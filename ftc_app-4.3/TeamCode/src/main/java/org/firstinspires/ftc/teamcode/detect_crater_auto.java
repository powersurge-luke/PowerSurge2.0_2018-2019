package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

@Autonomous (name = "Detect Crater")

public class detect_crater_auto extends LinearOpMode {
    public HardwareMappings robot = new HardwareMappings();

    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private int i = 0;

    public String mineralPosition = null;


    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);
        robot.initVuforia(hardwareMap);

        while (!robot.vuforiaLoaded && !isStopRequested()){

        }


        Methods_Climbing climber = new Methods_Climbing(robot, this);
        Methods_Intake intake = new Methods_Intake(robot, this);
        Methods_DriveTrain drivetrain = new Methods_DriveTrain(robot, this);
        Methods_Sensors sensor = new Methods_Sensors(robot, this);

        //init TensorFlow
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            robot.initTfod(hardwareMap);
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }


        waitForStart();

        //hold marker
        robot.marker.setPosition(1);

        //init TensorFlow
        if (robot.tfod != null) {
            robot.tfod.activate();
        }

        ElapsedTime runtime = new ElapsedTime();

        double timeout = runtime.seconds() + 8;

        //scan for minerals
        while (opModeIsActive() && mineralPosition == null && runtime.seconds() < timeout){
            if (robot.tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = robot.tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    telemetry.addLine();
                    if (updatedRecognitions.size() > 1) {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;
                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData("Object Detected:", recognition.getLabel());
                            telemetry.addData("Confidence", recognition.getConfidence());
                            telemetry.addData("Height", recognition.getHeight());
                            telemetry.addData("Bottom Coordinate", recognition.getBottom());
                            telemetry.addLine();

                            if (recognition.getHeight() < 130 && recognition.getBottom() < 490) {
                                i++;
                            }

                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL) && recognition.getHeight() > 130) {
                                goldMineralX = (int) recognition.getLeft();
                            } else if (silverMineral1X == -1 && recognition.getHeight() > 130) {
                                silverMineral1X = (int) recognition.getLeft();
                            } else{
                                silverMineral2X = (int) recognition.getLeft();
                            }
                        }
                        if (goldMineralX != -1 && (silverMineral1X != -1 || silverMineral2X != -1)) {
                            if (goldMineralX < silverMineral1X || goldMineralX < silverMineral2X) {
                                telemetry.addData("Gold Mineral Position", "Left");
                                mineralPosition = "left";
                            } else if (goldMineralX > silverMineral1X || goldMineralX > silverMineral2X) {
                                telemetry.addData("Gold Mineral Position", "Center");
                                mineralPosition = "center";
                            }
                        } else {
                            telemetry.addData("Gold Mineral Position", "Right");
                            mineralPosition = "right";
                        }
                    }
                    telemetry.update();
                }
            }
        }
        if (robot.tfod != null) {
        robot.tfod.shutdown();
    }

        //relieve pressure off lock
        while (!sensor.isLiftDown()) {
            climber.MoveClimb(-1);
        }

        //unlock
        climber.Lock(.5);

        //drop from lander
        climber.liftUp();

        //drive to and knock off correct mineral
        if (mineralPosition == ("left")) {
            drivetrain.timedDrive(1.5, 1, -1, 0, .7);
        } else if (mineralPosition == ("right")) {
            drivetrain.timedDrive(2.2, 1, 1, 0, .7);
        }
        else {
            drivetrain.timedDrive(1.6, 1, .2, 0, .7);
        }

        robot.intakeFolder.setPower(-1);
        sleep(100);
        robot.intakeFolder.setPower(0);

        drivetrain.timedDrive(4, -1, -.2, 0, .5);
    }
}