package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

public class HardwareMappings {

    DcMotor motorFrontRight = null;
    DcMotor motorFrontLeft = null;
    DcMotor motorBackRight = null;
    DcMotor motorBackLeft = null;
    DcMotor climbingMotor = null;
    DcMotor intakeExtender = null;
    DcMotor intakeFolder = null;
    DcMotor intake = null;

    Servo hopperFolderRight = null;
    Servo hopperFolderLeft = null;
    Servo lock = null;
    Servo marker = null;

    DigitalChannel extender = null;
    DigitalChannel slideTouchBottom = null;
    DigitalChannel slideTouchTop = null;
    boolean vuforiaLoaded = false;
    //DigitalChannel slidesTouch = null;

    HardwareMap hwMap = null;

    public final double servoGearRatio = (1);
    public final double ticksPerRev = 280;
    public final double inchesPerRev = 6.28;
    public final double servoConversion = 1/360;
    public double power = .7;
    public double angle = 0;
    public double servoPosition = 0;
    public int position = 1;

    public static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    public static final String VUFORIA_KEY = "AUVgjQP/////AAABmb3BI0UfXkvXjf8m09dxfUw/8W7ad8JRA3P1XUkCK1AJOsuf0wQcdSp0dwpEn4j7j+UaGpLMrav8Me2tS2PIYRl3CA42abXYvWlQqRD6Kc/1CB+JzGxn+z5FE0rU+NN1u4pIPRURZK7UAXePLUL9Uq+B4awZ/DAczA5vaQPodGK97YTkWwGI5lUXoMzPiJNz5xJ+w5toHDmEx02Wh+sd047dWszFAyXJQKOFdah5JXIB9m1TXU+wWeI/f3EZ23cOgBLYz2XaGDUvqT9LENNmRqh53SDbrWyjUPv7mK2KZpEOZOoCv3xNVdvanH4YTvzNIIU6xUFwPFiTxSG1fFzsOCICLsFgew3wWfSDFtpAq7kz";

    public VuforiaLocalizer vuforia;

    public TFObjectDetector tfod;



    /* Constructor */
    public HardwareMappings(){

    }

    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        motorFrontRight = hwMap.get(DcMotor.class, "motor front right");
        motorFrontLeft = hwMap.get(DcMotor.class, "motor front left");
        motorBackLeft = hwMap.get(DcMotor.class, "motor back left");
        motorBackRight = hwMap.get(DcMotor.class, "motor back right");
        climbingMotor = hwMap.get(DcMotor.class, "ClimbDrive");
        intakeExtender = hwMap.get(DcMotor.class, "intakeExtender");
        intakeFolder = hwMap.get(DcMotor.class, "intakeFolder");
        intake = hwMap.get(DcMotor.class, "intake");

        hopperFolderLeft = hwMap.get(Servo.class, "hopperFolderLeft");
        hopperFolderRight = hwMap.get(Servo.class, "hopperFolderRight");
        lock = hwMap.get(Servo.class, "lock");
        marker = hwMap.get(Servo.class, "marker");



        extender = hwMap.get(DigitalChannel.class, "extender");
        slideTouchBottom = hwMap.get(DigitalChannel.class, "bottomSlide");
        slideTouchTop = hwMap.get(DigitalChannel.class, "topSlide");



    }

    public void initVuforia(HardwareMap ahwMap) {

        //Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        vuforiaLoaded = true;

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    public void initTfod(HardwareMap ahwMap) {
        int tfodMonitorViewId = ahwMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", ahwMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.4;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }


}
