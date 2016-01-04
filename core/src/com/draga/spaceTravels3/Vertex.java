package com.draga.spaceTravels3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;
import com.draga.PooledVector2;

public class Vertex implements Pool.Poolable
{
    private Color         color;
    private PooledVector2 position;

    public void set(Color color, PooledVector2 position)
    {
        this.color = color;
        this.position = position;
    }

    public PooledVector2 getPosition()
    {
        return position;
    }

    public Color getColor()
    {
        return color;
    }

    @Override
    public void reset()
    {
        this.position.close();
        this.position = null;
        this.color = null;
    }
}
