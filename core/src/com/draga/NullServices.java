package com.draga;

import com.badlogic.gdx.pay.PurchaseManagerConfig;

public class NullServices extends Services
{
    @Override
    protected PurchaseManagerConfig getPurchaseManagerConfig()
    {
        return null;
    }

    @Override
    public void share()
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

    @Override
    public boolean hasPurchasedSku(String sku)
    {
        return true;
    }
}
