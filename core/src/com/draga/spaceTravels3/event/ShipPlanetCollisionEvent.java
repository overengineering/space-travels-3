package com.draga.spaceTravels3.event;

import com.badlogic.gdx.utils.Pool;
import com.draga.spaceTravels3.gameEntity.Planet;
import com.draga.spaceTravels3.gameEntity.Ship;

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
