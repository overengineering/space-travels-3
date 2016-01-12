package com.draga.spaceTravels3.event;

import com.draga.spaceTravels3.gameEntity.Planet;
import com.draga.spaceTravels3.gameEntity.Ship;

public class ShipPlanetCollisionEvent
{
    public Ship   ship;
    public Planet planet;

    public ShipPlanetCollisionEvent(Ship ship, Planet planet)
    {
        this.ship = ship;
        this.planet = planet;
    }
}
