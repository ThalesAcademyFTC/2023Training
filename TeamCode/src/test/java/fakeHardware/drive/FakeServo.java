package fakeHardware.drive;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.Range;

@SuppressWarnings("unused")
public class FakeServo implements Servo {
    protected Direction direction = Direction.FORWARD;
    protected double limitPositionMin = MIN_POSITION;
    protected double limitPositionMax = MAX_POSITION;

    private double servoPosition;

    @Override
    public ServoController getController() {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public int getPortNumber() {
        return 0;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setPosition(double position) {
        position = Range.clip(position, MIN_POSITION, MAX_POSITION);

        double scaled = Range.scale(position, MIN_POSITION, MAX_POSITION, limitPositionMin, limitPositionMax);

        servoPosition = scaled;
    }

    @Override
    public double getPosition() {
        double reportedPosition = servoPosition;

        double scaled = Range.scale(reportedPosition, limitPositionMin, limitPositionMax, MIN_POSITION, MAX_POSITION);

        return Range.clip(scaled, MIN_POSITION, MAX_POSITION);
    }

    @Override
    public void scaleRange(double min, double max) {
        min = Range.clip(min, MIN_POSITION, MAX_POSITION);
        max = Range.clip(max, MIN_POSITION, MAX_POSITION);

        if (min >= max) {
            throw new IllegalArgumentException("min must be less than max");
        }

        limitPositionMin = min;
        limitPositionMax = max;
    }

    @Override
    public Manufacturer getManufacturer() {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public String getDeviceName() {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public String getConnectionInfo() {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        this.limitPositionMin = MIN_POSITION;
        this.limitPositionMax = MAX_POSITION;
        this.direction = Direction.FORWARD;
    }

    @Override
    public void close() {

    }

    private double reverse(double position) {
        return MAX_POSITION - position + MIN_POSITION;
    }
}