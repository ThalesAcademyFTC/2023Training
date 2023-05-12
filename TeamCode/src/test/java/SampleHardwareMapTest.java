import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Spark;
import org.firstinspires.ftc.teamcode.Tele;
import org.junit.jupiter.api.Test;

import java.io.File;

import fakeHardware.FakeHardwareMapFactory;

/**
 * This class is meant to ensure that all of the motors listed in the file are on the
 * hardwareMap. Only works for XML file hardwareMaps.
 */
public class SampleHardwareMapTest {

    @Test
    public void sampleHardwareMapTest() {
        File hardwareMapFile = new File("src/main/res/xml/sparky.xml");
        HardwareMap hwMap = new HardwareMap(null,null);

        try {
            hwMap = FakeHardwareMapFactory.getFakeHardwareMap(hardwareMapFile);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        Spark robot = new Spark(hwMap);

        robot.rest();

        assertEquals(robot.motor1.getPower(), 0);
        assertEquals(robot.motor2.getPower(), 0);
        assertEquals(robot.motor3.getPower(), 0);
        assertEquals(robot.motor4.getPower(), 0);

        robot.moveForward(1);

        assertEquals(robot.motor1.getPower(), 1);
        assertEquals(robot.motor2.getPower(), 1);
        assertEquals(robot.motor3.getPower(), 1);
        assertEquals(robot.motor4.getPower(), 1);


        robot.turnLeft(0.5);

        assertEquals(robot.motor1.getPower(), -0.5);
        assertEquals(robot.motor2.getPower(), 0.5);
        assertEquals(robot.motor3.getPower(), -0.5);
        assertEquals(robot.motor4.getPower(), 0.5);

        robot.rest();

        assertEquals(robot.motor1.getPower(), 0);
        assertEquals(robot.motor2.getPower(), 0);
        assertEquals(robot.motor3.getPower(), 0);
        assertEquals(robot.motor4.getPower(), 0);


    }


}
