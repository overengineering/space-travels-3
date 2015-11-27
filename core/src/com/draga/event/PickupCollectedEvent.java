package com.draga.event;

import com.badlogic.gdx.utils.Pool;
import com.draga.gameEntity.Pickup;

public class PickupCollectedEvent implements Pool.Poolable
{
    public Pickup pickup;

    @Override
    public void reset()
    {
        pickup = null;
    }

    public void set(Pickup pickup)
    {
        this.pickup = pickup;
    }
}
