package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.draga.Constants;

public class InputManager
{
    private static final String LOGGING_TAG = InputManager.class.getSimpleName();

    private static final float   KEYBOARD_ACCELERATION       = 0.05f;
    private static       Vector2 KEYBOARD_ACCELERATION_DELTA = new Vector2();

    /**
     * Returns a vector with length from 0 to 1, representing where the input is pointing to,
     * abstracting away the fact that it could be a mobile accelerometer, mouse clicks, etc.
     *
     * @return A Vector2 of length from 0 to 1 of where the input is pointing
     */
    public static Vector2 getInputForce()
    {
        Vector2 input;
        switch (Gdx.app.getType())
        {
            // TODO: preferences based with adapters?
            case Android:
            case iOS:
                input = getAccelerometerInput();
                break;
            case Desktop:
                input = getKeyboardInput();
                break;
            default:
                Gdx.app.error(
                    LOGGING_TAG, "Device type " + Gdx.input.getRotation() + " not implemented.");
                input = new Vector2();
                break;
        }
        return input;
    }

    private static Vector2 getAccelerometerInput()
    {
        Vector2 input = getDeviceAccelerationForDeviceOrientation();

        // Max the gravity by the Earth gravity to avoid excessive force being applied if the device is shaken.
        input = input.clamp(0, Constants.EARTH_GRAVITY);

        // Scale the input by the Earth's gravity so that I'll be between 1 and 0
        input = input.scl(1 / Constants.EARTH_GRAVITY);

        return input;
    }

    private static Vector2 getDeviceAccelerationForDeviceOrientation()
    {
        Vector2 input = new Vector2();
        switch (Gdx.input.getRotation())
        {
            case 0:
                input.x = Gdx.input.getAccelerometerX();
                input.y = Gdx.input.getAccelerometerY();
                break;
            case 90:
                input.x = Gdx.input.getAccelerometerY();
                input.y = -Gdx.input.getAccelerometerX();
                break;
            case 180:
                input.x = -Gdx.input.getAccelerometerX();
                input.y = -Gdx.input.getAccelerometerY();
                break;
            case 270:
                input.x = -Gdx.input.getAccelerometerY();
                input.y = Gdx.input.getAccelerometerX();
                break;
            default:
                Gdx.app.error(
                    LOGGING_TAG, "Orientation " + Gdx.input.getRotation() + " not implemented.");
        }
        return input;
    }

    /**
     * Checks arrow, WASD and num pad keys and add 1 for each direction requested, then brings the
     * vector between 0 and 1.
     */
    private static Vector2 getKeyboardInput()
    {
        Vector2 input = new Vector2().setZero();

        if (Gdx.input.isKeyPressed(Input.Keys.UP)
            || Gdx.input.isKeyPressed(Input.Keys.W)
            || Gdx.input.isKeyPressed(Input.Keys.DPAD_UP))
        {
            input.add(0, KEYBOARD_ACCELERATION*2);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
            || Gdx.input.isKeyPressed(Input.Keys.D)
            || Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT))
        {
            input.add(KEYBOARD_ACCELERATION*2, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)
            || Gdx.input.isKeyPressed(Input.Keys.S)
            || Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))
        {
            input.add(0, -KEYBOARD_ACCELERATION*2);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
            || Gdx.input.isKeyPressed(Input.Keys.A)
            || Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT))
        {
            input.add(-KEYBOARD_ACCELERATION*2, 0);
        }

        if (input.isZero())
        {
            KEYBOARD_ACCELERATION_DELTA.setZero();
        }
        else
        {
            KEYBOARD_ACCELERATION_DELTA.add(input);
        }

        KEYBOARD_ACCELERATION_DELTA = KEYBOARD_ACCELERATION_DELTA.clamp(0, 1);

        return KEYBOARD_ACCELERATION_DELTA.cpy();
    }
}
