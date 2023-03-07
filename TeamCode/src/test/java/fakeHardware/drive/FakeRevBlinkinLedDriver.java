package fakeHardware.drive;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.ServoControllerEx;

public class FakeRevBlinkinLedDriver extends RevBlinkinLedDriver {

    private RevBlinkinLedDriver.BlinkinPattern currentPattern;

    public FakeRevBlinkinLedDriver(int port) {
        super(null, port);
    }

    public void setPattern(BlinkinPattern pattern) {
        this.currentPattern = pattern;
    }

    public BlinkinPattern getCurrentPattern() {
        return currentPattern;
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Lynx;
    }

    @Override
    public String getDeviceName() {
        return "FakeRevBlinkinledDriver";
    }

    @Override
    public int getVersion() {
        return 1;
    }

}
