package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class GabeTele extends OpMode {

    private GabeSpark robot;

    public static final double MAXIMUM_SPEED = 0.8;

    private static final int NINETY_DEGREES = 440;

    @Override
    public void init() {
        robot = new GabeSpark(this);
    }

    @Override
    public void loop() {

        if (gamepad1.atRest()) robot.rest();
        else {

            if (Math.abs(gamepad1.left_stick_y) > 0.3) {
                robot.moveForward(-gamepad1.left_stick_y);
            }

            if (Math.abs(gamepad1.left_stick_x) > 0.3) {
                robot.turnLeft(gamepad1.left_stick_x);
            }

        }
    }
}
