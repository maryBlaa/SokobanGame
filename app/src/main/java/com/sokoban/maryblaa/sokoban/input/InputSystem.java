package com.sokoban.maryblaa.sokoban.input;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.sokoban.maryblaa.sokoban.input.InputEvent.InputDevice;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class InputSystem implements View.OnKeyListener, View.OnTouchListener, SensorEventListener {


    private static final String TAG = InputSystem.class.getSimpleName();
    private Queue<InputEvent> inputQueue;
    private Queue<InputEvent> inputPool;

    public InputSystem(View view) {
        int maxInputEvents = 128;
        inputQueue = new ArrayBlockingQueue<InputEvent>(maxInputEvents);
        inputPool = new ArrayBlockingQueue<InputEvent>(maxInputEvents);
        for (int i = 0; i < maxInputEvents; ++i) {
            inputPool.add(new InputEvent());
        }

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(this);
        view.setOnTouchListener(this);

        Context context = view.getContext();
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null)
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscope != null)
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME);

        Sensor rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (rotation != null)
            sensorManager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    public void onSensorChanged(SensorEvent event) {
        InputDevice device = InputDevice.NONE;
        InputEvent.InputAction action = InputEvent.InputAction.UPDATE;
        float time = event.timestamp / 1000.0f;
        float v0 = 0, v1 = 0, v2 = 0, v3 = 0;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                device = InputDevice.ACCELEROMETER;
                v0 = event.values[0];
                v1 = event.values[1];
                v2 = event.values[2];
                break;

            case Sensor.TYPE_GYROSCOPE:
                device = InputDevice.GYROSCOPE;
                v0 = event.values[0];
                v1 = event.values[1];
                v2 = event.values[2];
                break;

            case Sensor.TYPE_ROTATION_VECTOR:
                device = InputDevice.ROTATION;
                v0 = event.values[0];
                v1 = event.values[1];
                v2 = event.values[2];
                if (event.values.length > 3) {
                    v3 = event.values[3];
                } else {
                    // Viertes Element berechnen, falls nicht vorhanden.
                    // Siehe auch: Android SensorEvent Dokumentation:
                    // http://developer.android.com/reference/android/hardware/SensorEvent.html
                    v3 = (float) Math.cos(Math.asin(Math.sqrt(v0 * v0 + v1 * v1 + v2 * v2)));
                }
                break;

            default:
                return;
        }

        InputEvent inputEvent = inputPool.poll();
        if (inputEvent == null)
            return;

        inputEvent.set(device, action, time, 0, v0, v1, v2, v3);
        inputQueue.add(inputEvent);
    }

    public boolean onTouch(View view, MotionEvent event) {
        InputDevice device = InputDevice.TOUCHSCREEN;
        InputEvent.InputAction action = InputEvent.InputAction.NONE;
        float time = event.getEventTime() / 1000.0f;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                action = InputEvent.InputAction.DOWN;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                action = InputEvent.InputAction.UP;
                break;

            case MotionEvent.ACTION_MOVE:
                action = InputEvent.InputAction.MOVE;
                break;

            default:
                return false;
        }


        for(int i = 0; i < event.getPointerCount(); i++) {
            float x = event.getX(i);
            float y = event.getY(i);

            InputEvent inputEvent = inputPool.poll();
            if (inputEvent == null)
                return false;
            inputEvent.set(device, action, time, 0, x, y, 0, 0);
            inputQueue.add(inputEvent);
        }

        return true;
    }

    public boolean onKey(View view, int keycode, KeyEvent event) {
        InputDevice device = InputDevice.KEYBOARD;
        InputEvent.InputAction action = InputEvent.InputAction.NONE;
        float time = event.getEventTime() / 1000.0f;

        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                action = InputEvent.InputAction.DOWN;
                break;

            case KeyEvent.ACTION_UP:
                action = InputEvent.InputAction.UP;
                break;

            default:
                return false;
        }

        InputEvent inputEvent = inputPool.poll();
        if (inputEvent == null)
            return false;

        inputEvent.set(device, action, time, keycode, 0, 0, 0, 0);
        inputQueue.add(inputEvent);

        return true;
    }

    public InputEvent peekEvent() {
        return inputQueue.peek();
    }

    public void popEvent() {
        InputEvent inputEvent = inputQueue.poll();
        if (inputEvent == null)
            return;

        inputPool.add(inputEvent);
    }

}
