package com.draga.spaceTravels3.event;

public class LoseEvent
{
    private final boolean planetIsDestination;
    private final boolean shipIsTooFast;

    public LoseEvent(boolean planetIsDestination, boolean shipIsTooFast)
    {
        this.planetIsDestination = planetIsDestination;
        this.shipIsTooFast = shipIsTooFast;

    }

    public boolean isPlanetIsDestination()
    {
        return this.planetIsDestination;
    }

    public boolean isShipIsTooFast()
    {
        return this.shipIsTooFast;
    }
}
