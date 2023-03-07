package fakeHardware.sensors;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

public class FakeRevTouchSensor extends RevTouchSensor {

    private boolean pressed;
    public FakeRevTouchSensor(int physicalPort) {

        super(null, physicalPort);

    }

    @Override
    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean flag) {
        pressed = flag;
    }

    @Override
    public String getDeviceName() {
        return "Fake RevTouchSensor";
    }

    @Override
    public int getVersion() {
        return 1;
    }

}
