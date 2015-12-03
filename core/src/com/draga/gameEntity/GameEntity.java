package com.draga.gameEntity;

import com.draga.component.PhysicsComponent;
import com.draga.component.graphicComponent.GraphicComponent;
import com.draga.component.miniMapGraphicComponent.MiniMapGraphicComponent;
import com.draga.component.miniMapGraphicComponent.NullMiniMapGraphicComponent;

public abstract class GameEntity
{
    public PhysicsComponent physicsComponent;

    public GraphicComponent graphicComponent;

    public MiniMapGraphicComponent miniMapGraphicComponent =
        new NullMiniMapGraphicComponent(null, null);

    public abstract void update(float deltaTime);

    public void dispose()
    {
        physicsComponent.dispose();
        graphicComponent.dispose();
    }
}
