package com.draga.android;

import com.crashlytics.android.Crashlytics;
import com.draga.ExceptionHandler;

public class FabricExceptionHandler implements ExceptionHandler
{
    @Override
    public void handle(String tag, Exception exception, String message)
    {
        Crashlytics.logException(exception);
        Crashlytics.log(tag + " : " + message);
    }

    @Override
    public void handle(String tag, String message)
    {
        Crashlytics.log(tag + " : " + message);
    }
}
