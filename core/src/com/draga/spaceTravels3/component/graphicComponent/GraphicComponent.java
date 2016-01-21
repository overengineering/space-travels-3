package com.draga.spaceTravels3.component.graphicComponent;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

public abstract class GraphicComponent
{
    protected PhysicsComponent physicsComponent;

    private   float            width;
    private   float            height;
    private   float            halfWidth;
    private   float            halfHeight;

    public GraphicComponent(PhysicsComponent physicsComponent, float width, float height)
    {
        this.physicsComponent = physicsComponent;
        setHeight(height);
        setWidth(width);
    }

    public abstract void draw();

    public abstract void dispose();

    public float getWidth()
    {
        return this.width;
    }

    public void setWidth(float width)
    {
        this.width = width;
        this.halfWidth = width / 2f;

    }

    public float getHeight()
    {
        return this.height;
    }

    public void setHeight(float height)
    {
        this.height = height;
        this.halfHeight = height / 2f;
    }

    public float getHalfWidth()
    {
        return this.halfWidth;
    }

    public float getHalfHeight()
    {
        return this.halfHeight;
    }

    public abstract TextureRegion getTexture();
}
