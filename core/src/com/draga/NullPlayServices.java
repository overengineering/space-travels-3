package com.draga;

public class NullPlayServices implements PlayServices
{
    @Override
    public void signIn()
    {

    }

    @Override
    public void signOut()
    {

    }

    @Override
    public void rateGame()
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
    public void submitScore(int highScore, String leaderboardID)
    {

    }

    @Override
    public void showLeaderboard(String leaderboardID)
    {

    }

    @Override
    public boolean isSignedIn()
    {
        return false;
    }

    @Override
    public void updateLeaderboard(String leaderboardID, int score)
    {

    }
}
