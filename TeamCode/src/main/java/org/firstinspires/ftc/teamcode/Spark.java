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

    public static final int PRIMED = 1000;
    public static final int LOW = 1500;
    public static final int MEDIUM = 3000;
    public static final int HIGH = 4170;
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

    public enum ArmState {
        prime,
        load,
        high,
        med,
        low,
        normal
    }

    Spark.ArmState armStatus = Spark.ArmState.normal;

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
                clawServo = hwMap.servo.get("clawServo");
                armMotor = hwMap.dcMotor.get("armMotor");
                //armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                armTouch = hwMap.get(DigitalChannel.class, "armTouch");
                armTouch.setMode(DigitalChannel.Mode.INPUT);
                //Set directions to ensure that robot moves forward when
                //all motor power is 1
                motor1.setDirection(DcMotor.Direction.REVERSE);
                motor2.setDirection(DcMotor.Direction.FORWARD);
                motor3.setDirection(DcMotor.Direction.REVERSE);
                motor4.setDirection(DcMotor.Direction.FORWARD);
                armMotor.setDirection(DcMotor.Direction.REVERSE);
                armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                //Add motors to arrays
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

    public void armUp(double pace) {
        armMotor.setPower(pace);
    }

    public void armDown(double pace) {
        if (armIsDown()){
            armStop();
        } else {
            armMotor.setPower(-pace);
        }
    }

    public void armStop(){
        armMotor.setPower(0);
    }

    public boolean armIsDown() { return armTouch.getState(); }

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
                    checkArm();
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
    public void armUpFT(int ticks, double speed) {
        //Blocks until the robot has gotten to the desired location.
        this.rest();
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setTargetPosition(ticks);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armUp(speed);

        while (auton.opModeIsActive() && !auton.isStopRequested()) {
            if (armMotor.getCurrentPosition() >= armMotor.getTargetPosition() + 2 || armMotor.getCurrentPosition() <= armMotor.getTargetPosition() - 2) {
                telemetry.addData("armMotor", armMotor.getCurrentPosition());
                telemetry.update();
                continue;
            } else {
                break;
            }
        }
        armMotor.setPower(0);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }
    public void armDownFT(int ticks, double speed) {
        //Blocks until the robot has gotten to the desired location.
        this.rest();
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setTargetPosition(-ticks);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armDown(speed);

        while (auton.opModeIsActive() && !auton.isStopRequested()) {
            if (armIsDown()){
                break;
            }
            if (armMotor.getCurrentPosition() >= armMotor.getTargetPosition() + 2 || armMotor.getCurrentPosition() <= armMotor.getTargetPosition() - 2) {
                telemetry.addData("armMotor", armMotor.getCurrentPosition());
                telemetry.update();
                continue;
            } else {
                break;
            }
        }
        armMotor.setPower(0);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //0.08 Scissor Position
    public void servoClose() {
        clawServo.setPosition(0.08);
    }

    //0.15 Starting Position to Pick Up Cone
    public void servoOpen() {
        clawServo.setPosition(0.15);
    }

    public void servoPrepare() {
        clawServo.setPosition(0.3);
    }

    public void waitForArm(){
        while (armMotor.getCurrentPosition() >= armMotor.getTargetPosition() + 2 || armMotor.getCurrentPosition() <= armMotor.getTargetPosition() - 2) {
            telemetry.addData("armMotor", armMotor.getCurrentPosition());
            telemetry.update();
            if (!auton.opModeIsActive() || auton.isStopRequested()){
                break;
            }
            continue;
        }
    }
    public void armLoad() {
        armDown(0.5);
        while(!armIsDown() && auton.opModeIsActive() && !auton.isStopRequested()) {
            continue;
        }
        armStop();
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        auton.sleep(100);
        servoClose();
    }

    public void armPrimed() {
        armMotor.setTargetPosition(PRIMED);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armUp(0.5);

        waitForArm();

        armStop();
    }

    public void armLow() {
        armMotor.setTargetPosition(LOW);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armUp(0.5);

        waitForArm();

        armStop();
    }

    public void armMedium() {
        armMotor.setTargetPosition(MEDIUM);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armUp(0.5);

        waitForArm();

        armStop();
    }

    public void armHigh() {
        armMotor.setTargetPosition(HIGH);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armUp(0.5);

        waitForArm();

        armStop();
    }

    public void armLoadTele() {
        armDown(0.5);

        armStatus = Spark.ArmState.load;
    }

    public void armPrimedTele() {
        armMotor.setTargetPosition(PRIMED);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armUp(0.5);
        armStatus = Spark.ArmState.prime;
    }

    public void armLowTele() {
        armMotor.setTargetPosition(LOW);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armUp(0.5);
        armStatus = Spark.ArmState.low;
    }

    public void armMediumTele() {
        armMotor.setTargetPosition(MEDIUM);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armUp(0.5);
        armStatus = Spark.ArmState.med;
    }

    public void armHighTele() {
        armMotor.setTargetPosition(HIGH);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armUp(0.5);
        armStatus = Spark.ArmState.high;
    }

    public void armSet() {
        armMotor.setTargetPosition(PRIMED);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armUp(0.5);

        waitForArm();

        armStop();

    }

    public boolean checkArm(){

        //Initial check to stop arm if too low

        if (armStatus != Spark.ArmState.normal) {

            if (armStatus == Spark.ArmState.load){
                if (armIsDown()){

                    armStop();
                    armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    servoClose();
                    armStatus = Spark.ArmState.normal;
                    armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    return false;
                }
                return true;
            }

            if ((armMotor.getCurrentPosition() >= armMotor.getTargetPosition() + 2 || armMotor.getCurrentPosition() <= armMotor.getTargetPosition() - 2)) {
                telemetry.addData("armMotor", armMotor.getCurrentPosition());
                telemetry.update();
            } else {
                armStop();

                if (armStatus == Spark.ArmState.prime) {
                    servoOpen();
                }

                armStatus = Spark.ArmState.normal;
                armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
            return true;
        } else {
            return false;
        }
    }

    public void armOverride(){
        if (checkArm()) {
            armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
}