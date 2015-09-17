package com.draga.component;

import com.badlogic.gdx.graphics.glutils.FloatTextureData;

public abstract class PhysicComponent
{

    public float mass;
    private float rotation = 0;

    public abstract void update(float elapsed);

    public float getRotation()
    {
        return rotation;
    }

    public void applyRotation(float i)
    {
        rotation += i;
    }

    public abstract float getX();

    public abstract void setX(float x);

    public abstract float getY();

    public abstract void setY(float y);

    public abstract float getWidth();

    public abstract float getHeight();

    public abstract void applyYForce(float forceY);

    public abstract void applyXForce(float forceX);

    public abstract void applyForce(float forceX, float forceY);
}
