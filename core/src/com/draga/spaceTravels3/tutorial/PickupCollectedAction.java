package com.draga.spaceTravels3.tutorial;

import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.event.PickupCollectedEvent;
import com.draga.spaceTravels3.gameEntity.Pickup;
import com.google.common.eventbus.Subscribe;

public abstract class PickupCollectedAction extends TutorialAction
{
    private final Pickup  pickup;
    private       boolean isPickupCollected;

    public PickupCollectedAction(Pickup pickup)
    {
        this.pickup = pickup;
        this.isPickupCollected = false;
        Constants.General.EVENT_BUS.register(this);
    }

    @Override
    protected boolean isTriggered()
    {
        return this.isPickupCollected;
    }

    @Subscribe
    public void pickupCollected(PickupCollectedEvent pickupCollectedEvent)
    {
        if (pickupCollectedEvent.pickup.equals(this.pickup))
        {
            this.isPickupCollected = true;
            Constants.General.EVENT_BUS.unregister(this);
        }
    }
}
