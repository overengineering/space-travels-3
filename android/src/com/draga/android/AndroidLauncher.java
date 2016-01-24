package com.draga.android;

import android.content.Intent;
import android.os.Bundle;
import com.android.vending.billing.utils.IabHelper;
import com.android.vending.billing.utils.IabResult;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.draga.errorHandler.ErrorHandlerProvider;
import com.draga.spaceTravels3.SpaceTravels3;
import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import io.fabric.sdk.android.Fabric;


public class AndroidLauncher extends AndroidApplication
{
    private static final String LOGGING_TAG = AndroidLauncher.class.getSimpleName();

    private AndroidServices androidServices;
    private IabHelper       iabHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());


        // In app billing.
        String base64EncodedPublicKey = "";
        // compute your public key and store it in base64EncodedPublicKey
        this.iabHelper = new IabHelper(this, base64EncodedPublicKey);
        this.iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener()
        {
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess())
                {
                    // Oh noes, there was a problem.
                    ErrorHandlerProvider.handle(
                        LOGGING_TAG,
                        "Problem setting up In-app Billing: " + result);
                }
                // Hooray, IAB is fully set up!
            }
        });


        // Fabric init and ErrorHandler.
        Fabric.with(this, new Crashlytics(), new Answers());
        ErrorHandlerProvider.addErrorHandler(new FabricErrorHandler());


        // Google Analytics init and ErrorHandling.
        GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(this);
        final Tracker tracker = googleAnalytics.newTracker("UA-72699204-1");

        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        googleAnalytics.reportActivityStart(this);

        ErrorHandlerProvider.addErrorHandler(new GoogleAnalyticsErrorHandler(tracker));


        this.androidServices = new AndroidServices(this);
        this.androidServices.googleSignIn();


        // App config and launch.
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useCompass = false;
        config.useAccelerometer = true;
        config.hideStatusBar = true;
        config.useWakelock = true;

        initialize(new SpaceTravels3(this.androidServices), config);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.androidServices.onStart(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        this.androidServices.onStop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (this.iabHelper != null)
        {
            this.iabHelper.dispose();
        }
        this.iabHelper = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        this.androidServices.onActivityResult(requestCode, resultCode, data);
    }
}
