package com.draga.spaceTravels3.event;

public class VerifyPurchaseEvent
{
    public boolean hasFullVersion = false;

    public VerifyPurchaseEvent(boolean hasFullVersion)
    {
        this.hasFullVersion = hasFullVersion;
    }
}
