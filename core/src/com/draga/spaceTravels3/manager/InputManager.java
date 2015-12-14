package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.draga.spaceTravels3.input.inputProvider.AccelerometerInputProvider;
import com.draga.spaceTravels3.input.inputProvider.KeyboardInputProvider;
import com.draga.spaceTravels3.input.inputProvider.TouchInputProvider;

public class InputManager
{
    private static final String LOGGING_TAG = InputManager.class.getSimpleName();
    private static Vector2 inputForce;

    /**
     * Returns a vector with length from 0 to 1, representing where the input is pointing to,
     * abstracting away the fact that it could be a mobile accelerometer, mouse clicks, etc.
     *
     * @return A Vector2 of length from 0 to 1 of where the input is pointing
     */
    public static Vector2 getInputForce()
    {
        return inputForce.cpy();
    }

    public static void update()
    {
        Vector2 input;
        switch (Gdx.app.getType())
        {
            case Android:
            case iOS:
                switch (SettingsManager.getSettings().inputType)
                {
                    case ACCELEROMETER:
                        input = AccelerometerInputProvider.PROVIDER.getInput();
                        break;
                    case TOUCH:
                        input = TouchInputProvider.PROVIDER.getInput();
                        break;
                    default:
                        Gdx.app.error(
                            LOGGING_TAG,
                            SettingsManager.getSettings().inputType
                                + " input type not implemented.");
                        input = new Vector2();
                }
                break;
            case Desktop:
                input = KeyboardInputProvider.PROVIDER.getInput();
                if (input.isZero())
                {
                    input = TouchInputProvider.PROVIDER.getInput();
                }
                break;
            default:
                Gdx.app.error(
                    LOGGING_TAG, "Device type " + Gdx.input.getRotation() + " not implemented.");
                input = new Vector2();
                break;
        }
        inputForce = input;
    }
}
