package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Tele", group="Template")
//@Disabled
public class Tele extends OpMode {

    private Spark robot;
    double speedMod;

    @Override
    public void init() {

        //This code initializes the drivetrain. Make sure that you have the right drivetrain selected
        robot = new Spark(this, Spark.Drivetrain.MECHANUM);
        speedMod = 0.7;
    }

    @Override
    public void loop() {
        //MOVEMENT
        //First, we want to make the robot rest if the gamepad is not being touched

        // telemetry.addData("ArmTouch: ", robot.armIsDown());
        // telemetry.update();
        //If the gamepad is NOT at rest, then we want to see what we need to do.
        telemetry.addData("Touch", robot.armIsDown());
        telemetry.addData("Speed", speedMod);
        telemetry.addData("ArmCheck", robot.checkArm());
        telemetry.update();

        if (gamepad2.a) {
            robot.armLoadTele();
        }

        //primed preset
        else if (gamepad2.y) {
            robot.armPrimedTele();
        }

        //low junction preset
        else if (gamepad2.dpad_down) {
            robot.armLowTele();
        }

        //medium junction preset
        else if (gamepad2.dpad_right) {
            robot.armMediumTele();
        }

        //high junction preset
        else if (gamepad2.dpad_up) {
            robot.armHighTele();
        }

        else if (gamepad1.left_stick_button) {
            robot.armMotor.setPower(-0.5);
        }

        if (gamepad1.atRest() && gamepad2.atRest()) robot.rest();
        else {
            //If the gamepad is NOT at rest, then we want to see what we need to do.
            //GAMEPAD 1 CODE


            if (gamepad2.left_stick_y < -0.3) {
                robot.armOverride();
                robot.armUp(-gamepad2.left_stick_y);
            } else if (gamepad2.left_stick_y > 0.3) {
                robot.armOverride();
                robot.armDown(gamepad2.left_stick_y);
            } else if  (!robot.checkArm()){
                robot.armStop();
            }

            if (gamepad2.left_trigger > 0.2) {
                robot.servoOpen();
            } else if (gamepad2.right_trigger > 0.2) {
                robot.servoClose();
            }

            if (gamepad2.right_stick_x > 0.8) {
                robot.servoPrepare();
            }

            speedMod = 0.7;

            if (gamepad1.right_trigger > 0.3) {
                speedMod = 1;
            }

            robot.mechanumMovT(gamepad1.left_stick_x * speedMod, -gamepad1.left_stick_y * speedMod, gamepad1.right_stick_x * speedMod);
        }
    }
}