package com.draga;

import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseSystem;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.event.PurchasedEvent;

import java.util.ArrayList;

public abstract class Services
{
    private ArrayList<String> purchasedSkus = new ArrayList<>();

    public Services()
    {
    }

    public void setupPurchaseManager()
    {
        PurchaseSystem.onAppRestarted();
        if (PurchaseSystem.hasManager())
        {
            PurchaseManagerConfig purchaseManagerConfig = getPurchaseManagerConfig();
            PurchaseSystem.install(new PurchaseObserver(), purchaseManagerConfig);
        }
    }

    protected abstract PurchaseManagerConfig getPurchaseManagerConfig();

    public void purchaseSku(String sku)
    {
        if (PurchaseSystem.hasManager())
        {
            PurchaseSystem.purchase(sku);
        }
    }

    public abstract void share();

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

    public boolean hasPurchasedSku(String sku)
    {
        for (String purchasedSku : this.purchasedSkus)
        {
            if (purchasedSku.equals(sku))
            {
                return true;
            }
        }

        return false;
    }

    public void setPurchasedSku(String sku)
    {
        this.purchasedSkus.add(sku);
        Constants.General.EVENT_BUS.post(new PurchasedEvent(sku));
    }
}
