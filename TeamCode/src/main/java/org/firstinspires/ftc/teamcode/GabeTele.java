package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="GabeTele")
//@Disabled
public class GabeTele extends OpMode {

    public GabeSpark robot;

    @Override
    public void init() {
        robot = new GabeSpark(hardwareMap);
    }

    @Override
    public void loop() {

        if (gamepad1.atRest()) robot.rest();
        else {

            if (Math.abs(gamepad1.left_stick_y) > 0.3) {
                robot.moveForward(-gamepad1.left_stick_y);
            }

            if (Math.abs(gamepad1.left_stick_x) > 0.3) {
                robot.turnLeft(-gamepad1.left_stick_x);
            }

        }
    }
}
