package fakeHardware.control;

/**
 * The FakeOnOffButton is meant to provide fake on/off trigger for testing the robot using code.
 */
public class FakeOnOffButton {

    /** Whether the button is pressed or not */
    private boolean isPressed;

    /**
     * Returns whether the button is pressed
     * @return true if the button is pressed
     */
    public boolean isPressed() {
        return isPressed;
    }

    /**
     * Sets the value of the button.
     * @param value true or false for whether the button is pressed
     */
    public void setPressed(boolean value) {
        isPressed = value;
    }

}
