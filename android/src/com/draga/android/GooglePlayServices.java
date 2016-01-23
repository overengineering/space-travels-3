package com.draga.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.draga.PlayServices;
import com.draga.errorHandler.ErrorHandlerProvider;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.example.games.basegameutils.GameHelper;

public class GooglePlayServices implements PlayServices
{
    private static final String LOGGING_TAG = GooglePlayServices.class.getSimpleName();

    private final Activity   activity;
    private final GameHelper gameHelper;

    private Runnable onSignInSucceededRunnable;

    private int requestCode = 1;

    public GooglePlayServices(Activity activity)
    {
        this.activity = activity;

        this.gameHelper = new GameHelper(activity, GameHelper.CLIENT_GAMES);
        this.gameHelper.enableDebugLog(false);

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
                    GooglePlayServices.this.onSignInSucceededRunnable;
                if (onSignInSucceededRunnable != null)
                {
                    GooglePlayServices.this.onSignInSucceededRunnable = null;
                    onSignInSucceededRunnable.run();
                }
            }
        };

        this.gameHelper.setup(gameHelperListener);
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

    @Override
    public void shareFacebook()
    {
        Uri uri;
        try
        {// TODO: 23/01/2016 this won't work, prolly needs facebook sdk?
            this.activity.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            uri = Uri.parse("fb://publish/profile/me?text=http://www.example.com");
        } catch (PackageManager.NameNotFoundException e)
        {
            uri = Uri.parse(FACEBOOK_SHARE_URI);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        this.activity.startActivityForResult(
            intent,
            GooglePlayServices.this.requestCode);
    }

    @Override
    public void invite()
    {
        runLoggingIn(new Runnable()
        {
            @Override
            public void run()
            {
                // Emails will get both title as subject and message as body but SMSs will only
                // show the message
                Intent intent = new AppInviteInvitation
                    .IntentBuilder("Checkout this game!")
                    // Despite what the docs says it will throw without message!
                    .setMessage("Checkout this game: Space Travels 3")
                    .build();
                GooglePlayServices.this.activity.startActivityForResult(
                    intent,
                    GooglePlayServices.this.requestCode);
            }
        });
    }

    @Override
    public void signIn()
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
    public boolean isSignedIn()
    {
        return this.gameHelper.isSignedIn();
    }

    @Override
    public void signOut()
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
    public void showLeaderboards()
    {
        runLoggingIn(new Runnable()
        {
            @Override
            public void run()
            {
                Intent allLeaderboardsIntent = Games.Leaderboards.getAllLeaderboardsIntent(
                    GooglePlayServices.this.gameHelper.getApiClient());
                GooglePlayServices.this.activity.startActivityForResult(
                    allLeaderboardsIntent,
                    GooglePlayServices.this.requestCode);
            }
        });
    }

    @Override
    public void updateLeaderboard(final String leaderboardID, final int score)
    {
        runLoggingIn(new Runnable()
        {
            @Override
            public void run()
            {
                Games.Leaderboards.submitScore(
                    GooglePlayServices.this.gameHelper.getApiClient(),
                    leaderboardID,
                    score);
            }
        });
    }

    @Override
    public void showLeaderboard(final String leaderboardID)
    {
        runLoggingIn(new Runnable()
        {
            @Override
            public void run()
            {
                Intent leaderboardIntent = Games.Leaderboards.getLeaderboardIntent(
                    GooglePlayServices.this.gameHelper.getApiClient(),
                    leaderboardID,
                    LeaderboardVariant.TIME_SPAN_ALL_TIME,
                    LeaderboardVariant.COLLECTION_SOCIAL);
                GooglePlayServices.this.activity.startActivityForResult(
                    leaderboardIntent,
                    GooglePlayServices.this.requestCode);
            }
        });
    }

    @Override
    public void unlockAchievement(final String achievementID)
    {
        runLoggingIn(new Runnable()
        {
            @Override
            public void run()
            {
                Games.Achievements.unlock(
                    GooglePlayServices.this.gameHelper.getApiClient(),
                    achievementID);
            }
        });
    }

    @Override
    public void showAchievements()
    {
        runLoggingIn(new Runnable()
        {
            @Override
            public void run()
            {
                Intent achievementsIntent = Games.Achievements.getAchievementsIntent(
                    GooglePlayServices.this.gameHelper.getApiClient());
                GooglePlayServices.this.activity.startActivityForResult(
                    achievementsIntent,
                    GooglePlayServices.this.requestCode);
            }
        });
    }

    @Override
    public void rateApp()
    {
        Uri uri = Uri.parse("market://details?id=" + this.activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
            Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try
        {
            this.activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e)
        {
            this.activity.startActivity(new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getPlayStoreUriString())));
        }
    }

    private String getPlayStoreUriString()
    {
        return "http://play.google.com/store/apps/details?id="
            + this.activity.getPackageName();
    }

    private void runLoggingIn(Runnable runnable)
    {
        if (isSignedIn())
        {
            runnable.run();
        }
        else
        {
            this.onSignInSucceededRunnable = runnable;
            signIn();
        }
    }
}
