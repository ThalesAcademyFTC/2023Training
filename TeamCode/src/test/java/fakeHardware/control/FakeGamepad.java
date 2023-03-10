package fakeHardware.control;

import com.qualcomm.robotcore.hardware.Gamepad;

public class FakeGamepad extends Gamepad {

    private boolean resting;

    public void pressA(){
        a = true;
    }

    public void unPressA(){
        a = false;
    }

    public void pressB(){
        b = true;
    }

    public void unPressB(){
        b = false;
    }

    public void pressX(){
        x = true;
    }

    public void unPressX(){
        x = false;
    }

    public void pressY(){
        y = true;
    }

    public void unPressY(){
        y = false;
    }

    public void pressDpadUp(){
        dpad_up = false;
    }

    public void unPressDpadUp(){
        dpad_up = false;
    }

    public void pressDpadDown(){
        dpad_down = false;
    }

    public void unPressDpadDown(){
        dpad_down = false;
    }

    public void pressDpadLeft(){
        dpad_left = false;
    }

    public void unPressDpadLeft(){
        dpad_left = false;
    }

    public void pressDpadRight(){
        dpad_right = false;
    }

    public void unPressDpadRight(){
        dpad_right = false;
    }

    public void holdRightTrigger(float press){
        right_trigger = press;
    }

    public void releaseRightTrigger(){
        right_trigger = 0;
    }

    public void holdLeftTrigger(float press){
        left_trigger = press;
    }

    public void releaseLeftTrigger(){
        left_trigger = 0;
    }

    public void holdLeftBumper(){
        left_bumper = true;
    }

    public void releaseLeftBumper(){
        left_bumper = false;
    }

    public void holdRightBumper(){
        right_bumper = true;
    }

    public void releaseRightBumper(){
        right_bumper = false;
    }

    public void holdRightStickButton(){
        right_stick_button = true;
    }

    public void releaseRightStickButton(){
        right_stick_button = false;
    }

    public void holdLeftStickButton(){
        left_stick_button = true;
    }

    public void releaseLeftStickButton(){
        left_stick_button = false;
    }

    public void setLeftStick(float x,  float y){
        left_stick_x = x;
        left_stick_y = -y; //Negates y because gamepad y is flipped

    }

    public void setRightStick(float x,  float y){
        right_stick_x = x;
        right_stick_y = -y; //Negates y because gamepad y is flipped
    }

    @Override
    public boolean atRest(){
        if (Math.abs(left_stick_x) > 0 || Math.abs(left_stick_y) > 0){
            resting = false;
        } else if (Math.abs(right_stick_x) > 0 || Math.abs(right_stick_y) > 0){
            resting = false;
        } else {
            resting = true;
        }

        return resting;
    }


}
