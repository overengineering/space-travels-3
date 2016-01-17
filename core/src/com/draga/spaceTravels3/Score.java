package com.draga.spaceTravels3;

import com.badlogic.gdx.utils.Pool;

public class Score implements Pool.Poolable
{
    private int pickupPoints;
    private int timePoints;
    private int fuelPoints;

    public void set(int pickupsCollected, float elapsedSeconds, float percentageFuelRemaining)
    {
        this.pickupPoints = pickupsCollected * Constants.Game.PICKUP_POINTS;
        this.timePoints = -(int) (elapsedSeconds * Constants.Game.TIME_POINTS);
        this.fuelPoints = (int) (percentageFuelRemaining * Constants.Game.FUEL_POINTS);
    }

    public int getTotalScore()
    {
        return this.pickupPoints + this.timePoints + this.fuelPoints;
    }

    public int getPickupPoints()
    {
        return this.pickupPoints;
    }

    public int getTimePoints()
    {
        return this.timePoints;
    }

    public int getFuelPoints()
    {
        return this.fuelPoints;
    }

    @Override
    public void reset()
    {
        this.pickupPoints = 0;
        this.timePoints = 0;
        this.fuelPoints = 0;
    }
}
