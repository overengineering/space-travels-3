package com.draga;

public abstract class NullServices extends Services
{
    public NullServices()
    {
        super("");
    }

    @Override
    public void share()
    {

    }

    @Override
    public void purchaseFullVersion()
    {

    }

    @Override
    public boolean hasFullVersion()
    {
        return false;
    }

    @Override
    public void googleSignIn()
    {

    }

    @Override
    public boolean googleIsSignedIn()
    {
        return false;
    }

    @Override
    public void googleSignOut()
    {

    }

    @Override
    public void googleShowLeaderboards()
    {

    }

    @Override
    public void googleUpdateLeaderboard(String leaderboardID, int score)
    {

    }

    @Override
    public void googleShowLeaderboard(String leaderboardID)
    {

    }

    @Override
    public void googleUnlockAchievement(String string)
    {

    }

    @Override
    public void googleShowAchievements()
    {

    }

    @Override
    public void rateApp()
    {

    }
}
