package com.draga.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

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
}
