package com.draga.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import com.draga.PlayServices;
import com.draga.errorHandler.ErrorHandlerProvider;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.example.games.basegameutils.GameHelper;

public class GooglePlayServices implements PlayServices
{
    private static final String LOGGING_TAG = GooglePlayServices.class.getSimpleName();

    private final Activity activity;
    private final GameHelper gameHelper;

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
    public void showLeaderboards()
    {
        if (isSignedIn())
        {
            Intent allLeaderboardsIntent = Games.Leaderboards.getAllLeaderboardsIntent(
                this.gameHelper.getApiClient());
            this.activity.startActivityForResult(allLeaderboardsIntent, this.requestCode);
        }
        else
        {
            signIn();
        }
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
    public void unlockAchievement(String achievementID)
    {
        Games.Achievements.unlock(
            this.gameHelper.getApiClient(),
            achievementID);
    }

    @Override
    public void showAchievements()
    {
        if (isSignedIn())
        {
            Intent achievementsIntent = Games.Achievements.getAchievementsIntent(
                this.gameHelper.getApiClient());
            this.activity.startActivityForResult(achievementsIntent, this.requestCode);
        }
        else
        {
            signIn();
        }
    }

    @Override
    public void submitScore(int highScore, String leaderboardID)
    {
        if (isSignedIn())
        {
            Games.Leaderboards.submitScore(
                this.gameHelper.getApiClient(),
                leaderboardID, highScore);
        }
    }

    @Override
    public void showLeaderboard(String leaderboardID)
    {
        if (isSignedIn())
        {
            Intent leaderboardIntent = Games.Leaderboards.getLeaderboardIntent(
                this.gameHelper.getApiClient(),
                leaderboardID,
                LeaderboardVariant.TIME_SPAN_ALL_TIME,
                LeaderboardVariant.COLLECTION_SOCIAL);
            this.activity.startActivityForResult(leaderboardIntent, this.requestCode);
        }
        else
        {
            signIn();
        }
    }

    @Override
    public boolean isSignedIn()
    {
        return this.gameHelper.isSignedIn();
    }

    @Override
    public void updateLeaderboard(String leaderboardID, int score)
    {
        if (isSignedIn())
        {
            Games.Leaderboards.submitScore(this.gameHelper.getApiClient(), leaderboardID, score);
        }
        else
        {
            signIn();
        }
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
                Uri.parse("http://play.google.com/store/apps/details?id="
                    + this.activity.getPackageName())));
        }
    }
}
