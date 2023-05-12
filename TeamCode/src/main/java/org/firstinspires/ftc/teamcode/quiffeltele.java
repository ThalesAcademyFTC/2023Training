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


            //Ryan and Ayden's Carousel Manifesto
            //clockwise carousel
            if (gamepad1.left_bumper) {
                robot.moveSpinny(1);
            }

            //counter-clockwise carousel
            if (gamepad.right_bumper) {
                robot.moveSpinny(-1);
            }
                
            if(gamepad1.dpad_up) {
                robot.upArmMotor(gamepad1.dpad_up);
            }   
        
            if(gamepad1.dpad_down) { 
                robot.downArmMotor(gamepad1.dpad_down);
            }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
            //claw code
            if(gamepad2.right_trigger > 0) {
                clawOpenTele();
            }
            
            if(gamepad2.left_trigger > 0) {
                clawCloseTele();
            }
        }
    }
}

