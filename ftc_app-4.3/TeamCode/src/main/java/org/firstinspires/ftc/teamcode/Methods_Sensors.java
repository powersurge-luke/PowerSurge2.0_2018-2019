package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

public class Methods_Sensors extends HardwareMappings {

    public static HardwareMappings robot = null;
    private LinearOpMode opModeObj;


    public Methods_Sensors(HardwareMappings hardwaremap, LinearOpMode opMode){
        robot = hardwaremap;
        opModeObj = opMode;
    }

    public boolean isLiftDown(){
        robot.slideTouchBottom.setMode(DigitalChannel.Mode.INPUT);

        if(robot.slideTouchBottom.getState()){
            return false;
        }
        else{
            return true;
        }
    }
    public boolean isSlideIn(){
        robot.extender.setMode(DigitalChannel.Mode.INPUT);

        if(robot.extender.getState()){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean isLiftUp(){
        robot.slideTouchTop.setMode(DigitalChannel.Mode.INPUT);

        if(robot.slideTouchTop.getState()){
            return false;
        }
        else{
            return true;
        }
    }
}
