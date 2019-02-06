package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


public class Methods_Climbing {
    public static HardwareMappings robot = null;
    private LinearOpMode opModeObj = null;
    private Methods_Sensors sensor = new Methods_Sensors(robot, opModeObj);

    public Methods_Climbing(HardwareMappings hardwaremap, LinearOpMode opMode){
        robot = hardwaremap;
        opModeObj = opMode;
    }


    public void MoveClimb(double power){
        robot.climbingMotor.setPower(power);

    }

    public void Lock(double position)
    {
        robot.lock.setPosition(position);
    }


    public void liftUp() {

        ElapsedTime runtime = new ElapsedTime();

        runtime.reset();
        double timeout = runtime.seconds() + 3;

        while (opModeObj.opModeIsActive() && !sensor.isLiftUp() && runtime.seconds() < timeout) {
            robot.climbingMotor.setPower(1);
        }
        robot.climbingMotor.setPower(0);
    }

    public void liftDown() {

        while (opModeObj.opModeIsActive() && !sensor.isLiftDown()) {
            robot.climbingMotor.setPower(-1);
        }
        robot.climbingMotor.setPower(0);

    }
}