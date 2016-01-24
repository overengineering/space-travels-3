package com.draga;

import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseSystem;
import com.badlogic.gdx.pay.Transaction;
import com.draga.errorHandler.ErrorHandlerProvider;

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
            PurchaseSystem.install(new PurchaseObserver(), purchaseManagerConfig);

            PurchaseSystem.purchaseRestore();
        }
    }

    public void purchaseFullVersion()
    {
        PurchaseSystem.purchase(this.fullVersionIdentifier);
    }

    public boolean hasFullVersion()
    {
        return this.hasFullVersion;
    }

    public abstract void facebookShare();

    public abstract void facebookInvite();

    public abstract boolean facebookCanInvite();

    public abstract void googleInvite();

    public abstract void googleSignIn();

    public abstract boolean googleIsSignedIn();

    public abstract void googleSignOut();

    public abstract void googleShowLeaderboards();

    public abstract void googleUpdateLeaderboard(String leaderboardID, int score);

    public abstract void googleShowLeaderboard(String leaderboardID);

    public abstract void googleUnlockAchievement(String string);

    public abstract void googleShowAchievements();

    public abstract void rateApp();

    private class PurchaseObserver implements com.badlogic.gdx.pay.PurchaseObserver
    {
        private final String LOGGING_TAG = PurchaseObserver.class.getSimpleName();

        @Override
        public void handleInstall()
        {

        }

        @Override
        public void handleInstallError(Throwable e)
        {
            ErrorHandlerProvider.handle(this.LOGGING_TAG, "Purchase manager install error", e);
        }

        @Override
        public void handleRestore(Transaction[] transactions)
        {
            for (Transaction transaction : transactions)
            {
                if (transaction.getIdentifier().equals(Services.this.fullVersionIdentifier))
                {
                    Services.this.hasFullVersion = true;
                }
            }
        }

        @Override
        public void handleRestoreError(Throwable e)
        {
            ErrorHandlerProvider.handle(this.LOGGING_TAG, "Purchase manager restore error", e);
        }

        @Override
        public void handlePurchase(Transaction transaction)
        {
            if (transaction.getIdentifier().equals(Services.this.fullVersionIdentifier))
            {
                Services.this.hasFullVersion = true;
            }
            // TODO: 24/01/2016 thanks message
        }

        @Override
        public void handlePurchaseError(Throwable e)
        {
            ErrorHandlerProvider.handle(this.LOGGING_TAG, "Purchase manager purchase error", e);
        }

        @Override
        public void handlePurchaseCanceled()
        {

        }
    }
}
