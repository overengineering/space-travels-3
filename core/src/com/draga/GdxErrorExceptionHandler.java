package com.draga;

import com.badlogic.gdx.Gdx;

public class GdxErrorExceptionHandler implements ExceptionHandler
{
    @Override
    public void handle(String tag, Exception exception, String message)
    {
        Gdx.app.error(tag, message, exception);
    }

    @Override
    public void handle(String tag, String message)
    {
        Gdx.app.error(tag, message);

    }
}
