package com.draga.component.graphicComponent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.draga.component.PhysicsComponent;

public abstract class GraphicComponent
{
    protected PhysicsComponent physicsComponent;
    public    float            width;
    public    float            height;

    public GraphicComponent(PhysicsComponent physicsComponent, float height, float width)
    {
        this.physicsComponent = physicsComponent;
        this.height = height;
        this.width = width;
    }

    public abstract void update(float deltaTime);

    public abstract void draw(SpriteBatch spriteBatch);

    public abstract void dispose();

    public abstract boolean isFinished();
}
