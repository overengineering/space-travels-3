package com.draga.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;

public final class GraphicsUtils
{
    private GraphicsUtils()
    {
    }

    public static void enableBlending()
    {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void disableBlending()
    {
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public static int getClosestPowerOfTwo(int value)
    {
        int nextPowerOfTwo = MathUtils.nextPowerOfTwo(value);
        int previousPowerOfTwo = nextPowerOfTwo / 2;

        if (nextPowerOfTwo - value > value - previousPowerOfTwo)
        {
            return previousPowerOfTwo;
        }
        else
        {
            return nextPowerOfTwo;
        }
    }
}
