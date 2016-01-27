package com.draga;

import com.badlogic.gdx.pay.Transaction;
import com.draga.errorHandler.ErrorHandlerProvider;
import com.draga.spaceTravels3.SpaceTravels3;

public class PurchaseObserver implements com.badlogic.gdx.pay.PurchaseObserver
{
    private final String LOGGING_TAG = PurchaseObserver.class.getSimpleName();

    @Override
    public void handleInstall()
    {
        SpaceTravels3.services.restorePurchase();
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
            if (transaction.getIdentifier().equals(SpaceTravels3.services.fullVersionIdentifier)
                && transaction.isPurchased())
            {
                SpaceTravels3.services.setHasFullVersion(true);
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
        if (transaction.getIdentifier().equals(SpaceTravels3.services.fullVersionIdentifier)
            && transaction.isPurchased())
        {
            SpaceTravels3.services.setHasFullVersion(true);
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
