package com.draga.spaceTravels3.event;

import com.draga.spaceTravels3.gameEntity.Pickup;

public class PickupCollectedEvent
{
    public Pickup pickup;

    public PickupCollectedEvent(Pickup pickup)
    {
        this.pickup = pickup;
    }
}
