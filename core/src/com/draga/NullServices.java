package com.draga;

public class NullServices implements Services
{
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
    public void facebookShare()
    {

    }

    @Override
    public void facebookInvite()
    {

    }

    @Override
    public boolean facebookCanInvite()
    {
        return false;
    }

    @Override
    public void googleInvite()
    {

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
