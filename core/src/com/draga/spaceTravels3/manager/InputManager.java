package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.Gdx;
import com.draga.ErrorHandlerProvider;
import com.draga.PooledVector2;
import com.draga.spaceTravels3.input.inputProvider.AccelerometerInputProvider;
import com.draga.spaceTravels3.input.inputProvider.InputProvider;
import com.draga.spaceTravels3.input.inputProvider.KeyboardInputProvider;
import com.draga.spaceTravels3.input.inputProvider.TouchInputProvider;

public abstract class InputManager
{
    private static final String LOGGING_TAG = InputManager.class.getSimpleName();
    private static PooledVector2 inputForce = PooledVector2.newVector2(0f, 0f);
    private static InputProvider touchInputProvider;
    private static InputProvider accelerometerInputProvider;
    private static InputProvider keyboardInputProvider;

    private InputManager()
    {
    }

    public static void create()
    {
        inputForce = PooledVector2.newVector2(0f, 0f);
        touchInputProvider = new TouchInputProvider();
        accelerometerInputProvider = new AccelerometerInputProvider();
        keyboardInputProvider = new KeyboardInputProvider();
    }

    /**
     * Returns a vector with length from 0 to 1, representing where the input is pointing to,
     * abstracting away the fact that it could be a mobile accelerometer, mouse clicks, etc.
     *
     * @return A PooledVector2 of length from 0 to 1 of where the input is pointing
     */
    public static PooledVector2 getInputForce()
    {
        return inputForce.cpy();
    }

    public static void update()
    {
        inputForce.close();

        PooledVector2 input;
        switch (Gdx.app.getType())
        {
            case Android:
            case iOS:
                switch (SettingsManager.getSettings().inputType)
                {
                    case ACCELEROMETER:
                        input =  accelerometerInputProvider.getInput();
                        break;
                    case TOUCH:
                        input = touchInputProvider.getInput();
                        break;
                    default:
                        ErrorHandlerProvider.handle(
                            LOGGING_TAG,
                            SettingsManager.getSettings().inputType
                                + " input type not implemented.");
                        input = PooledVector2.newVector2(0f, 0f);
                }
                break;
            case Desktop:
                input = keyboardInputProvider.getInput();
                if (input.isZero())
                {
                    input = touchInputProvider.getInput();
                }
                break;
            default:
                ErrorHandlerProvider.handle(
                    LOGGING_TAG, "Device type " + Gdx.input.getRotation() + " not implemented.");
                input = PooledVector2.newVector2(0f, 0f);
                break;
        }
        inputForce = input;
    }

    public static void dispose()
    {

    }
}
