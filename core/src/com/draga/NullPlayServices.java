package com.draga;

public class NullPlayServices implements PlayServices
{
    @Override
    public void signIn()
    {

    }

    @Override
    public boolean isSignedIn()
    {
        return false;
    }

    @Override
    public void signOut()
    {

    }

    @Override
    public void showLeaderboards()
    {

    }

    @Override
    public void submitScore(int highScore, String leaderboardID)
    {

    }

    @Override
    public void updateLeaderboard(String leaderboardID, int score)
    {

    }

    @Override
    public void showLeaderboard(String leaderboardID)
    {

    }

    @Override
    public void unlockAchievement(String string)
    {

    }

    @Override
    public void showAchievements()
    {

    }

    @Override
    public void rateApp()
    {

    }
}
