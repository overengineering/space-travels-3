package com.draga.gameEntity;

import com.draga.component.graphicComponent.GraphicComponent;
import com.draga.component.miniMapGraphicComponent.MiniMapGraphicComponent;
import com.draga.component.miniMapGraphicComponent.NullMiniMapGraphicComponent;
import com.draga.component.PhysicsComponent;

public abstract class GameEntity
{
    public PhysicsComponent physicsComponent;

    public GraphicComponent graphicComponent;

    public MiniMapGraphicComponent miniMapGraphicComponent = new NullMiniMapGraphicComponent();

    public abstract void update(float deltaTime);

    public void dispose()
    {
        physicsComponent.dispose();
        graphicComponent.dispose();
    }
}
