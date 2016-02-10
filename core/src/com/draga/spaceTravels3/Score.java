package com.draga.spaceTravels3;

public class Score
{
    private int pickupPoints;
    private int timePoints;
    private int fuelPoints;

    public Score(int pickupsCollected, float elapsedSeconds, float percentageFuelRemaining)
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
}
