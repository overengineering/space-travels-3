package com.draga.event;

import com.badlogic.gdx.utils.Pool;

public class FuelChangeEvent implements Pool.Poolable
{
    public float oldFuel;
    public float newFuel;
    public float maxFuel;

    public void set(float oldFuel, float newFuel, float maxFuel)
    {
        this.oldFuel = oldFuel;
        this.newFuel = newFuel;
        this.maxFuel = maxFuel;
    }

    @Override
    public void reset()
    {
        oldFuel = 0f;

        newFuel = 0f;
        maxFuel = 0f;
    }
}
