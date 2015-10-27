package com.draga.event;

import com.badlogic.gdx.utils.Pool;
import com.draga.entity.Planet;
import com.draga.entity.Ship;

public class ShipPlanetCollisionEvent implements Pool.Poolable
{
    public Ship   ship;
    public Planet planet;

    @Override
    public void reset()
    {
        ship = null;
        planet = null;
    }
}
