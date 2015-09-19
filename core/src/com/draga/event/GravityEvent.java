package com.draga.event;

import com.badlogic.gdx.utils.Pool;

/**
 * Created by Administrator on 19/09/2015.
 */
public class GravityEvent implements Pool.Poolable
{
    public float x;
    public float y;
    public float elapsed;

    public GravityEvent(float x, float y, float elapsed)
    {
        this.x = x;
        this.y = y;
        this.elapsed = elapsed;
    }

    @Override
    public void reset()
    {
        this.x = 0f;
        this.y = 0f;
        this.elapsed = 0f;
    }
}
