package com.draga.spaceTravels3.gameEntity;

import com.draga.spaceTravels3.component.graphicComponent.GraphicComponent;
import com.draga.spaceTravels3.component.miniMapGraphicComponent.MiniMapGraphicComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

public abstract class GameEntity
{
    public PhysicsComponent physicsComponent;

    public GraphicComponent graphicComponent;

    public MiniMapGraphicComponent miniMapGraphicComponent =
        MiniMapGraphicComponent.NULL_MINI_MAP_GRAPHIC_COMPONENT;

    public abstract void update(float deltaTime);

    public void dispose()
    {
        this.physicsComponent.dispose();
        this.graphicComponent.dispose();
        this.miniMapGraphicComponent.dispose();
    }
}
