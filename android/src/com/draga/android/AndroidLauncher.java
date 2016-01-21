package com.draga.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.draga.errorHandler.ErrorHandlerProvider;
import com.draga.spaceTravels3.SpaceTravels3;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import io.fabric.sdk.android.Fabric;


public class AndroidLauncher extends AndroidApplication
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Fabric init and ErrorHandler.
        Fabric.with(this, new Crashlytics(), new Answers());

        ErrorHandlerProvider.addErrorHandler(new FabricErrorHandler());

        // Google Analytics init and ErrorHandling.
        GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(this);
        final Tracker tracker = googleAnalytics.newTracker("UA-72699204-1");

        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);

        googleAnalytics.reportActivityStart(this);

        ErrorHandlerProvider.addErrorHandler(new GoogleAnalyticsErrorHandler(tracker));

        // App config and launch.
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useCompass = false;
        config.useAccelerometer = true;
        config.hideStatusBar = true;
        config.useWakelock = true;

        initialize(new SpaceTravels3(), config);
    }

}
