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
    public void showAchievement(String achievementID)
    {

    }

    @Override
    public void submitScore(int highScore, String leaderboardID)
    {

    }

    @Override
    public void showScore(String leaderboardID)
    {

    }

    @Override
    public boolean isSignedIn()
    {
        return false;
    }
}
