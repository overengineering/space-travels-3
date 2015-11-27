package com.draga.entity;

import com.draga.graphicComponent.GraphicComponent;
import com.draga.physic.PhysicsComponent;

public abstract class GameEntity
{
    public PhysicsComponent physicsComponent;

    public GraphicComponent graphicComponent;

    public abstract void update(float deltaTime);

    public void dispose()
    {
        physicsComponent.dispose();
        graphicComponent.dispose();
    }

    public abstract void drawMiniMap();
}
