package com.draga.spaceTravels3.event;

public class PurchasedEvent
{
    public final String sku;

    public PurchasedEvent(String sku)
    {
        this.sku = sku;
    }
}
