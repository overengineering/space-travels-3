package com.draga.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.draga.Services;
import com.draga.errorHandler.ErrorHandlerProvider;
import com.draga.spaceTravels3.Constants;
import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.example.games.basegameutils.GameHelper;

public class AndroidServices extends Services
{
    private static final String LOGGING_TAG = AndroidServices.class.getSimpleName();

    private final Activity   activity;
    private       GameHelper gameHelper;

    /**
     * If an action that requires Google sign in is requested and we are not currently signed it
     * then saves it in this runnable that gets executed, if not null, if login is successful.
     */
    private Runnable onGoogleSignInSucceededRunnable;

    private int activityRequestCode = 1;

    public AndroidServices(Activity activity)
    {
        super("full_version");
        this.activity = activity;

        setupPurchaseManager();

        setupGameHelper(activity);

        FacebookSdk.sdkInitialize(activity);

/*
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
        });*/
    }

    private void setupPurchaseManager()
    {
        PurchaseManagerConfig purchaseManagerConfig = new PurchaseManagerConfig();

        Offer fullVersionOffer = new Offer();
        fullVersionOffer.putIdentifierForStore(
            PurchaseManagerConfig.STORE_NAME_ANDROID_GOOGLE,
            this.fullVersionIdentifier);
        purchaseManagerConfig.addOffer(fullVersionOffer);

        purchaseManagerConfig.addStoreParam(
            PurchaseManagerConfig.STORE_NAME_ANDROID_GOOGLE,
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuxHcBKJW8RRYrMfo68bOMM1gXWqmGDe1ytGNXfPdk7ArMIB6vtLmZAwbyt38L3XYjYDnhysBMmhV9JYuCAe/zNzRl0G+IC75MmJVYUnUuXQUKrTC/6qvcNm5mgn2rRs1F+9I5uGYiHibXrYt8zNETkNIEP1W94X6H0we6pqPQjg9sEjp5ok3aetAtIHFQkuzT2OB7AcPIRxZcup6ZVUbvLvUbGqo3V38ik3wgj7S/oEybSnX/Wjw1ALpML7Eh/fTyaDyCVy8NM4WPEag/Hix1+Hz0OfJQ/yLl8keXhYA8um03y52vKZK2vu8efSVrAeTeF9lOCYMEKd4YkKHOB9vFQIDAQAB");

        initPurchaseManager(purchaseManagerConfig);
    }

    private void setupGameHelper(Activity activity)
    {
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
    }
/*
    private void verifyPurchase()
    {
        try
        {
            this.hasFullVersion = this.iabHelper
                .queryInventory(false, Collections.singletonList(this.fullVersionSKU))
                .hasPurchase(this.fullVersionSKU);
        } catch (IabException e)
        {
            ErrorHandlerProvider.handle(
                LOGGING_TAG,
                e.getResult().getResponse() + "\r\n" + e.getResult().getMessage(),
                e);
        }
    }*/

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

    /**
     * Share on FB through FB SDK. Creates a browser dialog if the FB app is not installed.
     */
    @Override
    public void facebookShare()
    {
        ShareLinkContent content = new ShareLinkContent.Builder()
            .setContentUrl(Uri.parse(getPlayStoreUriString()))
            .build();
        ShareDialog.show(this.activity, content);
    }

    /**
     * Invite friends on FB. This only works if the FB app is installed OR some other criteria are
     * met so facebookCanInvite() should be checked before allowing the user to do it.
     */
    @Override
    public void facebookInvite()
    {
        if (AppInviteDialog.canShow())
        {
            AppInviteContent content = new AppInviteContent.Builder()
                .setApplinkUrl(getPlayStoreUriString())
                .build();
            AppInviteDialog.show(this.activity, content);
        }
    }

    @Override
    public boolean facebookCanInvite()
    {
        return AppInviteDialog.canShow();
    }

    /**
     * Sign into G play and invite contacts through email or SMS.
     */
    @Override
    public void googleInvite()
    {
        googleRunLoggingIn(new Runnable()
        {
            @Override
            public void run()
            {
                // Emails will get both title as subject and message as body but SMSs will only
                // show the message.
                Intent intent = new AppInviteInvitation
                    .IntentBuilder("Checkout this game!")
                    // Despite what the docs says it will throw without message!
                    .setMessage("Checkout this game: Space Travels 3")
                    .build();
                AndroidServices.this.activity.startActivityForResult(
                    intent,
                    AndroidServices.this.activityRequestCode);
            }
        });
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

    /*
        @Override
        public void purchaseFullVersion(String fullVersionIdentifier)
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
                                AndroidServices.this.hasFullVersion = true;
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
    */
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
}
