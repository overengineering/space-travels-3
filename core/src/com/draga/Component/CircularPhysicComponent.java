package com.draga.component;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Pools;
import com.draga.event.GameEventBus;
import com.draga.event.GravityEvent;

/**
 * Created by Administrator on 03/09/2015.
 */
public class CircularPhysicComponent extends PhysicComponent
{
    Circle circle;

    public CircularPhysicComponent(float mass, float radius, float x, float y)
    {
        this.mass = mass;
        this.circle = new Circle(x, y, radius);
    }

    @Override
    public void update(float elapsed)
    {
        GravityEvent gravityEvent = Pools.obtain(GravityEvent.class);
        gravityEvent.set(getX(), getY(), mass, elapsed);
        GameEventBus.GRAVITY_EVENT_BUS.post(gravityEvent);
        Pools.free(gravityEvent);
    }

    @Override
    public float getX()
    {
        return circle.x;
    }

    @Override
    public void setX(float x)
    {
        circle.setX(x);
    }

    @Override
    public float getY()
    {
        return circle.y;
    }

    @Override
    public void setY(float y)
    {
        circle.setY(y);
    }

    @Override
    public float getWidth()
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
