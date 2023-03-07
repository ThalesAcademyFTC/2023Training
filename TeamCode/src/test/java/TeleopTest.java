import org.firstinspires.ftc.teamcode.GabeTele;
import org.junit.jupiter.api.Test;

import java.io.File;

import fakeHardware.FakeHardwareMapFactory;
import fakeHardware.control.FakeGamepad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class TeleopTest {

    @Test
    public void GabeTeleTest() {

        //Note, the only two things you would need to change to swap Teleops would be GabeTele
        GabeTele tele = new GabeTele();

        //Also may need to adjust the hardwareMap path
        File hardwareMapFile = new File("src/test/res/xml/sample_hardware_map.xml");

        try {
            tele.hardwareMap = FakeHardwareMapFactory.getFakeHardwareMap(hardwareMapFile);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        tele.gamepad1 = (FakeGamepad) tele.gamepad1;

        tele.gamepad1 = (FakeGamepad) tele.gamepad1;

        //Here is where init code is run.
        tele.init();

        //-----------------------------------------------
        //Any post-initialization tests  would go here


        //------------------------------------------------

        Thread Opmode = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    tele.loop();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // Do nothing, it was supposed to be interrupted.
                    }
                }
            }
        });

        Opmode.start();

        //-----------------------------------------------
        //Here is where any gamepad commands and tests would go.




        //------------------------------------------------

        Opmode.interrupt(); // Here is where the opmode stops running, after all tests are complete.

    }


}
