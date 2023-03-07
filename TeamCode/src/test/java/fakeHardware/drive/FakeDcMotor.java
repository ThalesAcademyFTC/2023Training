package fakeHardware.drive;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import java.util.HashMap;
import java.util.Map;

public class FakeDcMotor implements DcMotorEx {

    private boolean motorEnable = true;

    private Map<RunMode, PIDCoefficients> pidCoefficients = new HashMap<>();

    private Map<RunMode, PIDFCoefficients> pidfCoefficients = new HashMap<>();

    private double motorPower;

    private int currentEncoderPosition;

    private int targetPositionTolerance;

    private double simpleVelocity;

    private int targetPosition;

    private Direction direction = Direction.FORWARD;

    private RunMode runMode;

    private boolean isBusy;

    @Override
    public void setMotorEnable() {
        motorEnable = true;
    }

    @Override
    public void setMotorDisable() {
        motorEnable = false;
    }

    @Override
    public boolean isMotorEnabled() {
        return motorEnable;
    }

    @Override
    public void setVelocity(double angularRate) {
        simpleVelocity = angularRate;
    }

    @Override
    public void setVelocity(double angularRate, AngleUnit unit) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public double getVelocity() {
       return simpleVelocity;
    }

    @Override
    public double getVelocity(AngleUnit unit) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void setPIDCoefficients(RunMode mode, PIDCoefficients pidCoefficients) {
        this.pidCoefficients.put(mode,pidCoefficients);
    }

    @Override
    public void setPIDFCoefficients(RunMode mode, PIDFCoefficients pidfCoefficients) throws UnsupportedOperationException {
        this.pidfCoefficients.put(mode,pidfCoefficients);
    }

    @Override
    public void setVelocityPIDFCoefficients(double p, double i, double d, double f) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void setPositionPIDFCoefficients(double p) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public PIDCoefficients getPIDCoefficients(RunMode mode) {
        return pidCoefficients.get(mode);
    }

    @Override
    public PIDFCoefficients getPIDFCoefficients(RunMode mode) {
        return pidfCoefficients.get(mode);
    }

    @Override
    public void setTargetPositionTolerance(int tolerance) {
        this.targetPositionTolerance = tolerance;
    }

    @Override
    public int getTargetPositionTolerance() {
        return targetPositionTolerance;
    }

    @Override
    public double getCurrent(CurrentUnit unit) {
        return 0;
    }

    @Override
    public double getCurrentAlert(CurrentUnit unit) {
        return 0;
    }

    @Override
    public void setCurrentAlert(double current, CurrentUnit unit) {

    }

    @Override
    public boolean isOverCurrent() {
        return false;
    }

    @Override
    public MotorConfigurationType getMotorType() {
        return null;
    }

    @Override
    public void setMotorType(MotorConfigurationType motorType) {

    }

    @Override
    public DcMotorController getController() {
        return null;
    }

    @Override
    public int getPortNumber() {
        return 0;
    }

    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {

    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        return null;
    }

    @Override
    public void setPowerFloat() {

    }

    @Override
    public boolean getPowerFloat() {
        return false;
    }

    @Override
    public void setTargetPosition(int position) {
        this.targetPosition = position;
    }

    @Override
    public int getTargetPosition() {
        return targetPosition;
    }

    @Override
    public boolean isBusy() {
        return isBusy;
    }

    public void setCurrentPosition(int currentMotorPosition) {
        this.currentEncoderPosition = currentMotorPosition;
    }

    @Override
    public int getCurrentPosition() {
        return currentEncoderPosition;
    }

    @Override
    public void setMode(RunMode mode) {
        runMode = mode;

        isBusy = (mode == RunMode.RUN_TO_POSITION);
    }

    @Override
    public RunMode getMode() {
        return runMode;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public void setPower(double power) {
        this.motorPower = power;
    }

    @Override
    public double getPower() {
        return motorPower;
    }

    @Override
    public Manufacturer getManufacturer() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {

    }
}
