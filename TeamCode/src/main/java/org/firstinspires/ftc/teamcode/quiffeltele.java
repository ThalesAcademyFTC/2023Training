package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="QuiffelTele")
//@Disabled
public class quiffeltele extends OpMode {

    public Spark robot;

    @Override
    public void init() {
        robot = new Spark(this, Spark.Drivetrain.SPARKY );
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
            if (gamepad1.right_bumper) {
                robot.moveSpinny(-1);
            }
                
            if(gamepad1.dpad_up) {
                robot.upArmMotor(1);
            }   
        
            if(gamepad1.dpad_down) { 
                robot.downArmMotor(1);
            }
                                                                            
            //claw code
            if(gamepad2.right_trigger > 0) {
                robot.clawOpenTele();
            }
            
            if(gamepad2.left_trigger > 0) {
                robot.clawCloseTele();
            }
        }
    }
}

