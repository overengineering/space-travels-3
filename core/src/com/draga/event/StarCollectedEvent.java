package com.draga.event;

import com.badlogic.gdx.utils.Pool;
import com.draga.entity.Star;

public class StarCollectedEvent implements Pool.Poolable
{
    public Star star;

    @Override
    public void reset()
    {
        star = null;
    }

    public void set(Star star)
    {
        this.star = star;
    }
}
