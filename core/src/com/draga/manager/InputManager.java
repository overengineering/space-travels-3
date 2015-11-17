package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.draga.Constants;

public class InputManager
{
    public static final  float  DEAD_ZONE           = 0.15f;
    private static final String LOGGING_TAG         = InputManager.class.getSimpleName();
    /**
     * Change tilt range. E.g. 1.0f = 90 degree max. 0.5f = 45 degrees max.
     */
    private static final float  ACCELEROMETER_RANGE = 0.5f;

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
                switch (SettingsManager.touchInputType)
                {
                    case ACCELEROMETER:
                        input = getAccelerometerInput();
                        break;
                    case TOUCH:
                        input = getTouchInput();
                        break;
                    default:
                        Gdx.app.error(
                            LOGGING_TAG,
                            SettingsManager.touchInputType + " input type not implemented.");
                        input = new Vector2();
                }
                break;
            case Desktop:
                input = getKeyboardInput();
                if (input.isZero())
                {
                    input = getTouchInput();
                }
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
        Vector2 input = getDeviceAccelerationForDeviceOrientation()
            .scl(1f / ACCELEROMETER_RANGE);

        // Max the gravity by the Earth gravity to avoid excessive force being applied if the device is shaken.
        input = input.clamp(0, Constants.EARTH_GRAVITY);

        // Scale the input by the Earth's gravity so that I'll be between 1 and 0
        input = input.scl(1 / Constants.EARTH_GRAVITY);

        input.x = applyDeadZone(input.x);
        input.y = applyDeadZone(input.y);

        return input;
    }

    private static Vector2 getTouchInput()
    {
        Vector2 input;
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
            // Height - Y because 0,0 of input is top left, unlike to rest of the API.
            input = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

            input.x = input.x / Gdx.graphics.getWidth();
            input.y = input.y / Gdx.graphics.getHeight();

            // Change coordinates system from [0,1] to [-1,1].
            input.x = input.x * 2 - 1;
            input.y = input.y * 2 - 1;

            input.x = applyDeadZone(input.x);
            input.y = applyDeadZone(input.y);

            Gdx.app.log(LOGGING_TAG, input.x + " - " + input.y);
        }
        else
        {
            input = Vector2.Zero;
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
            input.add(0, 1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
            || Gdx.input.isKeyPressed(Input.Keys.D)
            || Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT))
        {
            input.add(1, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)
            || Gdx.input.isKeyPressed(Input.Keys.S)
            || Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))
        {
            input.add(0, -1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
            || Gdx.input.isKeyPressed(Input.Keys.A)
            || Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT))
        {
            input.add(-1, 0);
        }

        input.clamp(0, 1);

        return input;
    }

    private static Vector2 getDeviceAccelerationForDeviceOrientation()
    {
        Vector2 input = new Vector2(Gdx.input.getAccelerometerX(), Gdx.input.getAccelerometerY());

        adjustAccelerometerForScreenRotation(input);

        return input;
    }

    private static float applyDeadZone(float value)
    {
        float sign = Math.signum(value);

        if (Math.abs(value) > DEAD_ZONE)
        {
            // Move range up by dead zone.
            value -= DEAD_ZONE * sign;

            // Squash to (dead zone to 1). So dead zone is 0, screen max is 1
            value /= (1 - DEAD_ZONE);
        }
        else
        {
            value = 0f;
        }

        return value;
    }

    private static void adjustAccelerometerForScreenRotation(Vector2 input)
    {
        Vector2 adjustedInput = new Vector2();
        switch (Gdx.input.getRotation())
        {
            case 0:
                adjustedInput.x = input.x;
                adjustedInput.y = input.y;
                break;
            case 90:
                adjustedInput.x = input.y;
                adjustedInput.y = -input.x;
                break;
            case 180:
                adjustedInput.x = -input.x;
                adjustedInput.y = -input.y;
                break;
            case 270:
                adjustedInput.x = -input.y;
                adjustedInput.y = input.x;
                break;
            default:
                Gdx.app.error(
                    LOGGING_TAG, "Orientation " + Gdx.input.getRotation() + " not implemented.");
        }

        input.set(adjustedInput);
    }
}
