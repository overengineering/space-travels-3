package com.draga.android;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.crashlytics.android.Crashlytics;
import com.draga.errorHandler.ErrorHandler;

public class FabricErrorHandler implements ErrorHandler
{
    @Override
    public void handle(String tag, Throwable throwable, String message)
    {
        // Wraps the parameters in a new exception because logging messages doesn't seems to work.
        RuntimeException runtimeException = new RuntimeException(tag + " : " + message, throwable);
        Crashlytics.logException(runtimeException);
    }

    @Override
    public void handle(String tag, String message)
    {
        // Wraps the parameters in an exception because logging messages doesn't seems to work.
        RuntimeException runtimeException = new RuntimeException(tag + " : " + message);
        Crashlytics.logException(runtimeException);
    }
}
