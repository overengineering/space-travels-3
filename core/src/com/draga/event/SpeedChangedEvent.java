package com.draga.event;

import com.badlogic.gdx.utils.Pool;

public class SpeedChangedEvent implements Pool.Poolable
{
    public float speed;

    @Override
    public void reset()
    {
        speed = 0;
    }
}
