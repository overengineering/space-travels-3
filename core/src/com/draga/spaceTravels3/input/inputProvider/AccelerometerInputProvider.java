package com.draga.spaceTravels3.input.inputProvider;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.input.inputModifier.DeadZoneInputModifier;
import com.draga.spaceTravels3.input.inputModifier.ScaleInputModifier;

/**
 * The raw input are the accelerometer X and Y axis, appropriately rotated according to the screen
 * rotation. Earth's gravity is then used to scale this input and a dead zone is applied.
 */
public class AccelerometerInputProvider extends InputProvider
{
    private static final String LOGGING_TAG = AccelerometerInputProvider.class.getSimpleName();

    public AccelerometerInputProvider()
    {
        addInputModifier(new ScaleInputModifier(Constants.General.EARTH_GRAVITY));
        addInputModifier(new DeadZoneInputModifier(Constants.Game.DEAD_ZONE));
    }

    @Override
    protected Vector2 getRawInput()
    {
        Vector2 input = new Vector2();
        // Rotate the vector "manually" instead of using input.rotate(Gdx.input.getRotation())
        // because it doesn't need expensive operations.
        switch (Gdx.input.getRotation())
        {
            case 0:
                input.set(Gdx.input.getAccelerometerX(), Gdx.input.getAccelerometerY());
                break;
            case 90:
                input.set(Gdx.input.getAccelerometerY(), -Gdx.input.getAccelerometerX());
                break;
            case 180:
                input.set(-Gdx.input.getAccelerometerX(), -Gdx.input.getAccelerometerY());
                break;
            case 270:
                input.set(-Gdx.input.getAccelerometerY(), Gdx.input.getAccelerometerX());
                break;
            default:
                Gdx.app.error(
                    LOGGING_TAG,
                    "Orientation " + Gdx.input.getRotation() + " not implemented.");
        }

        input.scl(Constants.Game.ACCELEROMETER_RANGE);

        return input;
    }
}
