package com.draga.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.draga.PlayServices;
import com.draga.errorHandler.ErrorHandlerProvider;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

public class GooglePlayServices implements PlayServices
{
    private static final String LOGGING_TAG = GooglePlayServices.class.getSimpleName();

    private final AndroidLauncher androidLauncher;
    private final GameHelper      gameHelper;

    private int requestCode = 1;

    public GooglePlayServices(AndroidLauncher androidLauncher)
    {
        this.androidLauncher = androidLauncher;

        this.gameHelper = new GameHelper(androidLauncher, GameHelper.CLIENT_GAMES);
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
    public void rateGame()
    {
        final String appPackageName =
            this.androidLauncher.getPackageName();
        try
        {
            this.androidLauncher.startActivity(new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe)
        {
            this.androidLauncher.startActivity(new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
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
    public void showAchievements()
    {
        if (isSignedIn())
        {
            Intent achievementsIntent = Games.Achievements.getAchievementsIntent(
                this.gameHelper.getApiClient());
            this.androidLauncher.startActivityForResult(achievementsIntent, this.requestCode);
        }
        else
        {
            signIn();
        }
    }

    @Override
    public void showLeaderboard(String leaderboardID)
    {
        if (isSignedIn())
        {
            this.androidLauncher.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                this.gameHelper.getApiClient(),
                leaderboardID), this.requestCode);
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
}
