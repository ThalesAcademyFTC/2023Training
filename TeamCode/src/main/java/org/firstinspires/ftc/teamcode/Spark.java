package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;


import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Spark {

    //Define servo and motor variables
    public DcMotor motor1, motor2, motor3, motor4;
    public CRServo crservo1;
    public Servo clawServo;
    public DcMotor carouselMotor, armMotor;
    public ColorSensor sensorColor;
    public TouchSensor touchSensor;
    private HardwareMap hwMap;
    LinearOpMode auton;
    OpMode tele;
    public Drivetrain drive;
    DigitalChannel armTouch;
    public Telemetry telemetry;

    public DcMotor[] forward, front, right, left, special, unique;

    public enum Drivetrain {
        HOLONOMIC,
        MECHANUM,
        TANK
    }

    //Constructor for Testing HardwareMap
    public Spark(HardwareMap hwMap) {
        this.hwMap = hwMap;

        this.tele = new Tele();

        telemetry = tele.telemetry;

        drive = Drivetrain.MECHANUM;

        setupHardware(drive);

    }


    //Constructor for Teleop
    public Spark(OpMode opmode, Drivetrain type) {

        this.tele = opmode;
        hwMap = opmode.hardwareMap;
        telemetry = opmode.telemetry;
        drive = type;

        setupHardware(type);
    }

    //Constructor for Autonomous
    public Spark(LinearOpMode opmode, Drivetrain type) {

        this.auton = opmode;
        hwMap = opmode.hardwareMap;
        telemetry = opmode.telemetry;
        drive = type;

        setupHardware(type);
    }

    public void setupHardware(Drivetrain type){
        switch (type) {
            case HOLONOMIC:
                //Assign motors
                carouselMotor = hwMap.dcMotor.get("carouselMotor");
                motor1 = hwMap.dcMotor.get("motor1");
                motor2 = hwMap.dcMotor.get("motor2");
                motor3 = hwMap.dcMotor.get("motor3");
                motor4 = hwMap.dcMotor.get("motor4");
                armMotor = hwMap.dcMotor.get("armMotor");
                //Set motor directions;
                motor1.setDirection(DcMotor.Direction.FORWARD);
                motor2.setDirection(DcMotor.Direction.REVERSE);
                motor3.setDirection(DcMotor.Direction.FORWARD);
                motor4.setDirection(DcMotor.Direction.REVERSE);
                armMotor.setDirection(DcMotor.Direction.FORWARD);
                //Set motor purposes
                forward = new DcMotor[]{motor1, motor2, motor3, motor4, carouselMotor};
                right = new DcMotor[]{motor2, motor4};
                left = new DcMotor[]{motor1, motor3};
                special = new DcMotor[]{motor1, motor4};
                unique = new DcMotor[]{motor2, motor3};
                break;
            case TANK:
                motor1 = hwMap.dcMotor.get("motor1");
                motor2 = hwMap.dcMotor.get("motor2");
                motor1.setDirection(DcMotor.Direction.FORWARD);
                motor2.setDirection(DcMotor.Direction.REVERSE);
                forward = new DcMotor[]{motor1, motor2};
                right = new DcMotor[]{motor1};
                left = new DcMotor[]{motor2};
                break;
            case MECHANUM:
                //Map all motors to proper variables.
                motor1 = hwMap.dcMotor.get("motor1");
                motor2 = hwMap.dcMotor.get("motor2");
                motor3 = hwMap.dcMotor.get("motor3");
                motor4 = hwMap.dcMotor.get("motor4");
                //Set directions to ensure that robot moves forward when
                //all motor power is 1
                motor1.setDirection(DcMotor.Direction.REVERSE);
                motor2.setDirection(DcMotor.Direction.FORWARD);
                motor3.setDirection(DcMotor.Direction.REVERSE);
                motor4.setDirection(DcMotor.Direction.FORWARD);
                //forward array contains all motors
                forward = new DcMotor[]{motor1, motor2, motor3, motor4};
                //left array contains motors that move at -1 power to turn left
                left = new DcMotor[]{motor1, motor3};
                //right array contains motors that move at -1 power to turn right
                right = new DcMotor[]{motor2, motor4};
                //special and unique are arrays for left/right movement in holonomic and mechanum
                special = new DcMotor[]{motor2, motor3};
                unique = new DcMotor[]{motor1, motor4};
                break;
            default:
                telemetry.addLine("Invalid type " + type + " passed to Anvil's init function. Nothing has been set up.");
                break;
        }
        if (forward != null) {
            for (DcMotor x : forward) {
                x.setPower(0);
                x.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }
    }

    //Movement, turning, and resting methods for all current drive trains
    public void moveForward(double pace) {
        for (DcMotor x : forward) x.setPower(pace);
    }

    public void turnRight(double pace) {
        for (DcMotor x : right) x.setPower(-pace);
        for (DcMotor x : left) x.setPower(pace);
    }

    public void turnLeft(double pace) {
        for (DcMotor x : left) x.setPower(-pace);
        for (DcMotor x : right) x.setPower(pace);
    }

    public void moveBackward(double pace) {
        for (DcMotor x : forward)
            x.setPower(-pace);
    }

    public void rest() {
        for (DcMotor x : forward) {
            x.setPower(0);
        }
    }

    public void moveRight(double pace) {
        for (DcMotor x : unique) x.setPower(pace);
        for (DcMotor x : special) x.setPower(-pace);
    }

    public void moveLeft(double pace) {
        for (DcMotor x : unique) x.setPower(-pace);
        for (DcMotor x : special) x.setPower(pace);
    }

    // Prioritizes turning over lateral movement when both are happening at the same time
    public void mechanumMovT(double x, double y, double turn){
        double angle = Math.atan2(y,x); //Finds direction joystick is pointing
        double magnitude = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)); //Pythagorean Theorem
        //front right and back left motors
        double turnFactor = 1-Math.abs(turn);
        motor2.setPower(Math.sin(angle - 0.25 * Math.PI) * turnFactor* magnitude - turn);
        motor3.setPower(Math.sin(angle - 0.25 * Math.PI) * turnFactor * magnitude + turn);

        //front left and back right motors
        motor1.setPower(Math.sin(angle + 0.25 * Math.PI) * turnFactor * magnitude + turn);
        motor4.setPower(Math.sin(angle + 0.25 * Math.PI) * turnFactor * magnitude - turn);
    }
    // Prioritizes lateral movement over turning when both are happening at the same time
    public void mechanumMovL(double x, double y, double turn){
        double angle = Math.atan2(y,x); //Finds direction joystick is pointing
        double magnitude = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)); //Pythagorean Theorem
        //front right and back left motors
        double lateralFactor = 1-magnitude;
        motor2.setPower(Math.sin(angle - 0.25 * Math.PI) * magnitude - turn * lateralFactor);
        motor3.setPower(Math.sin(angle - 0.25 * Math.PI) * magnitude + turn * lateralFactor);

        //front left and back right motors
        motor1.setPower(Math.sin(angle + 0.25 * Math.PI) * magnitude + turn * lateralFactor);
        motor4.setPower(Math.sin(angle + 0.25 * Math.PI) * magnitude - turn * lateralFactor);

    }

    public double getRed(){
        return sensorColor.red();
    }

    public double getBlue(){
        return sensorColor.blue();
    }

    public double getGreen(){
        return sensorColor.green();
    }

    public double getAlpha(){
        return sensorColor.alpha();
    }

    //The functions below are used in autonomous for precise movements.
    //They use ticks to move, which is a unit of wheel rotation

    public void waitForMotors(){ // This method safely loops while checking if the opmode is active.
        boolean finished = false;
        while (auton.opModeIsActive() && !finished && !auton.isStopRequested()) {
            for (DcMotor x : forward) {
                if (x.getCurrentPosition() >= x.getTargetPosition() + 2 || x.getCurrentPosition() <= x.getTargetPosition() - 2) {
                    telemetry.addData("motor1", motor1.getCurrentPosition());
                    telemetry.addData("motor2", motor2.getCurrentPosition());
                    telemetry.addData("motor3", motor3.getCurrentPosition());
                    telemetry.addData("motor4", motor4.getCurrentPosition());
                    telemetry.update();
                    continue;
                } else {
                    finished = true;
                }
            }
        }
    }
    public void resetDriveEncoders(){
        for (DcMotor x : forward) {
            x.setPower(0);
            x.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            x.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public void turnRightFT(int ticks, double speed) {
        //Blocks until the robot has gotten to the desired location.
        resetDriveEncoders();

        for(DcMotor x: left){
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            x.setTargetPosition(ticks);
        }
        for(DcMotor x: right){
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            x.setTargetPosition(-ticks);
        }

        this.turnRight(speed);
        waitForMotors();

        resetDriveEncoders();
    }
    public void turnLeftFT(int ticks, double speed) {
        //Blocks until the robot has gotten to the desired location.
        resetDriveEncoders();

        for(DcMotor x: left){
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            x.setTargetPosition(-ticks);
        }
        for(DcMotor x: right){
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            x.setTargetPosition(ticks);
        }
        this.turnLeft(speed);
        waitForMotors();

        resetDriveEncoders();
    }
    public void moveRightFT(int ticks, double speed) {
        //Blocks until the robot has gotten to the desired location.

        resetDriveEncoders();

        for(DcMotor x: special){
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            x.setTargetPosition(-ticks);
        }
        for(DcMotor x: unique){
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            x.setTargetPosition(ticks);
        }
        this.moveRight(speed);

        waitForMotors();

        resetDriveEncoders();

    }
    public void moveLeftFT(int ticks, double speed) {
        //Blocks until the robot has gotten to the desired location.

        resetDriveEncoders();

        for(DcMotor x: special){
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            x.setTargetPosition(ticks);
        }
        for(DcMotor x: unique){
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            x.setTargetPosition(-ticks);
        }
        this.moveLeft(speed);
        waitForMotors();

        resetDriveEncoders();

    }
    public void moveForwardFT(int ticks, double speed) {
        //Blocks until the robot has gotten to the desired location.
        resetDriveEncoders();

        for(DcMotor x: forward){
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            x.setTargetPosition(ticks);
        }
        this.moveForward(speed);
        waitForMotors();

        resetDriveEncoders();

    }
    public void moveBackwardFT(int ticks, double speed) {
        //Blocks until the robot has gotten to the desired location.

        resetDriveEncoders();

        for(DcMotor x: forward){
            x.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            x.setTargetPosition(-ticks);
        }
        this.moveBackward(speed);

        waitForMotors();

        resetDriveEncoders();
    }

}