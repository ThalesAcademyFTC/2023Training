import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.GabeTele;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;

import fakeHardware.FakeHardwareMapFactory;
import fakeHardware.control.FakeGamepad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeleopTest {

    //Note, the only two things you would need to change to swap Teleops would be GabeTele
    //You could put in whatever Teleop opmode you want here
    GabeTele tele = new GabeTele();

    FakeGamepad gamepad1 = new FakeGamepad();

    FakeGamepad gamepad2 = new FakeGamepad();

    Thread Opmode;



   private void startTeleopLoop(){
        Opmode = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    tele.loop();
                }
            }
        });

        Opmode.start();
    }

    private void updateGamepads(){
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

        //Initializes Gamepads. Necessary because phone is not running the code, we are
        //Kinda scary right?

    }

    @Order(0)
    @Test
    public void testInit(){
        //Here is where init code is run.
        tele.init();

        //-----------------------------------------------
        //Any post-initialization tests would go below
        tele.robot.rest();

        assertEquals(0, tele.robot.motor1.getPower());
        assertEquals(0, tele.robot.motor2.getPower());
        assertEquals(0, tele.robot.motor3.getPower());
        assertEquals(0, tele.robot.motor4.getPower());

        tele.robot.moveForward(1);

        assertEquals(1, tele.robot.motor1.getPower());
        assertEquals(1, tele.robot.motor2.getPower());
        assertEquals(1, tele.robot.motor3.getPower());
        assertEquals(1, tele.robot.motor4.getPower());


        tele.robot.turnLeft(0.5);

        assertEquals(-0.5, tele.robot.motor1.getPower());
        assertEquals(0.5, tele.robot.motor2.getPower());
        assertEquals(-0.5, tele.robot.motor3.getPower());
        assertEquals(0.5, tele.robot.motor4.getPower());

        tele.robot.rest();

        assertEquals(0, tele.robot.motor1.getPower());
        assertEquals(0, tele.robot.motor2.getPower());
        assertEquals(0, tele.robot.motor3.getPower());
        assertEquals(0, tele.robot.motor4.getPower());

        //-----------------------------------------------
    }

    @Order(3)
    @Test
    public void moveForwardTest(){
       startTeleopLoop();
        //Start Teleop Loop here for main loop tests

        //Here is where any gamepad commands and tests would go.

        gamepad1.setLeftStick(0,1);

        //This code is necessary to update the Teleop GamePad, after each gamepad command
        updateGamepads();


        assertEquals(1, tele.robot.motor1.getPower());
        assertEquals(1, tele.robot.motor2.getPower());
        assertEquals(1, tele.robot.motor3.getPower());
        assertEquals(1, tele.robot.motor4.getPower());

        Opmode.interrupt();

    }
    @Order(1)
    @Test
    public void turnLeftTest() {
       startTeleopLoop();

        gamepad1.setLeftStick(-1,0);

        updateGamepads();

        assertEquals(-1, tele.robot.motor1.getPower());
        assertEquals(1, tele.robot.motor2.getPower());
        assertEquals(-1, tele.robot.motor3.getPower());
        assertEquals(1, tele.robot.motor4.getPower());

        Opmode.interrupt();
    }

    @Order(2)
    @Test
    public void turnRightTest() {
       startTeleopLoop();

        gamepad1.setLeftStick(1,0);

        updateGamepads();

        assertEquals(1, tele.robot.motor1.getPower());
        assertEquals(-1, tele.robot.motor2.getPower());
        assertEquals(1, tele.robot.motor3.getPower());
        assertEquals(-1, tele.robot.motor4.getPower());

        Opmode.interrupt();
        tele.robot.rest();
    }

    @BeforeEach
    public void clearGamepads(){
        gamepad1 = new FakeGamepad();
        gamepad2 = new FakeGamepad();
        tele.gamepad1 = new FakeGamepad();
        tele.gamepad2 = new FakeGamepad();
    }

    @AfterAll
    public void endOpMode(){
        Opmode.interrupt();
    }


}
