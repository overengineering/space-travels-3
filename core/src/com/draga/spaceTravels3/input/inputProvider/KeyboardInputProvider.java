package com.draga.spaceTravels3.input.inputProvider;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.draga.PooledVector2;

/**
 * The raw input is 1 for each WASD or direction (including keypad) pressed;
 */
public class KeyboardInputProvider extends InputProvider
{
    @Override
    protected PooledVector2 getRawInput()
    {
        PooledVector2 input = PooledVector2.newVector2(0f, 0f);

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

        return input;
    }
}
