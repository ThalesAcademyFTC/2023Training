import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.Tele;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.text.DecimalFormat;

import fakeHardware.FakeHardwareMapFactory;
import fakeHardware.control.FakeGamepad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeleopTest {

    //Note, the only two things you would need to change to swap Teleops would be GabeTele
    //You could put in whatever Teleop opmode you want here
    private static Tele tele = new quiffeltele();

    private static FakeGamepad gamepad1 = new FakeGamepad();

    private static FakeGamepad gamepad2 = new FakeGamepad();

    // This is just used to round to two decimal places.
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private final static double MECHANUM_MAX_SPEED = 0.7071067811865;

    private void updateGamepads() {
        tele.gamepad1.copy(gamepad1);
        tele.gamepad2.copy(gamepad2);
    }

    @BeforeAll
    public void setupTeleTests(){

        //Also may need to adjust the hardwareMap path
        File hardwareMapFile = new File("src/test/res/xml/sample_hardware_map.xml");

        try {
            tele.hardwareMap = FakeHardwareMapFactory.getFakeHardwareMap(hardwareMapFile);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Order(0)
    @Test
    public void testInit(){
        //Here is where init code is run.
        tele.init();

        //-----------------------------------------------
        //Any post-initialization tests would go below
        tele.robot.rest();

        testAtRest();

        tele.robot.moveForward(1);

        testMoveForward( 1 );


        tele.robot.turnLeft(0.5);

        testTurnLeft(0.5 );

        tele.robot.rest();

        testAtRest();

        //-----------------------------------------------

        //Here is where you run function tests
        
        tele.robot.moveSpinny(1);
        
        assertEquals(1, tele.robot.carouselMotor.getPower());

        tele.robot.upArmMotor(1);

        assertEquals(1, tele.robot.armMotor.getPower());


    }

    @Test
    public void moveForwardTest(){
        //Start Teleop Loop here for main loop tests

        //Here is where any gamepad commands and tests would go.

        gamepad1.setLeftStick(0,1);
        updateGamepads();

        //Runs the main teleop loop
        tele.loop();

        //This code is necessary to update the Teleop GamePad, after each gamepad command

        //-----------------------------------------------
        // Any tests on the robot after gamepad commands take effect
        testMoveForward( MECHANUM_MAX_SPEED );

    }

    @Test
    public void turnLeftTest() {

        gamepad1.setRightStick(-1, 0);
        updateGamepads();

        //Runs the main teleop loop
        tele.loop();


        testTurnLeft(1);

    }

    @Test
    public void turnRightTest() {


        gamepad1.setRightStick(1, 0);
        updateGamepads();

        //Runs the main teleop loop
        tele.loop();


        testTurnRight(1);

    }

    @BeforeEach
    public void clearGamepads(){
        gamepad1 = new FakeGamepad();
        gamepad2 = new FakeGamepad();
        tele.gamepad1 = new FakeGamepad();
        tele.gamepad2 = new FakeGamepad();
    }

    //Relevant functions to make life easier

    private void testMoveForward(double pace){
        for ( DcMotor x: tele.robot.forward ) {
            assertEquals(df.format(pace), df.format(x.getPower()));
        }
    }

    private void testMoveBackward(double pace){
        for ( DcMotor x: tele.robot.forward ) {
            assertEquals(df.format(-pace), df.format(x.getPower()));
        }
    }

    private void testTurnLeft(double pace){
        assertEquals(df.format(-pace), df.format(tele.robot.motor1.getPower()));
        assertEquals(df.format(pace), df.format(tele.robot.motor2.getPower()));
        assertEquals(df.format(-pace), df.format(tele.robot.motor3.getPower()));
        assertEquals(df.format(pace), df.format(tele.robot.motor4.getPower()));
    }

    private void testTurnRight(double pace){
        assertEquals(df.format(pace), df.format(tele.robot.motor1.getPower()));
        assertEquals(df.format(-pace), df.format(tele.robot.motor2.getPower()));
        assertEquals(df.format(pace), df.format(tele.robot.motor3.getPower()));
        assertEquals(df.format(-pace), df.format(tele.robot.motor4.getPower()));
    }

    private void testAtRest(){
        for ( DcMotor x: tele.robot.forward ) {
            assertEquals(0, x.getPower());
        }
    }


}
