package com.draga;

import com.badlogic.gdx.Gdx;

public class GdxErrorHandler implements ErrorHandler
{
    @Override
    public void handle(String tag, Throwable throwable, String message)
    {
        Gdx.app.error(tag, message, throwable);
    }

    @Override
    public void handle(String tag, String message)
    {
        Gdx.app.error(tag, message);

    }
}
