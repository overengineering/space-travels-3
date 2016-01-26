package com.draga;

import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseSystem;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.event.VerifyPurchaseEvent;

public abstract class Services
{
    protected final String fullVersionIdentifier;
    private boolean hasFullVersion = false;

    public Services(String fullVersionIdentifier)
    {
        this.fullVersionIdentifier = fullVersionIdentifier;
    }

    protected void initPurchaseManager(PurchaseManagerConfig purchaseManagerConfig)
    {
        if (PurchaseSystem.hasManager())
        {
            PurchaseSystem.install(new PurchaseObserver(), purchaseManagerConfig, false);
        }
    }

    public void setHasFullVersion(boolean hasFullVersion)
    {
        this.hasFullVersion = hasFullVersion;
        Constants.General.EVENT_BUS.post(new VerifyPurchaseEvent(hasFullVersion));
    }

    public void purchaseFullVersion()
    {
        PurchaseSystem.purchase(this.fullVersionIdentifier);
    }

    public abstract void setupPurchaseManager();

    public abstract void share();

    public boolean hasFullVersion()
    {
        return this.hasFullVersion;
    }

    public abstract void googleSignIn();

    public abstract boolean googleIsSignedIn();

    public abstract void googleSignOut();

    public abstract void googleShowLeaderboards();

    public abstract void googleUpdateLeaderboard(String leaderboardID, int score);

    public abstract void googleShowLeaderboard(String leaderboardID);

    public abstract void googleUnlockAchievement(String string);

    public abstract void googleShowAchievements();

    public abstract void rateApp();

    public void restorePurchase()
    {
        PurchaseSystem.purchaseRestore();
    }
}
