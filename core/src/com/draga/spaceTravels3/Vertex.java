package com.draga.spaceTravels3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.draga.Vector2;

public class Vertex implements Pool.Poolable
{
    private Color   color;
    private Vector2 position;

    public void set(Color color, Vector2 position)
    {
        this.color = color;
        this.position = position;
    }

    public Vector2 getPosition()
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
        Pools.free(this.position);
        this.position = null;
        this.color = null;
    }
}
