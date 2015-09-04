package com.draga.Component;

import com.badlogic.gdx.math.Circle;

/**
 * Created by Administrator on 03/09/2015.
 */
public class CircularPhysicComponent extends PhysicComponent
{
    Circle circle;

    public CircularPhysicComponent(float mass, int diameter, int x, int y)
    {
        this.mass = mass;
        this.circle = new Circle(x, y, diameter);
    }

    @Override
    public void update(float elapsed)
    {

    }

    @Override
    public float getX()
    {
        return circle.x;
    }

    @Override
    public float getY()
    {
        return circle.y;
    }

    @Override
    public float getWith()
    {
        return circle.radius * 2;
    }

    @Override
    public float getHeight()
    {
        return circle.radius * 2;
    }

    public float getRadius()
    {
        return circle.radius;
    }

    @Override
    public void applyYForce(float forceY)
    {
        circle.y += forceY;
    }

    @Override
    public void applyXForce(float forceX)
    {
        circle.x += forceX;
    }

    @Override
    public void applyForce(float forceX, float forceY)
    {
        circle.x += forceX;
        circle.y += forceY;
    }
}
