package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Tele")
@Disabled
public class Tele extends OpMode {

    public Spark robot;

    @Override
    public void init() {
        robot = new Spark(this, Spark.Drivetrain.MECHANUM );
    }

    @Override
    public void loop() {
  
            
        

        if (gamepad1.atRest()) robot.rest();
        else {

            robot.mechanumMovT( gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

        
          
        }
    }         
}



