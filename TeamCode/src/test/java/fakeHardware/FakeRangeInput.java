package fakeHardware;

/**
 * This FakeRangeInput is meant to provide a template for ranged input. Intended for testing without the robot.
 * Meant to simulate a gamepad stick.
 */
public class FakeRangeInput {
    private float currentPos = 0;

    /** The minimum position of the ranged input */
    private float minPosition = -1;

    /** The maximum position of the ranged input*/
    private float maxPosition = 1;

    /**
     * Constructs the ranged input object with a minimum position and maximum position
     * @param minPosition the minimum position that the ranged input can reach
     * @param maxPosition the maximum position that the ranged input can reach
     */
    public FakeRangeInput(float minPosition, float maxPosition) {
        this.minPosition = minPosition;
        this.maxPosition = maxPosition;
    }

    /**
     * Sets the current position of the ranged input. Sets it to max and min
     * if it is above those values
     * @param currentPosition the new current position of the ranged input
     */
    public void setCurrentPosition(float currentPosition) {

        if (currentPosition > maxPosition) {

            currentPosition = maxPosition;

        } else if (currentPosition < minPosition) {

            currentPosition = minPosition;

        }

        this.currentPos = currentPosition;
    }

    /**
     * Gets the position of the ranged input
     * @return the ranged input's position
     */
    public float getPosition() {
        return currentPos;
    }

    /**
     * Gets the max input position of the ranged input
     * @return the ranged input's max position
     */
    public float getMaxPosition() {
        return maxPosition;
    }

    /**
     * Gets the minimum input position of the ranged input
     * @return the ranged input's minimum position
     */
    public float getMinPosition() {
        return minPosition;
    }
}
