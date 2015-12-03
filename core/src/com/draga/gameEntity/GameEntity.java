package com.draga.gameEntity;

import com.draga.component.PhysicsComponent;
import com.draga.component.graphicComponent.GraphicComponent;
import com.draga.component.miniMapGraphicComponent.MiniMapGraphicComponent;

public abstract class GameEntity
{
    public PhysicsComponent physicsComponent;

    public GraphicComponent graphicComponent;

    public MiniMapGraphicComponent miniMapGraphicComponent =
        MiniMapGraphicComponent.NULL_MINI_MAP_GRAPHIC_COMPONENT;

    public abstract void update(float deltaTime);

    public void dispose()
    {
        physicsComponent.dispose();
        graphicComponent.dispose();
        miniMapGraphicComponent.dispose();
    }
}
