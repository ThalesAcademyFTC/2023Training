package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class GabeSpark {

    /* Hardware map for the opmode */
    HardwareMap hwMap;

    public DcMotor motor1, motor2, motor3, motor4;
    public DcMotor[] forward, left, right;

    public GabeSpark(HardwareMap hardwareMap){
        hwMap = hardwareMap;

        motor1 = hwMap.dcMotor.get("motor1");
        motor2 = hwMap.dcMotor.get("motor2");
        motor3 = hwMap.dcMotor.get("motor3");
        motor4 = hwMap.dcMotor.get("motor4");

        forward = new DcMotor[]{motor1, motor2, motor3, motor4};
        left = new DcMotor[]{motor1, motor3};
        right = new DcMotor[]{motor2, motor4};

    }

    public void rest(){
        for(DcMotor x: forward){
            x.setPower(0);
        }
    }

    public void moveForward(double speed){
        for(DcMotor x: forward){
            x.setPower(speed);
        }
    }

    public void turnLeft(double speed){
        for(DcMotor x: left){
            x.setPower(-speed);
        }

        for(DcMotor x: right){
            x.setPower(speed);
        }
    }

    public void turnRight(double speed){
        for(DcMotor x: left){
            x.setPower(speed);
        }

        for(DcMotor x: right){
            x.setPower(-speed);
        }
    }

}
