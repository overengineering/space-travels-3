package com.draga;

import com.badlogic.gdx.utils.Pool;

public class FuelChangeEvent implements Pool.Poolable
{
    public float fuel;

    @Override
    public void reset()
    {
        fuel = 0f;
    }

    public void set(float fuel)
    {
        this.fuel = fuel;
    }
}
