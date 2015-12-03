package com.draga.component.graphicComponent;

import com.draga.component.PhysicsComponent;

public abstract class GraphicComponent
{
    protected PhysicsComponent physicsComponent;
    private   float            width;
    private   float            height;
    private   float            halfWidth;
    private   float            halfHeight;

    public GraphicComponent(PhysicsComponent physicsComponent, float height, float width)
    {
        this.physicsComponent = physicsComponent;
        setHeight(height);
        setWidth(width);
    }

    public abstract void update(float deltaTime);

    public abstract void draw();

    public abstract void dispose();

    public abstract boolean isFinished();

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
        this.halfWidth = width / 2f;

    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
        this.halfHeight = height / 2f;
    }

    public float getHalfWidth()
    {
        return halfWidth;
    }

    public float getHalfHeight()
    {
        return halfHeight;
    }
}
