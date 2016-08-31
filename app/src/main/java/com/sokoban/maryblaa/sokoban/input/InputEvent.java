package com.sokoban.maryblaa.sokoban.input;

public class InputEvent {

    public enum InputDevice {
        NONE,
        KEYBOARD,
        TOUCHSCREEN,
        ACCELEROMETER,
        GYROSCOPE,
        ROTATION
    }

    public enum InputAction {
        NONE,
        DOWN,
        UP,
        MOVE,
        UPDATE
    }

    private InputDevice device;
    private InputAction action;
    private float time;
    private int keycode;
    private float[] values = new float[4];

    public void set(InputDevice device, InputAction action, float time, int keycode, float v0, float v1, float v2, float v3) {
        this.device = device;
        this.action = action;
        this.time = time;
        this.keycode = keycode;
        this.values[0] = v0;
        this.values[1] = v1;
        this.values[2] = v2;
        this.values[3] = v3;
    }


    public InputDevice getDevice() {
        return device;
    }

    public void setDevice(InputDevice device) {
        this.device = device;
    }

    public InputAction getAction() {
        return action;
    }

    public void setAction(InputAction action) {
        this.action = action;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public int getKeycode() {
        return keycode;
    }

    public void setKeycode(int keycode) {
        this.keycode = keycode;
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }
}
