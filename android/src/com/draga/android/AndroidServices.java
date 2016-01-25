package com.draga.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import com.android.vending.billing.utils.IabException;
import com.android.vending.billing.utils.IabHelper;
import com.android.vending.billing.utils.IabResult;
import com.android.vending.billing.utils.Purchase;
import com.draga.Services;
import com.draga.errorHandler.ErrorHandlerProvider;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.event.VerifyPurchaseEvent;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.example.games.basegameutils.GameHelper;

import java.util.Collections;

public class AndroidServices implements Services
{
    private static final String LOGGING_TAG = AndroidServices.class.getSimpleName();

    private final Activity   activity;
    private final GameHelper gameHelper;

    /**
     * If an action that requires Google sign in is requested and we are not currently signed it
     * then saves it in this runnable that gets executed, if not null, if login is successful.
     */
    private Runnable onGoogleSignInSucceededRunnable;

    private int activityRequestCode = 1;
    
    private String  fullVersionSKU = "full_version";
    private boolean hasFullVersion = false;

    private IabHelper iabHelper;

    public AndroidServices(Activity activity)
    {
        this.activity = activity;

        this.gameHelper = new GameHelper(activity, GameHelper.CLIENT_GAMES);
        this.gameHelper.enableDebugLog(Constants.General.IS_DEBUGGING);

        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
        {
            @Override
            public void onSignInFailed()
            {
            }

            @Override
            public void onSignInSucceeded()
            {
                Runnable onSignInSucceededRunnable =
                    AndroidServices.this.onGoogleSignInSucceededRunnable;
                if (onSignInSucceededRunnable != null)
                {
                    AndroidServices.this.onGoogleSignInSucceededRunnable = null;
                    onSignInSucceededRunnable.run();
                }
            }
        };

        this.gameHelper.setup(gameHelperListener);

        // In app billing.
        String base64EncodedPublicKey =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuxHcBKJW8RRYrMfo68bOMM1gXWqmGDe1ytGNXfPdk7ArMIB6vtLmZAwbyt38L3XYjYDnhysBMmhV9JYuCAe/zNzRl0G+IC75MmJVYUnUuXQUKrTC/6qvcNm5mgn2rRs1F+9I5uGYiHibXrYt8zNETkNIEP1W94X6H0we6pqPQjg9sEjp5ok3aetAtIHFQkuzT2OB7AcPIRxZcup6ZVUbvLvUbGqo3V38ik3wgj7S/oEybSnX/Wjw1ALpML7Eh/fTyaDyCVy8NM4WPEag/Hix1+Hz0OfJQ/yLl8keXhYA8um03y52vKZK2vu8efSVrAeTeF9lOCYMEKd4YkKHOB9vFQIDAQAB";
        // compute your public key and store it in base64EncodedPublicKey
        this.iabHelper = new IabHelper(this.activity, base64EncodedPublicKey);
        this.iabHelper.enableDebugLogging(Constants.General.IS_DEBUGGING);

        this.iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener()
        {
            public void onIabSetupFinished(IabResult result)
            {
                if (result.isSuccess())
                {
                    verifyPurchase();
                }
                else
                {
                    ErrorHandlerProvider.handle(
                        LOGGING_TAG,
                        "Problem setting up In-app Billing: " + result);
                }
            }
        });
    }

    private void verifyPurchase()
    {
        try
        {
            boolean hasFullVersion = this.iabHelper
                .queryInventory(false, Collections.singletonList(this.fullVersionSKU))
                .hasPurchase(this.fullVersionSKU);
            setHasFullVersion(hasFullVersion);
        } catch (IabException e)
        {
            ErrorHandlerProvider.handle(
                LOGGING_TAG,
                e.getResult().getResponse() + "\r\n" + e.getResult().getMessage(),
                e);
        }
    }

    public void setHasFullVersion(boolean hasFullVersion)
    {
        this.hasFullVersion = hasFullVersion;
        Constants.General.EVENT_BUS.post(new VerifyPurchaseEvent(hasFullVersion));
    }

    @Override
    public void share()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Checkout this game: Space Travels 3");
        intent.putExtra(Intent.EXTRA_TITLE, "Checkout this game: Space Travels 3");
        // FB messenger will cut the text out ref. https://developers.facebook.com/bugs/332619626816423
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Checkout this game: Space Travels 3\r\n"
                + getPlayStoreUriString());
        this.activity.startActivity(Intent.createChooser(intent, "Share using"));
    }

    @Override
    public void purchaseFullVersion()
    {
        // TODO: 24/01/2016 pass payload to identify user as described here https://developer.android.com/training/in-app-billing/purchase-iab-products.html
        try
        {
            this.iabHelper.launchPurchaseFlow(
                this.activity,
                this.fullVersionSKU,
                this.activityRequestCode,
                new IabHelper.OnIabPurchaseFinishedListener()
                {
                    @Override
                    public void onIabPurchaseFinished(IabResult result, Purchase purchase)
                    {
                        if (result.isSuccess())
                        {
                            AndroidServices.this.setHasFullVersion(true);
                        }
                        else
                        {
                            ErrorHandlerProvider.handle(
                                LOGGING_TAG,
                                result.getResponse() + "\r\n" + result.getMessage());
                        }
                    }
                });
        } catch (IllegalStateException e)
        {
            ErrorHandlerProvider.handle(
                LOGGING_TAG,
                "Exception thrown trying to launch a purchase flow",
                e);
        }
    }

    @Override
    public boolean hasFullVersion()
    {
        return this.hasFullVersion;
    }

    @Override
    public void googleSignIn()
    {
        try
        {
            this.gameHelper.beginUserInitiatedSignIn();
        } catch (Exception e)
        {
            ErrorHandlerProvider.handle(LOGGING_TAG, "Log in failed", e);
        }
    }

    @Override
    public boolean googleIsSignedIn()
    {
        return this.gameHelper.isSignedIn();
    }

    @Override
    public void googleSignOut()
    {
        try
        {
            this.gameHelper.signOut();
        } catch (Exception e)
        {
            ErrorHandlerProvider.handle(LOGGING_TAG, "Log out failed", e);
        }
    }

    @Override
    public void googleShowLeaderboards()
    {
        googleRunLoggingIn(new Runnable()
        {
            @Override
            public void run()
            {
                Intent allLeaderboardsIntent = Games.Leaderboards.getAllLeaderboardsIntent(
                    AndroidServices.this.gameHelper.getApiClient());
                AndroidServices.this.activity.startActivityForResult(
                    allLeaderboardsIntent,
                    AndroidServices.this.activityRequestCode);
            }
        });
    }

    @Override
    public void googleUpdateLeaderboard(final String leaderboardID, final int score)
    {
        googleRunLoggingIn(new Runnable()
        {
            @Override
            public void run()
            {
                Games.Leaderboards.submitScore(
                    AndroidServices.this.gameHelper.getApiClient(),
                    leaderboardID,
                    score);
            }
        });
    }

    @Override
    public void googleShowLeaderboard(final String leaderboardID)
    {
        googleRunLoggingIn(new Runnable()
        {
            @Override
            public void run()
            {
                Intent leaderboardIntent = Games.Leaderboards.getLeaderboardIntent(
                    AndroidServices.this.gameHelper.getApiClient(),
                    leaderboardID,
                    LeaderboardVariant.TIME_SPAN_ALL_TIME,
                    LeaderboardVariant.COLLECTION_SOCIAL);
                AndroidServices.this.activity.startActivityForResult(
                    leaderboardIntent,
                    AndroidServices.this.activityRequestCode);
            }
        });
    }

    @Override
    public void googleUnlockAchievement(final String achievementID)
    {
        googleRunLoggingIn(new Runnable()
        {
            @Override
            public void run()
            {
                Games.Achievements.unlock(
                    AndroidServices.this.gameHelper.getApiClient(),
                    achievementID);
            }
        });
    }

    @Override
    public void googleShowAchievements()
    {
        googleRunLoggingIn(new Runnable()
        {
            @Override
            public void run()
            {
                Intent achievementsIntent = Games.Achievements.getAchievementsIntent(
                    AndroidServices.this.gameHelper.getApiClient());
                AndroidServices.this.activity.startActivityForResult(
                    achievementsIntent,
                    AndroidServices.this.activityRequestCode);
            }
        });
    }

    /**
     * Open a new activity to rate the app. If G play store can't be opened the browser is
     * pointed there instead.
     */
    @Override
    public void rateApp()
    {
        Uri uri = Uri.parse("market://details?id=" + this.activity.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        // I know is deprecated but I don't want to increase the min SDK.
        //noinspection deprecation
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
            Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try
        {
            this.activity.startActivity(intent);
        } catch (ActivityNotFoundException e)
        {
            this.activity.startActivity(new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getPlayStoreUriString())));
        }
    }

    private void googleRunLoggingIn(Runnable runnable)
    {
        if (googleIsSignedIn())
        {
            runnable.run();
        }
        else
        {
            this.onGoogleSignInSucceededRunnable = runnable;
            googleSignIn();
        }
    }

    private String getPlayStoreUriString()
    {
        return "http://play.google.com/store/apps/details?id="
            + this.activity.getPackageName();
    }

    public void onStart(Activity activity)
    {
        this.gameHelper.onStart(activity);
    }

    public void onStop()
    {
        this.gameHelper.onStop();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        this.gameHelper.onActivityResult(requestCode, resultCode, data);
    }

    public void onDestroy()
    {
        if (this.iabHelper != null)
        {
            this.iabHelper.dispose();
        }
        this.iabHelper = null;
    }
}
