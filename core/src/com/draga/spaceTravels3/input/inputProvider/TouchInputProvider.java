package com.draga.spaceTravels3.input.inputProvider;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.input.inputModifier.DeadZoneInputModifier;
import com.draga.spaceTravels3.input.inputModifier.RangeInputModifier;

/**
 * The raw input is the distance in pixels between the center of the screen and the touch or click.
 * Zero if the screen is not clicked/touched. The input is then scaled by the half of the smallest
 * dimension of the screen and a dead zone is applied.
 */
public class TouchInputProvider extends InputProvider
{
    public TouchInputProvider()
    {
        float halfSmallestDimension =
            Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 2f;
        addInputModifier(new RangeInputModifier(halfSmallestDimension));

        addInputModifier(new DeadZoneInputModifier(Constants.Game.DEAD_ZONE));
    }

    @Override
    protected Vector2 getRawInput()
    {
        Vector2 input = new Vector2();
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
            // Height flipped because 0,0 of input is top left, unlike to rest of the API.
            input.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

            // This was my idea but before I could put it down Lee totally sneakily
            // solved just a tiny bit of it. (Stefano)

            // Distance between the click and the center of the screen.
            input.sub(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        }

        return input;
    }
}
