package fakeHardware.sensors;

import com.qualcomm.robotcore.hardware.TouchSensor;

public class FakeTouchSensor extends FakeDigitalChannel implements TouchSensor {

    @Override
    public double getValue() {

        if (isPressed()){
            return 1;
        }
        return 0;
    }

    @Override
    public boolean isPressed() {
        return !getState();
    }
}