package com.draga.android;

import com.crashlytics.android.Crashlytics;
import com.draga.ErrorHandler;

public class FabricErrorHandler implements ErrorHandler
{
    @Override
    public void handle(String tag, Throwable throwable, String message)
    {
        Crashlytics.logException(throwable);
        Crashlytics.log(tag + " : " + message);
    }

    @Override
    public void handle(String tag, String message)
    {
        Crashlytics.log(tag + " : " + message);
    }
}
