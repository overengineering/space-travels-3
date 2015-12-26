package com.draga.spaceTravels3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Vertex implements Pool.Poolable
{
    private Color   color;
    private Vector2 position;

    public Vertex(Color color, Vector2 position)
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
        this.color = null;
        this.position = null;
    }
}
