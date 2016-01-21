package com.draga.android;

import com.draga.errorHandler.ErrorHandler;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

class GoogleAnalyticsErrorHandler implements ErrorHandler
{
    private final Tracker tracker;

    public GoogleAnalyticsErrorHandler(Tracker tracker)
    {
        this.tracker = tracker;
    }

    @Override
    public void handle(String tag, Throwable throwable, String message)
    {
        this.tracker.send(new HitBuilders.ExceptionBuilder()
            .setDescription(tag + "\r\n" + message + "\r\n" + throwable.getMessage())
            .setFatal(false)
            .build());
    }

    @Override
    public void handle(String tag, String message)
    {
        this.tracker.send(new HitBuilders.ExceptionBuilder()
            .setDescription(tag + "\r\n" + message)
            .setFatal(false)
            .build());
    }
}
